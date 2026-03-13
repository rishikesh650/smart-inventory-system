package com.inventory.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmailWithAttachment(String to, String subject, String body, byte[] attachment, String fileName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            if (attachment != null) {
                helper.addAttachment(fileName, new org.springframework.core.io.ByteArrayResource(attachment));
            }
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public void sendLowStockAlert(com.inventory.entity.Product product, int currentStock) {
        System.out.println("LOW STOCK ALERT: " + product.getName() + " is at " + currentStock);
        // logic for sending actual alert
    }

    public void sendOrderApprovalRequest(com.inventory.entity.PurchaseOrder order) {
        System.out.println("ORDER APPROVAL REQUEST: PO#" + order.getId());
    }
}
