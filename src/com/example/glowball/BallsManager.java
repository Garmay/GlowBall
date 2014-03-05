package com.example.glowball;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class BallsManager {
	public static final int boxWidth=100;
	public static final int boxHeight=100;
	private ArrayList<ImageView> restoredViewList;
	private Context context;
	private ViewGroup container;
	private Random random;
	private int screenWidth;

	public ColorGenerator colorGenerator;

	public BallsManager(Context context, ViewGroup container) {
		restoredViewList = new ArrayList<ImageView>();
		this.context = context;
		this.container = container;
		random = new Random();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		screenWidth = displaymetrics.widthPixels;
		colorGenerator = new ColorGenerator();
	}

	public ImageView getImageView() {
		if (restoredViewList.isEmpty()) {
			ImageView iv = new ImageView(context);
			iv.setTag(Boolean.valueOf(false));
			iv.setOnClickListener(onClickListener);
			container.addView(iv);
			return setUpBall(iv);
		} else {
			ImageView iv = restoredViewList.remove(0);
			iv.setTag(Boolean.valueOf(false));
			return setUpBall(iv);
		}

	}

	public void RestoreView(ImageView restoredView) {
		restoredViewList.add(restoredView);
	}

	// set parameter of ball(just like width、height)
	private ImageView setUpBall(ImageView ball) {
		RelativeLayout.LayoutParams rlp = (LayoutParams) ball.getLayoutParams();
		rlp.width = boxWidth;
		rlp.height = boxHeight;
		rlp.setMargins(random.nextInt(screenWidth - boxWidth), 0, 0, 0);
		ball.setLayoutParams(rlp);
		ball.setBackgroundColor(colorGenerator.getColor());
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

	public class ColorGenerator {
		private int colorArr[];
		private Random rand;

		public ColorGenerator() {
			rand = new Random();
			colorArr = new int[] { Color.GREEN, Color.BLUE, Color.RED };
		}

		public int getColor() {
			return colorArr[rand.nextInt(colorArr.length)];
		}
	}

	public void clearCachedImages(){
		restoredViewList.removeAll(restoredViewList);
	}
}
