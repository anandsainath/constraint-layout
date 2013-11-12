package edu.gatech.constraints.library;

import edu.gatech.constraints.cassowary.ClVariable;

public class ClDimension {

	public ClDimension(int width, int height) {
		this._clv_width = new ClVariable(height);
		this._clv_height = new ClVariable(width);
	}

	public ClVariable Width() {
		return _clv_width;
	}

	public ClVariable Height() {
		return _clv_height;
	}

	public double widthValue() {
		return Width().value();
	}

	public double heightValue() {
		return Height().value();
	}

	public String toString() {
		return "Width: " + _clv_width.toString() + ", Height: "
				+ _clv_height.toString();
	}

	private ClVariable _clv_width;
	private ClVariable _clv_height;
}
