package shima.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Xfermodes extends Activity {

	public Paint paint;
	public PaintView paintView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		paintView = (PaintView)findViewById(R.id.paintView);
		paintView.xfermodes = this;

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(20);
		paint.setColor(Color.RED);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.draw_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.pencil:
			paint.setXfermode(null);
			paint.setAlpha(255);
			break;
		case R.id.eraser:
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			paint.setAlpha(0);
			break;
		case R.id.clear:
			paintView.path = new Path(); //empty path
			paintView.pcanvas.drawColor(Color.WHITE);
			paintView.invalidate();
			break;
		}
		return true;
	}
}