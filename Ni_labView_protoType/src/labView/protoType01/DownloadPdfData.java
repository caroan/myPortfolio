package labView.protoType01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

import javax.net.ssl.HttpsURLConnection;

import android.util.Log;


public class DownloadPdfData{
	public DownloadPdfData() {
		// TODO Auto-generated constructor stub
	}
	public boolean downLoadToSdcard(String _downLoadPath, URL _url){
		int bufferSize;
//		Log.d("TAG", ""+_url);
		try {
			HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
			if(conn != null){
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(10000);
				conn.setUseCaches(false);
				if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
					int dataSize = conn.getContentLength();
					//			Log.d("TAG", ""+dataSize);
					byte[] buffer = new byte[dataSize];
					InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(new File(_downLoadPath));
					while(true){
						bufferSize = is.read(buffer);
						if(bufferSize <= 0){
							break;
						}
						fos.write(buffer, 0, bufferSize);
					}
					is.close();
					fos.close();
				}
				conn.disconnect();
			}
		}catch (MalformedURLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
