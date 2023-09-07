package com.zonek.apps.gfxherror.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class ImageViews extends AppCompatImageView {


    public ImageViews(Context context) {
        this(context, null);
    }
    public ImageViews(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImageViews(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context,0,1);

    }

    private void init(final Context context, final int pressed, final int unpressed){

        setOnTouchListener(new OnTouchListener() {
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    ImageViews.this.getParent().requestDisallowInterceptTouchEvent(true);
                    view.setScaleX(0.92f);
                    view.setScaleY(0.92f);
                } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                    ImageViews.this.getParent().requestDisallowInterceptTouchEvent(false);
                    view.setScaleX(1f);
                    view.setScaleY(1f);
                  //  setClicksound(context);
                }
                return false;
            }
        });
    }





}
