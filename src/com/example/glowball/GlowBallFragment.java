package com.example.glowball;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GlowBallFragment extends Fragment {

	private Context context;
	private BallsManager ballsManager;
	private Handler handler;
	private int screenHeight;
	private LinearInterpolator linearInterpolator;
	private boolean closeThread=false;

	@Override
	public void onPause() {
		super.onPause();
		closeThread=true;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		linearInterpolator = new LinearInterpolator();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.game_fragment, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ballsManager = new BallsManager(context,(ViewGroup) getActivity().findViewById(R.id.game_main));
		handler.post(ballGenerator);
	}
	
	private Runnable ballGenerator = new Runnable(){

		@Override
		public void run() {
			moveBall(ballsManager.getImageView());
			if(!closeThread)handler.postDelayed(ballGenerator, 500);
		}
		
	};
	
	private void moveBall(final ImageView ball){
		ValueAnimator va = ValueAnimator.ofInt(0,screenHeight);
		va.setDuration(3500);
		va.setInterpolator(linearInterpolator);
		va.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if((Boolean)ball.getTag()){
					animation.cancel();
				}
				RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)ball.getLayoutParams(); 
				rlp.setMargins(rlp.leftMargin, (Integer)animation.getAnimatedValue(), rlp.rightMargin, rlp.bottomMargin);
				ball.setLayoutParams(rlp);
				
			}
		});
		va.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {	
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {	
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {	
				
				ValueAnimator alpha = ObjectAnimator.ofFloat(ball, "alpha", 1.0f,0f);
				ValueAnimator scaleX = ObjectAnimator.ofFloat(ball, "scaleX",0f);
				ValueAnimator scaleY = ObjectAnimator.ofFloat(ball, "scaleY",0f);
				ValueAnimator rotate = ObjectAnimator.ofFloat(ball, "rotation", 0f,360f);
				AnimatorSet set = new AnimatorSet();
				set.setDuration(300);
				set.playTogether(alpha,scaleX,scaleY,rotate);
				set.addListener(new AnimatorListener() {
					
					@Override
					public void onAnimationStart(Animator animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animator animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animator animation) {
						// TODO Auto-generated method stub
						ballsManager.RestoreView(ball);
					}
					
					@Override
					public void onAnimationCancel(Animator animation) {
						// TODO Auto-generated method stub
						
					}
				});
				set.start();
				
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {	
			}
		});
		va.start();
	}

	
}
