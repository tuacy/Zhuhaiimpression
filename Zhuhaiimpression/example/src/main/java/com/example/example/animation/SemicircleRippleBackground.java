package com.example.example.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.example.R;

import java.util.ArrayList;

/**
 * @author: tuacy
 * @date: 2015/10/12 20:29
 * @version: V1.0
 */
public class SemicircleRippleBackground extends RelativeLayout {

	private static final int   DEFAULT_RIPPLE_COUNT  = 6;
	private static final int   DEFAULT_DURATION_TIME = 3000;
	private static final float DEFAULT_SCALE         = 6.0f;
	private static final int   DEFAULT_FILL_TYPE     = 0;

	private int   rippleColor;
	private float rippleStrokeWidth;
	private float rippleRadius;
	private int   rippleDurationTime;
	private int   rippleAmount;
	private float rippleScale;
	private int   rippleType;
	private Paint paint;
	private boolean                 animationRunning = false;
	private ArrayList<RippleView>   rippleViewList   = new ArrayList<RippleView>();
	private ArrayList<AnimationSet> animationSets    = new ArrayList<AnimationSet>();


	private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			/* no need delay when animation restart */
			animation.setStartOffset(0);
		}
	};

	public SemicircleRippleBackground(Context context) {
		super(context);
	}

	public SemicircleRippleBackground(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
	}

	public SemicircleRippleBackground(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initAttrs(context, attrs);
		initData();
	}

	private void initAttrs(final Context context, final AttributeSet attrs) {
		if (isInEditMode())
			return;

		if (null == attrs) {
			throw new IllegalArgumentException("Attributes should be provided to this view,");
		}

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleBackground);
		rippleColor=typedArray.getColor(R.styleable.RippleBackground_rb_color, getResources().getColor(R.color.rippelColor));
		rippleStrokeWidth=typedArray.getDimension(R.styleable.RippleBackground_rb_strokeWidth,
												  getResources().getDimension(R.dimen.rippleStrokeWidth));
		rippleRadius=typedArray.getDimension(R.styleable.RippleBackground_rb_radius,getResources().getDimension(R.dimen.rippleRadius));
		rippleDurationTime=typedArray.getInt(R.styleable.RippleBackground_rb_duration, DEFAULT_DURATION_TIME);
		rippleAmount=typedArray.getInt(R.styleable.RippleBackground_rb_rippleAmount,DEFAULT_RIPPLE_COUNT);
		rippleScale=typedArray.getFloat(R.styleable.RippleBackground_rb_scale, DEFAULT_SCALE);
		rippleType=typedArray.getInt(R.styleable.RippleBackground_rb_type, DEFAULT_FILL_TYPE);
		typedArray.recycle();
		initData();
	}

	private void initData() {
		for (View view : rippleViewList) {
			removeView(view);
		}
		rippleViewList.clear();

		paint = new Paint();
		paint.setAntiAlias(true);
		if(rippleType==DEFAULT_FILL_TYPE){
			rippleStrokeWidth=0;
			paint.setStyle(Paint.Style.FILL);
		}else
			paint.setStyle(Paint.Style.STROKE);
		paint.setColor(rippleColor);

		int radius = (int) (rippleRadius + rippleStrokeWidth);
		LayoutParams rippleParams=new LayoutParams(2 * radius, 2 * radius);
		rippleParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
		rippleParams.addRule(CENTER_HORIZONTAL, TRUE);
		rippleParams.setMargins(0, 0, 0, -radius);

		for (int i = 0; i < rippleAmount; i++) {

			RippleView rippleView = new RippleView(getContext());
			addView(rippleView, rippleParams);
			rippleViewList.add(rippleView);
		}

		startRippleAnimation();
	}

	private void initAnimations() {

		animationSets.clear();
		for (int i = 0; i < rippleAmount; i++) {

			int rippleDelay = rippleDurationTime / rippleAmount;
			Animation scaleAnimation = new ScaleAnimation(1.0f, rippleScale, 1.0f, rippleScale, Animation.RELATIVE_TO_SELF, 0.5f,
														  Animation.RELATIVE_TO_SELF, 0.5f);
			scaleAnimation.setDuration(rippleDurationTime);
			scaleAnimation.setRepeatCount(Animation.INFINITE);
			scaleAnimation.setRepeatMode(Animation.RESTART);
			scaleAnimation.setStartOffset(i * rippleDelay);
			scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
			scaleAnimation.setFillBefore(true);
			scaleAnimation.setAnimationListener(mAnimationListener);

			Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
			alphaAnimation.setDuration(rippleDurationTime);
			alphaAnimation.setRepeatCount(Animation.INFINITE);
			alphaAnimation.setRepeatMode(Animation.RESTART);
			alphaAnimation.setStartOffset(i * rippleDelay);
			alphaAnimation.setFillBefore(true);
			alphaAnimation.setAnimationListener(mAnimationListener);

			AnimationSet animationSet = new AnimationSet(true);

			animationSet.addAnimation(scaleAnimation);
			animationSet.addAnimation(alphaAnimation);
			animationSets.add(animationSet);
		}
	}

	public void startRippleAnimation() {
		if (!isRippleAnimationRunning()) {
			initAnimations(); // need initAnimations here, because must reset start offset.
			for (int i = 0; i < rippleAmount; i++) {
				RippleView view = rippleViewList.get(i);
				view.setVisibility(VISIBLE);
				view.startAnimation(animationSets.get(i));
			}
			animationRunning = true;
		}
	}

	public void stopRippleAnimation() {
		if (isRippleAnimationRunning()) {
			animationRunning = false;
			for (int i = 0; i < animationSets.size(); i++) {
				rippleViewList.get(i).clearAnimation();
			}
		}
	}

	public boolean isRippleAnimationRunning(){
		return animationRunning;
	}


	private class RippleView extends View {

		public RippleView(Context context) {
			super(context);
			this.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			int radius=(Math.min(getWidth(),getHeight()))/2;
			canvas.drawCircle(radius, radius, radius-rippleStrokeWidth, paint);
		}
	}
}
