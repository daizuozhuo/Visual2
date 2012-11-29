import java.util.Comparator;


public class Label{
	private int count;
	private static int MAXCOUNT=612;
	private String word;
	private int timeLine[];
	static int labelCount;
	public Label(String word){
		this.word=word;
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
	public String getWord(){
		return word;
	}
	public void addTimeLine(int j,int day){
		if(day < MAXCOUNT){
			timeLine[day]+=j;
			count+=j;
		}
	}
	public int getTimeLine(int i){
		return timeLine[i];
	}
	public int getCount(){
		return count;
	}
	
}
