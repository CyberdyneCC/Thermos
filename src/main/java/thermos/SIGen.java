package thermos;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
 
//Randomly decide how many points to use as start points
int num_nodes = r.nextInt(9) + 3;
for (int i = 0; i < num_nodes; i++)
{
//Randomly select start point coordinates
int centerx = r.nextInt(width);
int centery = r.nextInt(height);
 
//Randomly decide how many node branches to create
int num_branches = r.nextInt(16) + 5;
//Choose a random color
Color c = null;
switch (r.nextInt(6))
{
case 0:
c = Color.BLUE;
break;
case 1:
c = Color.RED;
break;
case 2:
c = Color.GREEN;
break;
case 3:
c = Color.ORANGE;
break;
case 4:
c = Color.YELLOW;
break;
case 5:
c = Color.WHITE;
break;
}
//Create the branches
for (int a = 0; a < num_branches; a++)
{
//"Transform" the graphics object so that drawn branches will originate for the start point at a specified angle
AffineTransform aft = AffineTransform.getRotateInstance(Math.toRadians(360 * ((double) a / num_branches)), centerx, centery);
g.setTransform(aft);
 
//Recursively create the branch
branch(g, bi, centerx, centery, 2, 8, 50, 50, 20, 20, c);
}
}
ImageIO.write(bi, "png", file1);
 
//Display image
JFrame f = new JFrame();
f.getContentPane().setLayout(new FlowLayout());
f.getContentPane().add(new JLabel(new ImageIcon(bi)));
f.pack();
f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
f.setVisible(true);
 
}
 
public static void branch(Graphics2D g, BufferedImage bi, int lastx, int lasty, int node, int maxdepth, int ydv, int xdv, int ydm, int xdm, Color c)
{
//Choose the next branch 1 and 2 endpoints
int b1x = lastx + r.nextInt(xdv) + xdm, b1y = lasty + r.nextInt(ydv) + ydm;
int b2x = lastx - r.nextInt(xdv) - xdm, b2y = lasty + r.nextInt(ydv) + ydm;
if (node == maxdepth)
{
//Stop recursive call so that we dont create branches forever
return;
}
//Set the current branch color
g.setPaint(c);
//Draw branch lines
g.drawLine(lastx, lasty, b2x, b2y);
g.drawLine(lastx, lasty, b1x, b1y);
 
//Call the branch again, incrementing the node number and reducing the branch variation and making the color darker
//Divide into *two* new branch calls
branch(g, bi, b1x, b1y, ++node, maxdepth, (int) (ydv / 1.1), (int) (xdv / 1.1), ydm, xdm, c.darker());
branch(g, bi, b2x, b2y, node, maxdepth, (int) (ydv / 1.1), (int) (xdv / 1.1), ydm, xdm, c.darker());
}
 
public static int RGBTransform(int R, int G, int B)
{
R = (R << 16) & 0x00FF0000;
G = (G << 8) & 0x0000FF00;
B = B & 0x000000FF;
return 0xFF000000 | R | G | B;
}
}
