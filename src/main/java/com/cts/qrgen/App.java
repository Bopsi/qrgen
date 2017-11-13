package com.cts.qrgen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class App {
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader("urls.txt");
			br = new BufferedReader(fr);

			String sCurrentLine;
			int c = 0;

			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.replace(" ", "%20");
				sCurrentLine = sCurrentLine.replace(",", "%20");
				
				String id = (sCurrentLine.split("=")[1]).split("&")[0];

				String ticketPath = "qrs/" + String.format("%03d", (++c)) + "-" + id + ".png";
				
				//System.out.println(sCurrentLine);
				//System.out.println("---------------");
				try {					
					BufferedImage smallQr = drawQr(145, sCurrentLine);
					BufferedImage bigQr = drawQr(200, sCurrentLine);

					BufferedImage target = ImageIO.read(new File("gate-pass.jpg"));
					Graphics2D g = (Graphics2D) target.getGraphics();
					g.setPaint(Color.BLACK);
					g.setFont(new Font("TimesRoman", Font.BOLD, 40)); 
					g.drawString(String.format("%03d", c), 1412, 295);
					g.drawImage(smallQr, 990, 215, null);
					g.drawImage(bigQr, 1395, 340, null);

					File outputfile = new File(ticketPath);
					ImageIO.write(target, "png", outputfile);
				
				} catch (WriterException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private static BufferedImage drawQr(int size,String text) throws WriterException {
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

		hintMap.put(EncodeHintType.MARGIN, 1);
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, size, size, hintMap);
		int CrunchifyWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth, BufferedImage.TYPE_INT_RGB);

		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < CrunchifyWidth; i++) {
			for (int j = 0; j < CrunchifyWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		return image;
	}
}
