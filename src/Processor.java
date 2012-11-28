import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
public class Processor {
	Processor(String company){
		File filepath=new File("data/"+company+"/docs.stas");
		File docs[] = filepath.listFiles();
		for(int i=0; i<docs.length;i++){
			for(File doc:docs[i].listFiles()){
				processDoc(doc);
			}
			
		}
	}
	
	private void processDoc(File doc){
		if(!doc.isFile()) return ;
		String content;
		try {
			FileInputStream stream = new FileInputStream(doc);
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			content= Charset.defaultCharset().decode(bb).toString();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
