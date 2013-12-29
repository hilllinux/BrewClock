package com.example.brewclock;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetTimeActivity extends Activity implements OnCheckedChangeListener,OnClickListener{
	Button btn_OK, btn_default, btn_up, btn_donw;
	SeekBar seek_bar;
	RadioGroup group; 
	TextView display;
	int training_time, break_time, times, current_btn;
	int currt_value;
	public LinearLayout linearLayout;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_time);
		// final EditText editText = (EditText)findViewById(R.id.edit);
		btn_OK 			= (Button) findViewById(R.id.btn_ok);
		btn_default 	= (Button) findViewById(R.id.btn_default);
		btn_up			= (Button) findViewById(R.id.btn_up);
		btn_donw		= (Button) findViewById(R.id.btn_down);
		seek_bar 		= (SeekBar) findViewById(R.id.seek_bar);
		group 			= (RadioGroup)this.findViewById(R.id.btn_group);
		training_time 	= 60;
		break_time 		= 10;
		times			= 12;
		display			= (TextView) findViewById(R.id.display);
		currt_value 	= training_time;
		display.setText(String.valueOf(currt_value));
		seek_bar.setProgress(currt_value);
		current_btn     = R.id.btn_training;
		display.setTextColor(Color.BLACK);
		
		btn_OK.setOnClickListener(this);
		btn_up.setOnClickListener(this);
		btn_donw.setOnClickListener(this);
		btn_default.setOnClickListener(this);
		
		group.setOnCheckedChangeListener(this);
		
		 linearLayout=(LinearLayout)this.findViewById(R.id.main_set_time);
		 linearLayout.setBackgroundColor(Color.WHITE);
		
		
		seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				currt_value = progress;
				display.setText(String.valueOf(currt_value));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		

	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(current_btn){
		case R.id.btn_training:
			training_time = currt_value;
			break;
		case R.id.btn_break:
			break_time = currt_value;
			break;
		default:
			times = currt_value;
			break;
		}
		
		switch(checkedId){
		case R.id.btn_training:
			currt_value = training_time;
			break;
		case R.id.btn_break:
			currt_value = break_time;
			break;
		default:
			currt_value = times;
			break;
		}
		current_btn = checkedId;
		display.setText(String.valueOf(currt_value));
		seek_bar.setProgress(currt_value);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_ok:
			Intent result = new Intent();
			Bundle basket = new Bundle();
			if(times < 1 )
				times = 2;
            switch(current_btn){
            case R.id.btn_training:
                training_time = currt_value;
                break;
            case R.id.btn_break:
                break_time = currt_value;
                break;
            default:
                times = currt_value;
                break;
            }
			basket.putInt("training time", training_time);
			basket.putInt("break time", break_time);
			basket.putInt("times", times);
			result.putExtras(basket);
			setResult(RESULT_OK, result);
			finish();
			break;
			
		case R.id.btn_default:
			training_time 	= 60;
			break_time 		= 10;
			times			= 12;
			currt_value 	= training_time;
			seek_bar.setProgress(currt_value);
			current_btn     = R.id.btn_training;
			group.clearCheck();
			group.check(R.id.btn_training);
			break;
			
		case R.id.btn_down:
			currt_value --;
			display.setText(String.valueOf(currt_value));
			seek_bar.setProgress(currt_value);
			break;
			
		case R.id.btn_up:
			currt_value ++;
			display.setText(String.valueOf(currt_value));
			seek_bar.setProgress(currt_value);
			break;
		
		}
	}
}
