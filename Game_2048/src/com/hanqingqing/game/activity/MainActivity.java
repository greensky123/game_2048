package com.hanqingqing.game.activity;

import com.hanqingqing.game.R;
import com.hanqingqing.game.dao.AnimLayer;
import com.hanqingqing.game.dao.GameView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private int score = 0;
	private TextView tvScore, tvBestScore;
	private LinearLayout root = null;
	private Button btnNewGame;
	private GameView gameView;
	private AnimLayer animLayer = null;
	private static MainActivity mainActivity = null;
	public static final String SP_KEY_BEST_SCORE = "bestScore";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		root = (LinearLayout) findViewById(R.id.container);
		root.setBackgroundColor(0xfffaf8ef);

		tvScore = (TextView) findViewById(R.id.tvScore);
		tvBestScore = (TextView) findViewById(R.id.tvBestScore);

		gameView = (GameView) findViewById(R.id.gameView);

		btnNewGame = (Button) findViewById(R.id.btnNewGame);
		btnNewGame.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gameView.startGame();
			}
		});

		animLayer = (AnimLayer) findViewById(R.id.animLayer);
	}

	public void clearScore() {
		score = 0;
		showScore();
	}

	public void showScore() {
		tvScore.setText(score + "");
	}

	public void addScore(int s) {
		score += s;
		showScore();

		int maxScore = Math.max(score, getBestScore());
		saveBestScore(maxScore);
		showBestScore(maxScore);
	}

	public void saveBestScore(int s) {
		SharedPreferences.Editor e = getPreferences(MODE_PRIVATE).edit();
		e.putInt(SP_KEY_BEST_SCORE, s);
		e.commit();
	}

	public int getBestScore() {
		return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
	}

	public void showBestScore(int s) {
		tvBestScore.setText(s + "");
	}

	public AnimLayer getAnimLayer() {
		return animLayer;
	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	public MainActivity() {
		mainActivity = this;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK) {
          new AlertDialog.Builder(this)
          .setTitle(getResources().getString(R.string.dialog_exitgame))
          .setMessage(getResources().getString(R.string.dialog_exitmessage))
          .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				MainActivity.this.finish();
			}
		})
          .setNegativeButton(getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
		 }
		return super.onKeyDown(keyCode, event);
	}
}
