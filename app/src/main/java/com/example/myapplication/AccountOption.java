package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;


public class AccountOption extends View {
    private String title;
    private int background;
//    private float mExampleDimension = 0; //
//    private Drawable mExampleDrawable;


    public AccountOption(Context context) {
        super(context);
        init(null, 0);
    }

    public AccountOption(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public AccountOption(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.AccountOption, defStyle, 0);

        title = a.getString(R.styleable.AccountOption_title);
        background = a.getColor(R.styleable.AccountOption_backgroundColor, Color.BLACK);

//        if (a.hasValue(R.styleable.AccountOption_exampleDrawable)) {
//            mExampleDrawable = a.getDrawable(
//                    R.styleable.AccountOption_exampleDrawable);
//            mExampleDrawable.setCallback(this);
//        }
        a.recycle();
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}