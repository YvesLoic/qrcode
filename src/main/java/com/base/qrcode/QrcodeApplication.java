package com.base.qrcode;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.base.qrcode.generator.Generator;
import com.base.qrcode.model.Product;
import com.google.zxing.WriterException;

@SpringBootApplication
public class QrcodeApplication {

	private final static Generator generator = new Generator();

	public static void main(String[] args) {
		SpringApplication.run(QrcodeApplication.class, args);
		List<Product> products = generator.productGenerator(3);
		for (Product product : products) {
			try {
				generator.generateEAN13Barcode(product);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}
	}

}
