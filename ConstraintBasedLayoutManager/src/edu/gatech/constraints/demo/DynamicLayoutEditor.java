package edu.gatech.constraints.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import edu.gatech.constraints.cassowary.ClStrength;
import edu.gatech.constraints.library.LinearConstraintLayout;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class DynamicLayoutEditor extends Activity {
	private Spinner componentName, componentType, constraintStrength, layoutWidth, layoutHeight;
	private EditText constraintExpr;
	private CheckBox fixWidth, fixHeight, fixX, fixY;
	private Button viewLayout, add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dynamic_layout_editor);

		constraintExpr = (EditText) findViewById(R.id.ET_ConstraintExpr);
		fixX = (CheckBox) findViewById(R.id.CB_FitX);
		fixY = (CheckBox) findViewById(R.id.CB_FitY);
		fixWidth = (CheckBox) findViewById(R.id.CB_FitWidth);
		fixHeight = (CheckBox) findViewById(R.id.CB_FitHeight);
		viewLayout = (Button) findViewById(R.id.Btn_ViewLayout);
		add = (Button) findViewById(R.id.Btn_AddComponent);
		componentName = (Spinner) findViewById(R.id.S_ComponentName);
		componentType = (Spinner) findViewById(R.id.S_ComponentType);
		constraintStrength = (Spinner) findViewById(R.id.S_ConstraintStrength);
		layoutWidth = (Spinner) findViewById(R.id.S_LayoutWidth);
		layoutHeight = (Spinner) findViewById(R.id.S_LayoutHeight);

		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClStrength constraint_strength = null;
				int layout_width, layout_height;
				
				DynamicView.ConstraintItem.ComponentType componentElement = null;
				String expr = (constraintExpr.getText().toString().length() == 0) ? null : constraintExpr.getText()
						.toString();
				if (expr != null) {
					String constraintStrengthStr = (String) constraintStrength.getSelectedItem();
					Functions.d(expr+" -> "+constraintStrengthStr);
					if (constraintStrengthStr.equals("Required")) {
						constraint_strength = ClStrength.required;
					} else if (constraintStrengthStr.equals("Medium")) {
						constraint_strength = ClStrength.medium;
					} else if (constraintStrengthStr.equals("Strong")) {
						constraint_strength = ClStrength.strong;
					} else {
						constraint_strength = ClStrength.weak;
					}
				}
				String viewTypeStr = (String) componentType.getSelectedItem();
				
				if (viewTypeStr.equals("EditText")) {
					Functions.d("View type is edit text");
					componentElement = DynamicView.ConstraintItem.ComponentType.EDIT_TEXT;
				} else {
					Functions.d("View type is text view");
					componentElement = DynamicView.ConstraintItem.ComponentType.BUTTON;
				}
				
				String layoutWidthStr = (String)layoutWidth.getSelectedItem();
				if(layoutWidthStr.equals("Match Parent")){
					layout_width = LinearConstraintLayout.LayoutParams.MATCH_PARENT;
				}else{
					layout_width = LinearConstraintLayout.LayoutParams.WRAP_CONTENT;
				}
				
				String layoutHeightStr = (String)layoutHeight.getSelectedItem();
				if(layoutHeightStr.equals("Match Parent")){
					layout_height = LinearConstraintLayout.LayoutParams.MATCH_PARENT;
				}else{
					layout_height = LinearConstraintLayout.LayoutParams.WRAP_CONTENT;
				}

				DynamicView.getInstance().addItem(fixX.isChecked(), fixY.isChecked(), fixWidth.isChecked(),
						fixHeight.isChecked(), (String)componentName.getSelectedItem(), expr,
						constraint_strength, componentElement, layout_width, layout_height);
			}
		});

		viewLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicLayoutEditor.this, DynamicLayoutOutput.class);
				startActivity(intent);
			}
		});
		
		/**
		 * Populating the constraint strength spinner.
		 */
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> componentNameAdapter = ArrayAdapter.createFromResource(this,
				R.array.res_ids, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		componentNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		componentName.setAdapter(componentNameAdapter);

		/**
		 * Populating the component Type spinner
		 */
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
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> componentTypeAdapter = ArrayAdapter.createFromResource(this,
				R.array.constraint_strength, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		componentTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		constraintStrength.setAdapter(componentTypeAdapter);
		
		/**
		 * Populating the layout_width and layout_height spinners.
		 */
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> dimensionAdapter = ArrayAdapter.createFromResource(this,
				R.array.dimension, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		dimensionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		layoutWidth.setAdapter(dimensionAdapter);
		layoutHeight.setAdapter(dimensionAdapter);
	}
}
