package edu.gatech.constraints.library;

import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import edu.gatech.constraints.cassowary.CL;
import edu.gatech.constraints.cassowary.ClLinearEquation;
import edu.gatech.constraints.cassowary.ClLinearExpression;
import edu.gatech.constraints.cassowary.ClLinearInequality;
import edu.gatech.constraints.cassowary.ClSimplexSolver;
import edu.gatech.constraints.cassowary.ClStayConstraint;
import edu.gatech.constraints.cassowary.ClStrength;
import edu.gatech.constraints.cassowary.ClVariable;
import edu.gatech.constraints.cassowary.ExCLInternalError;
import edu.gatech.constraints.cassowary.ExCLNonlinearExpression;
import edu.gatech.constraints.demo.Functions;
import edu.gatech.constraints.demo.R;

/**
 * Class that implements the constraint based layout manager in Android
 * Reference: http://developer.android.com/reference/android/view/ViewGroup.html
 * 
 * @author anandsainath
 */
@SuppressLint("DrawAllocation")
public class LinearConstraintLayout extends LinearLayout {

	private ClSimplexSolver solver;
	SparseArray<ViewElement> elements;
	public static final String DEPENDENT_VAR = "@id/";
	private static final String[] operators = { "+", "-", "/", "*" };

	public LinearConstraintLayout(Context context) {
		super(context);
		solver = new ClSimplexSolver();
		solver.setAutosolve(false);
		elements = new SparseArray<ViewElement>();
	}

	public LinearConstraintLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		solver = new ClSimplexSolver();
		solver.setAutosolve(false);
		elements = new SparseArray<ViewElement>();
	}

	public LinearConstraintLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		solver = new ClSimplexSolver();
		solver.setAutosolve(false);
		elements = new SparseArray<ViewElement>();
	}

	/**
	 * Allows the children to measure themselves and then compute the measures
	 * of this view based on the children
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * Function that will solve the constraints and will layout the children
	 * once the constraints are met.
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				View view = getChildAt(i);
				ViewElement element = new ViewElement(view);
				elements.append(view.getId(), element);
			}

			try {
				for (int i = 0; i < elements.size(); i++) {
					addConstraints(elements.get(elements.keyAt(i)));
				}
				solver.solve();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Functions.d("Error occured" + e.getMessage());
				e.printStackTrace();
			}

			Functions.d("After solving");

			for (int i = 0; i < elements.size(); i++) {
				elements.get(elements.keyAt(i)).setDimension();
			}
		}
	}

	public void addConstraints(ViewElement element) throws Exception {
		LinearConstraintLayout.LayoutParams params = (LinearConstraintLayout.LayoutParams) element.view
				.getLayoutParams();
		if (params != null) {
			if (params.constraint_expr != null) {
				String[] constraints = params.constraint_expr.split(";");
				for (String constraint : constraints) {
					if (constraint.contains("LEQ")) {
						solver.addConstraint(getInequalityConstraint(CL.Op.LEQ, element, constraint));
						Functions.d("A LEQ constraint must have been added!");
					} else if (constraint.contains("GEQ")) {
						solver.addConstraint(getInequalityConstraint(CL.Op.GEQ, element, constraint));
						Functions.d("A GEQ constraint must have been added!");
					} else if (constraint.contains("=")) {
						// equality constraint.
						solver.addConstraint(addEqualityConstraint(params, element, constraint));
						Functions.d("A constraint must have been added!");
					}
				}
			}// end params.constraint_expr != null

			ClStayConstraint stayConstraint;

			if (params.fixWidth) {
				stayConstraint = new ClStayConstraint(element.dimension.Width(), ClStrength.strong);
				solver.addConstraint(stayConstraint);
			}
			if (params.fixHeight) {
				stayConstraint = new ClStayConstraint(element.dimension.Height(), ClStrength.strong);
				solver.addConstraint(stayConstraint);
			}

			if (params.fixX) {
				stayConstraint = new ClStayConstraint(element.topLeft.X(), ClStrength.strong);
				solver.addConstraint(stayConstraint);
			}

			if (params.fixY) {
				stayConstraint = new ClStayConstraint(element.topLeft.Y(), ClStrength.strong);
				solver.addConstraint(stayConstraint);
			}

		}// end params != null
	}

	private ClLinearInequality getInequalityConstraint(CL.Op operator, ViewElement source, String constraint)
			throws ExCLInternalError, ExCLNonlinearExpression {
		ClLinearExpression cle = null;
		String parts[] = null;
		switch (operator) {
		case GEQ:
			parts = constraint.split("GEQ", 2);
			break;
		case LEQ:
			parts = constraint.split("LEQ", 2);
			break;
		}
		cle = (ClLinearExpression) evaluatePostFixExpression(new InfixToPostfix().convertInfixToPostfix(parts[1]),
				source);
		ClVariable lhs = getVariable(parts[0], source);
		return new ClLinearInequality(lhs, operator, cle);
	}

	private ClLinearEquation addEqualityConstraint(LinearConstraintLayout.LayoutParams params, ViewElement source,
			String constraint) throws Exception {
		String parts[] = constraint.split("=", 2);
		ClLinearExpression cle = null;
		cle = (ClLinearExpression) evaluatePostFixExpression(new InfixToPostfix().convertInfixToPostfix(parts[1]),
				source);
		Functions.d("Going to call getVariable for the LHS in addEqualityConstraint");
		ClVariable lhs = getVariable(parts[0], source);
		return new ClLinearEquation(lhs, cle, params.constraint_expr_strength);
	}

	private CL evaluatePostFixExpression(List<String> postFixExpr, ViewElement source) throws ExCLNonlinearExpression {
		Iterator<String> iterator = postFixExpr.iterator();
		MyStackList<ClLinearExpression> stack = new MyStackList<ClLinearExpression>();

		while (iterator.hasNext()) {
			String str = iterator.next();
			if (contains(operators, str)) {
				// operator
				ClLinearExpression a = stack.pop();
				ClLinearExpression b = stack.pop();
				if (str.equals("+")) {
					b = b.plus(a);
				} else if (str.equals("-")) {
					b = b.minus(a);
				} else if (str.equals("/")) {
					b = b.divide(a);
				}
				stack.push(b);
			} else {
				try {
					double constant = Double.parseDouble(str);
					stack.add(new ClLinearExpression(constant));
				}
				catch (NumberFormatException nfe) {
					stack.add(new ClLinearExpression(getVariable(str, source)));
				}
			}
		}
		return stack.pop();
	}

	private ClVariable getVariable(String notation, ViewElement source) {
		ViewElement temp = null;
		ClVariable variable = null;
		Functions.d("GetVariable called, Notation:" + notation + ", source:");
		if (notation.contains(DEPENDENT_VAR)) {
			String dependent_name = notation.replace(DEPENDENT_VAR, "");
			String names[] = dependent_name.split("[.]", 2);
			int resId = getResources().getIdentifier(names[0], "id", getContext().getPackageName());
			temp = elements.get(resId);
		} else if (notation.contains("self")) {
			temp = source;
		} else if (notation.length() > 0) {
			Functions.d(notation + " is being parsed as an integer!");
			return new ClVariable(Integer.parseInt(notation));
		}

		if (notation.contains(".w")) {
			Functions.d("Width procesed: " + temp.dimension.widthValue() + " notation: " + notation);
			variable = temp.dimension.Width();
		} else if (notation.contains(".h")) {
			variable = temp.dimension.Height();
		} else if (notation.contains(".x")) {
			variable = temp.topLeft.X();
		} else if (notation.contains(".y")) {
			variable = temp.topLeft.Y();
		}
		return variable;
	}

	private boolean contains(String[] haystack, String needle) {
		boolean contains = false;
		for (String item : haystack) {
			if (needle.equalsIgnoreCase(item)) {
				contains = true;
				break; // No need to look further.
			}
		}
		return contains;
	}

	/**
	 * Any layout manager that doesn't scroll will want this.
	 */
	@Override
	public boolean shouldDelayChildPressedState() {
		return false;
	}

	/***
	 * Function that are required when there are attributes specified using the
	 * XML file
	 ***/
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LinearConstraintLayout.LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	@Override
	protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	/*** Custom Layout parameters class ***/
	public static class LayoutParams extends LinearLayout.LayoutParams {

		public String constraint_expr;
		public ClStrength constraint_expr_strength = ClStrength.weak;
		public Boolean fixWidth, fixHeight, fixX, fixY;

		public static final int INVALID_ID = -1;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);

			/**
			 * Fetch the values of the various attributes from the XML file
			 */
			TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CLP);
			constraint_expr = a.getString(R.styleable.CLP_constraint_expr);
			fixHeight = a.getBoolean(R.styleable.CLP_fixHeight, true);
			fixWidth = a.getBoolean(R.styleable.CLP_fixWidth, true);
			fixX = a.getBoolean(R.styleable.CLP_fixX, false);
			fixY = a.getBoolean(R.styleable.CLP_fixY, false);

			switch (a.getInt(R.styleable.CLP_constraint_expr_strength, 4)) {
			case 1:
				constraint_expr_strength = ClStrength.required;
				break;
			case 2:
				constraint_expr_strength = ClStrength.strong;
				break;
			case 3:
				constraint_expr_strength = ClStrength.medium;
				break;
			case 4:
				constraint_expr_strength = ClStrength.weak;
				break;
			}
			a.recycle();
		}

		public LayoutParams(int width, int height) {
			super(width, height);
			if (fixWidth == null) {
				fixWidth = true;
			}
			if (fixHeight == null) {
				fixHeight = true;
			}
			if (fixX == null) {
				fixX = false;
			}
			if (fixY == null) {
				fixY = false;
			}

		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

	}
}
