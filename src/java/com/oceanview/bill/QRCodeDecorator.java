package com.oceanview.bill;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;

public class QRCodeDecorator extends BillDecorator {

    private String qrContent;

    public QRCodeDecorator(Bill bill, String qrContent) {
        super(bill);
        this.qrContent = qrContent;
    }

    @Override
    public void generate(Document document) throws Exception {

        // 1️⃣ Generate original bill first
        super.generate(document);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Scan QR for Booking Details"));
        document.add(new Paragraph(" "));

        // 2️⃣ Generate QR Code
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(
                qrContent,
                BarcodeFormat.QR_CODE,
                200,
                200
        );

        BufferedImage image =
                new BufferedImage(200, 200,
                        BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                image.setRGB(x, y,
                        matrix.get(x, y)
                        ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }

        ByteArrayOutputStream baos =
                new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        Image qrImage =
                Image.getInstance(baos.toByteArray());

        qrImage.scaleAbsolute(150, 150);

        document.add(qrImage);
    }
}