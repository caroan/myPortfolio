package game.PushPangProto;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
public class PresentPushPangClearPangMode extends AbstractPresentPushPangGaming{
	private boolean cannotMove;
	private ViewPushPangGaming myView;
	private int mapSizeX;
	private int mapSizeY;
	private PushPangUnits [][] unitArray;
	private RegularPosData[][] basisPos;
	private boolean holdBtnState;
	private boolean unHoldBtnState;
	private int timeTick = 1;
	private Timer myTimer = new Timer();
	private TimerTask countTime;
	private ModelPushPangGame gameData;
	private boolean isResume;
	private int currentTime;
	private PushPangUnits[][] alreadyexsistUnitArray;
	private int unitTypesInGame;
	private int eachTypeUnitMax;
	private DefineGameMusic myMusic;
	private final int GameStart = 0;
	private final int GameOver = 1;
	private final int GameRestart = 2;
	private final int GamePause = 3;
	
	private int clearPangCount;
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		myView.getGameLayout().removeAllViews();
		gameData = new ModelPushPangGame(unitArray, currentTime, clearPangCount);
		outState.putSerializable("gamedata", gameData);
	}
	
	protected Dialog onCreateDialog(int id){
    	switch (id) {
		case GameStart:
			return new AlertDialog.Builder(PresentPushPangClearPangMode.this)
			.setTitle("Are You Ready ? ")
			.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setStageSettings();
					calculateGameState();
					myView.getTimeBar().initTimeProgressBar(300);
					myView.getTimeBar().setCurrentTime(currentTime);
					setCountTimerTask();
					myTimer.scheduleAtFixedRate(countTime, 0, 1000);
				}
			})
			.setCancelable(false)
			.create();
		case GamePause:
			return new AlertDialog.Builder(PresentPushPangClearPangMode.this)
			.setTitle("Pause")
			.setCancelable(false)
			.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					timeTick = 1;
				}
			})
			.create();
		}
    	
    	return null;
    }
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		// TODO Auto-generated method stub
		super.onPrepareDialog(id, dialog);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myMusic = DefineGameMusic.createInstance(this);
		setMapSize();
		setContentView( (FrameLayout)myView.getWholeLayout() );
		initVars();
		unitArray = myView.getUnitArray();
		myGameType = ClearPangMode;
		if(savedInstanceState == null){
			isResume = false;
		}else{
			isResume = true;
			ModelPushPangGame savedData = (ModelPushPangGame)savedInstanceState.getSerializable("gamedata");
			alreadyexsistUnitArray = savedData.getUnitArray();
			currentTime = savedData.getCurrentTime();
			clearPangCount = savedData.getClearPangCount();
		}
		setGameScoreBoard();
		showDialog(GameStart);
		basisPos = myView.getBasisPos();
	}
	
	Handler timerHandler = new Handler(){
		public void handleMessage(Message msg){
			int cTime = 0;
			cTime = myView.getTimeBar().getCurrentTime();
				
			if(cTime < myView.getTimeBar().getMaxTime()){
				myView.getTimeBar().setCurrentTime(cTime+timeTick);
			}else{
				countTime.cancel();
				myView.getGameStateImage().setMyState(2);
				setForeGroundEnabled(true);
			}
		}
	};
	
	private void setForeGroundEnabled(boolean _foregourndEnabledState){
		boolean gameGoundEnableState = !(_foregourndEnabledState);
		if(_foregourndEnabledState){
			myView.getGameStateImage().setVisibility(View.VISIBLE);
		}else{
			myView.getGameStateImage().setVisibility(View.GONE);
		}
		myView.getHoldBtn().setEnabled(gameGoundEnableState);
		myView.getAllHoldBtn().setEnabled(gameGoundEnableState);
		myView.getAllUnHoldBtn().setEnabled(gameGoundEnableState);
	}
	
	public void setCountTimerTask(){
		countTime = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				timerHandler.sendEmptyMessage(0);
			}
		};
	}
	
	private void initVars(){
		holdBtnState = unHoldBtnState = false;
		cannotMove = false;
		clearPangCount = 0;
	}
	
	private void setMapSize(){
		int[] mapSizeXY = getIntent().getIntArrayExtra("MapSize");
		myView = new ViewPushPangGaming(this, mapSizeXY[0], mapSizeXY[1]);
		mapSizeX = mapSizeXY[0];
		mapSizeY = mapSizeXY[1];
	}
	
	
	
	/**초기 시작, UnHold, 유닛의 이동 등의 이벤트가 끝나면  
	 * 배열 데이터를 돌면서 3개이상 인접한 녀석이 누구인지 계산하고 이를 처리하는 함수 */
	public void calculateGameState(){
		checkWillExplorsionUnits();
		if(countAndCheckClearPangCondition()){
			clearPangCount++;
			setGameScoreBoard();
			myView.getAllUnHoldBtn().setEnabled(false);
			calculateHandler.sendEmptyMessageDelayed(0, 1100);
			return ;
		}
		resetEachUnitIsExplorsionState();
	}
	
	Handler calculateHandler = new Handler(){
		public void handleMessage(Message msg) {
			timeTick = 0;
			makeNewUnits();
			myView.getAllUnHoldBtn().setEnabled(true);
			timeTick = 1;
		}
	};
	
	/**게임상 유닛중에 더 터질 것이 있는지 알아보는 함수*/
	private void makeNewUnits(){
		//폭발할 것들이 없을경우 다시 맵을 생성한다.
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
				unitArray[y][x] = null;
				myView.getUnitArray()[y][x] = null;
			}
		}
		myView.getGameLayout().removeAllViews();
		myView.initGameLayout(unitTypesInGame, eachTypeUnitMax);
		unitArray = myView.getUnitArray();
	}
	
	private void setGameScoreBoard(){
		myView.getScoreBoard().setText("클리어 팡 횟수  : " + clearPangCount);
	}
	
	/**스테이지를 세팅한다.*/
	private void setStageSettings(){
		myView.getGameLayout().removeAllViews();
		if(isResume){
			isResume = false;
			for(int y=0;y<mapSizeY;y++){
				for(int x=0;x<mapSizeX;x++){
					unitArray[y][x] = null;
					myView.getUnitArray()[y][x] = null;
				}
			}
			myView.relocateGameUnitInGameLayout(alreadyexsistUnitArray);
			unitArray = myView.getUnitArray();
			return;
		}
		unitTypesInGame = 7;
		eachTypeUnitMax = 5;
		myView.initGameLayout(unitTypesInGame, eachTypeUnitMax);
		unitArray = myView.getUnitArray();
		currentTime = 0;
		holdBtnState = unHoldBtnState = false;
		cannotMove = false;
	}
	
	/** 터질만한 세균을 세어보고 클리어 팡이 되었는지 확인한다. */
	private boolean countAndCheckClearPangCondition(){
		return isClearPang(countTargetVirus());
	}
	/**클리어팡이 되었는지 확인한다.
	 * _targetVirsuNum : countTargetVirus()를 통해 알아본 터질만한 세균수
	 * 클리어 팡이면 true, 아니면 false를 반환한다.*/
	private boolean isClearPang(int _targetVirsuNum){
		if(_targetVirsuNum >= 35){
			for(int y = 0; y<mapSizeY; y++){
				for(int x = 0; x<mapSizeX; x++){
					if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState()){
						continue;
					}
					unitArray[y][x].setMyExPlosionState(true);
				}
			}
			myView.getTimeBar().plusTime(30);
			return true;
		}
		return false;
	}
	/**터질만한 세균을 세어본다.
	 * 터질만한 세균을 세서 반환한다.*/
	private int countTargetVirus(){
		int targetVirusNum = 0;
		for(int y = 0; y<mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState()){
					continue;
				}
				if(unitArray[y][x].isExplosionCenter || unitArray[y][x].isExplosionSuburb){
					targetVirusNum ++;
				}
			}
		}
		return targetVirusNum;
	}
	
	/** isThisUnitWillExplorsion함수에서 현재 지목된 체크중인 유닛이 미리 체크된 폭발물이 아닐 경우 
	 *  주변의 유닛을 체크하여 같은 타입의 유닛이 2개이상 있을 경우 체크중인 유닛과 그 주위의 있는 같은 타입의 유닛은
	 *  폭발물로 체크된다. */
	private void calculateGameStateInUnExplorsionCase(int _x, int _y){
		int explorsionCount = 0;
		ArrayList<PushPangUnits> explorsionUnitList = new ArrayList<PushPangUnits>();
		
		if(_y+1 < mapSizeY && unitArray[_y+1][_x] != null && canIUseThis(unitArray[_y+1][_x])&&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y+1][_x].getMyUnitType()){
			explorsionCount++;
			explorsionUnitList.add(unitArray[_y+1][_x]);
		}//down
		if(_x+1 < mapSizeX && unitArray[_y][_x+1] != null && canIUseThis(unitArray[_y][_x+1]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y][_x+1].getMyUnitType() ){
			explorsionCount++;
			explorsionUnitList.add(unitArray[_y][_x+1]);
		}//right
		if(_y-1 >= 0 && unitArray[_y-1][_x] != null && canIUseThis(unitArray[_y-1][_x]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y-1][_x].getMyUnitType()){
			explorsionCount++;
			explorsionUnitList.add(unitArray[_y-1][_x]);
		}//up
		if(_x-1 >= 0 && unitArray[_y][_x-1] != null && canIUseThis(unitArray[_y][_x-1]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y][_x-1].getMyUnitType()){
			explorsionCount++;
			explorsionUnitList.add(unitArray[_y][_x-1]);
		}//left
		if(explorsionCount >= 2){
			unitArray[_y][_x].isExplosionCenter = true;
			for (PushPangUnits list : explorsionUnitList) {
				list.isExplosionSuburb = true;
			}
		}
		explorsionUnitList.clear();
	}
	
	/** isThisUnitWillExplorsion함수에서 현재 지목된 체크중인 유닛이 폭발물일 경우 폭발될 유닛의 타입과 
	 *  같은 주변의 유닛도 폭발물로 체크된다. */
	private void calculateGameStateInExplorsionCase(int _x, int _y){
		if(_y+1 < mapSizeY && unitArray[_y+1][_x] != null && canIUseThis(unitArray[_y+1][_x]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y+1][_x].getMyUnitType()){
			unitArray[_y+1][_x].isExplosionSuburb = true;
		}//down
		if(_x+1 < mapSizeX && unitArray[_y][_x+1] != null && canIUseThis(unitArray[_y][_x+1]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y][_x+1].getMyUnitType() ){
			unitArray[_y][_x+1].isExplosionSuburb = true;
		}//right
		if(_y-1 >= 0 && unitArray[_y-1][_x] != null && canIUseThis(unitArray[_y-1][_x]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y-1][_x].getMyUnitType()){
			unitArray[_y-1][_x].isExplosionSuburb = true;
		}//up
		if(_x-1 >= 0 && unitArray[_y][_x-1] != null && canIUseThis(unitArray[_y][_x-1]) &&
				unitArray[_y][_x].getMyUnitType() == unitArray[_y][_x-1].getMyUnitType()){
			unitArray[_y][_x-1].isExplosionSuburb = true;
		}//left
	}
	
	/**파라미터로 넘어온 유닛이 홀드 상태 또는 이미 폭발한 상태면 false를, 아니면 true를 반환한다.*/
	private boolean canIUseThis(PushPangUnits _p){
		if(_p.getMyExplosionState() == true || _p.getMyHoldState() == true){
			return false;
		}
		return true;
	}
	
	/**checkWillExplorsionUnits에서 순회하면서 체크할 유닛이 폭발물로 이미 지정되어 있는지 아닌지 나눈다.
	 * 이유는 각 케이스에 따라 처리하는 방식이 다르기 때문이다.*/
	private void isThisUnitWillExplorsion(int _x, int _y){
		//알고리즘 부분 나중에 여기에 비교하려는 대상이 Gone인지 또는 홀드 상태인지 확인하는 것도 추가한다.
		if(unitArray[_y][_x].isExplosionSuburb || unitArray[_y][_x].isExplosionCenter){
			calculateGameStateInExplorsionCase(_x, _y);
		}
		else if(unitArray[_y][_x].isExplosionSuburb==false && unitArray[_y][_x].isExplosionCenter==false){
			calculateGameStateInUnExplorsionCase(_x, _y);
		}
	}
	
	/**2중 배열을 순회하면서 각각의 배열이 폭발물인지 확인한다.*/
	private void checkWillExplorsionUnits(){
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
			//알고리즘이 등장하는 윗부분에서는 반드시 지금 잡은 녀석이 Gone인지 홀드상태인지, 아님 아이템인지 확인한다.
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState() == true
						|| unitArray[y][x].getMyHoldState() == true || unitArray[y][x].getMyUnitType() >= 100){
					continue;
				}
				isThisUnitWillExplorsion(x, y);
			}
		}
	}
	
	/**터치해서 움직이고 있는 점이 다른 유닛의 위치에 들어가면 다시 false를 리턴한다.*/
	public boolean canThisUnitKeepMoving(int _x, int _y, View _v) {
		// TODO Auto-generated method stub
		for(int y =0; y<mapSizeY; y++){
			for (int x = 0; x < mapSizeX; x++) {
				if(unitArray[y][x] == null || unitArray[y][x].equals(_v)){
					continue;
				}
				if( _y > unitArray[y][x].getTop() && _y < unitArray[y][x].getBottom() 
						&&_x > unitArray[y][x].getLeft() && _x < unitArray[y][x].getRight() 
						&& unitArray[y][x].getMyExplosionState() == false){
					return false;
				}
			}
		}
		return true;
	}
	
	/**유닛이 움직였으면 그 움직인 장소의 정위치에 자리하도록 _reqularPos에 값을 넣는다.
	 * 만약 실패 할 경우 _reqularPos에 -1을 넣는다.*/
	public void getCoordUnitRegularPosition(int _x, int _y, View _v, int[] _regularPos) {
		// TODO Auto-generated method stub
		for(int y =0; y<mapSizeY; y++){
			for (int x = 0; x < mapSizeX; x++) {
				if( _y > basisPos[y][x].getTop() && _y < basisPos[y][x].getBottom() 
						&&_x > basisPos[y][x].getLeft() && _x < basisPos[y][x].getRight() ){
					_regularPos[0] = basisPos[y][x].getLeft();
					_regularPos[1] = basisPos[y][x].getTop();
					return ;
				}
			}
		}
		_regularPos[0] = _regularPos[1] = -1;
	}
	
	//일단 밑에 놈들을 옮기는 것은 나중에 하기로 한다.
	/**유닛을 움직이는 이는 등의 이벤트가 끝난 후 뒷정리를 하는 함수*/
	public void resetUnitArrayState(int _x, int _y, View _v){
		resetUnitArray(_x, _y, _v);
		resetEachUnitIsExplorsionState();
	}
	
	/**움직인 유닛의 뷰를 모델에 적용한다.*/
	private void resetUnitArray(int _x, int _y, View _v){
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
				if(_v.equals(unitArray[y][x])){
					unitArray[y][x] = null;
				}
			}
		}
		for(int y = 0; y<mapSizeY;y++){
			for(int x = 0; x<mapSizeX; x++){
				if(basisPos[y][x].getLeft() == _x && basisPos[y][x].getTop() == _y){
					unitArray[y][x] = (PushPangUnits)_v;
				}
			}
		}
	}
	
	/**폭발 상태를 다시 초기화 시킨다.*/
	private void resetEachUnitIsExplorsionState(){
		for(int y=0; y<mapSizeY; y++){
			for(int x=0; x<mapSizeX; x++){
				if(unitArray[y][x] == null){
					continue;
				}
				unitArray[y][x].isExplosionCenter = false;
				unitArray[y][x].isExplosionSuburb = false;
			}
		}
	}
	
	private void setAllHoldState(boolean _holdState){
		for(int y=0; y<mapSizeY; y++){
			for(int x=0; x<mapSizeX; x++){
				if(unitArray[y][x] == null){
					continue;
				}
				unitArray[y][x].setMyHoldState(_holdState);
			}
		}
	}
	public int getMapSizeX(){
		return mapSizeX;
	}
	public int getMapSizeY(){
		return mapSizeY;
	}
	public PushPangUnits[][] getUnitArray(){
		return unitArray;
	}
	public boolean getHoldBtnState(){
		return holdBtnState;
	}
	public boolean getUnHoldBtnState(){
		return unHoldBtnState;
	}
	public int getTotalScore(){
		return 0;
	}
	public void setTotalScore(int _totalScore){
	}
	public int getObjectScore(){
		return 0;
	}
	public void setObjectScore(int _ObjectScore){
	}
	public int getUnitTypesInGame(){
		return unitTypesInGame;
	}
	public ViewPushPangGaming getMyView(){
		return myView;
	}
	
	/**위 버튼을 눌렀을 경우 아래 버튼은 모두 false가 된다.*/
	private void makeFalseBottomBtns(){
		unHoldBtnState = holdBtnState = false;
	}
	
	View.OnTouchListener touchForeGround = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(((ForeGroundNoticeImageView)v).getMyState() == 0){
					setStageSettings();
					calculateGameState();
					myView.getTimeBar().setCurrentTime(currentTime);
					setCountTimerTask();
					myTimer.scheduleAtFixedRate(countTime, 0, 1000);
				}
				else if(((ForeGroundNoticeImageView)v).getMyState() == 1){
					myView.getGameStateImage().setMyState(0);
				}else{
					PresentPushPangClearPangMode.this.finish();
				}
			}
			return true;
		}
	};
	View.OnTouchListener btnEvent = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			if(v.getId() == ViewPushPangGaming.PAUSE && event.getAction() == MotionEvent.ACTION_DOWN){
				timeTick = 0;
				showDialog(GamePause);
				//changeHoldStateAndBtnBackGround(v);
			}
			else if(v.getId() == ViewPushPangGaming.ALLHOLD && event.getAction() == MotionEvent.ACTION_UP){
				makeFalseBottomBtns();
				setAllHoldState(true);
				calculateGameState();
			}
			else if(v.getId() == ViewPushPangGaming.ALLUNHOLD && event.getAction() == MotionEvent.ACTION_UP){
				makeFalseBottomBtns();
				setAllHoldState(false);
				calculateGameState();
			}
			return true;
		}
	};

	@Override
	public OnTouchListener getTouchForeGround() {
		// TODO Auto-generated method stub
		return touchForeGround;
	}

	@Override
	public OnTouchListener getBtnEvent() {
		// TODO Auto-generated method stub
		return btnEvent;
	}

	@Override
	public int getMyGameType() {
		// TODO Auto-generated method stub
		return ClearPangMode;
	}
}
