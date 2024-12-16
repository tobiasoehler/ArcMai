package io.arcmai.mail.service;

import io.arcmai.mail.repository.OptionRepository;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.UUID;

@Service
public class EMLFileMailHandlerServiceImpl implements MailHandlerService {
    @Value("${io.arcmail.basePath}")
    private String basePath;
    @Value("${mail.imap.username}")
    private String username;
    private OptionRepository optionRepository;

    private static final Logger log = LoggerFactory.getLogger(EMLFileMailHandlerServiceImpl.class);

    public EMLFileMailHandlerServiceImpl(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    // Method to replace invalid characters in a file path
    public static String replaceInvalidCharacters(String path) {
        // Windows invalid characters (including / and \)
        String invalidChars = "[<>:\"/\\|?*]";

        // Replace invalid characters with an underscore or any other replacement
        return path.replaceAll(invalidChars, "_");
    }

    @Override
    public void handleReceivedMail(Message message, String folderPath) throws Exception {
        final MimeMessage messageToExtract = (MimeMessage) message;
        String path = String.format("%s/%s/%s",basePath, username, folderPath);
        Files.createDirectories(Path.of(path));
        String logFileName = optionRepository.findEMLFilename().getValue()+".eml";


        // TODO Date ersetzten
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        logFileName = logFileName.replace("{date}", sdf.format(message.getReceivedDate()));
        logFileName = logFileName.replace("{subject}", message.getSubject() != null ? message.getSubject() : "");
        logFileName= logFileName.replace("{messageId}", ((MimeMessage) message).getMessageID());

        if (!new File(logFileName).exists()){
            messageToExtract.writeTo(new FileOutputStream(path+logFileName));
        }
    }
}
