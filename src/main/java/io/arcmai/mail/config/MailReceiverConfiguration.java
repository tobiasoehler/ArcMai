package io.arcmai.mail.config;

import io.arcmai.mail.model.ImapSetting;
import io.arcmai.mail.repository.ImapSettingRepository;
import io.arcmai.mail.service.MailHandlerService;
import jakarta.mail.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.logging.Logger;

@Component
@EnableScheduling
public class MailReceiverConfiguration {
    private final MailHandlerService mailHandlerService;
    private final ImapSettingRepository imapSettingRepository;
    Logger logger =Logger.getLogger(MailReceiverConfiguration.class.getName());

    public MailReceiverConfiguration(MailHandlerService mailHandlerService, ImapSettingRepository imapSettingRepository) {
        this.mailHandlerService = mailHandlerService;
        this.imapSettingRepository = imapSettingRepository;
    }

    @Scheduled(fixedRateString = "#{optionRepository.findSyncInterval().valueAsLong * 60 * 1000}")
    public void saveMailsToDB() throws Exception {
        var setting = imapSettingRepository.findAll();
        for (ImapSetting imapSetting : setting) {
            handleMailbox(imapSetting);
        }
    }

    private void handleMailbox(ImapSetting setting) throws Exception {
        Store emailStore = null;

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.imap.socketFactory.fallback", false);
        javaMailProperties.put("mail.store.protocol", "imaps");
        javaMailProperties.put("mail.debug", true);

        Session emailSession = Session.getInstance(javaMailProperties);

        try {
            emailStore = emailSession.getStore();
            emailStore.connect(setting.getHost(), setting.getPort(), setting.getUsername(), setting.getPassword());

            for (Folder folder : emailStore.getDefaultFolder().list()){
                loopFolder(folder, "");
            }
        } finally {
            if (emailStore != null && emailStore.isConnected()) {
                emailStore.close();
            }
        }
    }

    private void loopFolder(Folder folder, String path) throws Exception {
        logger.info(String.format("Folder: %s", folder.getName()));
        Folder[] folders = folder.list();
        path += folder.getName()+"/";
        for (Folder f : folders) {
            loopFolder(f, path);
        }
        loopEmailsFromFolder(folder, path);
    }

    private void loopEmailsFromFolder(Folder emailFolder, String folderPath) throws Exception {
        try(emailFolder) {
            emailFolder.open(Folder.READ_ONLY);
            Message[] messages = emailFolder.getMessages();

            for (Message message : messages) {
                mailHandlerService.handleReceivedMail(message, folderPath);
            }
        } catch (MessagingException e){
            logger.warning(e.getMessage());
        }
    }
}
