package game.PushPangProto;

import android.content.Context;
import android.graphics.*;
import android.graphics.Shader.*;
import android.view.*;

public class TimeProgressBar extends View{
	private int maxTime;
	private int currentTime;
	private int progBarHeight;
	private int progBarWidth;
	private LinearGradient shader;
	
	public TimeProgressBar(Context context, int _maxTime) {
		super(context);
		// TODO Auto-generated constructor stub
		initTimeProgressBar(_maxTime);
	}

	public void initTimeProgressBar(int _maxTime){
		maxTime = _maxTime;
		currentTime = 0;
	}
	
	public void setMax(int _max){
		if(_max > 0){
			maxTime = _max;
			invalidate();
		}
	}
	public int getMaxTime(){
		return maxTime;
	}
	
	public void setCurrentTime(int _cTime){
		if(currentTime < 0 || _cTime > maxTime){
			return ;
		}
		currentTime = _cTime;
		invalidate();
	}
	
	public int getCurrentTime(){
		return currentTime;
	}
	
	/**블럭을 맞췄을 경우 추가되는 시간*/
	public void plusTime(int _pTime){
		if((currentTime - _pTime) >= 0){
			currentTime -= _pTime;
		}else{
			currentTime = 0;
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(shader == null){
			initShader();
		}
		RectF barRect = new RectF();
		drawChangeBar(barRect, canvas);
		drawOutLine(barRect, canvas);
	}
	private void initShader(){
		progBarHeight = getHeight() - getPaddingTop() - getPaddingBottom();
		progBarWidth = getWidth() - getPaddingRight();
		int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE};
		shader = new LinearGradient(0, 0, progBarWidth, 0, colors, null, TileMode.CLAMP);
	}
	private void drawChangeBar(RectF _barRect, Canvas _canvas){
		_barRect.left = getPaddingLeft();
		_barRect.right = progBarWidth - progBarWidth * currentTime / maxTime;
		_barRect.bottom = getPaddingTop() + progBarHeight;
		_barRect.top = getPaddingTop();
		
		Paint fillPaint = new Paint();
		fillPaint.setShader(shader);
		_canvas.drawRect(_barRect, fillPaint);
	}
	private void drawOutLine(RectF _barRect, Canvas _canvas){
		_barRect.top = getPaddingTop();
		Paint outLine = new Paint();
		outLine.setColor(Color.WHITE);
		outLine.setStyle(Paint.Style.STROKE);
		_canvas.drawRect(_barRect, outLine);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = 100, height = 10;

		switch (MeasureSpec.getMode(widthMeasureSpec)) {
		case MeasureSpec.AT_MOST:
			width = Math.min(MeasureSpec.getSize(widthMeasureSpec), width);
			break;
		case MeasureSpec.EXACTLY:
			width = MeasureSpec.getSize(widthMeasureSpec);
			break;
		}

		switch (MeasureSpec.getMode(heightMeasureSpec)) {
		case MeasureSpec.AT_MOST:
			height = Math.min(MeasureSpec.getSize(heightMeasureSpec), height);
			break;
		case MeasureSpec.EXACTLY:
			height = MeasureSpec.getSize(heightMeasureSpec);
			break;
		}

		setMeasuredDimension(width, height);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
