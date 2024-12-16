package io.arcmai.mail.service;


import jakarta.mail.Message;

public interface MailHandlerService {

    void handleReceivedMail(Message message, String folderPath) throws Exception;

}
