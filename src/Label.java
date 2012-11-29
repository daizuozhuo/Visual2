import java.util.Comparator;


public class Label{
	private int count;
	private int index;
	private final int MAXCOUNT=612;
	private String word;
	private int timeLine[];
	static int labelCount;
	public Label(String word){
		this.word=word;
		timeLine = new int[MAXCOUNT];
		count=0;
		index=0;
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
	
}
