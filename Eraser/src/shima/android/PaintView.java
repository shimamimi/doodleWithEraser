package shima.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {
	private static final float TOLERANCE = 6;
	
	Xfermodes xfermodes;
	private Bitmap bmp;
	Canvas pcanvas;
	Path path;
	private Paint bmpPaint;
	private PointF prev = new PointF();

	public PaintView(Context context) { this(context, null); }
	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.alpha(0));
		path = new Path();
		bmpPaint = new Paint();
	}
	@Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		pcanvas = new Canvas(bmp);
	}
	@Override protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.alpha(0));
		canvas.drawBitmap(bmp, 0, 0, bmpPaint);
		pcanvas.drawPath(path, xfermodes.paint);
	}
	@Override public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX(); float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.reset();
			path.moveTo(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(x - prev.x) >= TOLERANCE || Math.abs(y - prev.y) >= TOLERANCE) {
				path.quadTo(prev.x, prev.y, (prev.x + x) / 2, (prev.y + y) / 2);
			}
			break;
		case MotionEvent.ACTION_UP:
			path.lineTo(x, y);
			pcanvas.drawPath(path, xfermodes.paint);
			break;
		}
		prev.x = x; prev.y = y;
		invalidate();
		return true;
	}
}