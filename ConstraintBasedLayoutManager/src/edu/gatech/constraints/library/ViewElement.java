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
		int width = (int) dimension.widthValue();
		int height = (int) dimension.heightValue();
		if (DEBUG) {
			debug();
		}

		view.setX((float) topLeft.Xvalue());
		view.setY((float) topLeft.Yvalue());

		int viewWidth = isWidthConstrained() ? width : view.getLayoutParams().width;
		int viewHeight = isHeightConstrained() ? height : view.getLayoutParams().height;
		view.setLayoutParams(new LinearConstraintLayout.LayoutParams(viewWidth, viewHeight));
	}
}
