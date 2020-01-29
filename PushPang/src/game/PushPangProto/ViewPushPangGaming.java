package game.PushPangProto;

import java.util.*;
import android.graphics.*;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
//일단 원래 레이아웃대로 하고 그 이후 게임 레이아웃이 바깥에 나온 레이아웃으로 진행해본다.
public class ViewPushPangGaming {
	
	private AbstractPresentPushPangGaming myPresent;//자신의 프리젠트
	private FrameLayout fLay_wholeLayout;//전체 프레임 레이아웃
	private LinearLayout lLay_wholeWidgetArrangeLayout;//전체 프레임 레이아웃안에서 위젯을 정리하는 레이아웃
	private LinearLayout lLay_TopLayout;//맨 위의 레이아웃
	private LinearLayout lLay_MiddleLayout;//중앙의 레이아웃
	private LinearLayout lLay_BottmLayout;//맨 아래의 레이아웃
	public PushPangGameLayout ppgLay_gameLayout;//중앙의 레이아웃안에 들어가는 게임 레이아웃을 
	private LinearLayout lLay_foreGroundLayout;
	private VideoView video_clearPangAnimation;
	private ForeGroundNoticeImageView fgIvw_gameStateImage;
	
	private ImageView ivw_TopAdImage;//맨위의 광고 뷰
	private ImageView ivw_BottmAdImage;//맨 아래의 광고 뷰
	private Button btn_AllHold;
	private Button btn_AllUnHold;
	private Button btn_pause;
	private TextView txt_ScoreBoard;
	
	private int lLay_MiddleLayoutHeight;//중앙 레이아웃의 높이
	private int btnHeight = 70;//각 버튼들의 크기
	private int btnMagin = 1;//각 버튼들의 마진 크기
	private int adHeight = 20;
	private int adMargin = 1;
	private int mapSizeX;//x축으로 유닛의 수
	private int mapSizeY;//y축으로 유닛의 수

	private int unitWidth;//유닛들의 가로 크기
	private int unitHeight;//유닛들의 세로 크기
	private PushPangUnits[][] unitArray;//게임 레이아웃에 배치되는 유닛들의 배열
	private RegularPosData[][] basisPos;//게임 레이아웃의 유닛 정위치 클래스 배열
	private int[] numberOfUnitTypeHave;//각 타입이 있는 수
	private int maxNumEachTypeHave;//각 타입이 있어야 할 최대 수
	private TimeProgressBar timeBar;
	private int timeBarHeight = 10;
	private int timeBarMaxTime = 60;
	private int scoreBoardHeight = 25;
	
	public static final int PAUSE = 10000;
	public static final int ALLHOLD = 10001;
	public static final int ALLUNHOLD = 10002;
	
	/** 자신의 프리젠터를 받고 맵의 사이즈를 인자로 받는다.
	 * 	이후 기타 필요한 게임 변수를 초기화하고 게임 레이아웃을 구성한다. */
	public ViewPushPangGaming(AbstractPresentPushPangGaming _present, int _mapSizeX, int _mapSizeY) {
		// TODO Auto-generated constructor stub
		myPresent = _present;
		mapSizeX = _mapSizeX;
		mapSizeY = _mapSizeY;
		unitArray = new PushPangUnits[mapSizeY][mapSizeX];
		basisPos = new RegularPosData[mapSizeY][mapSizeX];
		setInitGamingVar();
		setInitGamingLayout();
	}
	
	
	//뷰 구성 코드 시작
	/**게임 레이아웃 안에 들어갈 유닛들의 위치와 크기, 그리고 타입을 정해준다.*/
	public void initGameLayout(int _unitTypes, int _maxNumEachTypeHave){
		unitWidth = myPresent.getWindowManager().getDefaultDisplay().getWidth()/mapSizeX;
		unitHeight = lLay_MiddleLayoutHeight/mapSizeY;
		numberOfUnitTypeHave = new int[_unitTypes];
		maxNumEachTypeHave = _maxNumEachTypeHave;
		for(int y = 0; y <mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				PushPangGameLayout.LayoutParams param = 
					new PushPangGameLayout.LayoutParams(unitWidth, unitHeight, x*unitWidth, y*unitHeight);
				basisPos[y][x] = new RegularPosData(x*unitWidth, y*unitHeight, x*unitWidth+unitWidth, y*unitHeight+unitHeight);
				if(x == mapSizeX-1 && y == mapSizeY-1){
					continue;
				}
				if(x == mapSizeX-2 && y == mapSizeY-1 && myPresent.getMyGameType() == myPresent.NormalMode){
					unitArray[y][x] = makeItem();
					unitArray[y][x].setMyPositionXY(x, y);
					ppgLay_gameLayout.addView(unitArray[y][x], param);
					continue;
				}
				unitArray[y][x] = makeMyVirus(_unitTypes);
				unitArray[y][x].setMyPositionXY(x, y);
				ppgLay_gameLayout.addView(unitArray[y][x], param);
			}
		}
		//testUnit();
		//testClearPangUnits();
	}
	private void testUnit(){
		for(int y = 0; y <mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				PushPangGameLayout.LayoutParams param = 
					new PushPangGameLayout.LayoutParams(unitWidth, unitHeight, x*unitWidth, y*unitHeight);
				basisPos[y][x] = new RegularPosData(x*unitWidth, y*unitHeight, x*unitWidth+unitWidth, y*unitHeight+unitHeight);
				if(x == 4 && y == 4){
					continue;
				}
				unitArray[y][x] = new PushPangUnits(myPresent);
			//	unitArray[y][x].setOnTouchListener(myPresent.unitTouchEvent);
				unitArray[y][x].setVisibility(View.VISIBLE);
				unitArray[y][x].setMyPositionXY(x, y);
			//	unitArray[y][x].setOnGestureListener(myPresent.re);
				ppgLay_gameLayout.addView(unitArray[y][x], param);
			}
		}
		unitArray[0][0].setMyUnitType(1);
		unitArray[0][1].setMyUnitType(1);
		unitArray[0][2].setMyUnitType(1);
		unitArray[0][3].setMyUnitType(3);
		unitArray[0][4].setMyUnitType(104);
		unitArray[0][5].setMyUnitType(3);
		unitArray[0][6].setMyUnitType(3);
		
		unitArray[1][0].setMyUnitType(2);
		unitArray[1][1].setMyUnitType(2);
		unitArray[1][2].setMyUnitType(2);
		unitArray[1][3].setMyUnitType(106);
		unitArray[1][4].setMyUnitType(105);
		unitArray[1][5].setMyUnitType(4);
		unitArray[1][6].setMyUnitType(4);
		
		unitArray[2][0].setMyUnitType(5);
		unitArray[2][1].setMyUnitType(5);
		unitArray[2][2].setMyUnitType(5);
		unitArray[2][3].setMyUnitType(5);
		unitArray[2][4].setMyUnitType(103);
		unitArray[2][5].setMyUnitType(4);
		unitArray[2][6].setMyUnitType(4);
		
		unitArray[3][0].setMyUnitType(5);
		unitArray[3][1].setMyUnitType(6);
		unitArray[3][2].setMyUnitType(102);
		unitArray[3][3].setMyUnitType(5);
		unitArray[3][4].setMyUnitType(5);
		unitArray[3][5].setMyUnitType(7);
		unitArray[3][6].setMyUnitType(7);
		
		unitArray[4][0].setMyUnitType(5);
		unitArray[4][1].setMyUnitType(5);
		unitArray[4][2].setMyUnitType(101);
		unitArray[4][3].setMyUnitType(6);
		unitArray[4][5].setMyUnitType(9);
		unitArray[4][6].setMyUnitType(7);
		
		unitArray[5][0].setMyUnitType(6);
		unitArray[5][1].setMyUnitType(6);
		unitArray[5][2].setMyUnitType(101);
		unitArray[5][3].setMyUnitType(6);
		unitArray[5][4].setMyUnitType(9);
		unitArray[5][5].setMyUnitType(6);
		unitArray[5][6].setMyUnitType(6);
		
		unitArray[6][0].setMyUnitType(8);
		unitArray[6][1].setMyUnitType(8);
		unitArray[6][2].setMyUnitType(101);
		unitArray[6][3].setMyUnitType(6);
		unitArray[6][4].setMyUnitType(9);
		unitArray[6][5].setMyUnitType(8);
		unitArray[6][6].setMyUnitType(8);
	}
	private void testClearPangUnits(){
		for(int y = 0; y <mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				PushPangGameLayout.LayoutParams param = 
					new PushPangGameLayout.LayoutParams(unitWidth, unitHeight, x*unitWidth, y*unitHeight);
				basisPos[y][x] = new RegularPosData(x*unitWidth, y*unitHeight, x*unitWidth+unitWidth, y*unitHeight+unitHeight);
				if(x == mapSizeX-1 && y == mapSizeY-1){
					continue;
				}
				unitArray[y][x] = new PushPangUnits(myPresent);
			//	unitArray[y][x].setOnTouchListener(myPresent.unitTouchEvent);
				unitArray[y][x].setVisibility(View.VISIBLE);
				unitArray[y][x].setMyPositionXY(x, y);
			//	unitArray[y][x].setOnGestureListener(myPresent.re);
				ppgLay_gameLayout.addView(unitArray[y][x], param);
			}
		}
		unitArray[0][0].setMyUnitType(1);
		unitArray[0][1].setMyUnitType(1);
		unitArray[0][2].setMyUnitType(1);
		unitArray[0][3].setMyUnitType(3);
		unitArray[0][4].setMyUnitType(3);
		unitArray[0][5].setMyUnitType(3);
		
		unitArray[1][0].setMyUnitType(2);
		unitArray[1][1].setMyUnitType(2);
		unitArray[1][2].setMyUnitType(2);
		unitArray[1][3].setMyUnitType(4);
		unitArray[1][4].setMyUnitType(4);
		unitArray[1][5].setMyUnitType(4);
		
		unitArray[2][0].setMyUnitType(5);
		unitArray[2][1].setMyUnitType(5);
		unitArray[2][2].setMyUnitType(5);
		unitArray[2][3].setMyUnitType(5);
		unitArray[2][4].setMyUnitType(4);
		unitArray[2][5].setMyUnitType(4);
		
		unitArray[3][0].setMyUnitType(6);
		unitArray[3][1].setMyUnitType(6);
		unitArray[3][2].setMyUnitType(6);
		unitArray[3][3].setMyUnitType(7);
		unitArray[3][4].setMyUnitType(7);
		unitArray[3][5].setMyUnitType(7);
		
		unitArray[4][0].setMyUnitType(10);
		unitArray[4][1].setMyUnitType(10);
		unitArray[4][2].setMyUnitType(10);
		unitArray[4][3].setMyUnitType(9);
		unitArray[4][4].setMyUnitType(9);
		unitArray[4][5].setMyUnitType(9);
		
		unitArray[5][0].setMyUnitType(11);
		unitArray[5][1].setMyUnitType(11);
		unitArray[5][2].setMyUnitType(11);
		unitArray[5][3].setMyUnitType(11);
		unitArray[5][4].setMyUnitType(11);
	}
	
	private PushPangUnits makeItem(){
		int type;
		type = ( ((int)(Math.random()*100))% 6 ) + 101;
		PushPangUnits tempItem = new PushPangUnits(myPresent);
		tempItem.setMyUnitType(type);
		return tempItem;
	}

	private PushPangUnits makeMyVirus(int _unitTypes){
		//Random unitType = new Random(System.currentTimeMillis());
		int type;
		while(true){
			type = ((int)(Math.random()*100))%_unitTypes;
			if(numberOfUnitTypeHave[type] >=  maxNumEachTypeHave){
				continue;
			}
			numberOfUnitTypeHave[type]++;
			break;
		}
		PushPangUnits tempUnits = new PushPangUnits(myPresent);
		tempUnits.setMyUnitType(type);
		return tempUnits;
	}
	
	private void setInitGamingVar() {
		// TODO Auto-generated method stub
	}

	private void setInitGamingLayout() {
		// TODO Auto-generated method stub
		setLayoutsInit();
		setLittleWigetInit();
		addAllSubToWhole();
	}

	private void addAllSubToWhole() {
		// TODO Auto-generated method stub
		addSubToSubLayout();
		addSubToWholeLayout();
	}

	private void addSubToWholeLayout() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams paramForAdView = new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				adHeight);
		paramForAdView.topMargin = adMargin;
		paramForAdView.bottomMargin = adMargin;
		fLay_wholeLayout.addView(lLay_wholeWidgetArrangeLayout,
				new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.FILL_PARENT,
						FrameLayout.LayoutParams.FILL_PARENT
						)
		);
		fLay_wholeLayout.addView(lLay_foreGroundLayout,
				new FrameLayout.LayoutParams(
						FrameLayout.LayoutParams.FILL_PARENT,
						FrameLayout.LayoutParams.FILL_PARENT
						)
		);
		lLay_wholeWidgetArrangeLayout.addView(ivw_TopAdImage,paramForAdView);
		lLay_wholeWidgetArrangeLayout.addView(lLay_TopLayout,
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
						)
		);
		lLay_wholeWidgetArrangeLayout.addView(lLay_MiddleLayout,
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
						)
		);
		lLay_wholeWidgetArrangeLayout.addView(lLay_BottmLayout,
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
						)
		);
		lLay_wholeWidgetArrangeLayout.addView(ivw_BottmAdImage,paramForAdView);
	}

	private void addSubToSubLayout() {
		// TODO Auto-generated method stub
		addSubToTopLayout();
		addSubToMiddleLayout();
		addSubToBottomLayout();
		addSubToForeGroundLayout();
	}
	
	private void addSubToForeGroundLayout(){
		lLay_foreGroundLayout.addView(fgIvw_gameStateImage, 
				new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT));
		lLay_foreGroundLayout.addView(video_clearPangAnimation);
	}

	private void addSubToBottomLayout() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,btnHeight);
		param.topMargin = btnMagin;
		param.bottomMargin = btnMagin;
		param.weight = 1;
		lLay_BottmLayout.addView(btn_pause, param);
		//lLay_BottmLayout.addView(btn_UnHold, param);
	}

	/**중간 게임 레이아웃 부분. 
	 * 여기에 프로그래스 위젯을 추가할 것이다.*/
	private void addSubToMiddleLayout() {
		// TODO Auto-generated method stub
		int titleAndStatusBarHeight = 0;//getTitleAndStatusBar();
		lLay_MiddleLayoutHeight = getMiddleLayoutHeight(titleAndStatusBarHeight);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, lLay_MiddleLayoutHeight);
		param.topMargin = 0;
		param.bottomMargin = 0;
		lLay_MiddleLayout.addView(txt_ScoreBoard, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, scoreBoardHeight));
		lLay_MiddleLayout.addView(timeBar, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, timeBarHeight));
		lLay_MiddleLayout.addView(ppgLay_gameLayout, param);
	}
	/** 중간 레이아웃이 쓸수 있는 크기를 정한다.*/
	private int getMiddleLayoutHeight(int _titleAndStatusBarHeight) {
		// TODO Auto-generated method stub
		return	myPresent.getWindowManager().getDefaultDisplay().getHeight()
		- _titleAndStatusBarHeight
		- (btnHeight*2 + adHeight*2 + adMargin*4 + btnMagin*4 + timeBarHeight + scoreBoardHeight);
	}
	/**맨 위의 타이틀바와 상태바의 크기를 알아낸다.
	 * 일단 이렇게 정하고 나중에 다시 바꾸기로 한다.*/
	private int getTitleAndStatusBar() {
		// TODO Auto-generated method stub
		/*		Rect rectgle= new Rect();
		Window window = myPresent.getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int statusBarHeight= rectgle.top;
		int contentViewTop= window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		int titleBarHeight= contentViewTop - statusBarHeight;*/
		int titleBarAndStatusBarHeight = 0;
		
		if(320 >= myPresent.getWindowManager().getDefaultDisplay().getHeight()){
			titleBarAndStatusBarHeight = 20*2;
		}else if(480 >= myPresent.getWindowManager().getDefaultDisplay().getHeight()
				&& 320 < myPresent.getWindowManager().getDefaultDisplay().getHeight()){
			titleBarAndStatusBarHeight = 25*2;
		}else{
			titleBarAndStatusBarHeight = 38*2;
		}
		
		return titleBarAndStatusBarHeight;
	}

	/**맨 위의 레이아웃에 버튼을 붙인다.*/
	private void addSubToTopLayout() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0,btnHeight);
		param.topMargin = btnMagin;
		param.bottomMargin = btnMagin;
		param.weight = 1;
		lLay_TopLayout.addView(btn_AllHold, param);
		lLay_TopLayout.addView(btn_AllUnHold, param);
	}

	/**버튼 등의 작은 위젯들을 초기화 한다.*/
	private void setLittleWigetInit() {
		// TODO Auto-generated method stub
		setAdImageView();
		setTopLayoutWidget();
		setMiddleLayoutWidget();
		setBottomLayoutWidget();
		setForeGroundLayoutWidget();
	}
	
	private void setForeGroundLayoutWidget(){
		//게임 오버, 게임 시작, 게임 스테이지 끝, 게임 중간 동영상 등등 준비 할것.
		video_clearPangAnimation = new VideoView(myPresent);
		video_clearPangAnimation.setVisibility(View.GONE);
		fgIvw_gameStateImage = new ForeGroundNoticeImageView(myPresent, 2);
		fgIvw_gameStateImage.setOnTouchListener(myPresent.getTouchForeGround());
		fgIvw_gameStateImage.setVisibility(View.GONE);
	}
	
	/** 나중에 TEXT는 없앤다.*/
	private void setBottomLayoutWidget() {
		// TODO Auto-generated method stub
		btn_pause = new Button(myPresent);
		btn_pause.setId(PAUSE);
		btn_pause.setOnTouchListener(myPresent.getBtnEvent());
		btn_pause.setText("Pause");
	}

	/** 나중에 넣기로 한다*/
	private void setMiddleLayoutWidget() {
		// TODO Auto-generated method stub
		txt_ScoreBoard = new TextView(myPresent);
		txt_ScoreBoard.setTextSize(13);
		txt_ScoreBoard.setTextColor(Color.BLACK);
		txt_ScoreBoard.setGravity(Gravity.NO_GRAVITY);
		txt_ScoreBoard.setText("스테이지 : 1 목표 : 0  점수 : 0");
		txt_ScoreBoard.setBackgroundColor(Color.WHITE);
		timeBar = new TimeProgressBar(myPresent, timeBarMaxTime);
	}

	/** 나중에 Text는 없앤다.*/
	private void setTopLayoutWidget() {
		// TODO Auto-generated method stub
		btn_AllHold = new Button(myPresent);
		btn_AllUnHold = new Button(myPresent);
		btn_AllHold.setId(ALLHOLD);
		btn_AllUnHold.setId(ALLUNHOLD);
		btn_AllHold.setOnTouchListener(myPresent.getBtnEvent());
		btn_AllUnHold.setOnTouchListener(myPresent.getBtnEvent());

		
		btn_AllHold.setText("All Hold");
		btn_AllUnHold.setText("All UnHold");
	}

	/**광고 뷰*/
	private void setAdImageView() {
		// TODO Auto-generated method stub
		ivw_BottmAdImage = new ImageView(myPresent);
		ivw_TopAdImage = new ImageView(myPresent);
	}

	/**모든 레이아웃을 만들고 초기설정을 한다.*/
	private void setLayoutsInit() {
		// TODO Auto-generated method stub
		setWholeLayout();
		setForeGroundLayout();
		setWholeWidgetArrageLayout();
		setTopLayout();
		setMiddleLayout();
		setBottomLayout();
		setPPGLayout();
	}

	/**게임 레이아웃을 만든다.*/
	private void setPPGLayout() {
		// TODO Auto-generated method stub
		ppgLay_gameLayout = new PushPangGameLayout(myPresent);
	}

	/**맨 아래 레이아웃을 만든다*/
	private void setBottomLayout() {
		// TODO Auto-generated method stub
		lLay_BottmLayout = new LinearLayout(myPresent);
		lLay_BottmLayout.setOrientation(LinearLayout.HORIZONTAL);
		lLay_BottmLayout.setBackgroundColor(Color.WHITE);
	}

	/**중앙 레이아웃을 만든다.*/
	private void setMiddleLayout() {
		// TODO Auto-generated method stub
		lLay_MiddleLayout = new LinearLayout(myPresent);
		lLay_MiddleLayout.setOrientation(LinearLayout.VERTICAL);
		lLay_MiddleLayout.setBackgroundColor(Color.BLUE);
	}

	/**맨 위 레이아웃을 만든다.*/
	private void setTopLayout() {
		// TODO Auto-generated method stub
		lLay_TopLayout = new LinearLayout(myPresent);
		lLay_TopLayout.setOrientation(LinearLayout.HORIZONTAL);
		lLay_TopLayout.setBackgroundColor(Color.WHITE);
	}

	/**전체 프레임 레이아웃안에서 위젯을 정렬시킬 리니어 레이아웃을 만든다.*/
	private void setWholeWidgetArrageLayout() {
		// TODO Auto-generated method stub
		lLay_wholeWidgetArrangeLayout = new LinearLayout(myPresent);
		lLay_wholeWidgetArrangeLayout.setOrientation(LinearLayout.VERTICAL);
	}
	
	private void setForeGroundLayout(){
		lLay_foreGroundLayout = new LinearLayout(myPresent);
		lLay_foreGroundLayout.setOrientation(LinearLayout.VERTICAL);
		lLay_foreGroundLayout.setGravity(Gravity.CENTER);
	}
	
	/**전체 레이아웃인 프레임 레이아웃을 만든다.*/
	private void setWholeLayout(){
		fLay_wholeLayout = new FrameLayout(myPresent);
	}

	/**전체 레이아웃을 반환한다.*/
	public ViewGroup getWholeLayout(){
		return fLay_wholeLayout;
	}
	//뷰 구성 코드 끝.
	
	public RegularPosData[][] getBasisPos(){
		return basisPos;
	}
	
	/**현재 뷰가 가지고 있던 푸시팡의 모델을 반환한다.(초기 게임 유닛을 배치 시키고 프리젠트에게 줄 때 쓴다.)*/
	public PushPangUnits[][] getUnitArray(){
		return unitArray;
	}
	/**프리젠트에서 변환한 푸시팡 유닛 배열을 뷰의 푸시팡 유닛 배열에 적용한다.*/
	public void setUnitArray(PushPangUnits[][] _unitArray){
		unitArray = _unitArray;
	}
	public Button getHoldBtn(){
		return btn_pause;
	}
	public TimeProgressBar getTimeBar(){
		return timeBar;
	}
	public TextView getScoreBoard(){
		return txt_ScoreBoard;
	}
	public ForeGroundNoticeImageView getGameStateImage(){
		return fgIvw_gameStateImage;
	}
	public VideoView getClearPangAnimation(){ 
		return video_clearPangAnimation;
	}
	public Button getAllHoldBtn(){
		return btn_AllHold;
	}
	public Button getAllUnHoldBtn(){
		return btn_AllUnHold;
	}
	public void relocateGameUnitInGameLayout(PushPangUnits[][] _unitArray){
		unitWidth = myPresent.getWindowManager().getDefaultDisplay().getWidth()/mapSizeX;
		unitHeight = lLay_MiddleLayoutHeight/mapSizeY;
		Log.d("reLocate",""+myPresent.getWindowManager().getDefaultDisplay().getWidth());
		for(int y = 0; y <mapSizeY; y++){
			for(int x = 0; x<mapSizeX; x++){
				basisPos[y][x] = new RegularPosData(x*unitWidth, y*unitHeight, x*unitWidth+unitWidth, y*unitHeight+unitHeight);
				if(_unitArray[y][x] == null){
					Log.d("relocate", "null");
					continue;
				}
				unitArray[y][x] = makeReLocateUnits(_unitArray[y][x]);
				unitArray[y][x].setMyPositionXY(x, y);
				PushPangGameLayout.LayoutParams param = 
					new PushPangGameLayout.LayoutParams(unitWidth, unitHeight, x*unitWidth, y*unitHeight);
				ppgLay_gameLayout.addView(unitArray[y][x], param);
			}
		}
	}
	
	private PushPangUnits makeReLocateUnits(PushPangUnits _unitArray){
		PushPangUnits tempUnit = new PushPangUnits(myPresent);
		tempUnit.setMyUnitType(_unitArray.getMyUnitType());
		tempUnit.setMyHoldState(_unitArray.getMyHoldState());
		tempUnit.setMyExPlosionState(_unitArray.getMyExplosionState());
		//tempUnit.setOnTouchListener(myPresent.unitTouchEvent);
		return tempUnit;
	}
	
	public ViewGroup getGameLayout(){
		return ppgLay_gameLayout;
	}
}

/**각 유닛의 정위치 좌표를 간직하고 데이터 클래스*/
class RegularPosData{
	public RegularPosData(int _left, int _top, int _right, int _bottom) {
		// TODO Auto-generated constructor stub
		top = _top;
		left = _left;
		right = _right;
		bottom = _bottom;
	}
	private int top;
	private int left;
	private int right;
	private int bottom;
	
	public int getTop(){
		return top;
	}
	public int getLeft(){
		return left;
	}
	public int getRight(){
		return right;
	}
	public int getBottom(){
		return bottom;
	}
}
