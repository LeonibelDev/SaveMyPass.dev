package com.dac.passwordmanager.service.pdf;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.dac.passwordmanager.dto.response.CredentialsDTO;

@Service
public class CreatePdfFile {

    public File createFile(List<CredentialsDTO> passwords, String filePassword, String userId)
            throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(50, 750);

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contentStream.showText("SaveMyPass.dev");
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.showText("Internal Password Export Report");
            contentStream.newLine();
            contentStream.newLine();
            contentStream.setFont(PDType1Font.HELVETICA, 10);

            for (CredentialsDTO password : passwords) {
                contentStream.showText("Site: " + password.getSite());
                contentStream.newLine();
                contentStream.showText("Username: " + password.getUsername());
                contentStream.newLine();
                contentStream.showText("Password: " + password.getPassword());
                contentStream.newLine();
                contentStream.newLine();
            }

            contentStream.endText();

        }

        AccessPermission ap = new AccessPermission();
        ap.setCanPrint(false);
        ap.setCanModify(false);
        ap.setReadOnly();
        ap.setCanPrint(false);

        StandardProtectionPolicy spp = new StandardProtectionPolicy(filePassword, filePassword, ap);
        spp.setEncryptionKeyLength(256);
        document.protect(spp);

        File pdfFile = File.createTempFile("passwords-" + userId + "-" + System.currentTimeMillis(), ".pdf");
        document.save(pdfFile);
        document.close();

        return pdfFile;

    }
}
