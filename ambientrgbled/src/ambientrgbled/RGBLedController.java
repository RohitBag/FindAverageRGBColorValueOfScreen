

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;

import javax.swing.JFrame;

import com.fazecast.jSerialComm.SerialPort;

/**
 * This program demonstrates how to capture a screenshot (full screen) as an
 * image which will be saved into a file.
 * 
 */
public class RGBLedController extends JFrame {
	private static final long serialVersionUID = 1L;
	private final static int FPS = 60;
	private final static String DEFAULT_ARDUINO_PORT = "COM3";

	public static void main(String[] args) {
		try {
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			Robot robot = new Robot();
			int rgb;
			int red = 0;
			int green = 0;
			int blue = 0;
			SerialPort arduinoPort = null;
			SerialPort[] allPorts = SerialPort.getCommPorts();
			for(SerialPort port : allPorts){
				if(port.getSystemPortName().equalsIgnoreCase(DEFAULT_ARDUINO_PORT)){
					arduinoPort = port;
				}
			}
			if(arduinoPort == null){
				System.err.println("Arduino not connected");
				Thread.sleep(5000);
				System.exit(0);
			}
			else if(arduinoPort.openPort()){
				System.out.println("Connected to arduino");
			}
			else{
				System.err.println("Problem in connecting to Arduino on selected Port");
				Thread.sleep(5000);
				System.exit(0);
			}
			PrintWriter output = new PrintWriter(arduinoPort.getOutputStream());

			/*
			 * Set the FPS as 1000/fps
			 */
			while (true) {
				Thread.sleep(1000 / FPS);
				red = 0;
				green = 0;
				blue = 0;
				BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
				// ImageIO.write(screenFullImage, "jpg", new File(fileName));

				for (int widthPixel = 0; widthPixel < screenFullImage.getWidth(); widthPixel++) {
					for (int heightPixel = 0; heightPixel < screenFullImage.getHeight(); heightPixel++) {
						rgb = screenFullImage.getRGB(widthPixel, heightPixel);
						red = red + ((rgb >> 16) & 0xFF);
						green = green + ((rgb >> 8) & 0xFF);
						blue = blue + ((rgb) & 0xFF);
						// System.out.println(widthPixel+" "+heightPixel);
					}
				}
				red = red / (screenFullImage.getWidth() * screenFullImage.getHeight());
				green = green / (screenFullImage.getWidth() * screenFullImage.getHeight());
				blue = blue / (screenFullImage.getWidth() * screenFullImage.getHeight());
				System.out.println(red + " " + green + " " + blue);
//				if(red > 100 && red > (blue + green)){
//					red = 254;
//					green = 0;
//					blue = 0;
//				}
//				if(blue > 100 && blue > (red + green)){
//					red = 0;
//					green = 0;
//					blue = 254;
//				}
//				if(green > 60 && green > (red + blue)){
//					red = 0;
//					green = 254;
//					blue = 0;
//				}

				// sending the rgb values to arduino
				output.write(0xff);
				output.write((byte)red);
				output.write((byte)green);
				output.write((byte)blue);
				output.flush();
				Thread.sleep(10);
			}
		} catch (AWTException | InterruptedException ex) {
			System.err.println(ex);
		}
	}
}
