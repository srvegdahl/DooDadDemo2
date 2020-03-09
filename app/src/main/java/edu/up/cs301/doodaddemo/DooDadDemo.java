package edu.up.cs301.doodaddemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

@TargetApi(9)
public class DooDadDemo extends Activity implements SensorEventListener {

    // tell whether to use the the accelerometer to move the spot
    private static final boolean USE_ACCEL = false;

    // the surface that things will be drawn upon
    MySurfaceView msv;

    // This is the spot that will move
    Spot spot;

    // This is the target spot
    Spot target;

    // the current preferred color for the spot
    private int defSpotColor = Color.RED;

    // the current preferred size for the target
    private int defTargetSize = Spot.INIT_SIZE;

    /**
     * "main" for the doodad demo. This method sets up necessary listeners and
     * initializes instance variables
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doodad_demo);

        // initialize instance variables
        this.msv = (MySurfaceView) this.findViewById(R.id.theSurfaceView);
        initSpots();

        // set up the accelerometer listener
        SensorManager mgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mAccelerometer = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mgr.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        // disable screen rotation (otherwise accelerometer interface is
        // frustrating)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    }// onCreate

    /**
     * initSpots
     *
     * creates a default target and spot on the canvas
     */
    protected void initSpots() {
        // Place a black target spot on the canvas
        target = new Spot();
        target.setColor(Color.BLACK);
        target.setSize(this.defTargetSize);
        msv.setTarget(target);

        // Make the other spot red and make sure it's not on top of the black
        // spot
        spot = new Spot();
        while (spot.overlaps(target)) {
            spot = new Spot();
        }
        spot.setColor(this.defSpotColor);
        msv.setSpot(spot);

        // repaint so the new spots appear
        msv.invalidate();
    }// initSpots

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_doodad_demo, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // respond to menu item selection
        int itemId = item.getItemId();
        if (itemId == R.id.resetMenuItem) {
            initSpots();
            return true;
        } else if (itemId == R.id.redBallMenuItem) {
            this.defSpotColor = Color.RED;
            this.spot.setColor(Color.RED);
            return true;
        } else if (itemId == R.id.blueBallMenuItem) {
            this.defSpotColor = Color.BLUE;
            this.spot.setColor(Color.BLUE);
            return true;
        } else if (itemId == R.id.yellowBallMenuItem) {
            this.defSpotColor = Color.YELLOW;
            this.spot.setColor(Color.YELLOW);
            return true;
        }

        return false;
    }// onOptionsItemSelected

    /** we don't care about this event */
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    /**
     * When the tilt sensors change move the spot in the indicated direction.
     * Also check to see if the ball now overlaps the target and react
     * appropriately. In practice, this seems to happen on a very frequent and
     * regular basis, even when there is no change in orientation of the screen.
     * So effectively, this becomes our "tick" method.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        // acceleration that will be applied
        float aX;
        float aY;

        if (USE_ACCEL) {
            // sense the accelerometer values. They are cast to int so that minor physical
            // errors in sensing will not cause acceleration when the device is totally
            // flat.
            aX = (int) event.values[0] * 0.25f; // roll
            aY = (int) event.values[1] * 0.25f; // pitch
        }
        else {
            // generate random acceleration values
            aX = (float)(10.0*(Math.random()-0.5));
            aY = (float)(10.0*(Math.random()-0.5));
        }

        int height = msv.getMyHeight();
        int width = msv.getMyWidth();
        if (width > 0.0 && height > 0.0) {
            // when window is not visible, sometimes height and width become zero;
            // in this case we don't want to move the spot. Otherwise, move the spot
            spot.moveSpot(-aX, aY, msv.getMyWidth(), msv.getMyHeight());
        }

        // If the spot has reached its target, then reset
        if (spot.overlaps(target)) {
            initSpots();
        }

        // redraw spot in new position
        msv.invalidate();
    }

}// class DooDadDemo
