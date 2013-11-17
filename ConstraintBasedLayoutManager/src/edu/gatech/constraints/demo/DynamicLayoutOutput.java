package edu.gatech.constraints.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.gatech.constraints.library.LinearConstraintLayout;

public class DynamicLayoutOutput extends Activity {

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

		for (DynamicView.ConstraintItem item : DynamicView.getInstance().getItemList()) {
			rootLayout.addView(getViewElement(item));
		}
		setContentView(rootLayout);
	}

	private View getViewElement(DynamicView.ConstraintItem item) {
		View view = null;
		LinearConstraintLayout.LayoutParams lp = new LinearConstraintLayout.LayoutParams(item.layout_width,
				item.layout_height);
		switch (item.componentType) {
		case TEXT_VIEW:
			view = new TextView(getApplicationContext());
			view.setBackgroundResource(android.R.color.white);
			((TextView) view).setTextColor(Color.BLACK);
			((TextView) view).setText("Text View");
			break;
		case EDIT_TEXT:
			view = new EditText(getApplicationContext());
			((EditText) view).setText("Edit Text");
			break;
		case BUTTON:
			view = new Button(getApplicationContext());
			((Button) view).setText("Button");
			break;
		}
		if (item.constraintExpr != null) {
			lp.constraint_expr = item.constraintExpr;
			lp.constraint_expr_strength = item.constraint_strength;
		}
		view.setId(getResources().getIdentifier("@id/"+item.componentName, "id", getApplicationContext().getPackageName()));
		lp.fixHeight = item.fixHeight;
		lp.fixWidth = item.fixWidth;
		lp.fixX = item.fixX;
		lp.fixY = item.fixY;
		view.setLayoutParams(lp);
		return view;
	}

}
