
public class Label {
	private int count=0;
	private String word;
	private int timeLine[];
	static int labelCount;
	public Label(String word){
		this.word=word;
		timeLine = new int[612];
	}
	public String getWord(){
		return word;
	}
	public int getTimeLine(int i){
		return timeLine[i];
	}
	public int getCount(){
		return count;
	}
	
}
