package com.cts.qrgen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GroupTickets {

	private static int counter = 1;
	private static int x = 0;
	private static int y = 0;

	public static void main(String[] args) {

		try {
			File folder = new File("qrs");
			File[] listOfFiles = folder.listFiles();
			BufferedImage target = new BufferedImage((1650 * 2), (600 * 7), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) target.createGraphics();
			for (int i = 28; i < 42; i++) {
				BufferedImage source = ImageIO.read(new File(listOfFiles[i].getAbsolutePath()));
				g.drawImage(source, x, y, null);
				counter++;
				updatePositions(1650, 600);
			}
			File outputfile = new File("qrs/aaaoutput.png");
			ImageIO.write(target, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	static void updatePositions(int w, int h) {
		if (counter > 1) {
			if (counter % 2 == 1) {
				y = y + h +1;
				x = 0;
			} else {
				x = w+1;
			}
		}
	}

}
