import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;

public class Painter 
{
	private Vector<Occurrence> words; // keywords to paint
	private Vector<Occurrence> current; // keywords to current exists
	private final static String fontfile = "res/times.ttf"; // Font
	private BufferedImage[] img;
	Graphics2D g;
    
	private static Point p_cen;
	private static final int max_num = 150;
	private static final int font_min = 18;
	private static final int font_max = 125;
	private static Point min_size;
	private Bound bound;
	private Shape bound_shape;
	private String color_style;
	Font font;
	Font fontBase;


	private final int height; // height of the picture
	private final int width; // width of the picture	
	private boolean is_rotate;
	private int currentTime;
	private ImageObserver observer;
	private boolean done;
	
	
	public Painter(int width, int height, ImageObserver observer) 
	{
		this.width = width;
		current = new Vector<Occurrence>();
		this.height = height;
		this.observer = observer;
	    done = false;	    
	    currentTime = 0;
		p_cen = new Point(width / 2, height / 2);
		min_size = new Point(0,0);
		color_style="warm";	
		

		//TODO: debug
		img = new BufferedImage[Label.MAXCOUNT];
		for (int i = 0; i < img.length; i++)
		{
			img[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
		
		//set shape
		bound=new Bound("", width, height);
		bound_shape=bound.get_shape();
		
		words = Occurrence.getOccu();
		
		try 
		{
			InputStream myStream = new BufferedInputStream(new FileInputStream(fontfile));
			fontBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
		}
		catch (Exception ex)
		{
	        ex.printStackTrace();
	    }
	}

	public String paint()
	{
		long startTime = new Date().getTime();
		if (words.size() == 0)
		{
			return "No Keywords Found!";
		}
		
		for (int time = 0; time < img.length; time ++)
		{
			currentTime = time;
			g = img[time].createGraphics();
			g.fillRect(0, 0, width, height); // Fill the picture with white			
					
			// draw the existing ones
			for (int j = 0; j < current.size(); j++)
			{	
				//check for existence
				if (!current.get(j).exists(time))
				{
					current.remove(j);
					j--;
					continue;
				}
				
				try   // Set the font
				{
					font = fontBase.deriveFont(Font.PLAIN, current.get(j).getSize(time));
				} 
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				g.setFont(font);
				g.drawString(current.get(j).getLabel().getStr(), current.get(j).X(), current.get(j).Y());

				System.out.println(current.get(j).getLabel().getStr() + " !!!!! " + font.getSize() +current.get(j).X() + "   " + current.get(j).Y()) ;
			}	
			
//			if (update) observer.imageUpdate(img, ImageObserver.ALLBITS, 0, 0, width, height);
			
			while(true)
			{
				if (paintStr() == 0) break;
				System.out.println((time + 1 ) + " / " + Label.MAXCOUNT + " done. Size: " + current.size());
			}		

			g.drawString(currentTime + "", 200, 200);
		}

		System.out.println("Paint Successful!");   
		long endTime = new Date().getTime();
		done = true;
		return "Time used: " + (endTime - startTime) / 1000 + "." + (endTime - startTime) % 1000 + " s.";
	}
	
	private void repaint() 
	{
//		for (int i = 0; i < words.size(); i++)
//		{
//			if (words.get(i).X() == -1) continue;		
//			try   // Set the font
//			{
//				font = fontBase.deriveFont(Font.PLAIN, words.get(i).getSize());
//			} 
//			catch (Exception ex)
//			{
//				ex.printStackTrace();
//			}
//			g.setFont(font);
//			g.drawString(words.get(i).getStr(), words.get(i).X(), words.get(i).Y());
//		}		
//		observer.imageUpdate(img, ImageObserver.ALLBITS, 0, 0, width, height);
	}

	public BufferedImage[] getImg()
	{
		return img;
	}
	
	
	private int paintStr()
	{
		//nothing to draw
		if (words.size() == 0 || words.get(0).getStart() > currentTime)
		{
			return 0;
		}
		
		//already expires
		if (!words.get(0).exists(currentTime))
		{
			System.out.println(words.get(0).getLabel().getStr() + " :expires brfoe drawing!");
			words.remove(0);
			return 1;
		}
		
		
		// Try to find an empty space of the string
		Point position = null;	
		Rectangle  bounds = null;
		Shape draw_word = null;
		AffineTransform tx = null;
		boolean found = false;
		Point[] str_vertex = new Point[4];
		
		//TODO::change
		int sides = 0;
		
		while (!found)
		{
			// Set the font
			try 
			{
				font = fontBase.deriveFont(Font.PLAIN, words.get(0).getSize(currentTime));
			} 
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			g.setFont(font);
			TextShape textshape = new TextShape(font,words.get(0).getLabel().getStr());
			draw_word=textshape.getShape();
			tx = new AffineTransform();
			bounds = draw_word.getBounds();
			str_vertex[0] = new Point(bounds.x, bounds.y);
			str_vertex[1] = new Point(bounds.x, bounds.y + bounds.height);
			str_vertex[2] = new Point(bounds.x + bounds.width, bounds.y + bounds.height);
			str_vertex[3] = new Point(bounds.x + bounds.width, bounds.y);
			
			if(Math.random()>0.5) {
				is_rotate=false;
			} else {
				is_rotate=true;
			}
			if(is_rotate) {	
				AffineTransform ax = new AffineTransform();
				ax.rotate(Math.PI/4*Math.random() ,0,0);
				ax.transform(str_vertex, 0, str_vertex, 0, 4);
				draw_word=ax.createTransformedShape(draw_word);
			}
			// Get the bounds of the string
			bounds = draw_word.getBounds();
			for (int j = sides; j > -1; j--)
			{
				//rotate it 4 times to fit space
				for(int k = 0; k < 6; k++) {
					position = searchSpace(bounds, j);
					if (position != null)
					{
						found = true;
						j = -1;
						break;
					} else {
						AffineTransform ax = new AffineTransform();
						ax.rotate(Math.PI/4*Math.random() ,0,0);
						ax.transform(str_vertex, 0, str_vertex, 0, 4);
						draw_word=ax.createTransformedShape(draw_word);
						bounds = draw_word.getBounds();
					}
				}
			}
			
			if (!found) // no space, try to make the word smaller
			{
//				words.get(i).setSize(words.get(i).getSize() - 10);
//				if (words.get(i).getSize() < font_min) // too small. no space available on the canvas
//				{
//					System.out.println("Warning! No space available!");
//					return 0;		
//				}
				return 0;	
			}
		}
		
		setColor(position);// Set the color of the string which is related to its position
		// Draw the string
		int x = (int) (position.x - bounds.getMinX());
		int y = (int) (position.y - bounds.getMinY());
		
		tx.setToTranslation(x, y);
		tx.transform(str_vertex, 0, str_vertex, 0, 4);
		draw_word=tx.createTransformedShape(draw_word);
//		Rectangle2D str_bounds = g.getFont().getStringBounds (words.get(i).getStr(), context);
//		g.drawRect(position.x, position.y, bounds.width, bounds.height);
//		g.drawLine((int)str_vertex[0].x, (int)str_vertex[0].y, (int)str_vertex[1].x, (int)str_vertex[1].y);
//		g.drawLine((int)str_vertex[2].x, (int)str_vertex[2].y, (int)str_vertex[1].x, (int)str_vertex[1].y);
//		g.drawLine((int)str_vertex[2].x, (int)str_vertex[2].y, (int)str_vertex[3].x, (int)str_vertex[3].y);
//		g.drawLine((int)str_vertex[3].x, (int)str_vertex[3].y, (int)str_vertex[0].x, (int)str_vertex[0].y);
		g.fill(draw_word);
		words.get(0).setPoint(x, y);
		int[] str_x = {(int)str_vertex[0].x, (int)str_vertex[1].x, (int)str_vertex[2].x, (int)str_vertex[3].x};
		int[] str_y = {(int)str_vertex[0].y, (int)str_vertex[1].y, (int)str_vertex[2].y, (int)str_vertex[3].y};
		words.get(0).setBounds(new Polygon(str_x, str_y, 4));
//		if (update) observer.imageUpdate(img, ImageObserver.ALLBITS, position.x, position.y, bounds.width, bounds.height);
		//copy from words to current
		current.add(words.get(0));
		words.remove(0);
		
		return 1;	
	}

	public int max (int a, int b) {
		return a > b ? a:b;
	}
	private void setColor(Point position)
	{
		if(color_style == "warm") {
//			System.out.println("warm");
			g.setColor(new Color(
					 max( position.x/5>250 ? 250 : position.x/5 , 120 ),
					 max( position.y/5>200 ? 200 : position.y/5 , 50 ),
					 max( (position.x+position.y)/10>250 ? 250 : (position.x+position.y)/10 , 120 )
					 ));
		}
		g.setColor(new Color(200, 200, 0));
	}
			

	private Point searchSpace(Rectangle bounds, int sides)
	{		
		// The bounds of the string
		int str_X;
		int str_Y;
		str_X = bounds.width;
		str_Y = bounds.height;
		int loop=1;
		int step=(int)(0.1*str_Y);
		if(step<1)step=1;
		int y=p_cen.y-loop;	
		int x=p_cen.x-loop;
		// The starting position of x and y
		int init_Y = y;
		int init_X = x;
		int left_bound=0;
		int right_bound=0;
		int up_bound=0;
		int low_bound=0;
		
		do
		{	
			if(min_size.x!=0){
				if(str_X>=min_size.x&&str_Y>=min_size.y)
				{
					break;
				}
				if(min_size.y==font_min)break;
			}
			if (isEmpty(x-str_X/2, y-str_Y/2, str_X, str_Y, sides))
			{
				return new Point(x-str_X/2, y-str_Y/2);
			}
			left_bound=p_cen.x-loop;
			right_bound=p_cen.x+loop;
			low_bound=p_cen.y-loop;
			up_bound=p_cen.y+loop;
			if(low_bound<=str_Y/2)low_bound=step+str_Y/2;
			if(up_bound >= height - str_Y / 2) up_bound = height - str_Y / 2;
			if(x<=left_bound)
			{
				if(y>low_bound)y=y-step;
				else x=x+step;
			}
			else if(x>=right_bound)
			{ 
				if(y<up_bound)y=y+step;
				else x=x-step;
			}
			else
			{
				if(y<=low_bound)
				{
					x=x+step;
				}else if(y>=up_bound)
				{
					x=x-step;
				}else{
					x=x+step;
				}
			}
			
			if(x<=init_X&&y<=init_Y)
			{
				init_X=left_bound-step;
				init_Y=low_bound-step;
				if(init_Y<=str_Y/2)init_Y=step+str_Y/2;
				loop=loop+step;
			}
			
		}	while (x < width - str_X / 2);	
		
			if (min_size.x==0) min_size.x=str_X;
			if (min_size.x>str_X) min_size.x=str_X;
			if(min_size.y==0) min_size.y=str_Y;
			if(min_size.y>str_Y) min_size.y=str_Y;
			
		return null;
	}	
	
	private boolean isEmpty(int x, int y, int str_X, int str_Y, int sides)
	{
		if (x + str_X >= width || y + str_Y >= height) return false;
	
		for (int l = 0; l < current.size(); l++)
		{
			if (current.get(l).getBounds().contains(x, y) || current.get(l).getBounds().contains(x + str_X, y + str_Y))
			{
				return false;
			}
		}
		
		int i = 0;
		int j = 0;
		
		for (j = 0; j < str_X; j += 1)
			if (!isInShape(x + j, y + j * str_Y / str_X)) return false;	

		for (j = 0; j < str_X; j += 1)
			if (!isInShape(x + str_X - j, y + j * str_Y / str_X)) return false;		
		
		i = str_Y / 2;
		for (j = 0; j < str_X; j += 1)
			if (!isInShape(x + j, y + i)) return false;	
		
		j = str_X /2;
		for (i = 0; i < str_Y; i += 1)
			if (!isInShape(x + j, y + i)) return false;		
		
		i = 0;
		for (j = 0; j < str_X; j += 1)
			if (!isInShape(x + j, y + i)) return false;
		
		i = str_Y;
		for (j = 0; j < str_X; j += 1)
			if (!isInShape(x + j, y + i)) return false;	
		
		j = 0;
		for (i = 0; i < str_Y; i += 1)
			if (!isInShape(x + j, y + i)) return false;
		
		j = str_X;
		for (i = 0; i < str_Y; i += 1)
			if (!isInShape(x + j, y + i)) return false;		
		
		//		g.drawLine(x, y, x+str_X, y);
//		g.drawLine(x, y, x, y+str_Y);
//		g.drawLine(x+str_X, y, x+str_X, y+str_Y);
//		g.drawLine(x+str_X, y+str_Y, x, y+str_Y);
		// not too far away from other words
		
		//if is the first word;
		if (sides == 0)
		{
			return true;
		}
		x -= 2;
		y -= 2;
		str_X += 4;
		str_Y += 4;
		i = 0;
		int count = 0;
		for (j = 0; j < str_X; j += 1)
			if (isNearWord(x + j, y + i)) 
			{
			count ++;
			if (count == sides)
			{
//				g.drawOval(x+j,y+i, 3, 3);
				return true;
			}
			break;
			}
		
		i = str_Y;
		for (j = 0; j < str_X; j += 1)
			if (isNearWord(x + j, y + i)) 
			{
			count ++;
			if (count == sides)
			{
//				g.drawOval(x+j,y+i, 3, 3);
				return true;
			}
			break;
			}
		
		j = 0;
		for (i = 0; i < str_Y; i += 1)
			if (isNearWord(x + j, y + i)) 
			{
			count ++;
			if (count == sides)
			{
//				g.drawOval(x+j,y+i, 3, 3);
				return true;
			}
			break;
			}
		
		j = str_X;
		for (i = 0; i < str_Y; i += 1)
			if (isNearWord(x + j, y + i)) 
			{
			count ++;
			if (count == sides)
			{
//				g.drawOval(x+j,y+i, 3, 3);
				return true;
			}
			break;
			}

		return false;
	}

	private boolean isInShape(int a, int b)
	{
		if (bound_shape.contains(a, b) && img[currentTime].getRGB(a, b) == Color.white.getRGB()) 
		{
//			g.drawOval((int) d.getX(), (int) d.getY(), 1, 1);
			return true;
		} 
		else 
		{
			return false;
		}
	}
	
	private boolean isNearWord(int a, int b)
	{
		if (!bound_shape.contains(a, b))
		{
			return false;
		}
		if (img[currentTime].getRGB(a, b) != Color.white.getRGB()) 
		{
			return true;
		} 
		else 
		{
			return false;
		}
	}

	public void setBackground(BufferedImage bimg)
	{
		g.drawImage(bimg, 0, 0, width, height, 0, 0, bimg.getWidth(), bimg.getHeight(), null);
		repaint();
//		for(int i = 0; i < width; i++) 
//		{
//			for(int j = 0; j < height; j++) 
//			{
//				if(fimg.getRGB(i,j) == Color.white.getRGB())
//				{
//					img.setRGB(i, j, bimg.getRGB(i * bimg.getWidth() / width, j * bimg.getHeight() / height));
//				}
//				else 
//				{
//					img.setRGB(i, j, fimg.getRGB(i, j));
//				}
//			}
//		}
	}

	public void setColorStyle(String str) {
		color_style=str;
		
	}
}
