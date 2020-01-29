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
	
	
	
	/**�ʱ� ����, UnHold, ������ �̵� ���� �̺�Ʈ�� ������  
	 * �迭 �����͸� ���鼭 3���̻� ������ �༮�� �������� ����ϰ� �̸� ó���ϴ� �Լ� */
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
	
	/**���ӻ� �����߿� �� ���� ���� �ִ��� �˾ƺ��� �Լ�*/
	private void isKeepGaming(){
		int type[] = new int[16];//���⼭ ������ ���ڴ� ���߿� ���� �ϱ�� �Ѵ�.
		//�� ������ ��Ȳ�� �ľ��Ѵ�.
		for(int y=0;y<mapSizeY;y++){
			for(int x=0;x<mapSizeX;x++){
				if(unitArray[y][x] == null || unitArray[y][x].getMyExplosionState()
						|| unitArray[y][x].getMyUnitType() >=  100){
					continue;
				}
				type[unitArray[y][x].getMyUnitType()]++;
			}
		}
		
		//���� �� ���� ������ 
		for(int i=0; i< 16; i++){
			if(type[i] > 2){
				return ;
			}
		}
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
		myView.getScoreBoard().setText("�������� : " + stageLevel +" ��ǥ : "+ objectScore + " ���� : " + totalScore);
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
	
	/**���������� �����Ѵ�.*/
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
	
	/**���������� Ŭ���� �ߴ��� �˾ƺ��� �Լ�*/
	private boolean isClearStage(){
		//���� �༮�߿� ���� ������ �༮�� �ִ��� Ȯ���ϴ� �Ͱ� ���������� Ŭ���� �ƴ��� Ȯ���Ѵ�.
		//���������� Ŭ���� �ߴ��� �����.
		if(objectScore <= 0){
			return true;
		}
		return false;
	}
	
	/** �����ؾ��� �������� üũ�� ���ֵ��� ���� ��Ų��. */
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
	
	/**�Ʒ� Ȧ�� ��ư�� �ٷ�� �Լ� �ϳ��� Ŭ���ϸ� �ٸ� �ϳ��� false�� �ȴ�.*/
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
