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
	
	
	
	/**�ʱ� ����, UnHold, ������ �̵� ���� �̺�Ʈ�� ������  
	 * �迭 �����͸� ���鼭 3���̻� ������ �༮�� �������� ����ϰ� �̸� ó���ϴ� �Լ� */
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
	
	/**���ӻ� �����߿� �� ���� ���� �ִ��� �˾ƺ��� �Լ�*/
	private void makeNewUnits(){
		//������ �͵��� ������� �ٽ� ���� �����Ѵ�.
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
		myView.getScoreBoard().setText("Ŭ���� �� Ƚ��  : " + clearPangCount);
	}
	
	/**���������� �����Ѵ�.*/
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
	
	/** �������� ������ ����� Ŭ���� ���� �Ǿ����� Ȯ���Ѵ�. */
	private boolean countAndCheckClearPangCondition(){
		return isClearPang(countTargetVirus());
	}
	/**Ŭ�������� �Ǿ����� Ȯ���Ѵ�.
	 * _targetVirsuNum : countTargetVirus()�� ���� �˾ƺ� �������� ���ռ�
	 * Ŭ���� ���̸� true, �ƴϸ� false�� ��ȯ�Ѵ�.*/
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
	/**�������� ������ �����.
	 * �������� ������ ���� ��ȯ�Ѵ�.*/
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
	
	/** isThisUnitWillExplorsion�Լ����� ���� ����� üũ���� ������ �̸� üũ�� ���߹��� �ƴ� ��� 
	 *  �ֺ��� ������ üũ�Ͽ� ���� Ÿ���� ������ 2���̻� ���� ��� üũ���� ���ְ� �� ������ �ִ� ���� Ÿ���� ������
	 *  ���߹��� üũ�ȴ�. */
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
	
	/** isThisUnitWillExplorsion�Լ����� ���� ����� üũ���� ������ ���߹��� ��� ���ߵ� ������ Ÿ�԰� 
	 *  ���� �ֺ��� ���ֵ� ���߹��� üũ�ȴ�. */
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
	
	/**�Ķ���ͷ� �Ѿ�� ������ Ȧ�� ���� �Ǵ� �̹� ������ ���¸� false��, �ƴϸ� true�� ��ȯ�Ѵ�.*/
	private boolean canIUseThis(PushPangUnits _p){
		if(_p.getMyExplosionState() == true || _p.getMyHoldState() == true){
			return false;
		}
		return true;
	}
	
	/**checkWillExplorsionUnits���� ��ȸ�ϸ鼭 üũ�� ������ ���߹��� �̹� �����Ǿ� �ִ��� �ƴ��� ������.
	 * ������ �� ���̽��� ���� ó���ϴ� ����� �ٸ��� �����̴�.*/
	private void isThisUnitWillExplorsion(int _x, int _y){
		//�˰��� �κ� ���߿� ���⿡ ���Ϸ��� ����� Gone���� �Ǵ� Ȧ�� �������� Ȯ���ϴ� �͵� �߰��Ѵ�.
		if(unitArray[_y][_x].isExplosionSuburb || unitArray[_y][_x].isExplosionCenter){
			calculateGameStateInExplorsionCase(_x, _y);
		}
		else if(unitArray[_y][_x].isExplosionSuburb==false && unitArray[_y][_x].isExplosionCenter==false){
			calculateGameStateInUnExplorsionCase(_x, _y);
		}
	}
	
	/**2�� �迭�� ��ȸ�ϸ鼭 ������ �迭�� ���߹����� Ȯ���Ѵ�.*/
	private void checkWillExplorsionUnits(){
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
			//�˰����� �����ϴ� ���κп����� �ݵ�� ���� ���� �༮�� Gone���� Ȧ���������, �ƴ� ���������� Ȯ���Ѵ�.
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState() == true
						|| unitArray[y][x].getMyHoldState() == true || unitArray[y][x].getMyUnitType() >= 100){
					continue;
				}
				isThisUnitWillExplorsion(x, y);
			}
		}
	}
	
	/**��ġ�ؼ� �����̰� �ִ� ���� �ٸ� ������ ��ġ�� ���� �ٽ� false�� �����Ѵ�.*/
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
	
	/**������ ���������� �� ������ ����� ����ġ�� �ڸ��ϵ��� _reqularPos�� ���� �ִ´�.
	 * ���� ���� �� ��� _reqularPos�� -1�� �ִ´�.*/
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
	
	//�ϴ� �ؿ� ����� �ű�� ���� ���߿� �ϱ�� �Ѵ�.
	/**������ �����̴� �̴� ���� �̺�Ʈ�� ���� �� �������� �ϴ� �Լ�*/
	public void resetUnitArrayState(int _x, int _y, View _v){
		resetUnitArray(_x, _y, _v);
		resetEachUnitIsExplorsionState();
	}
	
	/**������ ������ �並 �𵨿� �����Ѵ�.*/
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
	
	/**���� ���¸� �ٽ� �ʱ�ȭ ��Ų��.*/
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
	
	/**�� ��ư�� ������ ��� �Ʒ� ��ư�� ��� false�� �ȴ�.*/
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
