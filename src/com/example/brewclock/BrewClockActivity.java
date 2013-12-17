package com.example.brewclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.PowerManager.WakeLock;

public class BrewClockActivity extends Activity implements OnClickListener {
  /** Properties **/
  private static final int SetTimeActivity = 1; 
  protected Button startBrew;
  protected Button btn_SetTime_activity;
  
  protected TextView brewCountLabel;
  protected TextView brewTimeLabel;
  protected TextView brewStageLabel;
  PowerManager pm;
  PowerManager.WakeLock mWakeLock; 
  Uri notification;
  Ringtone r;
  public LinearLayout linearLayout;
  protected int brewTime = 3;
  protected CountDownTimer brewCountDownTimer;
  protected int brewCount = 0;
  protected boolean isBrewing = false;
  protected boolean is_break_time = false;
  protected int stage_counts = 11;
  protected int break_counts = 11;
  protected int training_time = 60;
  protected int break_time	  = 10;
  
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
    
    linearLayout=(LinearLayout)this.findViewById(R.id.main_LinearLayout);
    linearLayout.setBackgroundColor(Color.WHITE);
    
    startBrew = (Button) findViewById(R.id.brew_start);
    btn_SetTime_activity	= (Button) findViewById(R.id.set_time);
    brewCountLabel 	= (TextView) findViewById(R.id.brew_count_label);
    brewTimeLabel 	= (TextView) findViewById(R.id.brew_time);
    brewStageLabel 	= (TextView) findViewById(R.id.brew_stage_label);
    
    brewTimeLabel.setTextColor(Color.BLACK);
    startBrew.setTextColor(Color.BLACK);
    brewCountLabel.setTextColor(Color.BLACK);
    brewStageLabel.setTextColor(Color.BLACK);
    // Setup ClickListeners
    startBrew.setOnClickListener(this);
    btn_SetTime_activity.setOnClickListener(this);
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
		setBrewCount(0);
		btn_SetTime_activity.setClickable(false);
		brewCountDownTimer = new CountDownTimer(830 * 1000, 1000) {
			long result;

			public void onTick(long millisUntilFinished) {
				if (millisUntilFinished < 1000 * (stage_counts * 60 + break_counts * 10)) {
					if (is_break_time) {
						stage_counts--;
						is_break_time = false;
						setBrewCount((brewCount + 1) % 4);
						
					} else {
						break_counts--;
						is_break_time = true;
					}
					
					r.play();
				}

				result = millisUntilFinished / 1000	- (stage_counts * 60 + break_counts * 10);
				brewTimeLabel.setText(String.valueOf(result));
			}

			@Override
			public void onFinish() {
				startBrew.setText("Start");
				brewCountDownTimer = null;
				brewTimeLabel.setText("60");
				r.play();
				btn_SetTime_activity.setClickable(true);
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
    } 
    brewTimeLabel.setText("60");
    btn_SetTime_activity.setClickable(true);
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
    } else if(v == btn_SetTime_activity){
    	
    	Intent intent = new Intent(BrewClockActivity.this, SetTimeActivity.class);  
        startActivityForResult(intent, SetTimeActivity);
    }
  }
  
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
      super.onActivityResult(requestCode, resultCode, data);  
      
      if(requestCode == SetTimeActivity ){
    	  if(resultCode == RESULT_OK){
    		  training_time = data.getExtras().getInt("training time");
    		  break_time  	= data.getExtras().getInt("break time");
    		  brewTimeLabel.setText(String.valueOf(training_time));
    	  }
      }
//      switch(requestCode)  
//      {  
//          case SUBACTIVITY1:  
//              if (resultCode == RESULT_OK)  
//              {  
//                  Uri uriData = data.getData();  
//                  textView.setText(uriData.toString());  
//              }  
//              break;  
//          case SUBACTIVITY2:  
//              break;  
//      }  
  }  
}