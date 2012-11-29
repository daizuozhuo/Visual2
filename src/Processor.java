import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
public class Processor {;
	private int date;
	private Hashtable<String,Label> labels;
	
	//this is the sample usage
//	public static void main(String[] args){
//		Processor pro=new Processor("Sony");
//		List<Label> labels=pro.getLabels();
//		int max=labels.size();
//		Label label=labels.get(max-1);
//		System.out.println(label.getWord()+" "+label.getCount());
//		label=labels.get(max-2);
//		System.out.println(label.getWord()+" "+label.getCount());
//		
//	}
	
	Processor(String company){
		File filepath=new File("data/"+company+"/docs.stas");
		File docs[] = filepath.listFiles();
		date=Integer.parseInt(docs[0].getName());
		labels= new Hashtable<String,Label>();
		for(int i=0; i<docs.length;i++){
//		for(int i=0; i<1;i++){
			for(File doc:docs[i].listFiles()){
				processDoc(doc);
			}
			
		}
	}
	
	public int getDate(){
		return date;
	}
	
	@SuppressWarnings("unchecked")
	public List<Label> getLabels(){
		Collection<Label> mylabels=labels.values();
		List<Label> listlabels = new ArrayList<Label>(mylabels);
		Collections.sort(listlabels,Label.compareCount());
		return listlabels;
		
	}
	
	private void processDoc(File doc){
		if(!doc.isFile()) return ;
		String content;
		try {
			FileInputStream fstream = new FileInputStream(doc);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while((content=br.readLine())!= null) {
		//		System.out.println(content);
				processContent(content);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processContent(String content){
		String[] strs= content.split(" "); 
		String word= strs[0];
		int cnt = Integer.parseInt(strs[1]); 
		
		//search the word

		if(labels.containsKey(word)){
			Label label = labels.get(word);
			label.addTimeLine(cnt);
			labels.put(word,label);
		} else {
			Label label = new Label(word);
			label.addTimeLine(cnt);
			labels.put(word,label);
		}
	}

}
