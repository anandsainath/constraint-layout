package edu.gatech.constraints.library;

import android.view.View;
import edu.gatech.constraints.cassowary.ClPoint;
import edu.gatech.constraints.demo.Functions;

public class ViewElement {
	ClPoint topLeft;
	ClDimension dimension;
	View view;
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
		// view.setLayoutParams(new LayoutParams(r, b));
	}
}
