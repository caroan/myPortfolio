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
	
	/**현재 무엇을 공지하고 있는지 알려준다. 0=게임시작, 1=스테이지클리어, 2=게임오버*/
	public int getMyState(){
		return myState;
	}
	
	/**무엇을 공지해야 하는지 알려준다. 0=게임시작, 1=스테이지 클리어, 2=게임오버*/
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
