package edu.gatech.constraints.library;

import android.view.View;
import edu.gatech.constraints.cassowary.ClPoint;
import edu.gatech.constraints.demo.Functions;

public class ViewElement {
	ClPoint topLeft;
	ClDimension dimension;
	View view;
	boolean widthConstraint = false;
	boolean heightConstraint = false;
	private static final boolean DEBUG = true;

	public ViewElement(View view) {
		this.view = view;
		topLeft = new ClPoint(view.getX(), view.getY());
		dimension = new ClDimension(view.getHeight(), view.getWidth());
		debug();
	}

	public void debug() {
		Functions.d(topLeft.toString());
		Functions.d(dimension.toString());
	}
	
	public void setWidthConstraint() {
		widthConstraint = true;
	}

	public void setHeightConstraint() {
		heightConstraint = true;
	}
	
	public boolean isWidthConstrained() {
		return widthConstraint;
	}
	
	public boolean isHeightConstrained() {
		return heightConstraint;
	}
	
	public void setDimension() {
		int l = (int) topLeft.Xvalue();
		int t = (int) topLeft.Yvalue();
		int r = (int) dimension.widthValue();
		int b = (int) dimension.heightValue();
		if (DEBUG) {
			Functions.d(l + ", " + t + ", " + r + ", " + b);
			debug();
		}
		view.setX((float) topLeft.Xvalue());
		view.setY((float) topLeft.Yvalue());
		if (isWidthConstrained() && isHeightConstrained()) {
			view.setLayoutParams(new LinearConstraintLayout.LayoutParams(r, b));			
		} else if (isWidthConstrained()) {
			Functions.d("Width is constrained");
			view.setLayoutParams(new LinearConstraintLayout.LayoutParams(r, LinearConstraintLayout.LayoutParams.WRAP_CONTENT));			
		} else if (isHeightConstrained()) {
			
			view.setLayoutParams(new LinearConstraintLayout.LayoutParams(LinearConstraintLayout.LayoutParams.WRAP_CONTENT, b));
		} else {
			view.setLayoutParams(new LinearConstraintLayout.LayoutParams(LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT));
		}
	}
}
