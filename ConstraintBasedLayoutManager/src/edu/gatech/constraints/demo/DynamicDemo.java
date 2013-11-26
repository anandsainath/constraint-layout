package edu.gatech.constraints.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import edu.gatech.constraints.cassowary.ClStrength;
import edu.gatech.constraints.library.LinearConstraintLayout;

public class DynamicDemo extends Activity {

	private LinearConstraintLayout rootLayout;
	private LinearConstraintLayout.LayoutParams rootParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Declaring the root element and establishing its related parameters.
		 */
		rootLayout = new LinearConstraintLayout(getApplicationContext());
		rootLayout.setBackgroundResource(android.R.color.darker_gray);
		rootLayout.setOrientation(LinearConstraintLayout.VERTICAL);

		/**
		 * Defining the layout parameters for the rootLayout.
		 */
		rootParams = new LinearConstraintLayout.LayoutParams(LinearConstraintLayout.LayoutParams.MATCH_PARENT,
				LinearConstraintLayout.LayoutParams.MATCH_PARENT);

		rootLayout.setLayoutParams(rootParams);

		/**
		 * Declaring the first dummy text view and setting its view and layout
		 * parameters including its constraints.
		 * 
		 * Finally, adding it to the rootView.
		 */
		TextView TV_TextView1 = new TextView(getApplicationContext());
		TV_TextView1.setId(R.id.TV_DummyText1);
		TV_TextView1.setBackgroundResource(android.R.color.white);
		TV_TextView1.setTextColor(Color.BLACK);
		TV_TextView1.setText(R.string.text1Caption);
		
		LinearConstraintLayout.LayoutParams LP_TextView1 = new LinearConstraintLayout.LayoutParams(
				LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT);
		LP_TextView1.constraint_expr = "self.x = ( @id/TV_DummyText2.x + @id/TV_DummyText2.w ) - self.w";
		LP_TextView1.constraint_expr_strength = ClStrength.required;
		TV_TextView1.setLayoutParams(LP_TextView1);
		rootLayout.addView(TV_TextView1);
		
		/**
		 * Declaring the second dummy text view and setting its view and layout
		 * parameters including its constraints.
		 * 
		 * Finally adding it to the rootView
		 */
		TextView TV_TextView2 = new TextView(getApplicationContext());
		TV_TextView2.setId(R.id.TV_DummyText2);
		TV_TextView2.setBackgroundResource(android.R.color.white);
		TV_TextView2.setTextColor(Color.BLACK);
		TV_TextView2.setText(R.string.text2Caption);

		LinearConstraintLayout.LayoutParams LP_TextView2 = new LinearConstraintLayout.LayoutParams(
				LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT);
		LP_TextView2.fixX = true;
		LP_TextView2.fixY = true;
		TV_TextView2.setLayoutParams(LP_TextView2);
		rootLayout.addView(TV_TextView2);
		/**
		 * Set the content view to the rootLayout that was created.
		 */
		setContentView(rootLayout);
	}

}
