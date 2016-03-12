package thermos;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SIGen
{

	//Initialize a random number generator
	public static final Random r = new Random();

	//Image Size
	public static final int width = 64, height = 64;


	public static void gen(net.minecraft.server.MinecraftServer MS, File file1) throws Exception
	{

		//Create the image
		BufferedImage bi = new BufferedImage(width,
				height,
				BufferedImage.TYPE_INT_RGB
				);

		//Initialize Graphics2D for drawing on the image
		Graphics2D g = bi.createGraphics();

		//Some renderer hints that make the output image sharper
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g.setPaint(Color.DARK_GRAY);
		g.fillOval(0, 0, (int)width, (int)height);
		g.setFont(new Font("Consolas",Font.PLAIN,32));
		String s = "LOL";
		Rectangle2D r2d = g.getFontMetrics().getStringBounds(s, g);
                g.setPaint(Color.WHITE);
                float xPos = (float)((width - r2d.getWidth())/2);
                float yPos =  (float)(height + r2d.getHeight()/2)/2;
		g.drawString(s, xPos, yPos);			
		/*for(int i = 0; i < width; i++)
		{
			for(int j = 0; j < height; j++)
			{
				
			}
		}*/
		ImageIO.write(bi, "png", file1);

	}


	public static int RGBTransform(int R, int G, int B)
	{
		R = (R << 16) & 0x00FF0000;
		G = (G << 8) & 0x0000FF00;
		B = B & 0x000000FF;
		return 0xFF000000 | R | G | B;
	}
}
