package edu.gatech.constraints.demo;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.constraints.cassowary.ClStrength;

public class DynamicView {
	private static DynamicView _this = null;

	private List<ConstraintItem> viewItems;

	public static class ConstraintItem {
		public boolean fixX, fixY, fixWidth, fixHeight;
		public String componentName, constraintExpr;
		public ClStrength constraint_strength;
		public ComponentType componentType;
		public int layout_width, layout_height;

		public enum ComponentType {
			EDIT_TEXT, TEXT_VIEW, BUTTON;
		}
	}

	private DynamicView() {
		viewItems = new ArrayList<DynamicView.ConstraintItem>();
	}

	public List<ConstraintItem> getItemList() {
		return viewItems;
	}

	public ConstraintItem getConstraintsByName(String name) {
		for (ConstraintItem item : getItemList()) {
			if (item.componentName.equals(name)) {
				return item;
			}
		}
		return null;
	}

	public boolean isNameUsed(String name) {
		for (ConstraintItem item : getItemList()) {
			if (item.componentName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void addItem(boolean fixX, boolean fixY, boolean fixWidth, boolean fixHeight, String componentName,
			String constraintExpr, ClStrength constraint_strength, ConstraintItem.ComponentType componentType,
			int layout_width, int layout_height) {
		if (isNameUsed(componentName)) {
			getItemList().remove(getConstraintsByName(componentName));
		}
		ConstraintItem item = new ConstraintItem();
		item.fixHeight = fixHeight;
		item.fixWidth = fixWidth;
		item.fixX = fixX;
		item.fixY = fixY;
		item.layout_height = layout_height;
		item.layout_width = layout_width;
		item.componentName = componentName;
		if (constraintExpr != null) {
			item.constraint_strength = constraint_strength;
			item.constraintExpr = constraintExpr;
		}
		item.componentType = componentType;
		viewItems.add(item);
	}

	public static DynamicView getInstance() {
		if (_this == null) {
			_this = new DynamicView();
		}
		return _this;
	}
}
