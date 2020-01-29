package game.PushPangProto;
import android.view.*;
import android.util.*;
import android.widget.Adapter;
import android.content.*;

public class AdvancedGestureDetectorWrapper {
	// Interfaces
	private View myView;
	
	
    public static interface OnFinishedListener {
        public abstract void onFinished(MotionEvent e);
        public abstract void onFinished2(View v, MotionEvent e);
    }

    public void setMyView(View _myView){
    	myView = _myView;
    }
    
    // Classes
    public static class AdvancedOnGestureListener extends GestureDetector.SimpleOnGestureListener 
    implements OnFinishedListener{
    	
		@Override
		public void onFinished(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.d("Finish","Action_UP");
		}
		
		@Override
		public void onFinished2(View v, MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.d("onDoubleTap", "onDoubleTap");
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.d("onDoubleTapEvent", "onDoubleTapEvent");
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.d("onSingleTapConfirmed", "onSingleTapConfirmed");
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			Log.d("onFling","onFling");
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			Log.d("onScroll","onScroll");
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			Log.d("onSingleTapUp","onSingleTapUp");
			return false;
		}
    }
    
    // Fields
    private GestureDetector myGestureDetector;
    private AdvancedOnGestureListener myAdvancedListener;
    
    // Constructors
    public AdvancedGestureDetectorWrapper(Context context, AdvancedOnGestureListener listener) {
        myAdvancedListener = listener;
        myGestureDetector = new GestureDetector(context, myAdvancedListener);
    }
    
    private Context c;
    public AdvancedGestureDetectorWrapper(Context context) {
        c = context;
    }
    public void set(AdvancedOnGestureListener listener){
    	myAdvancedListener = listener;
        myGestureDetector = new GestureDetector(c, myAdvancedListener);
    }
 
    // Methods
    public boolean onTouchEvent(MotionEvent ev) {
    	boolean onTouchEvent = myGestureDetector.onTouchEvent(ev);
        
        if(ev.getAction() == MotionEvent.ACTION_UP) {
            myAdvancedListener.onFinished(ev);
            myAdvancedListener.onFinished2(myView,ev);
        }
        return onTouchEvent;
    }
}
