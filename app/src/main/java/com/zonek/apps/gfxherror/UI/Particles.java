package com.zonek.apps.gfxherror.UI;

import android.animation.TimeAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;

import com.zonek.apps.gfxherror.R;

import java.util.Random;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */
public class Particles extends View {

    private static class Star {
        private float x;
        private float y;
        private float scale;
        private float alpha;
        private float speed;
    }

    private static final int BASE_SPEED_DP_PER_S = 180;
    private static final int COUNT = 8;
    private static final int SEED = 10;

    private static final float SCALE_MIN_PART = 0.4f;
    private static final float SCALE_RANDOM_PART = 0.5f;
    private static final float ALPHA_SCALE_PART = 0.5f;
    private static final float ALPHA_RANDOM_PART = 0.5f;

    private final Star[] mStars = new Star[COUNT];
    private final Random mRnd = new Random(SEED);

    private TimeAnimator mTimeAnimator;
    private Drawable mDrawable;

    private float mBaseSpeed;
    private float mBaseSize;
    private long mCurrentPlayTime;


    public Particles(Context context) {
        super(context);
        init();
    }

    public Particles(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Particles(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
     //   mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.note6);
        setIcon(R.drawable.stars);
        mBaseSize = Math.max(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight()) / 2f;
        mBaseSpeed = BASE_SPEED_DP_PER_S * getResources().getDisplayMetrics().density;
    }

    public void setIcon(final int icon){
        mDrawable = ContextCompat.getDrawable(getContext(), icon);
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);

        for (int i = 0; i < mStars.length; i++) {
            final Star star = new Star();
            initializeStar(star, width, height);
            mStars[i] = star;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int viewHeight = getHeight();
        for (final Star star : mStars) {
            // Ignore the star if it's outside of the view bounds
            final float starSize = star.scale * mBaseSize;
            if (star.y + starSize < 0 || star.y - starSize > viewHeight) {
                continue;
            }

            // Save the current canvas state
            final int save = canvas.save();

            // Move the canvas to the center of the star
            canvas.translate(star.x, star.y);

            // Rotate the canvas based on how far the star has moved
            final float progress = (star.y + starSize) / viewHeight;
            canvas.rotate(360 * progress);

            // Prepare the size and alpha of the drawable
            final int size = Math.round(starSize);
            mDrawable.setBounds(-size, -size, size, size);
            mDrawable.setAlpha(Math.round(255 * star.alpha));

            // Draw the star to the canvas
            mDrawable.draw(canvas);


            // Restore the canvas to it's previous position and rotation
            canvas.restoreToCount(save);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mTimeAnimator = new TimeAnimator();
        mTimeAnimator.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (!isLaidOut()) {
                        // Ignore all calls before the view has been measured and laid out.
                        return;
                    }
                }
                updateState(deltaTime);
                invalidate();
            }
        });
        mTimeAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTimeAnimator.cancel();
        mTimeAnimator.setTimeListener(null);
        mTimeAnimator.removeAllListeners();
        mTimeAnimator = null;
    }


    public void pause() {
        if (mTimeAnimator != null && mTimeAnimator.isRunning()) {
            // Store the current play time for later.
            mCurrentPlayTime = mTimeAnimator.getCurrentPlayTime();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mTimeAnimator.pause();
            }
        }
    }


    public void resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mTimeAnimator != null && mTimeAnimator.isPaused()) {
                mTimeAnimator.start();
                // Why set the current play time?
                // TimeAnimator uses timestamps internally to determine the delta given
                // in the TimeListener. When resumed, the next delta received will the whole
                // pause duration, which might cause a huge jank in the animation.
                // By activity_setting the current play time, it will pick of where it left off.
                mTimeAnimator.setCurrentPlayTime(mCurrentPlayTime);
            }
        }
    }


    private void updateState(float deltaMs) {
        // Converting to seconds since PX/S constants are easier to understand
        final float deltaSeconds = deltaMs / 1000f;
        final int viewWidth = getWidth();
        final int viewHeight = getHeight();

        for (final Star star : mStars) {
            // Move the star based on the elapsed time and it's speed
            star.y -= star.speed * deltaSeconds;

            // If the star is completely outside of the view bounds after
            // updating it's position, recycle it.
            final float size = star.scale * mBaseSize;
            if (star.y + size < 0) {
                initializeStar(star, viewWidth, viewHeight);
            }
        }
    }

    private void initializeStar(Star star, int viewWidth, int viewHeight) {
        // Set the scale based on a min value and a random multiplier
        star.scale = SCALE_MIN_PART + SCALE_RANDOM_PART * mRnd.nextFloat();

        // Set X to a random value within the width of the view
        star.x = viewWidth * mRnd.nextFloat();

        // Set the Y position
        // Start at the bottom of the view
        star.y = viewHeight;
        // The Y value is in the center of the star, add the size
        // to make sure it starts outside of the view bound
        star.y += star.scale * mBaseSize;
        // Add a random offset to create a small delay before the
        // star appears again.
        star.y += viewHeight * mRnd.nextFloat() / 4f;

        // The alpha is determined by the scale of the star and a random multiplier.
        star.alpha = ALPHA_SCALE_PART * star.scale + ALPHA_RANDOM_PART * mRnd.nextFloat();
        // The bigger and brighter a star is, the faster it moves
        star.speed = mBaseSpeed * star.alpha * star.scale;
    }
}

