package game.PushPangProto;
import android.content.Context;
import android.util.*;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.*;
import android.graphics.*;
import android.view.*;
import android.view.View.MeasureSpec;

public class PushPangGameView extends View{
	
	public PushPangGameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public PushPangGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public PushPangGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		int wMode, hMode;
		int wSpec, hSpec;
		int width, height;
		
		width = 480;
		height = 500;
		
		wMode = MeasureSpec.getMode(widthMeasureSpec);
		wSpec = MeasureSpec.getSize(widthMeasureSpec);
		hMode = MeasureSpec.getMode(heightMeasureSpec);
		hSpec = MeasureSpec.getSize(heightMeasureSpec);
		
		switch(wMode){
		case MeasureSpec.AT_MOST:
			width = Math.min(wSpec, width);
			break;
		case MeasureSpec.EXACTLY:
			width = wSpec;
			break;
		case MeasureSpec.UNSPECIFIED:
			break;
		}
		switch(hMode){
		case MeasureSpec.AT_MOST:
			height = Math.min(hSpec, height);
			break;
		case MeasureSpec.EXACTLY:
			height = hSpec;
			break;
		case MeasureSpec.UNSPECIFIED:
			break;
		}
		
		setMeasuredDimension(width, height);
	}

	float y=0;
	float x=0;
	float fx;
	float fy;
	float lx=0;
	float ly=0;
	RectF r;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawColor(Color.WHITE);
		//r = new RectF(5+x,5+y,10+x,10+y);
		r = new RectF(5,5,this.getWidth()-5,this.getHeight()-5);
		Log.d("TAG", this.getWidth() + "  " +this.getHeight());
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawRect(r, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			fx= event.getX();
			fy= event.getY();
			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			x = event.getX() - fx + lx;
			y = event.getY() - fy + ly;
			Log.d("TAG2", x + "  " +y);
			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			x = event.getX() - fx + lx;
			y = event.getY() - fy + ly;
			lx = x;
			ly = y;
			invalidate();
			return true;
		}
		return super.onTouchEvent(event);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		//int childCount = super.getChildCount();
		//new Button(this.getContext()).layout(10, 10, 100, 100);
	}
}
