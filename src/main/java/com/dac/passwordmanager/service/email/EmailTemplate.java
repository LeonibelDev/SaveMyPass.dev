package com.dac.passwordmanager.service.email;

import com.dac.passwordmanager.entity.MessageEntity;

public class EmailTemplate {

  public static final String TEMPLATE = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <style>
                body { margin: 0; padding: 0; background-color: #f9fafb; }
              </style>
            </head>
            <body style="font-family: -apple-system, system-ui, sans-serif; color: #111827; background-color: #f9fafb;">
              <div style="max-width: 600px; margin: 0 auto; padding: 40px 20px;">

                <!-- LOGO -->
                <div style="text-align: center; margin-bottom: 32px;">
                  <img
                    src="https://1000logos.net/wp-content/uploads/2021/05/Claro-logo.png"
                    style="height: 70px;"
                    alt="Claro"
                  />
                </div>

                <!-- CARD -->
                <div style="background: #ffffff; border: 1px solid #e5e7eb; border-radius: 10px; padding: 32px;">

                  <h1 style="font-size: 20px; font-weight: 600; margin-bottom: 16px;">
                    {{TITLE}}
                  </h1>

                  <div style="font-size: 14px; line-height: 1.6; color: #4b5563;">
                    {{BODY}}
                  </div>

                  <!-- INFO DEL EVENTO -->
                  <div style="margin-top: 24px; padding: 16px; background: #f3f4f6; border-radius: 8px; font-size: 13px;">

        <strong>Detalles del evento:</strong><br><br>

        <!-- FECHA -->
        <div style="margin-bottom: 8px;">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="#6b7280" stroke-width="1.5" style="vertical-align: middle; margin-right: 6px;" viewBox="0 0 24 24">
            <rect x="3" y="4" width="18" height="18" rx="2"></rect>
            <line x1="16" y1="2" x2="16" y2="6"></line>
            <line x1="8" y1="2" x2="8" y2="6"></line>
            <line x1="3" y1="10" x2="21" y2="10"></line>
          </svg>
          <strong>Fecha:</strong> {{DATE}}
        </div>

        <!-- IP -->
        <div style="margin-bottom: 8px;">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="#6b7280" stroke-width="1.5" style="vertical-align: middle; margin-right: 6px;" viewBox="0 0 24 24">
            <circle cx="12" cy="12" r="9"></circle>
            <path d="M3 12h18"></path>
            <path d="M12 3a15 15 0 0 1 0 18"></path>
            <path d="M12 3a15 15 0 0 0 0 18"></path>
          </svg>
          <strong>IP:</strong> {{IP}}
        </div>

        <!-- UBICACIÓN -->
        <div style="margin-bottom: 8px;">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="#6b7280" stroke-width="1.5" style="vertical-align: middle; margin-right: 6px;" viewBox="0 0 24 24">
            <path d="M12 21s-6-5.33-6-10a6 6 0 1 1 12 0c0 4.67-6 10-6 10z"></path>
            <circle cx="12" cy="11" r="2.5"></circle>
          </svg>
          <strong>Ubicación:</strong> {{LOCATION}}
        </div>

        <!-- DISPOSITIVO -->
        <div>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="none" stroke="#6b7280" stroke-width="1.5" style="vertical-align: middle; margin-right: 6px;" viewBox="0 0 24 24">
            <rect x="3" y="4" width="18" height="14" rx="2"></rect>
            <line x1="8" y1="20" x2="16" y2="20"></line>
            <line x1="12" y1="18" x2="12" y2="20"></line>
          </svg>
          <strong>Dispositivo:</strong> {{DEVICE}}
        </div>

      </div>

                  <!-- BOTON -->
                  <div style="margin-top: 28px;">
                    <a href="{{BUTTON_URL}}"
                       style="display: inline-block; background-color: #ef4444; color: #fff; padding: 12px 20px; border-radius: 6px; text-decoration: none; font-size: 14px;">
                      {{BUTTON_TEXT}}
                    </a>
                  </div>

                  <!-- ALERTA -->
                  <div style="margin-top: 24px; font-size: 13px; color: #6b7280;">
                    Si no realizaste esta acción, cambia tu contraseña inmediatamente o contacta con soporte.
                  </div>

                </div>

                <!-- FOOTER -->
                <div style="margin-top: 32px; text-align: center;">
                  <p style="font-size: 12px; color: #9ca3af;">
                    Sistema corporativo de gestión de credenciales.<br>
                    © 2026 Claro
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