package edu.up.cs301.doodaddemo;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


/** This class represents a single round spot (circle) on the screen */
public class Spot {
	
	public static final int INIT_SIZE = 20;
	public static final int MIN_SIZE = 3;
	public static final int MAX_SIZE = 100;

	protected float x; // x-coord
	protected float y; // y-coord
	protected float vx; // velocity in x direction
	protected float vy; // velocity in y direction
	private int size = INIT_SIZE; // all spots begin at size 20
	protected Paint myPaint; // how the spot is drawn

	/** gives the spot a random colored paint */
	protected void setRandomPaint() {
		int color = Color.rgb((int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256));
		myPaint = new Paint();
		myPaint.setColor(color);
	}

	/** this ctor initializes a spot with random values */
	public Spot() {
		// place a spot in a random location
		this((int)(Math.random()*500)+5, (int)(Math.random()*500)+5);
	}

	/** this ctor creates a spot at a specified location */
	public Spot(int initX, int initY) {
		// place a spot in the specified location
		x = initX;
		y = initY;
		vx = vy = 0;
		setRandomPaint();
	}

	/** changes the spot's color */
	public void setColor(int newColor) {
		myPaint.setColor(newColor);
	}
	
	/**
	 * setPos
	 * 
	 * sets the spot's x,y position.
	 * 
	 * CAVEAT:  Caller is responsible for giving valid values!
	 *
	 * @param newX the new x position
	 * @param newY the new y position
	 */
	public void setPos(float newX, float newY)
	{

		this.x = newX;
		this.y = newY;
	}
	
	/**
	 * getPosX
	 * 
	 * gets the spot's x position.
	 * 
	 * @return 
	 * 	the spot's x-position
	 */
	public float getPosX()
	{
		return this.x;
	}
	
	/**
	 * getPosY
	 * 
	 * gets the spot's y position.
	 * 
	 * @return 
	 * 	the spot's y-position
	 */
	public float getPosY()
	{
		return this.y;
	}
	
	/**
	 * getSize
	 * 
	 * gets the spot's size
	 * 
	 * @return 
	 * 	the spot's size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * setSize
	 * 
	 * sets the spot's size
	 * 
	 * @param newSize
	 * 	the spot's new size
	 */
	public void setSize(int newSize) {
		if ((newSize >= MIN_SIZE)
				&& (newSize <= MAX_SIZE))
		{
			this.size = newSize;
		}
	}

	/**
	 * Changes the velocity of the spot based on its acceleration. Slows the
	 * spot down a bit, to model friction. Adjusts the spot's position based on
	 * its velocity. If the change would move the spot outside of a given wall
	 * (either 0 or the given wall locations), it bounces, losing about 20% of
	 * its velocity as it does.
	 * 
	 * @param aX
	 * 		acceleration to apply to the spot (x component)
	 * @param aY
	 *		acceleration to apply to the spot (y component)
	 * @param maxX
	 *		location of right wall (for bouncing)
	 * @param maxY
	 * 		location of bottom wall (for bouncing)
	 */
	public void moveSpot(float aX, float aY, int maxX, int maxY) {

		// change the velocity based on the acceleration 
		vx += aX;
		vy += aY;
		
		// slow down the velocity a bit (to simulate friction)
		vx *= 0.99f;
		vy *= 0.99f;
		
		// move the spot based on its velocity
		float newX = x + vx;
		float newY = y + vy;

		// If a spot goes off an edge, simulate a "bounce" by negating its velocity
		// in that direction and decreasing its magnitude by 20%
		if (newX + getSize() > maxX) {
			newX = maxX - getSize();
			vx = -0.8f * vx;
		}
		if (newY + getSize() > maxY) {
			newY = maxY - getSize();
			vy = -0.8f * vy;
		}
		if (newX - getSize() < 0) {
			newX = getSize();
			vx = -0.8f * vx;
		}
		if (newY - getSize() < 0) {
			newY = getSize();
			vy = -0.8f * vy;
		}
		
		// set the spot's new position
		setPos(newX, newY);
		
	}//moveSpot
	
	/**
	 * determines whether this spot overlaps another one
	 * 
	 * @param other  the other spot that I may overlap
	 * @return true if this spot overlaps the given spot
	 */
	public boolean overlaps(Spot other) {
		//Determine the square of the distance between the two spots
		float xDist = other.x - this.x;
		float yDist = other.y - this.y;
		float distSquared = xDist*xDist + yDist*yDist;

		// determine the sum of the square of the radii of the two spots
		float thisSize = this.getSize();
		float otherSize = other.getSize();
		float sumRadiiSquared = thisSize*thisSize + otherSize*otherSize;
		
		if (distSquared <= sumRadiiSquared) {
			Log.i("overlaps!", distSquared+" <= "+sumRadiiSquared);
		}
		return distSquared <= sumRadiiSquared;
	}//overlaps
	

	/**
	 * a spot knows how to draw itself on a given canvas
	 * 
	 * @param canvas
	 *            is the canvas to draw upon
	 */
	public void draw(Canvas canvas) {
		canvas.drawCircle(x, y, getSize(), myPaint);
	}
	
}// class Spot

