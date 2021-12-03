package com.example.myapplication.my_profile;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.R;


public class AccountOption extends FrameLayout {
    private String title_option;
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
        title_option = a.getString(R.styleable.AccountOption_title);
        titleView.setText(title_option);
        icon = a.getDrawable(R.styleable.AccountOption_icon);
        iconView.setImageDrawable(icon);
        icon_background = a.getColor(R.styleable.AccountOption_iconBackground,Color.BLACK);
        iconWrapper.setBackgroundTintList(ColorStateList.valueOf(icon_background));
        a.recycle();
    }

    public String getTitle_option() {
        return title_option;
    }

    public void setTitle_option(String title_option) {
        this.title_option = title_option;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIcon_background(int icon_background) {
        this.icon_background = icon_background;
    }

    public int getIcon_background() {
        return icon_background;
    }
}