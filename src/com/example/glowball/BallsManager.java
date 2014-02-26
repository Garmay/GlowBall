package com.example.glowball;

import java.util.ArrayList;
import java.util.Random;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BallsManager{
	private ArrayList<ImageView> restoredViewList;
	private Context context;
	private ViewGroup container;
	private Random random;
	private int screenWidth;
	private int heightRandomHeighten;
	private int heightRandomMin;
	public BallsManager(Context context,ViewGroup container){
		restoredViewList = new ArrayList<ImageView>();
		this.context = context;
		this.container = container;
		random = new Random();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		heightRandomHeighten = (int) (displaymetrics.heightPixels*0.2);
		heightRandomMin = (int) (displaymetrics.heightPixels*0.3);
		
	}
	public ImageView getImageView(){
		if(restoredViewList.isEmpty()){
			ImageView iv = new ImageView(context);
			iv.setTag(Boolean.valueOf(false));
			iv.setOnClickListener(onClickListener);
			container.addView(iv);
			return setUpBall(iv);
		}
		else{
			ImageView iv = restoredViewList.remove(0);
			iv.setTag(Boolean.valueOf(false));
			return setUpBall(iv);
		}
		
	}
	public void RestoreView(ImageView restoredView){
		restoredViewList.add(restoredView);
	}
	
	//set parameter of ball(just like width、height)
	private ImageView setUpBall(ImageView ball){
		RelativeLayout.LayoutParams rlp = (LayoutParams) ball.getLayoutParams();
		rlp.width=100;
		rlp.height=100;
		rlp.setMargins(random.nextInt(screenWidth-100), 0, 0, 0);
		ball.setLayoutParams(rlp);
		if(random.nextBoolean()){
			ball.setBackgroundColor(Color.BLUE);
		}
		else{
			ball.setBackgroundColor(Color.GREEN);
		}
		ball.setAlpha(1.0f);
		ball.setScaleX(1.0f);
		ball.setScaleY(1.0f);
		return ball;
	}

	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			v.setTag(Boolean.valueOf(true));
		}
	};
}
