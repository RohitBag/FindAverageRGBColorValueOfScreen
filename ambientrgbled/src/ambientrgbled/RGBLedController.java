package ambientrgbled;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;

/**
 * This program demonstrates how to capture a screenshot (full screen) as an
 * image which will be saved into a file.
 * 
 */
public class RGBLedController extends JFrame {
	private static final long serialVersionUID = 1L;
	static int FPS=5;
	public static void main(String[] args) {
		File theDir = new File("D://rgbLedScreenshot");

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + "rgbLedScreenshot");
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}
		try {
			//String fileName = "D://rgbLedScreenshot//FullScreenshot.jpg";
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			Robot robot = new Robot();
			int rgb;
			int red=0;
			int green=0;
			int blue=0;
			
			/*
			 * Set the FPS as 1000/fps
			 */
			while(true){
			Thread.sleep(1000/FPS);
			red=0;
			green=0;
			blue=0;
			BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
			//ImageIO.write(screenFullImage, "jpg", new File(fileName));
			
			for (int widthPixel = 0; widthPixel < screenFullImage.getWidth(); widthPixel++) {
				for (int heightPixel = 0; heightPixel < screenFullImage.getHeight(); heightPixel++){
					rgb = screenFullImage.getRGB(widthPixel, heightPixel);
					red =  red + ((rgb >> 16) & 0xFF);
					green = green + ((rgb >>  8) & 0xFF);
					blue = blue + ((rgb ) & 0xFF);
					//System.out.println(widthPixel+" "+heightPixel);
				}
			}
			red = red/(screenFullImage.getWidth()*screenFullImage.getHeight());
			green = green/(screenFullImage.getWidth()*screenFullImage.getHeight());
			blue = blue/(screenFullImage.getWidth()*screenFullImage.getHeight());
			System.out.println(red+" "+green+" "+blue);
			}
		} catch (AWTException | InterruptedException ex) {
			System.err.println(ex);
		}
	}
}