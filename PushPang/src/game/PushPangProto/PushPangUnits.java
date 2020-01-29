package game.PushPangProto;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import game.PushPangProto.PushPangGameLayout.LayoutParams;
import game.PushPangProto.AdvancedGestureDetectorWrapper.*;
import android.content.*;
import android.graphics.*;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.*;
import android.view.*;

public class PushPangUnits extends Button implements GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener{
	private int myUnitType;
	private AdvancedGestureDetectorWrapper myAdvancedGestureDetector;
	private GestureDetector myGestureDetector;
	public boolean isExplosionCenter;
	public boolean isExplosionSuburb;
	private boolean isHold;
	private boolean isExploded;
	AbstractPresentPushPangGaming myContext;
	private int pressedCoordX;
	private int pressedCoordY;
	private int pressedRawCoordX;
	private int pressedRawCoordY;
	private boolean cannotMove;
	private boolean excavatorMode;
	private PushPangGameLayout.LayoutParams unitMoveParam;
	private int[] myPositionXY;
	private DefineGameMusic myMusic;
	
	//굳이 center 와 suburb를 나눈 필요는 없다는 것을 알게 되었다.
	public void setMyPositionXY(int _x, int _y){
		myPositionXY[0] = _x;
		myPositionXY[1] = _y;
	}
	public PushPangUnits(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		isExplosionCenter = false;
		isExplosionSuburb = false;
		isHold = false;
		isExploded = false;
		isHold = true;
		isExploded = false;
		cannotMove = false;
		myContext = (AbstractPresentPushPangGaming)getContext();
		myGestureDetector = new GestureDetector(myContext, this);
		excavatorMode = false;
		myPositionXY = new int[2];
		myMusic = DefineGameMusic.createInstance(myContext);
	}
	public PushPangUnits(Context context, AttributeSet attrs) {
		super(context, attrs);
		isExplosionCenter = false;
		isExplosionSuburb = false;
		isHold = true;
		isExploded = false;
		cannotMove = false;
		myContext = (AbstractPresentPushPangGaming)getContext();
		myGestureDetector = new GestureDetector(myContext, this);
		excavatorMode = false;
		myPositionXY = new int[2];
	}
	
	/**자신의 유닛 타입을 반환한다.*/
	public int getMyUnitType(){
		return myUnitType;
	}
	
	
	
	/*public void setTestText(){
		setMyExPlosionState(true);
		//setVisibility(PushPangUnits.GONE);
	}*/
	
	/**자신의 홀드상태를 변화시키고 그에따른 이미지로 변환한다.*/
	public void setMyHoldState(boolean _holdState){
		isHold = _holdState;
		if(myUnitType >= 100){
			return ;
		}
		if(isHold){
			//홀드 이미지로 바꾼다.
			setBackgroundResource(DefineUnitType.getUnitHoldImage(myUnitType));
		}
		else{
			//원래 이미지로 바꾼다.
			setBackgroundResource(DefineUnitType.getUnitImage(myUnitType));
		}
	}
	/**자신의 홀드상태를 반환한다.*/
	public boolean getMyHoldState(){
		return isHold;
	}
	
	/**자신의 폭발 상태를 변화시키고 그에따라 폭발했을 경우 Gone을 폭발하지 않았을 경우 Visible로 변환한다.*/
	public void setMyExPlosionState(boolean _exPlosionState){
		isExploded = _exPlosionState;
		if(isExploded){
			if(myUnitType > 8){
				setBackgroundResource(DefineUnitType.getUnitImage(DefineUnitType.OVALVIRUSEXPLORED));
			}else{
				setBackgroundResource(DefineUnitType.getUnitImage(DefineUnitType.RECTVIRUSEXPLORED));
			}
			exploredHandler.sendEmptyMessageDelayed(0, 1000);
			//setVisibility(View.GONE);
		}else{
			this.setVisibility(PushPangUnits.VISIBLE);
		}
	}
	
	Handler exploredHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setVisibility(View.GONE);
			super.handleMessage(msg);
		}
	};
	
	/**자신의 폭발 상태를 반환한다.*/
	public boolean getMyExplosionState(){
		return isExploded;
	}
	
	/**자신의 유닛을 정하고 그것에 맞는 성격과 배경을 택한다.*/
	public void setMyUnitType(int _myUnitType){
		myUnitType = _myUnitType;
		if(isHold && myUnitType < 100){
			setBackgroundResource(DefineUnitType.getUnitHoldImage(myUnitType));
		}else{
			setBackgroundResource(DefineUnitType.getUnitImage(myUnitType));
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
/*	public void setOnGestureListener(AdvancedGestureDetectorWrapper l){
		myEvent = l;
	}*/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
//		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_UP){
			this.onMoveEnd(event);
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			this.onMove(event);
		}
		return myGestureDetector.onTouchEvent(event);
	}
	
	private void reSetMyPosition(){
		for(int y=0;y<myContext.getMapSizeY();y++){
			for(int x=0;x<myContext.getMapSizeX();x++){
				if(this.equals(myContext.getUnitArray()[y][x])){
					setMyPositionXY(x, y);
					return ;
				}
			}
		}
	}

	public boolean onMoveEnd(MotionEvent event){
		
		if(myContext.getHoldBtnState() == true || myContext.getUnHoldBtnState() == true){
			return true;
		}
		if(false == cannotMove){
			int []regularPos = new int[2];
			myContext.getCoordUnitRegularPosition((int)event.getRawX(), (int)event.getRawY()-myContext.getMyView().ppgLay_gameLayout.myTopCoordY, this, regularPos);
			unitMoveParam = (PushPangGameLayout.LayoutParams)getLayoutParams();
			if(regularPos[0] == -1 || regularPos[1] == -1){
				unitMoveParam.x = pressedRawCoordX;
				unitMoveParam.y = pressedRawCoordY;
			}else{
				unitMoveParam.x = regularPos[0];
				unitMoveParam.y = regularPos[1];
			}
			setLayoutParams(unitMoveParam);
			myContext.resetUnitArrayState(unitMoveParam.x, unitMoveParam.y, this);
			myContext.getMyView().setUnitArray(myContext.getUnitArray());
			myContext.calculateGameState();
			reSetMyPosition();
			//움직일때마다 하는 거면 유닛의 실제 위치를 변화시켜야 한다.
		}
		cannotMove= false;
		return true;
	}
	
	@Override
	public boolean onDown(MotionEvent event) {
		// TODO Auto-generated method stub
		if(myContext.getHoldBtnState() == true){
			setMyHoldState(true);
			return true;
		}else if(myContext.getUnHoldBtnState() == true){
			setMyHoldState(false);
			myContext.calculateGameState();
			return true;
		}
		
		pressedCoordX = (int)event.getX();
		pressedCoordY = (int)event.getY();
		unitMoveParam =  (PushPangGameLayout.LayoutParams)getLayoutParams();
		unitMoveParam.x = (int)event.getRawX()- pressedCoordX;
		unitMoveParam.y = (int)event.getRawY()
		-myContext.getMyView().ppgLay_gameLayout.myTopCoordY - pressedCoordY;
		pressedRawCoordX = unitMoveParam.x;
		pressedRawCoordY = unitMoveParam.y;
		
		setLayoutParams(unitMoveParam);
		return true;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	public boolean onMove(MotionEvent event){
		if(cannotMove || myContext.getHoldBtnState() == true || myContext.getUnHoldBtnState() == true){
			return true;
		}
		unitMoveParam = (PushPangGameLayout.LayoutParams)getLayoutParams();
		unitMoveParam.x = (int)event.getRawX()- pressedCoordX;
		unitMoveParam.y = (int)event.getRawY()
		-myContext.getMyView().ppgLay_gameLayout.myTopCoordY - pressedCoordY;
		
		//unitArray = myView.getUnitArray();
		
		if( false == myContext.canThisUnitKeepMoving((int)event.getRawX(), (int)event.getRawY()-myContext.getMyView().ppgLay_gameLayout.myTopCoordY, this)){
			cannotMove = true;
			unitMoveParam.x = pressedRawCoordX;
			unitMoveParam.y = pressedRawCoordY;
		}
		setLayoutParams(unitMoveParam);
		return true;
	}
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		/*if(cannotMove || myContext.getHoldBtnState() == true || myContext.getUnHoldBtnState() == true){
			return true;
		}
		unitMoveParam = (PushPangGameLayout.LayoutParams)getLayoutParams();
		unitMoveParam.x = (int)e2.getRawX()- pressedCoordX;
		unitMoveParam.y = (int)e2.getRawY()
		-myContext.getMyView().ppgLay_gameLayout.myTopCoordY - pressedCoordY;
		
		//unitArray = myView.getUnitArray();
		
		if( false == myContext.canThisUnitKeepMoving((int)e2.getRawX(), (int)e2.getRawY()-myContext.getMyView().ppgLay_gameLayout.myTopCoordY, this)){
			cannotMove = true;
			unitMoveParam.x = pressedRawCoordX;
			unitMoveParam.y = pressedRawCoordY;
		}
		setLayoutParams(unitMoveParam);
		*/return true;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean onDoubleTap(MotionEvent e) {
		// TODO Auto-generated method stub
		myMusic.play(DefineGameMusic.UNITPRESSED);
		setMyHoldState( !(getMyHoldState()) );
		if(getMyUnitType() >= 100){
			setActivation();
		}
		return false;
	}
	private int activateHorizonBomb(){
		int exploredUnitCount = 0;
		for(int x=0;x<myContext.getMapSizeX();x++){
			if(myContext.getUnitArray()[myPositionXY[1]][x] == null ||
					this.equals(myContext.getUnitArray()[myPositionXY[1]][x]) ||
					myContext.getUnitArray()[myPositionXY[1]][x].getMyUnitType() >= 100 ||
					myContext.getUnitArray()[myPositionXY[1]][x].getMyExplosionState()){
				continue;
			}
			myContext.getUnitArray()[myPositionXY[1]][x].setMyExPlosionState(true);
			exploredUnitCount++;
		}
		return exploredUnitCount;
	}
	
	private int activateVerticalBomb(){
		int exploredUnitCount = 0;
		for(int y=0;y<myContext.getMapSizeY();y++){
			if(myContext.getUnitArray()[y][myPositionXY[0]] == null ||
					this.equals(myContext.getUnitArray()[y][myPositionXY[0]]) ||
					myContext.getUnitArray()[y][myPositionXY[0]].getMyUnitType() >= 100 ||
					myContext.getUnitArray()[y][myPositionXY[0]].getMyExplosionState()){
				continue;
			}
			myContext.getUnitArray()[y][myPositionXY[0]].setMyExPlosionState(true);
			exploredUnitCount++;
		}
		return exploredUnitCount;
	}
	
	private int activateRandomBomb(){
		int exploredUnitCount = 0;
		int targetType = ((int)(Math.random()*10))% myContext.getUnitTypesInGame();
		for(int y=0; y<myContext.getMapSizeY();y++){
			for(int x=0;x<myContext.getMapSizeX();x++){
				if(myContext.getUnitArray()[y][x] == null ||
						myContext.getUnitArray()[y][x].getMyUnitType() >= 100 ||
						myContext.getUnitArray()[y][x].getMyExplosionState()){
					continue;
				}
				if(myContext.getUnitArray()[y][x].getMyUnitType() == targetType){
					myContext.getUnitArray()[y][x].setMyExPlosionState(true);
					exploredUnitCount++;
				}
			}
		}
		return exploredUnitCount;
	}
	
	private int activateSquareBomb(){
		int exploredUnitCount = 0;
		for(int y=0; y<myContext.getMapSizeY();y++){
			for(int x=0;x<myContext.getMapSizeX();x++){
				if(myContext.getUnitArray()[y][x] == null ||
						myContext.getUnitArray()[y][x].getMyUnitType() >= 100 ||
						myContext.getUnitArray()[y][x].getMyExplosionState()){
					continue;
				}
				if((x>=myPositionXY[0]-1 && x <= myPositionXY[0]+1) && 
						(y>=myPositionXY[1]-1 && y <= myPositionXY[1]+1) ){
					myContext.getUnitArray()[y][x].setMyExPlosionState(true);
					exploredUnitCount++;
				}
			}
		}
		return exploredUnitCount;
	}
	
	/**아이템일 경우 활성화를 시킨다.*/
	private void setActivation(){
		int exploredUnitCount = 1;
		switch (getMyUnitType()) {
		case DefineUnitType.EXCAVATOR:
			excavatorMode = true;
			break;
		case DefineUnitType.HORIZONBOMB:
			exploredUnitCount += activateHorizonBomb();
			break;
		case DefineUnitType.VETICALBOMB:
			exploredUnitCount += activateVerticalBomb();
			break;
		case DefineUnitType.CROSSBOMB:
			exploredUnitCount += activateHorizonBomb();
			exploredUnitCount += activateVerticalBomb();
			break;
		case DefineUnitType.RANDOMBOMB:
			exploredUnitCount += activateRandomBomb();
			break;
		case DefineUnitType.SQUAREBOMB:
			exploredUnitCount += activateSquareBomb();
			break;
		case DefineUnitType.TIMEMAGIC: 
			myContext.getMyView().getTimeBar().plusTime(10);
			break;
		}
		this.setMyExPlosionState(true);
		myContext.setTotalScore(myContext.getTotalScore() + exploredUnitCount);
		myContext.setObjectScore(myContext.getObjectScore() - exploredUnitCount);
	}
	
	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}
}
