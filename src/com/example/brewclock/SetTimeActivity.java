package com.example.brewclock;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetTimeActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    setContentView(R.layout.set_time);  
    //final EditText editText = (EditText)findViewById(R.id.edit);  
    Button btn_OK 		= (Button)findViewById(R.id.btn_ok);  
    Button btn_default 	= (Button)findViewById(R.id.btn_default);  
      
    btn_OK.setOnClickListener(new OnClickListener(){  
          public void onClick(View view){  
        	    Intent result = new Intent();
        	    Bundle basket = new Bundle();
        	    basket.putInt("training time", 65);
        	    basket.putInt("break time", 10);
                result.putExtras(basket);
        	    setResult(RESULT_OK, result);  
                finish();  

          }
 
    });  
      
} 
}
