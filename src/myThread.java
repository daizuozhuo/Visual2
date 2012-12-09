import java.awt.image.BufferedImage;


public class myThread extends Thread
{
	private Wordle wordle;
	private BufferedImage[] img;
	private int i;

	public myThread(Wordle wordle, BufferedImage[] img) 
	{
		this.wordle = wordle;
		this.img = img;
		i = 500;
	}

	@Override
	public void run() 
	{
		if (i >= img.length) return;
		wordle.setImg(img[i]);
   		wordle.repaint();
   		i++;
   		
   		System.out.println(i);
		try 
		{
			Thread.sleep(1000);
			run();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
	}

}
