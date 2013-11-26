Linear Constraint Layout
--------------------

An implementation of Cassowary, a linear constraint based layout manager in Android.

Linear Layout is the most widely used layout manager in Android. Part of the reason is that the rendering of this layout is much faster than compared to a Relative Layout. This is because the Relative Layout manager always has to do two measure passes to align views properly. But the advantage of the Linear Layout is lessened while creating complex layout structures that often require multiple nested layout elements and nested weights (read as bad performance during rendering). As an attempt to solve this problem, we have introduced a LinearConstraintLayout manager that bases its roots on the Linear Layout manager, but the view elements can be rearranged by specifying constraints.

It is based on the JAVA implementation of Cassowary which can be found [here](http://www.cs.washington.edu/research/constraints/cassowary/cassowary-0.60.tar.gz)

Constraints can be specified **statically** (or) **dynamically**. For view elements that need to be placed according to some constraint, they must be placed inside the ViewGroup called **LinearConstraintLayout** that is defined in this project.

Constraints can be with respect to three things:
* Depending on the position of another view element.
* Depending on the screen's width and height
* The view elements width or height (Eg: 200 <= Height <= 400 )

LinearConstraintLayout is a sub-class of LinearLayout and thus the rules that apply for LinearLayout also apply for LinearConstraintLayout. Eg ```android:orientation``` attribute must be specified on the LinearConstraintLayout element.

Key terms
--------------------
The basic building blocks of any constraint equation are as follows:
* **self** in an constraint equation refers to the current view element object itself.
* **@id/{RESOURCE_ID}** refers to a view element object by the name RESOURCE_ID.
* **screen** refers to the screen object.
* **x** attribute of an object that corresponds to its x-position on the screen (Eg: self.x refers to the current object's x-position)
* **y** attribute of an object that corresponds to its y-position on the screen (Eg: self.y refers to the current object's y-position)
* **w** attribute of an object that corresponds to its width on the screen (Eg: @id/TV_Name.w refers to the width of the view element object that can be identified by the name TV_Name)
* **h** attribute of an object that corresponds to its height on the screen
* **LHS = RHS** specifies an equality constraint
* **LHS LEQ RHS** or **LHS GEQ RHS** specifies an in-equality constraint

Static Constraints
--------------------
The view elements must be placed into the LinearConstraintLayout ViewGroup.
```XML
<edu.gatech.constraints.library.LinearConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:constraint="http://schemas.android.com/apk/res/edu.gatech.constraints.demo"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/darker_gray"
    android:orientation="vertical"
    tools:context=".StaticDemo" >

    <TextView
        android:id="@+id/TV_DummyText1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/white"
        android:text="@string/text1Caption"
        android:textColor="@android:color/black"
        constraint:constraint_expr="self.x = ( @id/TV_DummyText2.x + @id/TV_DummyText2.w ) - self.w"
        constraint:constraint_expr_strength="required"/>

    <TextView
        android:id="@+id/TV_DummyText2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:background="@android:color/white"
        android:text="@string/text2Caption"
        android:textColor="@android:color/black"
        constraint:constraint_expr="no_constraint"
        constraint:fixX="true"
        constraint:fixY="true" />

</edu.gatech.constraints.library.LinearConstraintLayout>
```

To specify constraints based on the view element itself, one could use the following attributes:
* ```constraint:fixX``` True / False, depending on whether the view element's X (top left) position should be fixed.
* ```constraint:fixY``` True / False, depending on whether the view element's Y (top left) position should be fixed.
* ```constraint:fixWidth``` True / False, depending on whether the view element's width should remain fixed. Defaults to **true**.
* ```constraint:fixHeight``` True / False, depending on whether the view element't height should remain fixed. Defaults to **true**.
* ```constraint:constraint_expr``` Refers to the string that specifies the constraint equation. Can also be ***no_constraint*** which means that a view element has no specific constraint.
* ```constraint:constraint_expr_strength``` [Required, Strong, Medium, Weak], Refers to the strength of the constraint that is represented by the constraint_expr attribute.

Dynamic Constraints
--------------------

The same constraints as above can also be specified dynamically, as shown below:

```JAVA
@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * Declaring the root element and establishing its related parameters.
		 */
		rootLayout = new LinearConstraintLayout(getApplicationContext());
		rootLayout.setBackgroundResource(android.R.color.darker_gray);
		rootLayout.setOrientation(LinearConstraintLayout.VERTICAL);

		/**
		 * Defining the layout parameters for the rootLayout.
		 */
		rootParams = new LinearConstraintLayout.LayoutParams(LinearConstraintLayout.LayoutParams.MATCH_PARENT,
				LinearConstraintLayout.LayoutParams.MATCH_PARENT);

		rootLayout.setLayoutParams(rootParams);

		/**
		 * Declaring the first dummy text view and setting its view and layout
		 * parameters including its constraints.
		 * 
		 * Finally, adding it to the rootView.
		 */
		TextView TV_TextView1 = new TextView(getApplicationContext());
		TV_TextView1.setId(R.id.TV_DummyText1);
		TV_TextView1.setBackgroundResource(android.R.color.white);
		TV_TextView1.setTextColor(Color.BLACK);
		TV_TextView1.setText(R.string.text1Caption);
		
		LinearConstraintLayout.LayoutParams LP_TextView1 = new LinearConstraintLayout.LayoutParams(
				LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT);
		LP_TextView1.constraint_expr = "self.x = ( @id/TV_DummyText2.x + @id/TV_DummyText2.w ) - self.w";
		LP_TextView1.constraint_expr_strength = ClStrength.required;
		TV_TextView1.setLayoutParams(LP_TextView1);
		rootLayout.addView(TV_TextView1);
		
		/**
		 * Declaring the second dummy text view and setting its view and layout
		 * parameters including its constraints.
		 * 
		 * Finally adding it to the rootView
		 */
		TextView TV_TextView2 = new TextView(getApplicationContext());
		TV_TextView2.setId(R.id.TV_DummyText2);
		TV_TextView2.setBackgroundResource(android.R.color.white);
		TV_TextView2.setTextColor(Color.BLACK);
		TV_TextView2.setText(R.string.text2Caption);

		LinearConstraintLayout.LayoutParams LP_TextView2 = new LinearConstraintLayout.LayoutParams(
				LinearConstraintLayout.LayoutParams.WRAP_CONTENT, LinearConstraintLayout.LayoutParams.WRAP_CONTENT);
		LP_TextView2.fixX = true;
		LP_TextView2.fixY = true;
		TV_TextView2.setLayoutParams(LP_TextView2);
		rootLayout.addView(TV_TextView2);
		/**
		 * Set the content view to the rootLayout that was created.
		 */
		setContentView(rootLayout);
	}
```

Constraint Satisfaction
------------------
In the case of constraints being specified for a view element, its best if the system is [over-constrained](http://en.wikipedia.org/wiki/Overdetermined_system) to an extent. Consider the following constraint equation
```constraint:constraint_expr = "self.h LEQ 200dp"``` .

The above constraint would probably be solved by assigning the current view elements height to zero. 

Cassowary is implemented such that the for a given set of constraints, the solution is a **weighted sum better** one. Thus the strength of the constraint plays an important role. In case the system is over-constrained, Cassowary will try to remove constraints one by one, the order of which is determined by the strength specified. By default, in our implementation, constraints such as ```constraint:fixX```, ```constraint:fixY```, ```constraint:fixWidth``` and ```constraint:fixHeight``` are  **strong** constraints.


Development Roadmap
-------------------
* Separating the base logic into a Android Library that can be re-used in other applications.

**PS**: We haven't used this in a full-fledged application as yet, thus there might be a few things that we haven't accounted for. 
