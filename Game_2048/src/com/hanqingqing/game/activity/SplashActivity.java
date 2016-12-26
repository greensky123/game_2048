package com.hanqingqing.game.activity;

import com.hanqingqing.game.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity{
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findViewById(R.id.splash_enter_btn).setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
	            startActivity(intent);
	            SplashActivity.this.finish();
	        }
	    });
	}

}
