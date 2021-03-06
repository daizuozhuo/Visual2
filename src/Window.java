import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;


@SuppressWarnings("serial")
public class Window extends JFrame {

	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuPreference;
	private JMenu menuColorStyle;
	private JMenuItem menuItem_start_single;
	private JMenuItem menuItem_back;
	private JMenuItem menuItem_save;
	private JMenuItem menuItem_exit;
	private JRadioButtonMenuItem menuItem_warm;
	private JCheckBoxMenuItem menuItem_update;
	private Wordle wordle;
	private Painter painter;
	private final int height = 512; // height of the picture
	private final int width = 512; // width of the picture
	
	public Window(String title)
	{
		super(title);
		setLocation(30,50);
		setVisible(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		wordle = new Wordle(width, height);
		setContentPane(wordle);
		setResizable(false);
		setMenubar();
		pack();
	}
	
	public void run() 
	{
	}
	
	//set menu;
	private void setMenubar()
	{
		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menuFile = new JMenu("File");		
		menuPreference = new JMenu("Preference");
		menuColorStyle = new JMenu("Color Style");
		menuBar.add(menuFile);
		menuBar.add(menuPreference);

		//a group of JMenuItems
		
		menuItem_start_single = new JMenuItem("Start",KeyEvent.VK_S);
		menuItem_back = new JMenuItem("Set background image",KeyEvent.VK_B);
		menuItem_save = new JMenuItem("Save Image",KeyEvent.VK_I);
		menuItem_exit = new JMenuItem("Exit",KeyEvent.VK_E);
		menuItem_update = new JCheckBoxMenuItem("Real Time Update", true);
		menuItem_warm = new JRadioButtonMenuItem("warmth style");
		
		menuItem_warm.setSelected(true);
		menuItem_warm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
            {
                painter.setColorStyle("warm");
            }
        });      
		
		menuItem_start_single.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
            {
                start();
            }
        });   

		menuItem_back.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
            {
                choose_backimg();
            }
        });    
		
		menuItem_save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
            {
                saveImage();
            }
        });   
		
		menuItem_exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });    
		
		
		
		menuFile.add(menuItem_start_single);
		menuFile.add(menuItem_back);
		menuFile.add(menuItem_save);
		menuFile.add(menuItem_exit);
		menuPreference.add(menuItem_update);
		menuColorStyle.add(menuItem_warm);
		menuPreference.add(menuColorStyle);
		this.setJMenuBar(menuBar);
	}
	
	private void start()
	{        
        // Analyse input init all labels
		Processor pro=new Processor("Sony");
		List<Label> labels=pro.getLabels();
		int[] date=pro.getDate();
		Iterator<Label> i = labels.iterator();
		while(i.hasNext())
		{
			i.next().init();
		}
        
//       	for (int i = 0 ; i < result.size(); i++) result.get(i).print();
//       	System.out.println("------------------ " + result.size() + " keywords found ------------------");

       	painter = new Painter(width, height, wordle,date);
	
   		JOptionPane.showMessageDialog(null, painter.paint(), "Message", 1/*, new ImageIcon(Toolkit.getDefaultToolkit().getImage("res/icon.jpg"))*/);
   	
   		BufferedImage[] img = painter.getImg();
   		myThread thread = new myThread(wordle, img);
   		thread.start();
	}
		
	public void choose_backimg()
	{
		JFileChooser chooser = new JFileChooser();
	    int returnVal = chooser.showOpenDialog(this);
	    if(returnVal == JFileChooser.APPROVE_OPTION)
	    {
	       System.out.println("You choose to open this file: " +
	            chooser.getSelectedFile().getName());
	       File file=chooser.getSelectedFile();
	       try 
	       {
	    	   BufferedImage bimg = ImageIO.read(file);
	    	   if (bimg == null) 
	    	   {
	    	   		JOptionPane.showMessageDialog(null, "Invalid image!", "Error!", 1);
	    	   		return;	    		   
	    	   }
	    	   painter.setBackground(bimg);
	       } 
	       catch (IOException e)
	       {
				System.out.println("use white bacground");
	       }
	    }
	}

	public void saveImage()
	{
		JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    int flag = chooser.showSaveDialog(this);
	    if (flag == JFileChooser.APPROVE_OPTION)
	    {
	    	String path = chooser.getSelectedFile().getAbsolutePath() + "/output.gif";
	    	try
	    	{
	    		if (wordle.saveImage(path) == 1)  // Save the picture
	    		{
			        JOptionPane.showMessageDialog(null, "Cannot save empty image!", "Save Image", 1);
			        return;	    			
	    		}
		        JOptionPane.showMessageDialog(null, "Successfully saved as " + path, "Save Image", 1);
	    	} 
	    	catch (IOException e2)
	    	{
	        JOptionPane.showMessageDialog(null, "Save Error!", "Save Image", 1);
	        e2.printStackTrace();
	    	}     
	    }
	}
	
}