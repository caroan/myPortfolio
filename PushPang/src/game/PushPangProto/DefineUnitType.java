package game.PushPangProto;

public class DefineUnitType {
	public static final int VIRUS_CAT_RECT = 0;
	public static final int VIRUS_EVIL_RECT = 1;
	public static final int VIRUS_MARINBOY_RECT = 2;
	public static final int VIRUS_PAPA_RECT = 3;
	public static final int VIRUS_SHYGUY_RECT = 4;
	public static final int VIRUS_SMART_RECT = 5;
	public static final int VIRUS_SPECIALFORCE_RECT = 6;
	public static final int VIRUS_VAMPIRE_RECT = 7;
	public static final int VIRUS_BLACKSUIT_RECT = 8;

	public static final int VIRUS_ANDROID_OVAL = 9;
	public static final int VIRUS_GIRL_OVAL = 10;
	public static final int VIRUS_JOKER_OVAL = 11;
	public static final int VIRUS_MELONG_OVAL = 12;
	public static final int VIRUS_MUMMY_OVAL = 13;
	public static final int VIRUS_PIG_OVAL = 14;
	public static final int VIRUS_SPORTS_OVAL = 15;
	
	
	public static final int EXCAVATOR = 100;
	public static final int HORIZONBOMB = 101;
	public static final int VETICALBOMB = 102;
	public static final int CROSSBOMB = 103;
	public static final int RANDOMBOMB = 104;
	public static final int SQUAREBOMB = 105;
	public static final int TIMEMAGIC = 106;
	
	public static final int RECTVIRUSEXPLORED = 1000;
	public static final int OVALVIRUSEXPLORED = 1001;
	
	private DefineUnitType(){
		
	}
	
	public static int getUnitImage(int _unitNum){
		switch (_unitNum) {
		case VIRUS_CAT_RECT:
			return R.drawable.game_virus_rect_cat;
		case VIRUS_EVIL_RECT:
			return R.drawable.game_virus_rect_evil;
		case VIRUS_MARINBOY_RECT:
			return R.drawable.game_virus_rect_marinboy;
		case VIRUS_PAPA_RECT:
			return R.drawable.game_virus_rect_papa;
		case VIRUS_SHYGUY_RECT:
			return R.drawable.game_virus_rect_shyguy;
		case VIRUS_SMART_RECT:
			return R.drawable.game_virus_rect_smart;
		case VIRUS_SPECIALFORCE_RECT:
			return R.drawable.game_virus_rect_specialforce;
		case VIRUS_VAMPIRE_RECT:
			return R.drawable.game_virus_rect_vampire;
		case VIRUS_BLACKSUIT_RECT:
			return R.drawable.game_virus_rect_blacksuit;
		case VIRUS_ANDROID_OVAL:
			return R.drawable.game_virus_oval_android;
		case VIRUS_GIRL_OVAL:
			return R.drawable.game_virus_oval_girl;
		case VIRUS_JOKER_OVAL:
			return R.drawable.game_virus_oval_joker;
		case VIRUS_MELONG_OVAL:
			return R.drawable.game_virus_oval_melong;
		case VIRUS_MUMMY_OVAL:
			return R.drawable.game_virus_oval_mummy;
		case VIRUS_PIG_OVAL:
			return R.drawable.game_virus_oval_pig;
		case VIRUS_SPORTS_OVAL:
			return R.drawable.game_virus_oval_sports;
			
		case EXCAVATOR:
			return -1;
		case HORIZONBOMB:
			return R.drawable.game_item_horizon;
		case VETICALBOMB:
			return R.drawable.game_item_vertical;
		case CROSSBOMB:
			return R.drawable.game_item_cross;
		case RANDOMBOMB:
			return R.drawable.game_item_random;
		case SQUAREBOMB:
			return R.drawable.game_item_square;
		case TIMEMAGIC:
			return R.drawable.game_item_time;
			
		case RECTVIRUSEXPLORED:
			return R.drawable.game_explored_unit_rect;
		case OVALVIRUSEXPLORED:
			return R.drawable.game_explored_unit_oval;
		}
		return -1;
	}
	
	public static int getUnitHoldImage(int _unitNum){
		switch (_unitNum) {
		case VIRUS_CAT_RECT:
			return R.drawable.game_hold_virus_rect_cat;
		case VIRUS_EVIL_RECT:
			return R.drawable.game_hold_virus_rect_evil;
		case VIRUS_MARINBOY_RECT:
			return R.drawable.game_hold_virus_rect_marinboy;
		case VIRUS_PAPA_RECT:
			return R.drawable.game_hold_virus_rect_papa;
		case VIRUS_SHYGUY_RECT:
			return R.drawable.game_hold_virus_rect_shyguy;
		case VIRUS_SMART_RECT:
			return R.drawable.game_hold_virus_rect_smart;
		case VIRUS_SPECIALFORCE_RECT:
			return R.drawable.game_hold_virus_rect_specialforce;
		case VIRUS_VAMPIRE_RECT:
			return R.drawable.game_hold_virus_rect_vampire;
		case VIRUS_BLACKSUIT_RECT:
			return R.drawable.game_hold_virus_rect_blacksuit;
		case VIRUS_ANDROID_OVAL:
			return R.drawable.game_hold_virus_oval_android;
		case VIRUS_GIRL_OVAL:
			return R.drawable.game_hold_virus_oval_girl;
		case VIRUS_JOKER_OVAL:
			return R.drawable.game_hold_virus_oval_joker;
		case VIRUS_MELONG_OVAL:
			return R.drawable.game_hold_virus_oval_melong;
		case VIRUS_MUMMY_OVAL:
			return R.drawable.game_hold_virus_oval_mummy;
		case VIRUS_PIG_OVAL:
			return R.drawable.game_hold_virus_oval_pig;
		case VIRUS_SPORTS_OVAL:
			return R.drawable.game_hold_virus_oval_sports;
		}
		return -1;
	}
}
