package labView.protoType01;

import java.io.File;
import java.net.*;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ThreadForDownLoad extends Thread{
	public Handler myHandler;
	private String path;
	private URL url;
	private ShowMachinePDfList con;
	private int myMachineType;
	private String fileName;
	private File file;
	private String savedPath;
	public ThreadForDownLoad(Handler _myHandler ,ShowMachinePDfList _con, int _myMachineType, String _fileName,
			String _savedPath, File _file) {
		// TODO Auto-generated constructor stub
		con = _con;
		myMachineType = _myMachineType;
		fileName = _fileName;
		file = _file;
		savedPath = _savedPath;
		myHandler = _myHandler;
	}
	
	public void run(){
		try {
			URL pdfUrl = DefineMachineType.getPdfAddressFromHomePage(myMachineType, fileName);
			new DownloadPdfData().downLoadToSdcard(savedPath, pdfUrl);
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Message msg = Message.obtain(myHandler, 12, intent);
			myHandler.sendMessage(msg);
			
	     } catch (MalformedURLException e) {
	    	 // TODO Auto-generated catch block
	    	 e.printStackTrace();
	    	 Message msg = Message.obtain(myHandler, 13);
	    	 myHandler.sendMessage(msg);
	    	 return ;
	     }
	}
}
