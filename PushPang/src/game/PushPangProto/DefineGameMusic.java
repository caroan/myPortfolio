package game.PushPangProto;

import java.util.HashMap;

import android.media.*;
import android.content.*;

public class DefineGameMusic {
	public static final int UNITPRESSED = 0;
	public static final int UNITEXPLORED_SMALL = 1;
	public static final int UNITEXPLORED_BIG = 2;
	
	private SoundPool sound;
	private HashMap soundData;
	private AudioManager myAudioManager;
	private Context myContext;
	private static DefineGameMusic myInstance;
	
	private DefineGameMusic() {
		// TODO Auto-generated constructor stub
	}
	
	public static DefineGameMusic createInstance(Context _context){
		if(myInstance == null){
			myInstance = new DefineGameMusic();
			myInstance.init(_context);
		}
		return myInstance;
	}
	public void init(Context _context){
		sound = new SoundPool(7,AudioManager.STREAM_MUSIC, 0);
		soundData = new HashMap();
		myAudioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);
		myContext = _context;
		int id = sound.load(_context, R.raw.unit_down, 0);
		soundData.put(0, id);
		id = sound.load(_context, R.raw.unit_explored_small, 0);
		soundData.put(1, id);
		id = sound.load(_context, R.raw.unit_explored_big, 0);
		soundData.put(2, id);
	}
	public void addExtraSound(int _index, int _soundId){
		if(_index < 3){
			new Exception().printStackTrace();
			return ;
		}
		int id = sound.load(myContext, _soundId, 1);
		soundData.put(_index, id);
	}
	
	public void play(int _index){
		float streamVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume/myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sound.play((Integer)soundData.get(_index), streamVolume, streamVolume, 1, 0, 1);
	}
	
	public void playLoop(int _index){
		float streamVolume = myAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume/myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		sound.play((Integer)soundData.get(_index), streamVolume, streamVolume, 1, -1, 1);
	}
}
