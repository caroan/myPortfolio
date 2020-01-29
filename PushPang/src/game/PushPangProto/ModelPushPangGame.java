package game.PushPangProto;

import java.io.Serializable;

public class ModelPushPangGame implements Serializable{
	private static final long serialVersionUID = 1000L;
	
	private static PushPangUnits[][] unitArray;
	private static int currentTime;
	private static int currentStage;
	private static int currentObjectScore;
	private static int currentTotalScore;
	private static int clearPangCount;
	
	/**일반 모드*/
	public ModelPushPangGame(PushPangUnits[][] _unitArray, int _cTime, int _cStage,
			int _cObjectScore, int _cTotalScore){
		unitArray = _unitArray;
		currentTime = _cTime;
		currentStage = _cStage;
		currentObjectScore = _cObjectScore;
		currentTotalScore = _cTotalScore;
	}
	/**클리어팡 모드*/
	public ModelPushPangGame(PushPangUnits[][] _unitArray, int _cTime, int _clearPangCount){
		unitArray = _unitArray;
		currentTime = _cTime;
		clearPangCount = _clearPangCount;
	}
	public static void setClearPangCount(int _clearPangCount){
		clearPangCount = _clearPangCount;
	}
	public static void setUnitArray(PushPangUnits[][] _unitArray){
		unitArray = _unitArray;
	}
	public static void setCurrentTime(int _currentTime){
		currentTime = _currentTime;
	}
	public static void setCurrentStatge(int _currentStage){
		currentStage = _currentStage;
	}
	public static void setCurrentObjectScore(int _currentObjectScore){
		currentObjectScore = _currentObjectScore;
	}
	public static void setCurrentTotalScore(int _currentTotalScore){
		currentTotalScore = _currentTotalScore;
	}
	
	public static PushPangUnits[][] getUnitArray(){
		return unitArray;
	}
	public static int getCurrentTime(){
		return currentTime;
	}
	public static int getCurrentStage(){
		return currentStage;
	}
	public static int getCurrentObjectScore(){
		return currentObjectScore;
	}
	public static int getCurrentTotalScore(){
		return currentTotalScore;
	}
	public static int getClearPangCount(){
		return clearPangCount;
	}
}
