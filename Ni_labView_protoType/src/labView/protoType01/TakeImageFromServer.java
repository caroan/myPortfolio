package labView.protoType01;

import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SlidingDrawer;
import android.widget.Toast;

public class TakeImageFromServer implements Runnable {
	public Handler myHandler;
	private String myIp;
	private int myPort;
	private String myFileName;
	private boolean isRun;
	private URL myFirstUrl;
	private URL mySecondUrl;
	
	public TakeImageFromServer(Handler _handler, String _ip, int _port, String _file) {
		// TODO Auto-generated constructor stub
		myHandler = _handler;
		myIp = _ip;
		myPort = _port;
		myFileName = _file;
		isRun = true;
	}
	
	private void secondConnectCheck(HttpURLConnection _connectInfo) throws IOException{
		if(_connectInfo != null){
			_connectInfo.setConnectTimeout(5000);
			_connectInfo.setReadTimeout(5000);
			_connectInfo.setUseCaches(false);
			switch(_connectInfo.getResponseCode()){
			case HttpURLConnection.HTTP_OK:
				InputStream is = null;
				try{
					is = mySecondUrl.openStream();
					Bitmap bit = BitmapFactory.decodeStream(is);
					Message msg = Message.obtain(myHandler, 0, bit);
					myHandler.sendMessage(msg);
				}catch(OutOfMemoryError e){
					Message msg = Message.obtain(myHandler, 11);
					myHandler.sendMessage(msg);
				}finally{
					if(is != null){
						is.close();
					}
				}
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:
				Message httpNotFound = Message.obtain(myHandler, 1);
				myHandler.sendMessage(httpNotFound);
				isRun = false;
				break;
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				Message httpUnauthorized = Message.obtain(myHandler, 2);
				myHandler.sendMessage(httpUnauthorized);
				isRun = false;
				break;
			default:
				Message etcProblem = Message.obtain(myHandler, 3);
				myHandler.sendMessage(etcProblem);
				isRun = false;
				break;
			}
			_connectInfo.disconnect();
		}else{
			//아예 생성 불가인 경우
			Message noExistCon = Message.obtain(myHandler, 5);
			myHandler.sendMessage(noExistCon);
			isRun = false;
		}
	}
	
	private void firstConnectCheck(HttpURLConnection _connectInfo) throws IOException{
		if(_connectInfo != null){
			_connectInfo.setConnectTimeout(10000);
			_connectInfo.setReadTimeout(10000);
			_connectInfo.setUseCaches(false);
			switch(_connectInfo.getResponseCode()){
			case HttpURLConnection.HTTP_OK:
				mySecondUrl = new URL("http", myIp, myPort, NIXMLParser.getName(myFirstUrl));
				HttpURLConnection connectInfo = (HttpURLConnection)mySecondUrl.openConnection();
				secondConnectCheck(connectInfo);
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:
				Message httpNotFound = Message.obtain(myHandler, 1);
				myHandler.sendMessage(httpNotFound);
				isRun = false;
				break;
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				Message httpUnauthorized = Message.obtain(myHandler, 2);
				myHandler.sendMessage(httpUnauthorized);
				isRun = false;
				break;
			default:
				Message etcProblem = Message.obtain(myHandler, 3);
				myHandler.sendMessage(etcProblem);
				isRun = false;
				break;
			}
		_connectInfo.disconnect();
		}else{
			//아예 생성 불가인 경우
			Message noExistCon = Message.obtain(myHandler, 5);
			myHandler.sendMessage(noExistCon);
			isRun = false;
		}
	}
	public void run() {
		while(isRun){
			try{
				myFirstUrl = new URL("http", myIp, myPort, "/" + myFileName+".html");
//				String t = NIXMLParser.getName(temp);	//여기서 계속 무한 루프를 돌고 있다.
				HttpURLConnection connectInfo = (HttpURLConnection)myFirstUrl.openConnection();
				firstConnectCheck(connectInfo);
			}catch(IOException e){
				Message IOProblem = Message.obtain(myHandler, 4);
				myHandler.sendMessage(IOProblem);
				isRun = false;
			}catch (Exception e) {
				// TODO: handle exception
				Message msg = Message.obtain(myHandler, 6);
				myHandler.sendMessage(msg);
				isRun = false;
			}
			/*try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}
	
	public boolean getIsRun(){
		return isRun;
	}
	public void setIsRun(boolean _isRun){
		isRun = _isRun;
	}
	
	public void setAll(String _myIp, int _myPort, String _myFileName){
		myIp = _myIp;
		myPort = _myPort;
		myFileName = _myFileName;
	}
}
