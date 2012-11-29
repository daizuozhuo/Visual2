import java.util.Collections;
import java.util.Vector;


public class Occurrence 
{
	private Label label;
	private int start;
	private int maxSize;
	private int size[];
	
	private static Vector<Occurrence> occu = new Vector<Occurrence>();
	
	
	public Occurrence(Label label, int start, int[] size)
	{
		this.label = label;
		this.start = start;
		this.size = size;
		maxSize = 0;
		
		for (int i = 0; i < size.length; i++)
		{
			maxSize = maxSize > size[i] ? maxSize : size[i];
		}
		
		occu.add(this);
	}
	

	public int getSize(int i) {return size[i - start];}
	public int getStart() {return start;}
	public Label getLabel() {return label;}
	public int getMax() {return maxSize;}
	public static void sort() {Collections.sort(occu, new MyComparator());}
}
