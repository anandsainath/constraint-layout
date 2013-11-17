package edu.gatech.constraints.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AppHome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_home);
	}

	public void staticDemo(View view) {
		Intent intent = new Intent(this, StaticDemo.class);
		startActivity(intent);
	}

	public void dynamicDemo(View view) {
		Intent intent = new Intent(this, DynamicDemo.class);
		startActivity(intent);
	}

	public void dynamicLayoutEditor(View view) {
		Intent intent = new Intent(this, DynamicLayoutEditor.class);
		startActivity(intent);
	}

}
