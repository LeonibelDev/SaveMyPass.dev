package com.dac.passwordmanager.service.email;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.dac.passwordmanager.entity.MessageEntity;

import jakarta.servlet.http.HttpServletRequest;

public class EmailTemplate {

  public static MessageEntity buildMessage(String recipient, String subject,
      String body, HttpServletRequest request) {
    MessageEntity msg = new MessageEntity();
    msg.setRecipient(recipient);
    msg.setSubject(subject);
    msg.setBody(body);
    msg.setUrl("http://localhost:5173/");
    msg.setButtonText("Go to SaveMyPass.dev");
    msg.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    msg.setDevice(request.getHeader("User-Agent"));
    msg.setIp(request.getRemoteAddr());
    msg.setLocation(request.getRemoteHost());
    return msg;
  }

  public static final String TEMPLATE = """
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          body { margin: 0; padding: 0; background-color: #F2F2F7; color: #1C1C1E; }
          @media only screen and (max-width: 600px) {
            .inner-body { padding: 32px 20px !important; border-radius: 24px !important; }
            .header { padding: 32px 0 16px 0 !important; }
          }
        </style>
      </head>
      <body style="font-family: -apple-system, BlinkMacSystemFont, 'SF Pro Text', 'Helvetica Neue', Arial, sans-serif; background-color: #F2F2F7; color: #1C1C1E; padding: 0; margin: 0; -webkit-font-smoothing: antialiased;">
        <div style="max-width: 600px; margin: 0 auto; padding: 20px;">

          <!-- Header -->
          <div class="header" style="padding: 48px 0 24px 0; text-align: center;">
            <div style="display: inline-block; background-color: #f97316; width: 48px; height: 48px; border-radius: 12px; margin-bottom: 12px; box-shadow: 0 4px 12px rgba(249, 115, 22, 0.15);">
               <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                 <tr>
                   <td align="center" valign="middle" style="color: #ffffff; font-size: 20px; font-weight: bold;">
                     <img src="https://img.icons8.com/ios-filled/50/ffffff/key.png" width="24" height="24" style="display: block; width: 24px; height: 24px;" alt="K">
                   </td>
                 </tr>
               </table>
            </div>
            <div style="font-size: 20px; font-weight: 700; letter-spacing: -0.02em; color: #1C1C1E;">
              SaveMyPass<span style="color: #f97316;">.dev</span>
            </div>
          </div>

          <!-- Card -->
          <div class="inner-body" style="background: #ffffff; border-radius: 28px; padding: 48px; box-shadow: 0 10px 30px rgba(0, 0, 0, 0.04); border: 1px solid rgba(0,0,0,0.02);">
            <h1 style="font-size: 24px; font-weight: 700; margin-bottom: 12px; color: #000000; text-align: center; tracking: -0.02em;">
              {{TITLE}}
            </h1>

            <div style="font-size: 16px; line-height: 1.5; color: #8E8E93; margin-bottom: 32px; text-align: center; font-weight: 400;">
              {{BODY}}
            </div>

            <!-- Info Table -->
            <div style="background: #F2F2F7; border-radius: 20px; padding: 20px; margin-bottom: 32px;">
              <table style="width: 100%; border-collapse: collapse;">
                <tr>
                  <td style="padding: 6px 0; font-size: 12px; color: #8E8E93; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Date</td>
                  <td style="padding: 6px 0; font-size: 14px; color: #1C1C1E; text-align: right; font-weight: 500;">{{DATE}}</td>
                </tr>
                <tr>
                  <td style="padding: 6px 0; font-size: 12px; color: #8E8E93; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">IP Address</td>
                  <td style="padding: 6px 0; font-size: 14px; color: #1C1C1E; text-align: right; font-weight: 500;">{{IP}}</td>
                </tr>
                <tr>
                  <td style="padding: 6px 0; font-size: 12px; color: #8E8E93; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Location</td>
                  <td style="padding: 6px 0; font-size: 14px; color: #1C1C1E; text-align: right; font-weight: 500;">{{LOCATION}}</td>
                </tr>
                <tr>
                  <td style="padding: 6px 0; font-size: 12px; color: #8E8E93; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Device</td>
                  <td style="padding: 6px 0; font-size: 14px; color: #1C1C1E; text-align: right; font-weight: 500;">{{DEVICE}}</td>
                </tr>
              </table>
            </div>

            <!-- CTA -->
            <div style="text-align: center;">
              <a href="{{BUTTON_URL}}" style="display: inline-block; background: #f97316; color: #ffffff; padding: 16px 36px; border-radius: 16px; text-decoration: none; font-weight: 700; font-size: 16px; box-shadow: 0 4px 12px rgba(249, 115, 22, 0.2);">
                {{BUTTON_TEXT}}
              </a>
            </div>
          </div>

          <!-- Footer -->
          <div style="padding: 32px 20px; text-align: center;">
            <p style="font-size: 13px; color: #8E8E93; font-weight: 500; margin-bottom: 4px;">
              Privacy by Default &bull; Zero-Knowledge Security
            </p>
            <p style="font-size: 11px; color: #C7C7CC; line-height: 1.4;">
              &copy; 2026 SaveMyPass.dev. Built for security. <br>
              If you didn't request this email, please ignore it.
            </p>
          </div>
        </div>
      </body>
      </html>
      """;

  public static String getTemplate(MessageEntity message) {
    return TEMPLATE
        .replace("{{TITLE}}", message.getSubject())
        .replace("{{BODY}}", message.getBody())
        .replace("{{BUTTON_URL}}", message.getUrl())
        .replace("{{BUTTON_TEXT}}", message.getButtonText())
        .replace("{{DATE}}", message.getDate())
        .replace("{{IP}}", message.getIp())
        .replace("{{LOCATION}}", message.getLocation())
        .replace("{{DEVICE}}", message.getDevice());
  }
}