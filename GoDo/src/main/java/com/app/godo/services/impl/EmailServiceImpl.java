package com.app.godo.services.impl;
import com.app.godo.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendRegistrationRequestSuccessEmail(String username, String address) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(address);
            helper.setFrom("info@godo.com");
            helper.setSubject("Account registration request received");
            helper.setText("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                    "    <title>Registration Request Received</title>\n" +
                    "    <style>\n" +
                    "        body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
                    "        table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
                    "        img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\n" +
                    "        body { height: 100% !important; margin: 0 !important; padding: 0 !important; width: 100% !important; background-color: #1c1c1c; }\n" +
                    "\n" +
                    "        @media screen and (max-width: 600px) {\n" +
                    "            .container {\n" +
                    "                width: 100% !important;\n" +
                    "                max-width: 100% !important;\n" +
                    "            }\n" +
                    "            .content {\n" +
                    "                padding: 20px !important;\n" +
                    "            }\n" +
                    "            .header {\n" +
                    "                padding: 20px 10px !important;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body style=\"margin: 0 !important; padding: 0 !important; background-color: #1c1c1c;\">\n" +
                    "\n" +
                    "    <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\" style=\"background-color: #1c1c1c; padding: 20px 0;\">\n" +
                    "                \n" +
                    "                <!--[if (gte mso 9)|(IE)]>\n" +
                    "                <table align=\"center\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"600\">\n" +
                    "                <tr>\n" +
                    "                <td align=\"center\" valign=\"top\" width=\"600\">\n" +
                    "                <![endif]-->\n" +
                    "\n" +
                    "                <table class=\"container\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"max-width: 600px; background-color: #2b2b2b; border-radius: 8px; overflow: hidden;\">\n" +
                    "                    \n" +
                    "                    <tr>\n" +
                    "                        <td align=\"left\" class=\"content\" style=\"padding: 20px 40px 40px 40px; font-family: Arial, sans-serif;\">\n" +
                    "                            \n" +
                    "                            <h1 style=\"font-size: 24px; font-weight: bold; color: #A3B18A; margin: 0 0 20px 0;\">\n" +
                    "                                We've Received Your Request!\n" +
                    "                            </h1>\n" +
                    "\n" +
                    "                            <p style=\"font-size: 16px; line-height: 1.5; color: #EAEAEA; margin: 0 0 15px 0;\">\n" +
                    "                                Hi "+ username + ",\n" +
                    "                            </p>\n" +
                    "                            <p style=\"font-size: 16px; line-height: 1.5; color: #EAEAEA; margin: 0 0 15px 0;\">\n" +
                    "                                Thank you for your interest. We've received your registration request, it is currently pending and soon will be reviewed by our admins.\n" +
                    "                            </p>\n" +
                    "                            <p style=\"font-size: 16px; line-height: 1.5; color: #EAEAEA; margin: 0 0 25px 0;\">\n" +
                    "                                This process usually takes 24-48 hours. You don't need to do anything else right now. We will send you another email as soon as your request is reviewed.\n" +
                    "                            </p>\n" +
                    "\n" +
                    "                            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                    "                                <tr>\n" +
                    "                                    <td style=\"border-bottom: 2px solid #A3B18A;\"></td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "\n" +
                    "                            <p style=\"font-size: 16px; line-height: 1.5; color: #EAEAEA; margin: 25px 0 0 0;\">\n" +
                    "                                Thank you for your patience!\n" +
                    "                                <br>\n" +
                    "                                The GoDo Team\n" +
                    "                            </p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "                    \n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\" style=\"padding: 20px 40px; background-color: #222222;\">\n" +
                    "                            <p style=\"font-size: 12px; line-height: 1.5; color: #888888; margin: 0;\">\n" +
                    "                                You received this email because you requested to register an account at our website.\n" +
                    "                            </p>\n" +
                    "                            <p style=\"font-size: 12px; line-height: 1.5; color: #888888; margin: 10px 0 0 0;\">\n" +
                    "                                &copy; 2025 GoDo. All rights reserved.\n" +
                    "                                <br>\n" +
                    "                                123 Coconut Street, Green Valley, 12345\n" +
                    "                            </p>\n" +
                    "                            <p style=\"font-size: 12px; line-height: 1.5; margin: 10px 0 0 0;\">\n" +
                    "                                <a href=\"#\" style=\"color: #A3B18A; text-decoration: none;\">Visit our Website</a>\n" +
                    "                            </p>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "                </table>\n" +
                    "\n" +
                    "                <!--[if (gte mso 9)|(IE)]>\n" +
                    "                </td>\n" +
                    "                </tr>\n" +
                    "                </table>\n" +
                    "                <![endif]-->\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>", true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}