package com.example.glowball;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GlowBallFragment extends Fragment {

	private Context context;
	private BallsManager ballsManager;
	private Handler handler;
	private int screenHeight;
	private LinearInterpolator linearInterpolator;
	private boolean closeThread;
	private ImageView underLine;
	private TextView scoreTxt;
	private int currentColor;
	private ViewGroup gameMainLay;

	private Button testBtn;

	@Override
	public void onPause() {
		super.onPause();
		closeThread = true;
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
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		linearInterpolator = new LinearInterpolator();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.game_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		gameMainLay = (ViewGroup) getActivity().findViewById(R.id.game_main);
		ballsManager = new BallsManager(context, gameMainLay);
		scoreTxt = (TextView) getActivity().findViewById(R.id.score);
		testBtn = (Button) getActivity().findViewById(R.id.button1);
		underLine = (ImageView) getActivity().findViewById(R.id.underLine);
		underLine.setBackgroundColor(currentColor = ballsManager.colorGenerator
				.getColor());

		testBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction().replace(R.id.main_lay, new GlowBallFragment())
																		.remove(GlowBallFragment.this)
																		.commit();
				
				
			}
		});

	}

	@Override
	public void onResume() {
		onGameStart();
		super.onResume();
	}

	private Runnable ballGenerator = new Runnable() {

		@Override
		public void run() {
			moveBall(ballsManager.getImageView());
			if (!closeThread)
				handler.postDelayed(ballGenerator, 500);
		}
	};

	private void moveBall(final ImageView ball) {
		ValueAnimator va = ValueAnimator.ofInt(0, screenHeight
				- (100 + underLine.getHeight()));
		va.setDuration(3500);
		va.setInterpolator(linearInterpolator);
		va.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if ((Boolean) ball.getTag()) {
					animation.cancel();
				}
				RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) ball
						.getLayoutParams();
				rlp.setMargins(rlp.leftMargin,
						(Integer) animation.getAnimatedValue(),
						rlp.rightMargin, rlp.bottomMargin);
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
				String s = (String) scoreTxt.getText();
				int score = Integer.parseInt(s);

				if ((Boolean) ball.getTag()) {// Has been touched
					if (currentColor == ((ColorDrawable) ball.getBackground())
							.getColor()) {// the same color
						score -= 10;
					} else {// not the same color
						score += 5;
					}
				} else {// Has not been touched
					if (currentColor == ((ColorDrawable) ball.getBackground())
							.getColor()) {// the same color
						score += 10;
					} else {// not the same color
						score -= 20;
					}
				}
				scoreTxt.setText("" + score);
				ValueAnimator alpha = ObjectAnimator.ofFloat(ball, "alpha",
						1.0f, 0f);
				ValueAnimator scaleX = ObjectAnimator.ofFloat(ball, "scaleX",
						0f);
				ValueAnimator scaleY = ObjectAnimator.ofFloat(ball, "scaleY",
						0f);
				ValueAnimator rotate = ObjectAnimator.ofFloat(ball, "rotation",
						0f, 360f);
				AnimatorSet set = new AnimatorSet();
				set.setDuration(300);
				set.playTogether(alpha, scaleX, scaleY, rotate);
				set.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						ballsManager.RestoreView(ball);
					}

					@Override
					public void onAnimationCancel(Animator animation) {

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

	private void onGameStart() {
		closeThread = false;
		handler.post(ballGenerator);// must be at the bottom of the code
	}

	private void onGameRestart() {
		closeThread = true;
		gameMainLay.removeAllViews();
		ballsManager.clearCachedImages();
		gameMainLay.addView(scoreTxt);
		gameMainLay.addView(underLine);
		gameMainLay.addView(testBtn);
		underLine.setBackgroundColor(currentColor = ballsManager.colorGenerator
				.getColor());
		scoreTxt.setText("0");
		//splash start
//		final ImageView splash = new ImageView(context);
//		splash.setBackgroundColor(Color.WHITE);
//		gameMainLay.addView(splash);
//		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams)splash.getLayoutParams();
//		rlp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//		rlp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//		splash.setLayoutParams(rlp);
//		ValueAnimator va = ObjectAnimator.ofFloat(splash, "alpha", 0f,1.0f,0f);
//		va.setDuration(3600);
//		va.addListener(new AnimatorListener() {
//			
//			@Override
//			public void onAnimationStart(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animator animation) {
//				// TODO Auto-generated method stub
//				gameMainLay.removeView(splash);
//				onGameStart();
//			}
//			
//			@Override
//			public void onAnimationCancel(Animator animation) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		va.start();
		
		onGameStart();
	}

}
