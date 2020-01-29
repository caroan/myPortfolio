package game.PushPangProto;

public class DefineButtonId {
	public final static int BTNID_GAMESTART = 11;
	public final static int BTNID_RANKINGID = 12;
	public final static int BTNID_HOWTO = 13;
	public final static int BTNID_GAMESETTING = 14;
	
	public final static int BTNID_NORMALMODE = 21;
	public final static int BTNID_SCENARIOMODE = 22;
	public final static int BTNID_CLEARPANGMODE = 23;
	public final static int BTNID_FIGUREMODE = 24;
	
	public final static int IVWID_MAPSIZEGUIDE = 31;
	public final static int BTNID_SMALLSIZE = 32;
	public final static int BTNID_BIGSIZE = 33;
	
	public static int getFirstLevelButtonImage(int _btnId){
		switch (_btnId) {
		case BTNID_GAMESTART:
			return R.drawable.doc_btn_ppstart_gamestart;
		case BTNID_RANKINGID:
			return R.drawable.ppstart_btn_rankingbtn;
		case BTNID_HOWTO:
			return R.drawable.ppstart_btn_howtobtn;
		case BTNID_GAMESETTING:
			return R.drawable.ppstart_btn_settingbtn;
		
		case BTNID_NORMALMODE:
			return R.drawable.ppstart_btn_normalbtn;
		case BTNID_SCENARIOMODE:
			return R.drawable.ppstart_btn_scenariobtn;
		case BTNID_CLEARPANGMODE:
			return R.drawable.ppstart_btn_clearpangbtn;
		case BTNID_FIGUREMODE:
			return R.drawable.ppstart_btn_figurebtn;
			
		case IVWID_MAPSIZEGUIDE:
			return R.drawable.ppstart_ivw_mapsizegameguide;
		case BTNID_SMALLSIZE:
			return R.drawable.ppstart_btn_smallsizemapbtn;
		case BTNID_BIGSIZE:
			return R.drawable.ppstart_btn_bigsizemapbtn;
		}
		return -1;
	}
}
