package com.example.brewclock;

import android.app.Activity;
import android.content.Context;
import android.media.*;
//import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.PowerManager.WakeLock;

public class BrewClockActivity extends Activity implements OnClickListener {
  /** Properties **/
  protected Button startBrew;
  protected TextView brewCountLabel;
  protected TextView brewTimeLabel;
  PowerManager pm;
  PowerManager.WakeLock mWakeLock; 
  Uri notification;
  Ringtone r;
  
  protected int brewTime = 3;
  protected CountDownTimer brewCountDownTimer;
  protected int brewCount = 0;
  protected boolean isBrewing = false;
  protected boolean is_break_time = false;
  protected int stage_counts = 11;
  protected int break_counts = 11;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // Connect interface elements to properties
    pm =  (PowerManager) getSystemService(Context.POWER_SERVICE);
    mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "BrewClock");
    
    notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    r = RingtoneManager.getRingtone(getApplicationContext(), notification); 
    
    startBrew = (Button) findViewById(R.id.brew_start);
    brewCountLabel = (TextView) findViewById(R.id.brew_count_label);
    brewTimeLabel = (TextView) findViewById(R.id.brew_time);
    
    // Setup ClickListeners
    startBrew.setOnClickListener(this);
    
    // Set the initial brew values
    setBrewCount(0);
    setBrewTime(60);
  }
  
  /** Methods **/
  
  /**
   * Set an absolute value for the number of minutes to brew. Has no effect if a brew
   * is currently running.
   * @param minutes The number of minutes to brew.
   */
  public void setBrewTime(int minutes) {
    if(isBrewing)
      return;
    
    brewTime = minutes;
    
    if(brewTime < 1)
      brewTime = 1;

    brewTimeLabel.setText(String.valueOf(brewTime));
  }
  
  /**
   * Set the number of brews that have been made, and update the interface. 
   * @param count The new number of brews
   */
  public void setBrewCount(int count) {
    brewCount = count;
    brewCountLabel.setText(String.valueOf(brewCount+1));
  }
  
  /**
   * Start the brew timer
   */
	public void startBrew() {
		is_break_time = false;
		stage_counts = 11;
		break_counts = 11;
		brewCount = 0;
		brewCountDownTimer = new CountDownTimer(830 * 1000, 1000) {
			long result;

			public void onTick(long millisUntilFinished) {
				if (millisUntilFinished < 1000 * (stage_counts * 60 + break_counts * 10)) {
					if (is_break_time) {
						stage_counts--;
						is_break_time = false;
					} else {
						break_counts--;
						is_break_time = true;
					}
					setBrewCount((brewCount + 1) % 4);
					
					r.play();
				}

				result = millisUntilFinished / 1000
						- (stage_counts * 60 + break_counts * 10);
				brewTimeLabel.setText(String.valueOf(result));
			}

			@Override
			public void onFinish() {
				startBrew.setText("Start");
				brewCountDownTimer = null;
			}
		};

		brewCountDownTimer.start();
		isBrewing = true;
		startBrew.setText("Stop");
		mWakeLock.acquire();
	}
  
  /**
   * Stop the brew timer
   */
  public void stopBrew() {
	isBrewing = false;
    if(brewCountDownTimer != null){
      brewCountDownTimer.cancel();
      brewCountDownTimer = null;
    } else{
    	brewTimeLabel.setText("Shit!");
    }
    startBrew.setText("Start");
    mWakeLock.release(); 
  }
  
  /** Interface Implementations **/
  public void onClick(View v) {
    if(v == startBrew) {
      if(isBrewing)
        stopBrew();
      else
        startBrew();
    }
  }
}