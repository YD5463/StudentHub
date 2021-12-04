package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomButton extends FrameLayout {
    private String btn_title;
    private int btn_background;

    public CustomButton(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButton, defStyle, 0);
        View root = inflate(getContext(), R.layout.custom_button, this);
        Button button = root.findViewById(R.id.custom_button);
        btn_title = a.getString(R.styleable.CustomButton_btnTitle);
        button.setText(btn_title);
        btn_background = a.getColor(R.styleable.CustomButton_btnColor,Color.BLACK);
        button.setBackgroundTintList(ColorStateList.valueOf(btn_background));
        a.recycle();
    }

    public int getBtn_background() {
        return btn_background;
    }

    public void setBtn_background(int btn_background) {
        this.btn_background = btn_background;
    }

    public String getBtn_title() {
        return btn_title;
    }

    public void setBtn_title(String btn_title) {
        this.btn_title = btn_title;
    }
}