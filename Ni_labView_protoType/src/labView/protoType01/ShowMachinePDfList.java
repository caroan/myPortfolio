package labView.protoType01;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShowMachinePDfList extends Activity{
	private ProgressDialog waitForAcceptDialog;
	ArrayAdapter<String> machineGuide;
	ArrayList<String> myPdfNames;
	MachinePdfListAdapter myAdapter;
	String sdcardPath;
	private int myMachineType;
	
	public Handler myDownLoadHandler = new Handler(){
		public void handleMessage(Message msg){
			waitForAcceptDialog.dismiss();
			switch(msg.what){
			case 12:
				try{
					startActivity(((Intent)msg.obj));
				}catch (ActivityNotFoundException  e) {
					// TODO: handle exception
					Toast.makeText(ShowMachinePDfList.this, "해당 파일을 열수가 없습니다.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 13:
				 Toast.makeText(ShowMachinePDfList.this, "인터넷이 연결이 안됩니다.", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.showmachinepdflist);
	        
	        setMachineType();
	        setListName();
	        setSavedInstanceState(savedInstanceState);
	        if(isFoundedSdCard() == false){
	        	CanNotFindSdCard();
	        }
	        myPdfNames = new ArrayList<String>();
	        callPdfList(myPdfNames);
	        
	        myAdapter = new MachinePdfListAdapter(this, R.layout.pdflistcontents ,myPdfNames);
	        ListView myPdfList = (ListView)findViewById(R.id.machinepdflist);
	        myPdfList.setAdapter(myAdapter);
	        myPdfList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(myPdfNames.get(arg2).equals("FAQ 가기")){
						openFaq();
						return;
					}
					callPdfData(myPdfNames.get(arg2).toString());
				}
			});
	 }
	 private void setListName() {
		// TODO Auto-generated method stub
		((TextView)findViewById(R.id.txt_machinename))
		.setText(DefineMachineType.getMachineName(myMachineType) + " 문서들.");
	}
	private void CanNotFindSdCard(){
		 sdcardPath = Environment.MEDIA_UNMOUNTED;
	    Toast.makeText(this, "Sd Card가 없습니다.", Toast.LENGTH_LONG).show();
	 }

	 private void callPdfList(ArrayList<String> _myGuides){
		 sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	     String savePath = sdcardPath + "/LABVIEW/" + DefineMachineType.getMachineName(myMachineType);
	     File willBeOpenFile = new File( savePath );
	     if( ! (willBeOpenFile.exists())){
	     	willBeOpenFile.mkdirs();
	     }
	     DefineMachineType.getMachineNeedPdfNames(_myGuides, myMachineType);
	 }
	 
	private Boolean isFoundedSdCard() {
		// TODO Auto-generated method stub
		String isThereSdcard = Environment.getExternalStorageState();
	    if(isThereSdcard.equals(Environment.MEDIA_MOUNTED)){
	    	return true;
        }
	    else{
	    	return false;
	    }
	}

	private void setSavedInstanceState(Bundle _savedInstanceState) {
		// TODO Auto-generated method stub
	}

	private void setMachineType(){
		 myMachineType = getIntent().getIntExtra("MachineType", DefineMachineType.ERROR_NOTFOUND_MACHINETYPE);
	     if(myMachineType == DefineMachineType.ERROR_NOTFOUND_MACHINETYPE){
	        Toast.makeText(this, "기기 선택에 예상치 못한 문제가 발생해 종료합니다.", Toast.LENGTH_SHORT).show();
	        finish();
	     }
	 }

	private void openFaq(){
		try{
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ni.com/support/ko/"));
			startActivity(intent);
		}catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "Can't not open Network", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void callPdfData(String _fileName){
		String savedPath = sdcardPath + "/LABVIEW/"+DefineMachineType.getMachineName(myMachineType)+"/"+_fileName;
		File file = new File(savedPath);
		String[] r = new String[2];
		r[0] = null;
		r[1] = null;
		r = _fileName.split("\\.");
		
		//Log.d("R",r[0] + "   ");
		
		if(file.exists()){
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try{
				startActivity(intent);
			}catch (ActivityNotFoundException  e) {
				// TODO: handle exception
				Toast.makeText(ShowMachinePDfList.this, "PDF를 열수 있는 마땅한 뷰어가 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
		else if(r.length < 2){
			try {
				String netAddress = DefineMachineType.getPdfAddressFromHomePage(myMachineType, _fileName).toString();	
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(netAddress));
				startActivity(intent);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			waitForAcceptDialog = ProgressDialog.show(ShowMachinePDfList.this, "Wait", "해당 파일이 없어 다운받아 여는 중 입니다.");
			ThreadForDownLoad downThread = new ThreadForDownLoad(myDownLoadHandler ,this, myMachineType, _fileName, savedPath, file);
			downThread.start();
			/*try {
				URL pdfUrl = DefineMachineType.getPdfAddressFromHomePage(myMachineType, _fileName);
				new DownloadPdfData().downLoadToSdcard(savedPath, pdfUrl);
				Uri path = Uri.fromFile(file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(path, "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				waitForAcceptDialog.dismiss();
				try{
					startActivity(intent);
				}catch (ActivityNotFoundException  e) {
					// TODO: handle exception
					waitForAcceptDialog.dismiss();
					Toast.makeText(ShowMachinePDfList.this, "해당 파일을 열수가 없습니다.", Toast.LENGTH_SHORT).show();
				}
		     } catch (MalformedURLException e) {
		    	 // TODO Auto-generated catch block
		    	 e.printStackTrace();
		    	 waitForAcceptDialog.dismiss();
		    	 Toast.makeText(this, "인터넷 연결이 안됩니다.", Toast.LENGTH_LONG).show();
		    	 return ;
		     }*/
		}
	}
	
/*	private void openPdf(String _fileName){
		File file = new File(sdcardPath + "/LABVIEW/"+DefineMachineType.getMachineName(myMachineType)+"/"+_fileName);
//		Toast.makeText(this, sdcardPath + "/LABVIEW/"+DefineMachineType.getMachineName(myMachineType)+"/" +_fileName, Toast.LENGTH_LONG).show();
		if(file.exists()){
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try{
//				Toast.makeText(DetailMachine.this, "existPdf", Toast.LENGTH_SHORT).show();
				startActivity(intent);
			}catch (ActivityNotFoundException  e) {
				// TODO: handle exception
				Toast.makeText(DetailMachine.this, "PDF를 열수 있는 마땅한 뷰어가 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
		else{
//Toast.makeText(DetailMachine.this, "file not exist sdcard", Toast.LENGTH_SHORT).show();
			
		}
	}*/
	 
	 Button.OnClickListener menuBtnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_gofag:
				openFaq();
				break;
			}
		}
	};
}

class MachinePdfListAdapter extends BaseAdapter{
	private Context myContext;
	private ArrayList<String> myItems;
	private int myLayout;
	private LayoutInflater inflater;
	private int viewPos;
	
	public MachinePdfListAdapter(Context _context, int _layout, ArrayList<String> _myItems) {
		// TODO Auto-generated constructor stub
		myContext = _context;
		myItems = _myItems;
		myLayout = _layout;
		inflater = (LayoutInflater)myContext.getSystemService(myContext.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return myItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return myItems.get(position).toString();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = inflater.inflate(myLayout, parent, false);
		}
		TextView text = (TextView)convertView.findViewById(R.id.txt_pdfname);
		text.setText(myItems.get(position).toString());
		
		return convertView;
	}
}