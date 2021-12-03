package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


public class AccountOption extends FrameLayout {
    private String title;
    private int icon_background;
    private Drawable icon;

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
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AccountOption, defStyle, 0);
        View root = inflate(getContext(), R.layout.account_option, this);
        TextView titleView = (TextView)root.findViewById(R.id.option_title);
        ImageView iconView = (ImageView)root.findViewById(R.id.option_icon);
        CardView iconWrapper = (CardView)root.findViewById(R.id.option_icon_wrapper);
        title = a.getString(R.styleable.AccountOption_title);
        titleView.setText(title);
        icon = a.getDrawable(R.styleable.AccountOption_icon);
        iconView.setImageDrawable(icon);
        icon_background = a.getColor(R.styleable.AccountOption_iconBackground,Color.BLACK);
        iconWrapper.setBackgroundTintList(ColorStateList.valueOf(icon_background));
        a.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void setIcon_background(int icon_background) {
        this.icon_background = icon_background;
    }

    public int getIcon_background() {
        return icon_background;
    }
}