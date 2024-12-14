package io.arcmai.mail.service;


import jakarta.mail.internet.MimeMessage;

public interface MailHandlerService {

    void handleReceivedMail(MimeMessage message);

}
