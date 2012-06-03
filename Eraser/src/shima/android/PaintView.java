package shima.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class PaintView extends View {
	private static final float TOLERANCE = 6;
	private Bitmap offScreenBitmap;
	private Canvas offScreenCanvas;
	private ImageView backgroundView;
	private Paint paint;
	private Path path;
	private PointF prev = new PointF();
	public enum PenType { PEN, ERASER } PenType penType = PenType.PEN;
	private PorterDuffXfermode eraserMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

	public PaintView(Context context) { this(context, null); }
	public PaintView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.alpha(0));
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(20);
		paint.setColor(Color.RED);
		path = new Path();
	}
	@Override protected void onSizeChanged(int w, int h, int pw, int ph) {
		super.onSizeChanged(w, h, pw, ph);
		offScreenBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		offScreenCanvas = new Canvas(offScreenBitmap);
		backgroundView = (ImageView)((Activity)getContext()).findViewById(R.id.backgroundView);
	}
	@Override protected void onDraw(Canvas canvas) {
		offScreenCanvas.drawPath(path, paint);
		canvas.drawBitmap(offScreenBitmap, 0, 0, null);
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
			offScreenCanvas.drawPath(path, paint);
			break;
		}
		prev.x = x; prev.y = y;
		invalidate();
		return true;
	}
	boolean setPenType(PenType type) {
		if (type == penType) return false;
		penType = type;
		switch (penType) {
		case PEN:
			paint.setXfermode(null);
			paint.setAlpha(255);
			break;
		case ERASER:
			paint.setXfermode(eraserMode);
			paint.setAlpha(0);
			break;
		}
		return true;
	}
	boolean setPenColor(int color) {
		if (penType == PenType.PEN) {
			paint.setColor(color);
			return true;
		}
		return false;
	}
	void setBgColor(int color) {
		if (backgroundView != null) {
			backgroundView.setImageDrawable(null);
			backgroundView.setBackgroundColor(color);
			backgroundView.invalidate();
			invalidate();
		}
	}
	void setBgImage(Drawable drawable) {
		if (backgroundView != null) {
			backgroundView.setImageDrawable(drawable);
			backgroundView.invalidate();
			invalidate();
		}
	}
}