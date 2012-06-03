/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	public PaintView pv;
	public float stroke = 4;
	public int colour = Color.RED;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.pv = new PaintView(getApplicationContext());
//		setContentView(pv);
		setContentView(R.layout.main);
		PaintView paintView = (PaintView)findViewById(R.id.paintView);
		paintView.xfermodes = this;

		this.paint = new Paint();
		this.paint.setAntiAlias(true);
		this.paint.setDither(true);
		this.paint.setColor(this.colour);
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeJoin(Paint.Join.ROUND);
		this.paint.setStrokeCap(Paint.Cap.ROUND);
		this.paint.setStrokeWidth(this.stroke);
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
			// erasing disabled
			paint.setXfermode(null);
			paint.setAlpha(255);
			return true;

		case R.id.eraser:
			// erasing enabled
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
			paint.setAlpha(0);
			
			return true;

		case R.id.stroke:
			this.strokeDialog();
			return true;

		case R.id.colour:
			this.colourPicker();
			return true;

		case R.id.clear:
			this.pv.path = new Path(); //empty path
			this.pv.pcanvas.drawColor(Color.WHITE);
			this.pv.invalidate();
			return true;
		}
		return true;
	}

	public void strokeDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Stroke");
		alert.setMessage("New stroke size");
		final EditText input = new EditText(this);
		input.setText(String.valueOf(stroke));
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				try {
					// tmp is used in case an exception is raised
					float tmp = Float.valueOf(value);
					stroke = tmp;
					paint.setStrokeWidth(stroke);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Invalid stroke value", Toast.LENGTH_SHORT).show();
				}
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	/*
	 * Thanks to these guys http://code.google.com/p/android-color-picker/
	 */
	public void colourPicker() {
	}

	/*
	 * This class holds the drawing surface
	 */
}