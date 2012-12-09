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
		Label.setMaxCount(docs.length);
		for(int i=0; i<docs.length;i++){
//		for(int i=0; i<1;i++){
			for(File doc:docs[i].listFiles()){
				processDoc(doc,i);
			}
			
		}
	}
	
	public int getDate(){
		return date;
	}
	
	public List<Label> getLabels(){
		Collection<Label> mylabels=labels.values();
		List<Label> listlabels = new ArrayList<Label>(mylabels);
		return listlabels;
		
	}

	
	private void processDoc(File doc,int day){
		if(!doc.isFile()) return ;
		String content;
		try {
			FileInputStream fstream = new FileInputStream(doc);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			while((content=br.readLine())!= null) {
		//		System.out.println(content);
				processContent(content,day);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void processContent(String content,int day){
		String[] strs= content.split(" "); 
		String word= strs[0];
		int cnt=0;
		try{
			cnt = Integer.parseInt(strs[1]); 
		} catch(Exception e){
			e.printStackTrace();
		}
		
		//search the word

		if(labels.containsKey(word)){
			Label label = labels.get(word);
			label.addTimeLine(cnt,day);
			labels.put(word,label);
		} else {
			Label label = new Label(word);
			label.addTimeLine(cnt,day);
			labels.put(word,label);
		}
	}
	private void processContent(String content){
		String[] strs= content.split(" "); 
		String word= strs[0];
		int cnt=0;
		try{
			cnt = Integer.parseInt(strs[1]); 
		} catch(Exception e){
			e.printStackTrace();
		}
		
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
