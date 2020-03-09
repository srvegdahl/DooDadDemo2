package edu.up.cs301.doodaddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView  {

	// track the current dimensions of this surface view
	private int width;
	private int height;

	// This is the spot that moves
	private Spot mySpot = new Spot();

	// This is the target it tries to move onto
	private Spot target = new Spot();

	/** called by the ctors to initialize the variables I've added to this class */
	private void myInitializationStuff() {
		// enables the 'onDraw' method to be called
		setWillNotDraw(false);

		// sets background color
		setBackgroundColor(Color.WHITE);
	}

	////////////////////// constructors
	public MySurfaceView(Context context) {
		super(context);
		myInitializationStuff();
	}

	public MySurfaceView(Context context, AttributeSet set) {
		super(context, set);
		myInitializationStuff();
	}

	public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		myInitializationStuff();
	}
	////////////////////// end constructors

	
	/** keep track of my dimensions on the screen */
	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		this.width = xNew;
		this.height = yNew;
	}

	// accessors
	// Note: getWidth and getHeight are already in View and used for something
	// slightly different
	public int getMyWidth() {
		return this.width;
	}

	public int getMyHeight() {
		return this.height;
	}

	/**
	 * replace my spot with a new one
	 * 
	 * @param newSpot
	 *            is the new Spot object
	 */
	public void setSpot(Spot newSpot) {
		if (newSpot != null) {
			this.mySpot = newSpot;
		}
	}

	/**
	 * replace my target with a new one
	 * 
	 * @param newTarget
	 *            is the new Spot object
	 */
	public void setTarget(Spot newTarget) {
		if (newTarget != null) {
			this.target = newTarget;
		}
	}

	/** draws the spots */
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		this.target.draw(canvas);
		this.mySpot.draw(canvas);
	}

}// class MySurfaceView
