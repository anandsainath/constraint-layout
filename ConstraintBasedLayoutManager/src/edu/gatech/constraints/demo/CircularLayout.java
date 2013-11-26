package edu.gatech.constraints.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import edu.gatech.constraints.cassowary.ClStrength;
import edu.gatech.constraints.library.LinearConstraintLayout;

public class CircularLayout extends Activity {
	private LinearConstraintLayout rootLayout;
	private LinearConstraintLayout.LayoutParams rootParams;
	float prevX = -1F, prevY = -1F;
	private Handler handle;
	private int layoutXY[] = new int[2];

	private void drawCircle(int centerX, int centerY) {
		Log.d("Output", "Draw circle called -> " + centerX + ":" + centerY);
		int radius = 200;
		int index = 1;
		for (int angle_in_degrees = 360; angle_in_degrees > 0; angle_in_degrees -= 45) {
			int x = (int) (Math.floor(radius * Math.cos(angle_in_degrees * (Math.PI / 180)))) + centerX;
			int y = centerY - (int) Math.floor(radius * Math.sin(angle_in_degrees * (Math.PI / 180)));
			TextView TV_TextView = new TextView(getApplicationContext());
			TV_TextView.setId(getResources()
					.getIdentifier("tv" + index, "id", getApplicationContext().getPackageName()));
			TV_TextView.setBackgroundResource(android.R.color.white);
			TV_TextView.setTextColor(Color.BLACK);
			TV_TextView.setText(angle_in_degrees + "");

			LinearConstraintLayout.LayoutParams LP_TextView1 = new LinearConstraintLayout.LayoutParams(
					LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT);
			LP_TextView1.constraint_expr = "self.x = " + x + " ; self.y = " + y + " ; self.w = 30dp";
			LP_TextView1.constraint_expr_strength = ClStrength.required;
			LP_TextView1.fixX = true;
			LP_TextView1.fixY = true;
			LP_TextView1.fixWidth = true;
			TV_TextView.setLayoutParams(LP_TextView1);
			TV_TextView.setGravity(Gravity.CENTER);
			TV_TextView.setPadding(2, 2, 2, 2);
			rootLayout.addView(TV_TextView);
			index++;
		}
		Log.d("Output", "Circle drawn!");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Declaring the root element and establishing its related parameters.
		 */
		rootLayout = new LinearConstraintLayout(getApplicationContext());
		rootLayout.setBackgroundResource(android.R.color.darker_gray);
		rootLayout.setOrientation(LinearConstraintLayout.VERTICAL);
		rootLayout.setClickable(true);
		handle = new Handler();

		rootLayout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, final MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					final int eventX = (int) event.getX();
					final int eventY = (int) event.getY();
					handle.post(new Runnable() {

						@Override
						public void run() {
							rootLayout.removeAllViews();
							drawCircle(eventX, eventY);
							//rootLayout.resolveConstraints();
						}
					});
				}

				return false;
			}
		});

		handle.post(new Runnable() {
			@Override
			public void run() {
				rootLayout.removeAllViews();
				drawCircle(553, 345);
				rootLayout.removeAllViews();
				drawCircle(553, 345);
				rootLayout.removeAllViews();
				drawCircle(550, 900);
			}
		});

		/**
		 * Defining the layout parameters for the rootLayout.
		 */
		rootParams = new LinearConstraintLayout.LayoutParams(LinearConstraintLayout.LayoutParams.MATCH_PARENT,
				LinearConstraintLayout.LayoutParams.MATCH_PARENT);
		rootLayout.setLayoutParams(rootParams);

		setContentView(rootLayout);
	}

}
