package org.oxy12.mintinvoiz.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Mint Invoiz!");

            String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #2196F3;">Welcome to Mint Invoiz, %s!</h2>
                        <p>Thank you for joining Mint Invoiz - your fresh invoice management solution.</p>
                        <p>You're all set with your <strong>Free</strong> account which includes:</p>
                        <ul>
                            <li>25 invoices per month</li>
                            <li>PDF generation (A4 format)</li>
                            <li>10 prints per month</li>
                            <li>Basic email support</li>
                        </ul>
                        <p>Ready to get started? <a href="https://mintinvoiz.com/dashboard" style="color: #2196F3;">Login to your dashboard</a></p>
                        <hr style="margin: 30px 0; border: none; border-top: 1px solid #eee;">
                        <p style="font-size: 12px; color: #666;">
                            © 2024 OXY12 · Mint Invoiz · All rights reserved<br>
                            Fresh invoices, minted for success.
                        </p>
                    </div>
                </body>
                </html>
                """, firstName);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            
            log.info("Welcome email sent successfully to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    public void sendPasswordResetOTP(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Mint Invoiz - Password Reset Code");

            String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #2196F3;">Password Reset Request</h2>
                        <p>You requested a password reset for your Mint Invoiz account.</p>
                        <div style="background: #f5f5f5; padding: 20px; border-radius: 5px; text-align: center; margin: 20px 0;">
                            <h3 style="margin: 0; font-size: 24px; letter-spacing: 2px;">%s</h3>
                        </div>
                        <p>This code will expire in 15 minutes. If you didn't request this reset, please ignore this email.</p>
                        <hr style="margin: 30px 0; border: none; border-top: 1px solid #eee;">
                        <p style="font-size: 12px; color: #666;">
                            © 2024 OXY12 · Mint Invoiz · All rights reserved
                        </p>
                    </div>
                </body>
                </html>
                """, otp);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            
            log.info("Password reset OTP sent successfully to {}", toEmail);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    public void sendInvoiceEmail(String toEmail, String invoiceNumber, String shareLink, String customMessage) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(String.format("Invoice %s from Mint Invoiz", invoiceNumber));

            String htmlContent = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #2196F3;">New Invoice: %s</h2>
                        %s
                        <div style="margin: 30px 0;">
                            <a href="%s" style="display: inline-block; background: #2196F3; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px;">
                                View Invoice
                            </a>
                        </div>
                        <p style="font-size: 14px; color: #666;">
                            You can view and download this invoice using the link above.
                        </p>
                        <hr style="margin: 30px 0; border: none; border-top: 1px solid #eee;">
                        <p style="font-size: 12px; color: #666;">
                            Generated by Mint Invoiz · © 2024 OXY12
                        </p>
                    </div>
                </body>
                </html>
                """, invoiceNumber, 
                customMessage != null ? "<p>" + customMessage + "</p>" : "<p>Please find your invoice details below.</p>",
                shareLink);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            
            log.info("Invoice email sent successfully to {} for invoice {}", toEmail, invoiceNumber);
        } catch (MessagingException e) {
            log.error("Failed to send invoice email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send invoice email", e);
        }
    }
}