package game.PushPangProto;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;

public class PresentPushPangStart extends Activity {
	private boolean isFinish;
	private ViewPushPangStart myView;
	private int menuLevel;
	private int[] mapSizeXY; 
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView = new ViewPushPangStart(this);
        setContentView(myView.getResultLayout());
        myView.addAllBtnToButtonControllerLayout();
        myView.lookFirstLevelBtnInButtonControllerLayout();
        isFinish = false;
        menuLevel = 0;
        mapSizeXY = new int[2];
    }
    
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
    		if(isFinish){
    			finish();
    		}else if(menuLevel == 0){
    			Toast.makeText(this, "한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
        		isFinish = true;
        	}else if(menuLevel == 1){
        		menuLevel--;
        		myView.lookFirstLevelBtnInButtonControllerLayout();
        	}else if(menuLevel == 2){
        		menuLevel--;
        		myView.lookSecondLevelBtnAfterGameStartInButtonControllerLayout();
        	}
    		return true;
    	}
    	return super.dispatchKeyEvent(event);
    }
    
    public Button.OnClickListener btnEvent = new OnClickListener(){
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isFinish = false;
			switch (v.getId()) {
			case DefineButtonId.BTNID_GAMESTART:
				myView.lookSecondLevelBtnAfterGameStartInButtonControllerLayout();
				menuLevel ++;
				break;
			case DefineButtonId.BTNID_RANKINGID:
				Toast.makeText(PresentPushPangStart.this, "랭킹", Toast.LENGTH_SHORT).show();
				break;
			case DefineButtonId.BTNID_HOWTO:
				Toast.makeText(PresentPushPangStart.this, "사용법", Toast.LENGTH_SHORT).show();
				break;
			case DefineButtonId.BTNID_GAMESETTING:
				Toast.makeText(PresentPushPangStart.this, "세팅", Toast.LENGTH_SHORT).show();
				break;
			
			case DefineButtonId.BTNID_NORMALMODE:
				menuLevel ++;
				myView.lookMapSizeSelectLevelWidgetAfterGameModeInButtonControllerLayout();
				break;
			case DefineButtonId.BTNID_SCENARIOMODE:
				Toast.makeText(PresentPushPangStart.this, "시나리오", Toast.LENGTH_SHORT).show();
				break;
			case DefineButtonId.BTNID_CLEARPANGMODE:
				mapSizeXY[0] = mapSizeXY[1] = 6;
				Intent startClearPangGameMode = new Intent(PresentPushPangStart.this, PresentPushPangClearPangMode.class);
				startClearPangGameMode.putExtra("MapSize", mapSizeXY);
				startActivity(startClearPangGameMode);
				break;
			case DefineButtonId.BTNID_FIGUREMODE:
				Toast.makeText(PresentPushPangStart.this, "모양 맞추기", Toast.LENGTH_SHORT).show();
				break;
			case DefineButtonId.BTNID_SMALLSIZE:
				mapSizeXY[0] = mapSizeXY[1] = 5;
				Intent startSmallNormalGameMode = new Intent(PresentPushPangStart.this, PresentPushPangNormalMode.class);
				startSmallNormalGameMode.putExtra("MapSize", mapSizeXY);
				startActivity(startSmallNormalGameMode);
				break;
			case DefineButtonId.BTNID_BIGSIZE:
				mapSizeXY[0] = mapSizeXY[1] = 7;
				Intent startBigNormalGameMode = new Intent(PresentPushPangStart.this, PresentPushPangNormalMode.class);
				startBigNormalGameMode.putExtra("MapSize", mapSizeXY);
				startActivity(startBigNormalGameMode);
				break;
			}
		}
    };
}