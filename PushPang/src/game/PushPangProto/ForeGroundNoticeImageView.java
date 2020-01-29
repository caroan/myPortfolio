package game.PushPangProto;

import android.view.*;
import android.widget.*;
import android.content.*;

public class ForeGroundNoticeImageView extends ImageView{
	private final int GAMESTART = 0;
	private final int STAGECLEAR = 1;
	private final int GAMEOVER = 2;
	
	private int myState;
	public ForeGroundNoticeImageView(Context context, int _myState) {
		// TODO Auto-generated constructor stub
		super(context);
		setMyState(_myState);
	}
	
	/**���� ������ �����ϰ� �ִ��� �˷��ش�. 0=���ӽ���, 1=��������Ŭ����, 2=���ӿ���*/
	public int getMyState(){
		return myState;
	}
	
	/**������ �����ؾ� �ϴ��� �˷��ش�. 0=���ӽ���, 1=�������� Ŭ����, 2=���ӿ���*/
	public void setMyState(int _myState){
		myState = _myState;
		if(myState == GAMESTART){
			setImageResource(R.drawable.test1);
		}else if(myState == STAGECLEAR){
			setImageResource(R.drawable.test2);
		}else {
			setImageResource(R.drawable.game_fore_gameover);
		}
	}
}
