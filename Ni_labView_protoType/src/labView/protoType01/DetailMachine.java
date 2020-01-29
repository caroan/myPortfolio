package labView.protoType01;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.impl.conn.tsccm.WaitingThreadAborter;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DetailMachine extends Activity{
	ArrayAdapter<String> machineGuide;
	String sdcardPath;
	
	private ScrollView wholeScroll;
	private LinearLayout wholeLinear;
	private LinearLayout layout_graph;
	private LinearLayout layout_menu;
	private LinearLayout layout_webConnection;
	private int myMachineType; 
	private Spinner selectPdf;
	private ImageView showGraph;
	private ProgressDialog wiatForAcceptDialog;
	private TakeImageFromServer imageTaker;
	private Thread threadForImageTaker;
	private boolean isShowingWaitDialog;
	private boolean gotImaageFromServer;
	private Matrix matrixForGraph;
	
	private float graphViewWidth;
	private float graphViewHeight;
	private float graphContentWidth;
	private float graphContentHeight;
	private float graphSizeChangeUnit;
	
	float graphMotionFirstPoint_X;
	float graphMotionFirstPoint_Y;
	float graphMotionCurrentPoint_X;
	float graphMotionCurrentPoint_Y;
	
	public Handler myHandler = new Handler(){
		public void handleMessage(Message msg){
			if(isShowingWaitDialog){
				wiatForAcceptDialog.dismiss();
				isShowingWaitDialog = false;
			}
			switch(msg.what){
			case 0:
				showGraph.setImageBitmap((Bitmap)msg.obj );
				if(gotImaageFromServer==false){
					initGraphViewMatrix();
					gotImaageFromServer = true;
				}
				break;
			case 1:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("잘못된 주소-파일명");
				makeSettingLayoutMode();
				break;
			case 2:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("권한없음");
				makeSettingLayoutMode();
				break;
			case 3:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("기타문제");
				makeSettingLayoutMode();
				break;
			case 4:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("잘못된 주소-ip,port");
				makeSettingLayoutMode();
				break;
			case 5:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("휴대폰 장비 이상");
				makeSettingLayoutMode();
				break;
			case 6:
				((TextView)findViewById(R.id.txtview_resulttryconnect)).setText("알수없는 문제");
				makeSettingLayoutMode();
				break;
			case 11:
				Toast.makeText(DetailMachine.this, "이미지가 너무 커서 감당이 안됩니다.", Toast.LENGTH_LONG).show();
				finish();
				break;
			}
		}
	};
	
	private void makeSettingLayoutMode(){
		layout_menu.setVisibility(LinearLayout.VISIBLE);
		layout_graph.setVisibility(LinearLayout.GONE);
		layout_webConnection.setVisibility(LinearLayout.VISIBLE);
	}
	private void makeGraphLayoutMode(){
		layout_menu.setVisibility(LinearLayout.VISIBLE);
		layout_graph.setVisibility(LinearLayout.VISIBLE);
		layout_webConnection.setVisibility(LinearLayout.GONE);
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {     
        	if(imageTaker != null){
            	imageTaker.setIsRun(false);
            }
        	finish();
        	return true;
        }
        return super.dispatchKeyEvent(event);
    }
	
	public void onSaveInstanceState(Bundle outState){
    	outState.putString("ipaddress", ((EditText)findViewById(R.id.edittxt_setip)).getText().toString());
    	outState.putString("port", ((EditText)findViewById(R.id.edittxt_setport)).getText().toString());
    	outState.putString("filename", ((EditText)findViewById(R.id.edittxt_setfilename)).getText().toString());		
    }
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        //makeWholeScroll();
	        makeWholeLinearInWholeScroll();
	        makeLayout_webConnectionInWholeLinear();
	        makeLayout_menuInWholeLinear();
	        makeLayout_graphInWholeLinear();
	        
	        addAllSubLayoutInWholeLayout();

	        setContentView(wholeLinear);
	        
	        settingEventListener();
	        
	        setMachineType();
	        setSavedInstanceState(savedInstanceState);
	        setSpinner();
	        
	        setNecessaryEtcVariable();
	 }
	 private void setNecessaryEtcVariable(){
		 gotImaageFromServer = false;
		 showGraph = (ImageView)findViewById(R.id.imageview_showgraph);
		 showGraph.setOnTouchListener(graphViewMovingEvent);
		 graphSizeChangeUnit = 0.1f;
	 }
	ImageView.OnTouchListener graphViewMovingEvent = new ImageView.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				graphMotionFirstPoint_X = event.getX();
				graphMotionFirstPoint_Y = event.getY();
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_MOVE:
				graphMotionCurrentPoint_X = event.getX();
				graphMotionCurrentPoint_Y = event.getY();
				showGraph.scrollBy( (int)(graphMotionFirstPoint_X - graphMotionCurrentPoint_X),
						(int)(graphMotionFirstPoint_Y - graphMotionCurrentPoint_Y) );
				graphMotionFirstPoint_X = graphMotionCurrentPoint_X;
				graphMotionFirstPoint_Y = graphMotionCurrentPoint_Y;
				return true;
			default:
				break;
			}
			return false;
		}
	};
	 
	 private int makePortNum(){
		 int portNum;
		 try{
			portNum = Integer.parseInt( ((EditText)findViewById(R.id.edittxt_setport)).getText().toString() );
			//Toast.makeText(this, "this", Toast.LENGTH_LONG).show();
		 }catch (Exception e) {
			// TODO: handle exception
			 portNum = 0;
			 Toast.makeText(this, "port번호가 제대로 입력되지 않습니다.", Toast.LENGTH_LONG).show();
		}
		 return portNum;
	 }
	 
	 private void setThread() {
		// TODO Auto-generated method stub
		 if(imageTaker == null){	// 초기
	    	 imageTaker = new TakeImageFromServer(myHandler,
					 ((EditText)findViewById(R.id.edittxt_setip)).getText().toString(),
					 makePortNum(),
					 ((EditText)findViewById(R.id.edittxt_setfilename)).getText().toString()
					 );
	     }
		else if(imageTaker != null && imageTaker.getIsRun()!= true){	// 실행 중단 상태
			imageTaker.setAll( ((EditText)findViewById(R.id.edittxt_setip)).getText().toString(),
					makePortNum(),
					((EditText)findViewById(R.id.edittxt_setfilename)).getText().toString());
			imageTaker.setIsRun(true);
			threadForImageTaker = null;
		}
		else{
//			Toast.makeText(this, "쓰레드 문제입니다.", Toast.LENGTH_LONG).show();
			imageTaker.setIsRun(false);
			finish();
		}
		threadForImageTaker = new Thread(imageTaker);
    	threadForImageTaker.setDaemon(true);
    	threadForImageTaker.start();
	}

	 private void CanNotFindSdCard(){
		 sdcardPath = Environment.MEDIA_UNMOUNTED;
	    Toast.makeText(this, "Sd Card가 없습니다.", Toast.LENGTH_LONG).show();
	 }
/*	 private void CanFindSdCard(ArrayList<String> _myGuides){
		 sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	     String savePath = sdcardPath + "/LABVIEW/" + DefineMachineType.getMachineName(myMachineType);
	     File willBeOpenFile = new File( savePath );
	     if( ! (willBeOpenFile.exists())){
	     	willBeOpenFile.mkdirs();
	     }
	     File[] pdfList = willBeOpenFile.listFiles();
	     for(File files : pdfList){
     		_myGuides.add(files.getName());
     	}
	 }*/
	 private void callPdfList(ArrayList<String> _myGuides){
		 sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
	     String savePath = sdcardPath + "/LABVIEW/" + DefineMachineType.getMachineName(myMachineType);
	     File willBeOpenFile = new File( savePath );
	     if( ! (willBeOpenFile.exists())){
	     	willBeOpenFile.mkdirs();
	     }
	     DefineMachineType.getMachineNeedPdfNames(_myGuides, myMachineType);
	 }
	 
	private void setSpinner() {
		// TODO Auto-generated method stub
		selectPdf = (Spinner)findViewById(R.id.spin_showpdf);
	    selectPdf.setPrompt("참조하고 싶은 PDF문서를 고르세요.");
	    ArrayList<String> myGuides = new ArrayList<String>();
	    myGuides.add("PDF설명서");
	        
	    String isThereSdcard = Environment.getExternalStorageState();
	    if(isThereSdcard.equals(Environment.MEDIA_MOUNTED)){
//	    	CanFindSdCard(myGuides);
	    	callPdfList(myGuides);
        }
	    else{
	    	CanNotFindSdCard();
	    }
	    machineGuide = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,myGuides);
	    selectPdf.setAdapter(machineGuide);
	        
	    selectPdf.setOnItemSelectedListener(new OnItemSelectedListener(){
	    	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
	    		if(position != 0){
//	    			openPdf(machineGuide.getItem(position).toString());
	    			callPdfData(machineGuide.getItem(position).toString());
	    		}
	    	}

	    	@Override
	    	public void onNothingSelected(AdapterView<?> arg0) {
	    		// TODO Auto-generated method stub
	    		Toast.makeText(DetailMachine.this, "취소됨", Toast.LENGTH_SHORT).show();
				}
	    });
	}

	private void setSavedInstanceState(Bundle _savedInstanceState) {
		// TODO Auto-generated method stub
		 if(_savedInstanceState != null){
	        	((EditText)findViewById(R.id.edittxt_setip)).setText(_savedInstanceState.getString("ipaddress"));
				((EditText)findViewById(R.id.edittxt_setport)).setText(_savedInstanceState.getString("port"));
				((EditText)findViewById(R.id.edittxt_setfilename)).setText(_savedInstanceState.getString("filename"));
	        }
	}

	private void setMachineType(){
		 myMachineType = getIntent().getIntExtra("MachineType", DefineMachineType.ERROR_NOTFOUND_MACHINETYPE);
	     if(myMachineType == DefineMachineType.ERROR_NOTFOUND_MACHINETYPE){
	        Toast.makeText(this, "기기 선택에 예상치 못한 문제가 발생해 종료합니다.", Toast.LENGTH_SHORT).show();
	        finish();
	     }
	     ( (TextView)findViewById(R.id.txtview_graphname) ).setText(DefineMachineType.getMachineName(myMachineType));
	 }
	 
	 private void settingEventListener() {
		// TODO Auto-generated method stub
		 findViewById(R.id.btn_confirmconnect).setOnClickListener(connectionPartListener);
		 findViewById(R.id.btn_refreshall).setOnClickListener(connectionPartListener);
		 
		 findViewById(R.id.btn_gofag).setOnClickListener(menuBtnClickListener);
		 findViewById(R.id.btn_resetip).setOnClickListener(menuBtnClickListener);
		 
		 findViewById(R.id.btn_adjustview).setOnClickListener(graphSizeChange);
		 findViewById(R.id.btn_expandgraph).setOnClickListener(graphSizeChange);
		 findViewById(R.id.btn_reducegraph).setOnClickListener(graphSizeChange);
	}

	private void addAllSubLayoutInWholeLayout() {
		// TODO Auto-generated method stub
		wholeLinear.addView(layout_menu);
		wholeLinear.addView(layout_webConnection);
        wholeLinear.addView(layout_graph);
        //wholeScroll.addView(wholeLinear);
	}
	
	private void makeLayout_graphInWholeLinear() {
		// TODO Auto-generated method stub
		layout_graph = (LinearLayout)View.inflate(this, R.layout.detailmachine_graph, null);
		layout_graph.setVisibility(LinearLayout.GONE);
	}
	
	private void setGraphHeightAndWidth(){
		try{
			graphContentHeight = ((ImageView)findViewById(R.id.imageview_showgraph)).getDrawable().getIntrinsicHeight();
			graphContentWidth = ((ImageView)findViewById(R.id.imageview_showgraph)).getDrawable().getIntrinsicWidth();
		}catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "ERR - 받아온 이미지가 없습니다.", Toast.LENGTH_LONG).show();
			return ;
		}
		graphViewWidth = ((ImageView)findViewById(R.id.imageview_showgraph)).getWidth();
		graphViewHeight = ((ImageView)findViewById(R.id.imageview_showgraph)).getHeight();
		
		//graphViewHeight = graphViewWidth*graphContentHeight/graphContentWidth;
	}
	
	private void initGraphViewMatrix(){
		setGraphHeightAndWidth();
		matrixForGraph = new Matrix();
		matrixForGraph.setScale(graphViewWidth/graphContentWidth, graphViewHeight/graphContentHeight);
		((ImageView)findViewById(R.id.imageview_showgraph)).setImageMatrix(matrixForGraph);
	}

	private void makeLayout_menuInWholeLinear() {
		// TODO Auto-generated method stub
		layout_menu = (LinearLayout)View.inflate(this, R.layout.detailmachine, null);
		layout_menu.setVisibility(LinearLayout.VISIBLE);
	}
	
	private void makeLayout_webConnectionInWholeLinear(){
		layout_webConnection = (LinearLayout)View.inflate(this, R.layout.detailmachine_showconnection,null);
	}

	private void makeWholeLinearInWholeScroll() {
		// TODO Auto-generated method stub
		wholeLinear = new LinearLayout(this);
        wholeLinear.setOrientation(LinearLayout.VERTICAL);
        wholeLinear.setBackgroundColor(Color.WHITE);
	}

	private void makeWholeScroll(){
		wholeScroll = new ScrollView(this);
		wholeScroll.setBackgroundColor(Color.WHITE);
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
	//problem0111
	private void callPdfData(String _fileName){
		String savedPath = sdcardPath + "/LABVIEW/"+DefineMachineType.getMachineName(myMachineType)+"/"+_fileName;
		File file = new File(savedPath);
		if(file.exists()){
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try{
				startActivity(intent);
			}catch (ActivityNotFoundException  e) {
				// TODO: handle exception
				Toast.makeText(DetailMachine.this, "PDF를 열수 있는 마땅한 뷰어가 없습니다.", Toast.LENGTH_SHORT).show();
			}
		}
		else{
			try {
				URL pdfUrl = DefineMachineType.getPdfAddressFromHomePage(myMachineType, _fileName);
				new DownloadPdfData().downLoadToSdcard(savedPath, pdfUrl);
				Uri path = Uri.fromFile(file);
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(path, "application/pdf");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				try{
					startActivity(intent);
				}catch (ActivityNotFoundException  e) {
					// TODO: handle exception
					Toast.makeText(DetailMachine.this, "해당 파일을 열수가 없습니다.", Toast.LENGTH_SHORT).show();
				}
		     } catch (MalformedURLException e) {
		    	 // TODO Auto-generated catch block
		    	 e.printStackTrace();
		    	 Toast.makeText(this, "인터넷 연결이 안됩니다.", Toast.LENGTH_LONG).show();
		    	 return ;
		     }
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
			case R.id.btn_resetip:
				makeSettingLayoutMode();
				if( imageTaker != null ){
					imageTaker.setIsRun(false);
				}
				break;
			}
		}
	};
	
	Button.OnClickListener graphSizeChange = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_adjustview:
				setGraphHeightAndWidth();
				matrixForGraph.setScale(graphViewWidth/graphContentWidth, graphViewHeight/graphContentHeight);
				((ImageView)findViewById(R.id.imageview_showgraph)).setImageMatrix(matrixForGraph);
				graphSizeChangeUnit = 0.1f;
				break;
			case R.id.btn_expandgraph:
				if(graphSizeChangeUnit < 2){	//너무 커지지 않게 막는다.
					matrixForGraph.postScale(1.1f, 1.1f);
					((ImageView)findViewById(R.id.imageview_showgraph)).setImageMatrix(matrixForGraph);
					graphSizeChangeUnit += 0.1f;
				}
				break;
			case R.id.btn_reducegraph:
				if(graphSizeChangeUnit > 0){	//사이즈 맞춘 것에서 더 작아지면 안된다.
					graphSizeChangeUnit -= 0.1f;
					matrixForGraph.postScale(0.9f,0.9f);
					((ImageView)findViewById(R.id.imageview_showgraph)).setImageMatrix(matrixForGraph);
				}
				break;
			}
		}
	};
	
	Button.OnClickListener connectionPartListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.btn_confirmconnect:
				setThread();
				makeGraphLayoutMode();
				isShowingWaitDialog = true;
				wiatForAcceptDialog = ProgressDialog.show(DetailMachine.this, "Wait", "접속중 입니다.(최대 15초)");
				break;
			case R.id.btn_refreshall:
				((EditText)findViewById(R.id.edittxt_setip)).setText("");
				((EditText)findViewById(R.id.edittxt_setport)).setText("");
				((EditText)findViewById(R.id.edittxt_setfilename)).setText("");
				break;
			}
		}
	};
}
