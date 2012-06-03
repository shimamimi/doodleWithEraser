package shima.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View {

		/**
		 * 
		 */
		Xfermodes xfermodes;
		private Bitmap bmp;
		Canvas pcanvas;
		Path path;
		private Paint bmpPaint;
		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;

		public PaintView(Context context) { this(context, null); }
		public PaintView(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.setBackgroundColor(Color.alpha(0));
			this.path = new Path();
			this.bmpPaint = new Paint();
		}
		
		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			this.bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//			Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.image);
//			if (!b.isMutable()) {
//				    this.bmp = b.copy(Bitmap.Config.ARGB_8888, true);
//			}
			this.pcanvas = new Canvas(this.bmp);
		}

		@Override
		protected void onDraw(Canvas canvas) {
//			canvas.drawColor(Color.WHITE);
//			canvas.drawColor(Color.BLUE);
			canvas.drawColor(Color.alpha(0));
			canvas.drawBitmap(this.bmp, 0, 0, this.bmpPaint);
//			canvas.drawPath(this.path, paint);
			pcanvas.drawPath(this.path, this.xfermodes.paint);
		}

		private void touch_start(float x, float y) {
			this.path.reset();
			this.path.moveTo(x, y);
			this.mX = x;
			this.mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - this.mX);
			float dy = Math.abs(y - this.mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				// draws a quadratic curve
				this.path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
				mX = x;
				mY = y;
			}
		}

		private void touch_up() {
			this.path.lineTo(mX, mY);
			// commit the path to our offscreen
			this.pcanvas.drawPath(this.path, this.xfermodes.paint);
			// kill this so we don't double draw
			this.path.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent e) {
			float x = e.getX();
			float y = e.getY();

			switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}