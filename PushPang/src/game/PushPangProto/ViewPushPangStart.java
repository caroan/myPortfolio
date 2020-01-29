package game.PushPangProto;

import android.content.*;
import android.opengl.Visibility;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;

public class ViewPushPangStart {
	private PresentPushPangStart myController;
	private FrameLayout fLay_wholeLayout;
	private LinearLayout lLay_ButtonControllerLayout;
	
//1단계 버튼
	private Button btn_gameStart;
	private Button btn_gameSetting;
	private Button btn_lookGameRanking;
	private Button btn_howToUse;
	
//게임 모드 버튼
	private Button btn_ScenarioModeBtn;
	private Button btn_normalModeBtn;
	private Button btn_clearPangModeBtn;
	private Button btn_figureModeBtn;
	
//맴 사이즈 정하기 버튼
	private ImageView ivw_mapSizeGuide;
	private Button btn_smallSizeMapBtn;
	private Button btn_bigSizeMapBtn;
	
	private LinearLayout.LayoutParams btnParam;
	
	public ViewPushPangStart(PresentPushPangStart _myController){
		myController = _myController;
		setPushPangStartInit();
	}
	
	public void setPushPangStartInit(){
		setLayoutInit();
		setLittleWigetInit();
	}

	private void setLittleWigetInit() {
		// TODO Auto-generated method stub
		setFirstStepBtnLevel();
		setSecondStepBtnAfterGameStart();
		setSelectMapSizeLevel();
		
	}

	private void setSelectMapSizeLevel() {
		// TODO Auto-generated method stub
		ivw_mapSizeGuide = new ImageView(myController);
		ivw_mapSizeGuide.setBackgroundResource(DefineButtonId.getFirstLevelButtonImage(DefineButtonId.IVWID_MAPSIZEGUIDE));
		ivw_mapSizeGuide.setId(DefineButtonId.getFirstLevelButtonImage(DefineButtonId.IVWID_MAPSIZEGUIDE));
		ivw_mapSizeGuide.setVisibility(ImageView.GONE); 
		
		btn_smallSizeMapBtn = makeMyBtnStyle(DefineButtonId.BTNID_SMALLSIZE);
		btn_bigSizeMapBtn = makeMyBtnStyle(DefineButtonId.BTNID_BIGSIZE);
	}

	private void setSecondStepBtnAfterGameStart() {
		// TODO Auto-generated method stub
		btn_normalModeBtn = makeMyBtnStyle(DefineButtonId.BTNID_NORMALMODE);
		btn_ScenarioModeBtn = makeMyBtnStyle(DefineButtonId.BTNID_SCENARIOMODE);
		btn_clearPangModeBtn = makeMyBtnStyle(DefineButtonId.BTNID_CLEARPANGMODE);
		btn_figureModeBtn = makeMyBtnStyle(DefineButtonId.BTNID_FIGUREMODE);
	}

	private void setFirstStepBtnLevel() {
		// TODO Auto-generated method stub
		btn_gameStart = makeMyBtnStyle(DefineButtonId.BTNID_GAMESTART);
		btn_lookGameRanking = makeMyBtnStyle(DefineButtonId.BTNID_RANKINGID);
		btn_howToUse = makeMyBtnStyle(DefineButtonId.BTNID_HOWTO);
		btn_gameSetting = makeMyBtnStyle(DefineButtonId.BTNID_GAMESETTING);
	}
	
	private Button makeMyBtnStyle(int _myId){
		 Button tempBtn = new Button(myController);
		 tempBtn.setBackgroundResource(DefineButtonId.getFirstLevelButtonImage(_myId));
		 tempBtn.setId(_myId);
		 tempBtn.setOnClickListener(myController.btnEvent);
		 tempBtn.setVisibility(Button.GONE);
		 return tempBtn;
	}

	private void setLayoutInit() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater)myController.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fLay_wholeLayout = (FrameLayout)inflater.inflate(R.layout.gamestart_layout, null);
	}
	
	private void setButtonParam(){
		btnParam = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
				);
		btnParam.weight = 1;
	}
	
	public void addAllBtnToButtonControllerLayout(){
		lLay_ButtonControllerLayout = (LinearLayout)myController.findViewById(R.id.linearlayout_initgame);
		setButtonParam();
		lLay_ButtonControllerLayout.addView(btn_gameStart, btnParam);
		lLay_ButtonControllerLayout.addView(btn_lookGameRanking, btnParam);
		lLay_ButtonControllerLayout.addView(btn_howToUse, btnParam);
		lLay_ButtonControllerLayout.addView(btn_gameSetting, btnParam);
		
		
		
		lLay_ButtonControllerLayout.addView(btn_normalModeBtn);
//		lLay_ButtonControllerLayout.addView(btn_ScenarioModeBtn, btnParam);
		lLay_ButtonControllerLayout.addView(btn_clearPangModeBtn);
//		lLay_ButtonControllerLayout.addView(btn_figureModeBtn, btnParam);
		lLay_ButtonControllerLayout.addView(ivw_mapSizeGuide, btnParam);
		
		lLay_ButtonControllerLayout.addView(btn_smallSizeMapBtn, btnParam);
		lLay_ButtonControllerLayout.addView(btn_bigSizeMapBtn, btnParam);
	}

	public void lookFirstLevelBtnInButtonControllerLayout() {
		// TODO Auto-generated method stub
		btn_gameStart.setVisibility(Button.VISIBLE);
		btn_lookGameRanking.setVisibility(Button.VISIBLE);
		btn_howToUse.setVisibility(Button.VISIBLE);
		btn_gameSetting.setVisibility(Button.VISIBLE);
		
		btn_normalModeBtn.setVisibility(Button.GONE);
		btn_ScenarioModeBtn.setVisibility(Button.GONE);
		btn_clearPangModeBtn.setVisibility(Button.GONE);
		btn_figureModeBtn.setVisibility(Button.GONE);
		ivw_mapSizeGuide.setVisibility(ImageView.GONE);
		btn_smallSizeMapBtn.setVisibility(Button.GONE);
		btn_bigSizeMapBtn.setVisibility(Button.GONE);
	}
	
	public void lookSecondLevelBtnAfterGameStartInButtonControllerLayout(){
		btn_gameStart.setVisibility(Button.GONE);
		btn_lookGameRanking.setVisibility(Button.GONE);
		btn_howToUse.setVisibility(Button.GONE);
		btn_gameSetting.setVisibility(Button.GONE);
		
		btn_normalModeBtn.setVisibility(Button.VISIBLE);
		btn_ScenarioModeBtn.setVisibility(Button.VISIBLE);
		btn_clearPangModeBtn.setVisibility(Button.VISIBLE);
		btn_figureModeBtn.setVisibility(Button.VISIBLE);
		
		ivw_mapSizeGuide.setVisibility(ImageView.GONE);
		btn_smallSizeMapBtn.setVisibility(Button.GONE);
		btn_bigSizeMapBtn.setVisibility(Button.GONE);
	}

	public void lookMapSizeSelectLevelWidgetAfterGameModeInButtonControllerLayout()	{
		btn_gameStart.setVisibility(Button.GONE);
		btn_lookGameRanking.setVisibility(Button.GONE);
		btn_howToUse.setVisibility(Button.GONE);
		btn_gameSetting.setVisibility(Button.GONE);
		btn_normalModeBtn.setVisibility(Button.GONE);
		btn_ScenarioModeBtn.setVisibility(Button.GONE);
		btn_clearPangModeBtn.setVisibility(Button.GONE);
		btn_figureModeBtn.setVisibility(Button.GONE);
		
		ivw_mapSizeGuide.setVisibility(ImageView.VISIBLE);
		btn_smallSizeMapBtn.setVisibility(Button.VISIBLE);
		btn_bigSizeMapBtn.setVisibility(Button.VISIBLE);
	}
	
	public FrameLayout getResultLayout(){
		return fLay_wholeLayout;
	}
}
