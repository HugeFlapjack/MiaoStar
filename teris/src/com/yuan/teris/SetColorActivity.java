package com.yuan.teris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SetColorActivity extends Activity {
	private SeekBar redColorBar;
	private SeekBar greenColorBar;
	private SeekBar blueColorBar;
	private CubeView view, view0, view1, view2, view3, view4, view5, view6;
	private Button btnOK;
	private Button btnCancel;
	private int cubeNo = 0;

	private int orginalColor[] = new int[7];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getCubeColor();
		setContentView(R.layout.activity_setcolor);

		redColorBar = (SeekBar) findViewById(R.id.seekBar_R);
		greenColorBar = (SeekBar) findViewById(R.id.seekBar_G);
		blueColorBar = (SeekBar) findViewById(R.id.seekBar_B);
		view = view0 = (CubeView) findViewById(R.id.cubeView0);
		view1 = (CubeView) findViewById(R.id.cubeView1);
		view2 = (CubeView) findViewById(R.id.cubeView2);
		view3 = (CubeView) findViewById(R.id.cubeView3);
		view4 = (CubeView) findViewById(R.id.cubeView4);
		view5 = (CubeView) findViewById(R.id.cubeView5);
		view6 = (CubeView) findViewById(R.id.cubeView6);
		btnOK = (Button) findViewById(R.id.btn_ok);
		btnCancel = (Button) findViewById(R.id.btn_cancel);

		setSeekBarProcess();

		// ÎªSeekBarÌí¼Ó¼àÌýÆ÷
		SeekBarListener seekBarListener = new SeekBarListener();
		redColorBar.setOnSeekBarChangeListener(seekBarListener);
		greenColorBar.setOnSeekBarChangeListener(seekBarListener);
		blueColorBar.setOnSeekBarChangeListener(seekBarListener);

		// Îª¸÷viewÌí¼Ó¼àÌýÆ÷
		ClickListener clickListener = new ClickListener();
		view0.setOnClickListener(clickListener);
		view1.setOnClickListener(clickListener);
		view2.setOnClickListener(clickListener);
		view3.setOnClickListener(clickListener);
		view4.setOnClickListener(clickListener);
		view5.setOnClickListener(clickListener);
		view6.setOnClickListener(clickListener);

		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				storeCubeColor();
				Intent i = new Intent();
				i.setClass(SetColorActivity.this, MainActivity.class);
				startActivity(i);
			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetColor();
				Intent i = new Intent();
				i.setClass(SetColorActivity.this, MainActivity.class);
				startActivity(i);
			}
		});

	}

	private void getCubeColor() {
		SharedPreferences colors = getSharedPreferences("gameInfo",
				MODE_PRIVATE);
		Cube1.color = orginalColor[0] = colors
				.getInt("color_cube1", 0xFF000000);
		Cube2.color = orginalColor[1] = colors
				.getInt("color_cube2", 0xFF000080);
		Cube3.color = orginalColor[2] = colors
				.getInt("color_cube3", 0xFF008000);
		Cube4.color = orginalColor[3] = colors
				.getInt("color_cube4", 0xFF800000);
		Cube5.color = orginalColor[4] = colors
				.getInt("color_cube5", 0xFFFF8000);
		Cube6.color = orginalColor[5] = colors
				.getInt("color_cube6", 0xFF8000FF);
		Cube7.color = orginalColor[6] = colors
				.getInt("color_cube7", 0xFFFF0080);
	}

	private void storeCubeColor() {
		SharedPreferences colors = getSharedPreferences("gameInfo",
				MODE_PRIVATE);
		SharedPreferences.Editor editor = colors.edit();
		editor.putInt("color_cube1", Cube1.color);
		editor.putInt("color_cube2", Cube2.color);
		editor.putInt("color_cube3", Cube3.color);
		editor.putInt("color_cube4", Cube4.color);
		editor.putInt("color_cube5", Cube5.color);
		editor.putInt("color_cube6", Cube6.color);
		editor.putInt("color_cube7", Cube7.color);

		editor.commit();
	}

	private void resetColor() {
		Cube1.color = orginalColor[0];
		Cube2.color = orginalColor[1];
		Cube3.color = orginalColor[2];
		Cube4.color = orginalColor[3];
		Cube5.color = orginalColor[4];
		Cube6.color = orginalColor[5];
		Cube7.color = orginalColor[6];
	}

	private void setSeekBarProcess() {
		int color = view.getColor();
		redColorBar.setProgress((color & 0x00FF0000) >> 16);
		greenColorBar.setProgress((color & 0x0000FF00) >> 8);
		blueColorBar.setProgress(color & 0x000000FF);
	}

	// SeekBar¼àÌýÆ÷
	private class SeekBarListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			if (seekBar == redColorBar) {
				view.changeColor((view.getColor() & 0xFF00FFFF) + progress
						* 0x00010000);
			} else if (seekBar == greenColorBar) {
				view.changeColor((view.getColor() & 0xFFFF00FF) + progress
						* 0x00000100);
			} else if (seekBar == blueColorBar) {
				view.changeColor((view.getColor() & 0xFFFFFF00) + progress
						* 0x00000001);
			} else {

			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

	}

	// viewµã»÷¼àÌýÆ÷
	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			view.setSelected(false);
			view = (CubeView) v;
			view.setSelected(true);
			setSeekBarProcess();
		}

	}
}
