package labView.protoType01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

public class Ni_labView_protoTypeActivity extends Activity {
//	ImageView initalImage;
	private boolean isEnd;
	public Ni_labView_protoTypeActivity() {
		// TODO Auto-generated constructor stub
		isEnd = false;
	}
	
	public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {     
        	if(isEnd){
        		finish();
        		return true;
        	}
        	Toast.makeText(this, "한번 더 뒤로 가기를 누르시면 프로그램이 종료됩니다.", Toast.LENGTH_LONG).show();
    		isEnd = true;
    		return true;
        }
        return super.dispatchKeyEvent(event);
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);
        
        ((Button)findViewById(R.id.graph_connect)).setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentOfShowGraphPage = new Intent(Ni_labView_protoTypeActivity.this, ShowGraphPage.class);
				startActivity(intentOfShowGraphPage);
			}
        	
        });
        
        ((Button)findViewById(R.id.machine_info)).setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intentOfShowCataloguePage = new Intent(Ni_labView_protoTypeActivity.this, CataloguePage.class);
				startActivity(intentOfShowCataloguePage);
			}
        	
        });
        
        /*initalImage = (ImageView)findViewById(R.id.first_page);
        initalImage.setOnTouchListener(new ImageView.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					isEnd = false;
					Intent intentOfShowCataloguePage = new Intent(Ni_labView_protoTypeActivity.this, CataloguePage.class);
					startActivity(intentOfShowCataloguePage);
				}
				return false;
			}
		});*/
    }
}