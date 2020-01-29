package game.PushPangProto;

import java.util.*;
import game.PushPangProto.AdvancedGestureDetectorWrapper.*;
import game.PushPangProto.PushPangGameLayout.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.content.*;
import android.widget.*;
import android.util.*;
import android.os.*;
import java.lang.Runtime;

public class PresentPushPangNormalMode extends AbstractPresentPushPangGaming{
	private boolean cannotMove;
	private ViewPushPangGaming myView;
	private int mapSizeX;
	private int mapSizeY;
	private PushPangUnits [][] unitArray;
	private RegularPosData[][] basisPos;
	private boolean holdBtnState;
	private boolean unHoldBtnState;
	private int totalScore;
	private int stageLevel;
	private int objectScore;
	private int timeTick = 1;
	private Timer myTimer = new Timer();
	private TimerTask countTime;
	private ModelPushPangGame gameData;
	private boolean isResume;
	private int currentTime;
	private PushPangUnits[][] alreadyexsistUnitArray;
	private int unitTypesInGame;
	private int eachTypeUnitMax;
	private boolean isUseItem;
	private boolean isClearPang;
	private DefineGameMusic myMusic;
	private final int GameStart = 0;
	private final int GameClear = 1;
	private final int GameOver = 2;
	private final int GameRestart = 3;
	private final int GamePause = 4;
	private Bundle onResumeData;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(onResumeData != null){
			isResume = true;
			ModelPushPangGame savedData = (ModelPushPangGame)onResumeData.getSerializable("gamedata");
			alreadyexsistUnitArray = savedData.getUnitArray();
			objectScore = savedData.getCurrentObjectScore();
			stageLevel = savedData.getCurrentStage();
			currentTime = savedData.getCurrentTime();
			totalScore = savedData.getCurrentTotalScore();
			//setStageSettings();
			showDialog(GameStart);
			Log.d("adfdaf","Resume");
		}
		super.onResume();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		myView.getGameLayout().removeAllViews();
		gameData = new ModelPushPangGame(unitArray, myView.getTimeBar().getCurrentTime(), stageLevel, objectScore, totalScore);
		outState.putSerializable("gamedata", gameData);
		onResumeData = new Bundle();
		onResumeData.putSerializable("gamedata", gameData);
		Log.d("end?", "end");
	}
	
	protected Dialog onCreateDialog(int id){
    	switch (id) {
		case GameStart:
			return new AlertDialog.Builder(PresentPushPangNormalMode.this)
			.setTitle("Are You Ready ? ")
			.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(isResume){
						countTime.cancel();
					}
					PresentPushPangNormalMode.this.setStageSettings();
					PresentPushPangNormalMode.this.calculateGameState();
					PresentPushPangNormalMode.this.myView.getTimeBar().setCurrentTime(currentTime);
					PresentPushPangNormalMode.this.setCountTimerTask();
					PresentPushPangNormalMode.this.myTimer.scheduleAtFixedRate(countTime, 0, 1000);
				}
			})
			.setCancelable(false)
			.create();
		case GameClear:
			return new AlertDialog.Builder(PresentPushPangNormalMode.this)
			.setTitle("Game Clear~!")
			.setCancelable(false)
			.setPositiveButton("Go Next Level", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PresentPushPangNormalMode.this.showDialog(GameStart);
				}
			})
			.create();
		case GameOver:
			return new AlertDialog.Builder(PresentPushPangNormalMode.this)
			.setTitle("Game Over")
			.setMessage("ReStart Game?")
			.setCancelable(false)
			.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PresentPushPangNormalMode.this.initVars();
					PresentPushPangNormalMode.this.setGameScoreBoard();
					PresentPushPangNormalMode.this.showDialog(GameStart);
				}
			})
			.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					PresentPushPangNormalMode.this.test();
				}
			})
			.create();
		case GamePause:
			return new AlertDialog.Builder(PresentPushPangNormalMode.this)
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
	
	private void test(){
		this.finish();
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
		myGameType = NormalMode;
		if(savedInstanceState == null){
			isResume = false;
		}else{
			isResume = true;
			ModelPushPangGame savedData = (ModelPushPangGame)savedInstanceState.getSerializable("gamedata");
			alreadyexsistUnitArray = savedData.getUnitArray();
			objectScore = savedData.getCurrentObjectScore();
			stageLevel = savedData.getCurrentStage();
			currentTime = savedData.getCurrentTime();
			totalScore = savedData.getCurrentTotalScore();
		}
		setGameScoreBoard();
		showDialog(GameStart);
//		myView.getGameStateImage().setMyState(0);
//		setForeGroundEnabled(true);
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
				//showDialog(GameOver);
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
		totalScore = 0;
		objectScore = 40;
		stageLevel = 0;
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
		myView.getAllUnHoldBtn().setEnabled(false);
		checkWillExplorsionUnits();
		explorCheckedUnits();
		calculateHandler.sendEmptyMessageDelayed(0, 1100);
	}
	
	Handler calculateHandler = new Handler(){
		public void handleMessage(Message msg) {
			timeTick = 0;
			if(isClearStage()){
				countTime.cancel();
				showDialog(GameClear);
				//myView.getGameStateImage().setMyState(1);
				//setForeGroundEnabled(true);
				myView.getAllUnHoldBtn().setEnabled(true);
				return ;
			}
			isKeepGaming();
			myView.getAllUnHoldBtn().setEnabled(true);
			timeTick = 1;
		}
	};
	
	/**게임상 유닛중에 더 터질 것이 있는지 알아보는 함수*/
	private void isKeepGaming(){
		int type[] = new int[16];//여기서 유닛의 숫자는 나중에 따로 하기로 한다.
		//각 유닛의 상황을 파악한다.
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState()
						|| unitArray[y][x].getMyUnitType() >=  100){
					continue;
				}
				type[unitArray[y][x].getMyUnitType()]++;
			}
		}
		
		//폭발 할 것이 있으면 
		for(int i=0; i< 16; i++){
			if(type[i] > 2){
				return ;
			}
		}
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
		myView.getScoreBoard().setText("스테이지 : " + stageLevel +" 목표 : "+ objectScore + " 점수 : " + totalScore);
	}
	
	private void setUnitTypesNumAndEachTypeUnitMax(){
		if(mapSizeX == 5){
			if(stageLevel == 0){
				unitTypesInGame = 5;
				eachTypeUnitMax = 5;
			}else if(stageLevel == 1){
				unitTypesInGame = 6;
				eachTypeUnitMax = 4;
			}else{
				unitTypesInGame = 8;
				eachTypeUnitMax = 3;
			}
		}else{
			if(stageLevel <= 1){
				unitTypesInGame = 8;
				eachTypeUnitMax = 6;
			}else if(stageLevel <= 2){
				unitTypesInGame = 10;
				eachTypeUnitMax = 5;
			}else if(stageLevel <= 3){
				unitTypesInGame = 12;
				eachTypeUnitMax = 4;
			}
		}
	}
	
	/**스테이지를 세팅한다.*/
	private void setStageSettings(){
		isClearPang = false;
		myView.getGameLayout().removeAllViews();
		setUnitTypesNumAndEachTypeUnitMax();
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
		myView.initGameLayout(unitTypesInGame, eachTypeUnitMax);
		unitArray = myView.getUnitArray();
		currentTime = 0;
		holdBtnState = unHoldBtnState = false;
		cannotMove = false;
		stageLevel++;
		objectScore = 0; 
		objectScore = 30 + stageLevel*10;
	}
	
	/**스테이지를 클리어 했는지 알아보는 함수*/
	private boolean isClearStage(){
		//남은 녀석중에 폭발 가능한 녀석이 있는지 확인하는 것과 스테이지가 클리어 됐는지 확인한다.
		//스테이지를 클리어 했는지 물어본다.
		if(objectScore <= 0){
			return true;
		}
		return false;
	}
	
	/** 폭발해야할 유닛으로 체크된 유닛들을 폭발 시킨다. */
	private void explorCheckedUnits(){
		int score = 0;
		for(int y = 0; y<mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState()){
					continue;
				}
				if(unitArray[y][x].isExplosionCenter || unitArray[y][x].isExplosionSuburb){
					unitArray[y][x].setMyExPlosionState(true);
					score ++;
				}/*
				else{
					unitArray[y][x].setMyUnitType(unitArray[y][x].getMyUnitType());
				}*/
			}
		}
		objectScore -= score;
		setGameScoreBoard();
		if(score == 3){
			myView.getTimeBar().plusTime(1);
			myMusic.play(DefineGameMusic.UNITEXPLORED_SMALL);
			totalScore += score;
		} 
		else if(score > 3 && score <= 5){
			myMusic.play(DefineGameMusic.UNITEXPLORED_SMALL);
			myView.getTimeBar().plusTime(2);
			totalScore += score + 1;
		}
		else if(score > 5 && score <= 12){
			myView.getTimeBar().plusTime(7);
			myMusic.play(DefineGameMusic.UNITEXPLORED_SMALL);
			totalScore += score + 2;
		}
		else if(score > 12 && score < (mapSizeX*mapSizeY-2) ){
			myView.getTimeBar().plusTime(15);
			myMusic.play(DefineGameMusic.UNITEXPLORED_SMALL);
			totalScore += score + 3;
		}
		else if(score >= (mapSizeX*mapSizeY-1)){
			myView.getTimeBar().plusTime(25);
			myMusic.play(DefineGameMusic.UNITEXPLORED_BIG);
			totalScore += score + 5;
		}
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
		return totalScore;
	}
	public void setTotalScore(int _totalScore){
		totalScore = _totalScore;
	}
	public int getObjectScore(){
		return objectScore;
	}
	public void setObjectScore(int _ObjectScore){
		objectScore = _ObjectScore;
	}
	public int getUnitTypesInGame(){
		return unitTypesInGame;
	}
	public ViewPushPangGaming getMyView(){
		return myView;
	}
	
	/**아래 홀드 버튼을 다루는 함수 하나를 클릭하면 다른 하나는 false가 된다.*/
/*	private void changeHoldStateAndBtnBackGround(View _v){
		if(_v.equals(myView.getHoldBtn())){
			holdBtnState = !holdBtnState;
			unHoldBtnState = false;
			if(holdBtnState){
				return ;
			}
		}else{
			unHoldBtnState = !unHoldBtnState;
			holdBtnState = false;
			if(unHoldBtnState){
				return ;
			}
		}
	}
	*/
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
					PresentPushPangNormalMode.this.finish();
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
	
	public int getMyGameType(){
		return NormalMode;
	}
}
