package edu.gatech.constraints.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

	private void resetViewElements() {
		componentName.setSelection(componentName.getCount());
		componentType.setSelection(componentType.getCount());
		constraintStrength.setSelection(constraintStrength.getCount());
		layoutWidth.setSelection(layoutWidth.getCount());
		layoutHeight.setSelection(layoutHeight.getCount());
		constraintExpr.setText(null);
		fixWidth.setChecked(true);
		fixHeight.setChecked(true);
		fixX.setChecked(false);
		fixY.setChecked(false);
	}

	private class CustomSpinnerAdapter extends ArrayAdapter<String> {

		int dropDownViewResource;
		boolean hasImage;

		public CustomSpinnerAdapter(Context context, int resource, boolean hasImage) {
			super(context, resource);
			dropDownViewResource = resource;
			this.hasImage = hasImage;
		}

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View view = LayoutInflater.from(getApplicationContext()).inflate(dropDownViewResource, null);
			((TextView) view.findViewById(android.R.id.text1)).setText(getItem(position));

			if (hasImage) {
				if (DynamicView.getInstance().isNameUsed(getItem(position))) {
					view.findViewById(R.id.IV_Used).setVisibility(View.VISIBLE);
				}
			}
			return view;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = LayoutInflater.from(getApplicationContext()).inflate(android.R.layout.simple_spinner_item, null);
			TextView tv = (TextView) v.findViewById(android.R.id.text1);
			tv.setTextColor(Color.BLACK);
			if (position == getCount()) {
				tv.setText("");
				tv.setHint(getItem(getCount())); // "Hint to be displayed"
			} else {
				tv.setText(getItem(position));
			}
			return v;
		}

		@Override
		public int getCount() {
			return super.getCount() - 1;
		}
	}

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
				} else if (viewTypeStr.equals("TextView")) {
					Functions.d("View type is text view");
					componentElement = DynamicView.ConstraintItem.ComponentType.TEXT_VIEW;
				} else {
					Functions.d("View type is text view");
					componentElement = DynamicView.ConstraintItem.ComponentType.BUTTON;
				}

				String layoutWidthStr = (String) layoutWidth.getSelectedItem();
				if (layoutWidthStr.equals("Match Parent")) {
					layout_width = LinearConstraintLayout.LayoutParams.MATCH_PARENT;
				} else {
					layout_width = LinearConstraintLayout.LayoutParams.WRAP_CONTENT;
				}

				String layoutHeightStr = (String) layoutHeight.getSelectedItem();
				if (layoutHeightStr.equals("Match Parent")) {
					layout_height = LinearConstraintLayout.LayoutParams.MATCH_PARENT;
				} else {
					layout_height = LinearConstraintLayout.LayoutParams.WRAP_CONTENT;
				}
				Functions.d((String) componentName.getSelectedItem());

				DynamicView.getInstance().addItem(fixX.isChecked(), fixY.isChecked(), fixWidth.isChecked(),
						fixHeight.isChecked(), (String) componentName.getSelectedItem(), expr, constraint_strength,
						componentElement, layout_width, layout_height);

				if (!viewLayout.isEnabled()) {
					viewLayout.setEnabled(true);
				}
				resetViewElements();
			}
		});

		viewLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DynamicLayoutEditor.this, DynamicLayoutOutput.class);
				startActivity(intent);
				resetViewElements();
			}
		});

		/**
		 * Populating the constraint strength spinner.
		 */
		ArrayAdapter<String> componentNameAdapter = new CustomSpinnerAdapter(getApplicationContext(),
				R.layout.spinner_item, true);
		for (String tmp : getResources().getStringArray(R.array.res_ids)) {
			componentNameAdapter.add(tmp);
		}
		componentName.setAdapter(componentNameAdapter);
		componentName.setSelection(componentNameAdapter.getCount());

		/**
		 * Populating the component Type spinner
		 */
		ArrayAdapter<String> componentTypeAdapter = new CustomSpinnerAdapter(getApplicationContext(),
				R.layout.spinner_item, false);
		for (String tmp : getResources().getStringArray(R.array.component_type)) {
			componentTypeAdapter.add(tmp);
		}
		componentType.setAdapter(componentTypeAdapter);
		componentType.setSelection(componentTypeAdapter.getCount());

		/**
		 * Populating the constraint strength spinner.
		 */
		ArrayAdapter<String> constraintStrengthAdapter = new CustomSpinnerAdapter(getApplicationContext(),
				R.layout.spinner_item, false);
		for (String tmp : getResources().getStringArray(R.array.constraint_strength)) {
			constraintStrengthAdapter.add(tmp);
		}
		constraintStrength.setAdapter(constraintStrengthAdapter);
		constraintStrength.setSelection(constraintStrengthAdapter.getCount());

		/**
		 * Populating the layout_width and layout_height spinners.
		 */
		ArrayAdapter<String> dimensionWidthAdapter = new CustomSpinnerAdapter(getApplicationContext(),
				R.layout.spinner_item, false);
		for (String tmp : getResources().getStringArray(R.array.dimension_width)) {
			dimensionWidthAdapter.add(tmp);
		}
		layoutWidth.setAdapter(dimensionWidthAdapter);
		layoutWidth.setSelection(dimensionWidthAdapter.getCount());

		AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				checkIfButtonEnabled();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		};
		ArrayAdapter<String> dimensionHeightAdapter = new CustomSpinnerAdapter(getApplicationContext(),
				R.layout.spinner_item, false);
		for (String tmp : getResources().getStringArray(R.array.dimension_height)) {
			dimensionHeightAdapter.add(tmp);
		}
		layoutHeight.setAdapter(dimensionHeightAdapter);
		layoutHeight.setSelection(dimensionHeightAdapter.getCount());
		layoutHeight.setOnItemSelectedListener(listener);

		layoutWidth.setOnItemSelectedListener(listener);
		componentName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String name = getResources().getStringArray(R.array.res_ids)[componentName.getSelectedItemPosition()];
				if (DynamicView.getInstance().isNameUsed(name)) {
					showSelectedProperties(name);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		componentType.setOnItemSelectedListener(listener);
	}

	private void showSelectedProperties(String name) {
		DynamicView.ConstraintItem item = DynamicView.getInstance().getConstraintsByName(name);
		switch (item.componentType) {
		case EDIT_TEXT:
			componentType.setSelection(0);
			break;
		case TEXT_VIEW:
			componentType.setSelection(1);
			break;
		case BUTTON:
			componentType.setSelection(2);
			break;
		}
		layoutWidth.setSelection((item.layout_width == LinearConstraintLayout.LayoutParams.MATCH_PARENT) ? 0 : 1);
		layoutHeight.setSelection((item.layout_height == LinearConstraintLayout.LayoutParams.MATCH_PARENT) ? 0 : 1);
		fixX.setSelected(item.fixX);
		fixY.setSelected(item.fixY);
		fixWidth.setSelected(item.fixWidth);
		fixHeight.setSelected(item.fixHeight);
		if (item.constraintExpr != null) {
			constraintExpr.setText(item.constraintExpr);
			if (item.constraint_strength.equals(ClStrength.required)) {
				constraintStrength.setSelection(0);
			} else if (item.constraint_strength.equals(ClStrength.strong)) {
				constraintStrength.setSelection(1);
			} else if (item.constraint_strength.equals(ClStrength.medium)) {
				constraintStrength.setSelection(2);
			} else if (item.constraint_strength.equals(ClStrength.weak)) {
				constraintStrength.setSelection(3);
			} else {
				constraintStrength.setSelection(4);
			}
		}
	}

	private void checkIfButtonEnabled() {
		int[] spinner_ids = { R.id.S_ComponentName, R.id.S_ComponentType, R.id.S_LayoutHeight, R.id.S_LayoutWidth };
		boolean spinnersValid = true, constraintValid = true;
		for (int id : spinner_ids) {
			Spinner spinner = (Spinner) findViewById(id);
			if (spinner.getSelectedItemPosition() == spinner.getCount()) {
				spinnersValid &= false;
			}
		}

		if ((constraintExpr.getText().toString().trim().length() != 0)
				&& (constraintStrength.getSelectedItemPosition() == constraintStrength.getCount())) {
			constraintValid = false;
		}
		add.setEnabled(spinnersValid && constraintValid);
	}
}
