package com.dac.passwordmanager.service.email;

import java.time.format.DateTimeFormatter;

import com.dac.passwordmanager.entity.MessageEntity;

public class EmailTemplate {

  public static final String TEMPLATE = """
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
          body { margin: 0; padding: 0; background-color: #fcfcfc; color: #1e293b; }
          @media only screen and (max-width: 600px) {
            .inner-body { padding: 30px 20px !important; }
          }
        </style>
      </head>
      <body style="font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #fcfcfc; color: #1e293b; padding: 40px 10px;">
        <div style="max-width: 600px; margin: 0 auto;">

          <div style="margin-bottom: 32px; text-align: center;">
            <div style="display: inline-block; background-color: #f97316; padding: 10px; border-radius: 12px; margin-bottom: 12px; box-shadow: 0 4px 12px rgba(249, 115, 22, 0.2);">
              <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M15 7C15 8.10457 14.1046 9 13 9C11.8954 9 11 8.10457 11 7C11 5.89543 11.8954 5 13 5C14.1046 5 15 5.89543 15 7Z" fill="white"/>
                <path fill-rule="evenodd" clip-rule="evenodd" d="M11.2929 11.2929C11.6834 10.9024 12.3166 10.9024 12.7071 11.2929L13.4142 12H16C16.5523 12 17 12.4477 17 13V15C17 15.5523 16.5523 16 16 16H15V17C15 17.5523 14.5523 18 14 18H12C11.4477 18 11 17.5523 11 17V14.4142L10.2929 13.7071C8.44599 11.8601 8.44599 8.86142 10.2929 7.01447C12.1398 5.16751 15.1385 5.16751 16.9855 7.01447C18.8324 8.86142 18.8324 11.8601 16.9855 13.7071L16.2784 13H14.8642L15.5713 13.7071C16.6447 12.6337 16.6447 10.8943 15.5713 9.82088C14.4978 8.74747 12.7584 8.74747 11.685 9.82088C11.1483 10.3576 11 11.107 11.2929 11.7589V11.2929Z" fill="white"/>
              </svg>
            </div>
            <div style="font-size: 20px; font-weight: 800; letter-spacing: -0.5px; color: #0f172a;">
              SaveMyPass<span style="color: #f97316;">.dev</span>
            </div>
          </div>

          <div class="inner-body" style="border: 1px solid #f1f5f9; border-radius: 24px; padding: 48px; background: #ffffff; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.02);">
            <h1 style="font-size: 24px; font-weight: 800; margin-bottom: 16px; color: #0f172a; text-align: center;">
              {{TITLE}}
            </h1>

            <div style="font-size: 16px; line-height: 1.6; color: #64748b; margin-bottom: 32px; text-align: center;">
              {{BODY}}
            </div>

            <div style="background: #f8fafc; border: 1px solid #f1f5f9; border-radius: 16px; padding: 24px; margin-bottom: 32px;">
              <table style="width: 100%; border-collapse: collapse;">
                <tr>
                  <td style="padding: 4px 0; font-size: 13px; color: #94a3b8; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Date</td>
                  <td style="padding: 4px 0; font-size: 14px; color: #475569; text-align: right;">{{DATE}}</td>
                </tr>
                <tr>
                  <td style="padding: 4px 0; font-size: 13px; color: #94a3b8; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">IP Address</td>
                  <td style="padding: 4px 0; font-size: 14px; color: #475569; text-align: right;">{{IP}}</td>
                </tr>
                <tr>
                  <td style="padding: 4px 0; font-size: 13px; color: #94a3b8; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Location</td>
                  <td style="padding: 4px 0; font-size: 14px; color: #475569; text-align: right;">{{LOCATION}}</td>
                </tr>
                <tr>
                  <td style="padding: 4px 0; font-size: 13px; color: #94a3b8; font-weight: 600; text-transform: uppercase; letter-spacing: 0.05em;">Device</td>
                  <td style="padding: 4px 0; font-size: 14px; color: #475569; text-align: right;">{{DEVICE}}</td>
                </tr>
              </table>
            </div>

            <div style="text-align: center;">
              <a href="{{BUTTON_URL}}" style="display: inline-block; background: #f97316; color: #ffffff; padding: 14px 32px; border-radius: 14px; text-decoration: none; font-weight: 700; font-size: 15px; box-shadow: 0 10px 15px -3px rgba(249, 115, 22, 0.2);">
                {{BUTTON_TEXT}}
              </a>
            </div>
          </div>

          <div style="margin-top: 32px; text-align: center;">
            <p style="font-size: 12px; color: #94a3b8; font-weight: 500; margin-bottom: 8px;">
              &copy; 2026 SaveMyPass.dev — Privacy by default.
            </p>
            <p style="font-size: 11px; color: #cbd5e1; line-height: 1.4;">
              If you did not expect this email, please ignore it or contact our support security team.
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