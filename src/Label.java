public class Label {
	public static int MAXCOUNT;
	private static final int SMOOTH = 7;	
	private static final int threshold = 15;
	private int count;
	private String word;
	private int timeLine[];
	private double size[];
	private double value; //how important the word is.

	public Label(String word)
	{
		this.word=word;
		timeLine = new int[MAXCOUNT];
		size = new double[MAXCOUNT];
		count=0;
	}

	public static void setMaxCount(int cnt){
		MAXCOUNT=cnt;
	}

	public String getWord(){
		return word;
	}

	public String getStr()
	{
		return word;
	}

	public void addTimeLine(int j,int day){
		if(day < MAXCOUNT){
			timeLine[day]+=j;
			count += j; 
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
		if (count == 0)
		{
			return;
		}

		int start = -1;
		value = 0;
		for (int i = 0; i < timeLine.length; i++)
		{						
			//calculate value
			if (timeLine[i] == 0)
				continue;
			double p = 1.0 * timeLine[i] / count;
			value -= p * Math.log(p); 
		}
		if (value < 0.01 || value > 2)
		{
			return;
		}

		for (int i = 0; i < timeLine.length; i++)
		{
			//calculate size
			double weightSum = 0;
			double s = 0;
			for (int j = -SMOOTH; j <= SMOOTH; j++)
			{
				if (i + j < 0 || i + j >= timeLine.length) continue;
				double weight = 1.0 / (1 + Math.abs(j));
				if (timeLine[i + j] > 0) 
					s += Math.log(timeLine[i + j]) * weight;
				weightSum += weight;
			}
			s /= weightSum;
//			if (s != 0.0) System.out.println(s);
			size[i] = s * 400;
			size[i] = size[i] > 60 ? 60 : size[i];

			//calculate occurrences
			if (size[i] > threshold && i < timeLine.length - 1)
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
//					System.out.println(i - start);
					if (i - start > 20)
					{
						int[] temp = new int[i - start];
						for (int j = start; j < i; j++)
						{
							temp [j - start] = (int)size[j];
						}
						new Occurrence(this, start, temp);
					}
					start = -1;
				}
			}
		}
	}

	public double getValue() {return value;}


}