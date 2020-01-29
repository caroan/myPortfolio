package game.PushPangProto;

import android.app.*;
import android.view.*;

public abstract class AbstractPresentPushPangGaming extends Activity{
	protected int myGameType;
	public static final int NormalMode = 0;
	public static final int ClearPangMode = 1;
	
	public abstract void setCountTimerTask();

	/**�ʱ� ����, UnHold, ������ �̵� ���� �̺�Ʈ�� ������  
	 * �迭 �����͸� ���鼭 3���̻� ������ �༮�� �������� ����ϰ� �̸� ó���ϴ� �Լ� */
	public abstract void calculateGameState();
	
	
	/**��ġ�ؼ� �����̰� �ִ� ���� �ٸ� ������ ��ġ�� ���� �ٽ� false�� �����Ѵ�.*/
	public abstract boolean canThisUnitKeepMoving(int _x, int _y, View _v);
	
	/**������ ���������� �� ������ ����� ����ġ�� �ڸ��ϵ��� _reqularPos�� ���� �ִ´�.
	 * ���� ���� �� ��� _reqularPos�� -1�� �ִ´�.*/
	public abstract void getCoordUnitRegularPosition(int _x, int _y, View _v, int[] _regularPos);
	
	/**������ �����̴� �̴� ���� �̺�Ʈ�� ���� �� �������� �ϴ� �Լ�*/
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
