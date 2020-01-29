package game.PushPangProto;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.FrameLayout.LayoutParams;

public class PushPangGameLayout extends ViewGroup{

	public int myTopCoordY;
	
	public PushPangGameLayout(Context context){
		super(context);
	}
	public PushPangGameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                PushPangGameLayout.LayoutParams ppgLayoutParams =
                        (PushPangGameLayout.LayoutParams) child.getLayoutParams();

                int childLeft = ppgLayoutParams.x;
                int childTop = ppgLayoutParams.y;
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());

            }
        }
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		int[] myCoord = new int[2];
		/*myTopCoord =*/ getLocationInWindow(myCoord);
		myTopCoordY = myCoord[1];
		//Log.d("TAG2", "" + t[0] +" " + t[1]);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	//	int chileCount = super.getChildCount();
	//	Log.d("Child", ""+chileCount+"TAG");
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        // Find out how big everyone wants to be
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // Find rightmost and bottom-most child
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;

                PushPangGameLayout.LayoutParams ppgLayoutParams
                        = (PushPangGameLayout.LayoutParams) child.getLayoutParams();

                childRight = ppgLayoutParams.x + child.getMeasuredWidth();
                childBottom = ppgLayoutParams.y + child.getMeasuredHeight();

                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }

        // Account for padding too
/*        maxWidth += mPaddingLeft + mPaddingRight;
        maxHeight += mPaddingTop + mPaddingBottom;*/

        // Check against minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        
        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
                resolveSize(maxHeight, heightMeasureSpec));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public static class LayoutParams extends ViewGroup.LayoutParams{
		public int x;
		public int y;
		
		public LayoutParams(int width, int height, int _x, int _y){
			super(width, height);
			x = _x;
			y = _y;
		}
		
		public LayoutParams(Context arg0, AttributeSet arg1) {
			super(arg0, arg1);
			// TODO Auto-generated constructor stub
		}
		
	}

}
