package game.PushPangProto;

import android.app.*;
import android.view.*;

public abstract class AbstractPresentPushPangGaming extends Activity{
	protected int myGameType;
	public static final int NormalMode = 0;
	public static final int ClearPangMode = 1;
	
	public abstract void setCountTimerTask();

	/**초기 시작, UnHold, 유닛의 이동 등의 이벤트가 끝나면  
	 * 배열 데이터를 돌면서 3개이상 인접한 녀석이 누구인지 계산하고 이를 처리하는 함수 */
	public abstract void calculateGameState();
	
	
	/**터치해서 움직이고 있는 점이 다른 유닛의 위치에 들어가면 다시 false를 리턴한다.*/
	public abstract boolean canThisUnitKeepMoving(int _x, int _y, View _v);
	
	/**유닛이 움직였으면 그 움직인 장소의 정위치에 자리하도록 _reqularPos에 값을 넣는다.
	 * 만약 실패 할 경우 _reqularPos에 -1을 넣는다.*/
	public abstract void getCoordUnitRegularPosition(int _x, int _y, View _v, int[] _regularPos);
	
	/**유닛을 움직이는 이는 등의 이벤트가 끝난 후 뒷정리를 하는 함수*/
	public abstract void resetUnitArrayState(int _x, int _y, View _v);
	
	public abstract int getMapSizeX();

	public abstract int getMapSizeY();

	public abstract PushPangUnits[][] getUnitArray();
	
	public abstract boolean getHoldBtnState();
	
	public abstract boolean getUnHoldBtnState();
	
	public abstract int getTotalScore();
	
	public abstract void setTotalScore(int _totalScore);
	
	public abstract int getObjectScore();

	public abstract void setObjectScore(int _ObjectScore);
	
	public abstract int getUnitTypesInGame();
	
	public abstract ViewPushPangGaming getMyView();
	
	public abstract View.OnTouchListener getTouchForeGround();
	
	public abstract View.OnTouchListener getBtnEvent();
	
	public abstract int getMyGameType();
}
