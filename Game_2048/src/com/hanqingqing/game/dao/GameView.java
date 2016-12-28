package com.hanqingqing.game.dao;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hanqingqing.game.R;
import com.hanqingqing.game.activity.MainActivity;
import com.hanqingqing.game.config.Config;

public class GameView extends LinearLayout {

	public GameView(Context context) {
		super(context);

		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initGameView();
	}

	private void initGameView(){
		setOrientation(LinearLayout.VERTICAL);
		setBackgroundColor(0xffbbada0);


		setOnTouchListener(new OnTouchListener() {

			private float startX,startY,offsetX,offsetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					startY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					offsetX = event.getX()-startX;
					offsetY = event.getY()-startY;


					if (Math.abs(offsetX)>Math.abs(offsetY)) {
						if (offsetX<-5) {
							swipeLeft();
						}else if (offsetX>5) {
							swipeRight();
						}
					}else{
						if (offsetY<-5) {
							swipeUp();
						}else if (offsetY>5) {
							swipeDown();
						}
					}

					break;
				}
				return true;
			}
		});
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		Config.CARD_WIDTH = (Math.min(w, h)-10)/Config.LINES;

		addCards(Config.CARD_WIDTH,Config.CARD_WIDTH);

		startGame();
	}

	private void addCards(int cardWidth,int cardHeight){

		Card c;

		LinearLayout line;
		LayoutParams lineLp;
		
		for (int y = 0; y < Config.LINES; y++) {
			line = new LinearLayout(getContext());
			lineLp = new LayoutParams(-1, cardHeight);
			addView(line, lineLp);
			
			for (int x = 0; x < Config.LINES; x++) {
				c = new Card(getContext());
				line.addView(c, cardWidth, cardHeight);

				cardsMap[x][y] = c;
			}
		}
	}

	public void startGame(){

		MainActivity aty = MainActivity.getMainActivity();
		aty.clearScore();
		aty.showBestScore(aty.getBestScore());

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				cardsMap[x][y].setNum(0);
			}
		}

		addRandomNum();
		addRandomNum();
	}

	private void addRandomNum(){

		emptyPoints.clear();

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				if (cardsMap[x][y].getNum()<=0) {
					emptyPoints.add(new Point(x, y));
				}
			}
		}

		if (emptyPoints.size()>0) {

			Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
			cardsMap[p.x][p.y].setNum(Math.random()>0.1?2:4);

			MainActivity.getMainActivity().getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
		}
	}


	private void swipeLeft(){

		boolean merge = false;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {

				for (int x1 = x+1; x1 < Config.LINES; x1++) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {

							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y],cardsMap[x][y], x1, x, y, y);

							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x--;
							merge = true;

						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);

							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeRight(){

		boolean merge = false;

		for (int y = 0; y < Config.LINES; y++) {
			for (int x = Config.LINES-1; x >=0; x--) {

				for (int x1 = x-1; x1 >=0; x1--) {
					if (cardsMap[x1][y].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
							cardsMap[x1][y].setNum(0);

							x++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y],x1, x, y, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x1][y].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeUp(){

		boolean merge = false;

		for (int x = 0; x < Config.LINES; x++) {
			for (int y = 0; y < Config.LINES; y++) {

				for (int y1 = y+1; y1 < Config.LINES; y1++) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y--;

							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;

					}
				}
			}
		}

		if (merge) {
			addRandomNum();
			checkComplete();
		}
	}
	private void swipeDown(){

		boolean merge = false;

		for (int x = 0; x < Config.LINES; x++) {
			for (int y = Config.LINES-1; y >=0; y--) {

				for (int y1 = y-1; y1 >=0; y1--) {
					if (cardsMap[x][y1].getNum()>0) {

						if (cardsMap[x][y].getNum()<=0) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
							cardsMap[x][y1].setNum(0);

							y++;
							merge = true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
							MainActivity.getMainActivity().getAnimLayer().createMoveAnim(cardsMap[x][y1],cardsMap[x][y], x, x, y1, y);
							cardsMap[x][y].setNum(cardsMap[x][y].getNum()*2);
							cardsMap[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
							merge = true;
						}

						break;
					}
				}
			}
		}

		if (merge) {
			this.invalidate();
			addRandomNum();
			checkComplete();
		}
	}

	private void checkComplete(){

		boolean complete = true;
		boolean Win = false;

		ALL:
			for (int y = 0; y < Config.LINES; y++) {
				for (int x = 0; x < Config.LINES; x++) {
						if (cardsMap[x][y].getNum()==0||
								(x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
								(x<Config.LINES-1&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
								(y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
								(y<Config.LINES-1&&cardsMap[x][y].equals(cardsMap[x][y+1]))) {
                           Win = false;
							complete = false;
							break ALL;
						}	
				
				
				}
			}

	Wina:	
		for (int y = 0; y < Config.LINES; y++) {
			for (int x = 0; x < Config.LINES; x++) {
				if (cardsMap[x][y].getNum() >= 2048) {
					complete = true;
					Win = true;
					break Wina;
				}
			}
			}	
		if (complete) {
			if (Win) {//游戏胜利是弹出的对话框
				new AlertDialog.Builder(getContext())
				.setTitle(getContext().getResources().getString(R.string.dialog_gamewin_title))
				.setMessage(getContext().getResources().getString(R.string.dialog_gamewin_message))
				.setPositiveButton(getContext().getResources().getString(R.string.dialog_gameover_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startGame();
						MainActivity.getMainActivity().clearScore();
					}
				})
				.setNegativeButton(getContext().getResources().getString(R.string.dialog_gameover_no), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						MainActivity.getMainActivity().finish();
					}
				})
				.show();
			} else {
				new AlertDialog.Builder(getContext())//游戏失败是弹出的对话框
				.setTitle(getContext().getResources().getString(R.string.dialog_gameover_title))
				.setMessage(getContext().getResources().getString(R.string.dialog_gameover_message))
				.setPositiveButton(getContext().getResources().getString(R.string.dialog_gameover_ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startGame();
					}
				})
				.setNegativeButton(getContext().getResources().getString(R.string.dialog_gameover_no), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.show();
			}
	
		}

	}

	private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
	private List<Point> emptyPoints = new ArrayList<Point>();
}
