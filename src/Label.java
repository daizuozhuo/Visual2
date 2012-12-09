

public class Label {
	private int count;
	private int index;
	private final int MAXCOUNT=612;
	private String word;
	private int timeLine[];
	private int size[];
	private double value; //how important the word is.
	
	private static final int SMOOTH = 5;
	private static final int threshold = 20;
	
	public Label(String word)
	{
		this.word=word;
		//TODO:: this is wrong...
		timeLine = new int[MAXCOUNT];
		count=0;
		index=0;
	}

	
	public String getWord()
	{
		return word;
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
