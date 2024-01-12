package com.base.qrcode.generator;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import com.base.qrcode.model.Product;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class Generator {

    public void generateBarcode(Product product) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter()
                    .encode(product.toString(), BarcodeFormat.QR_CODE, 400, 400);
            BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, 400, 400);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < 400; i++) {
                for (int j = 0; j < 400; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            ImageIO.write(bufferedImage, "png", new File(String.format("./images/%s.png", product.getId())));
            System.out.println("Code-barres généré avec succès !");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<Product> productGenerator(int size) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Product product = Product.builder()
                    .id(String.format("Prod_%03d", i)).canExpire(i % 2 == 0 ? true : false)
                    .ref(generateRef(5)).name(generateName(3, "Product"))
                    .contents(generateBarcodeContents(12, null))
                    .build();
            products.add(product);
        }
        return products;
    }

    private String generateRef(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomStringBuilder = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomStringBuilder.append(randomChar);
        }

        return randomStringBuilder.toString();
    }

    private String generateName(int length, String base) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder randomStringBuilder = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomStringBuilder.append(randomChar);
        }

        return String.format("%s_%s", base, randomStringBuilder.toString());
    }

    private String generateBarcodeContents(int length, String base) {
        String characters = "1234567890";
        StringBuilder randomStringBuilder = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomStringBuilder.append(randomChar);
        }

        return String.format("%s", randomStringBuilder.toString());
    }

    public void generateEAN13Barcode(Product product) throws WriterException {
        try {
            EAN13Writer ean13Writer = new EAN13Writer();
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.MARGIN, 2);

            BitMatrix bitMatrix = ean13Writer.encode(product.getContents(), BarcodeFormat.EAN_13, 300, 150, hints);

            BufferedImage bufferedImage = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                for (int y = 0; y < bitMatrix.getHeight(); y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ImageIO.write(bufferedImage, "png", new File(String.format("./images/%s.png", product.getId())));
            System.out.println("EAN-13 Barcode generated successfully!" + product.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
