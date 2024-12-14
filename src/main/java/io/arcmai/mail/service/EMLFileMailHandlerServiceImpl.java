package io.arcmai.mail.service;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.UUID;

@Service
public class EMLFileMailHandlerServiceImpl implements MailHandlerService {

    @Value("${io.arcmail.basePath}")
    private String basePath;
    @Value("${mail.imap.username}")
    private String username;


    private static final Logger log = LoggerFactory.getLogger(EMLFileMailHandlerServiceImpl.class);

    @Override
    public void handleReceivedMail(MimeMessage receivedMessage) {
        try {

            Folder folder = receivedMessage.getFolder();
            folder.open(Folder.READ_WRITE);

            Message[] messages = folder.getMessages();
            fetchMessagesInFolder(folder, messages);

            Arrays.asList(messages).stream().filter(message -> {
                MimeMessage currentMessage = (MimeMessage) message;
                try {
                    return currentMessage.getMessageID().equalsIgnoreCase(receivedMessage.getMessageID());
                } catch (MessagingException e) {
                    log.error("Error occurred during process message", e);
                    return false;
                }
            }).forEach(x -> extractMail(x, folder));

            folder.close(true);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void fetchMessagesInFolder(Folder folder, Message[] messages) throws MessagingException {
        FetchProfile contentsProfile = new FetchProfile();
        contentsProfile.add(FetchProfile.Item.ENVELOPE);
        contentsProfile.add(FetchProfile.Item.CONTENT_INFO);
        contentsProfile.add(FetchProfile.Item.FLAGS);
        contentsProfile.add(FetchProfile.Item.SIZE);
        folder.fetch(messages, contentsProfile);
    }

    // Method to replace invalid characters in a file path
    public static String replaceInvalidCharacters(String path) {
        // Windows invalid characters (including / and \)
        String invalidChars = "[<>:\"/\\|?*]";

        // Replace invalid characters with an underscore or any other replacement
        return path.replaceAll(invalidChars, "_");
    }

    private void extractMail(Message message, Folder folder) {
        try {
            final MimeMessage messageToExtract = (MimeMessage) message;
            String path = String.format("%s/%s/%s",basePath, username, getFolderPath(folder));
            Files.createDirectories(Path.of(path));
            String logFileName =
                            String.format("%s/%s-%s.eml",path, replaceInvalidCharacters(message.getSubject()), replaceInvalidCharacters(((MimeMessage) message).getMessageID()));
            if (!new File(logFileName).exists()){
                messageToExtract.writeTo(new FileOutputStream(logFileName));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String getFolderPath(Folder folder) throws MessagingException {
        String folderPath = "";
        if (folder != null && !folder.getName().isEmpty()){
            folderPath += folder.getName()+"/";
            folderPath += getFolderPath(folder.getParent());
        }

        return folderPath;
    }
}
