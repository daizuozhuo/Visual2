import java.util.Comparator;


public class Label {
	public static int MAXCOUNT=612;
	private static final int SMOOTH = 5;	
	private static final int threshold = 20;
	private int count;
	private int index;
	private String word;
	private int timeLine[];
	private int size[];
	private double value; //how important the word is.
	
	public Label(String word)
	{
		this.word=word;
		//TODO:: this is wrong...
		timeLine = new int[MAXCOUNT];
		count=0;
	}

	public static void setMaxCount(int cnt){
		MAXCOUNT=cnt;
	}
	@SuppressWarnings("rawtypes")
	public static Comparator compareCount(){
		return new Comparator(){
			public int compare(Object o1, Object o2){
				Label a=(Label)o1;
				Label b=(Label)o2;
				return a.getCount()-b.getCount();
			}
		};
	}

	
	public String getStr()
	{
		return word;
	}
	public void addTimeLine(int j,int day){
		if(day < MAXCOUNT){
			timeLine[day]+=j;
		}
	}
	
	public void addTimeLine(int j){
		if(index < MAXCOUNT){
			timeLine[index]+=j;
			index++;
			count+=j;
		}
	}
	
	public int getTimeLine(int i){
		return timeLine[i];
	}
	
	public int getCount(){
		return count;
	}
	
	public void init()
	{
		int start = -1;
		value = Math.log(count);
		for (int i = 0; i < timeLine.length; i++)
		{
			//calculate size
			double weightSum = 0;
			double s = 0;
			for (int j = -SMOOTH; j <= SMOOTH; j++)
			{
				double weight = 1 / (1 + Math.abs(j));
				s += Math.log(timeLine[i + j]) * weight;
				weightSum += weight;
			}
			s /= weightSum;
			size[i] = (int)s;
			
			//calculate occurrences
			if (size[i] > threshold)
			{
				//starts or continues
				if (start == -1)
					start = i;
			}
			else
			{
				//ends
				if (start != -1)
				{
					start = -1;
					int[] temp = new int[i = start];
					for (int j = start; i < i; j++)
					{
						temp [j - start] = size[j];
					}
					new Occurrence(this, start, temp);
				}
			}
			
			//calculate value
			if (timeLine[i] == 0)
				continue;
			double p = timeLine[i] / count;
			value *= Math.pow(p, p); 
		}
	}
	
	public double getValue() {return value;}
	
	
}
