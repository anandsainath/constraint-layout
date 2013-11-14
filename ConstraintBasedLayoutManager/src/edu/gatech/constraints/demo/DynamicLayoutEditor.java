package edu.gatech.constraints.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class DynamicLayoutEditor extends Activity {
	private Spinner componentType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_layout_editor);

		/**
		 * Populating the component Type spinner
		 */
		componentType = (Spinner) findViewById(R.id.S_ComponentType);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.component_type,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		componentType.setAdapter(adapter);

		/**
		 * Populating the constraint strength spinner.
		 */
		componentType = (Spinner) findViewById(R.id.S_ConstraintStrength);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> componentTypeAdapter = ArrayAdapter.createFromResource(this,
				R.array.constraint_strength, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		componentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		componentType.setAdapter(componentTypeAdapter);
	}
}
