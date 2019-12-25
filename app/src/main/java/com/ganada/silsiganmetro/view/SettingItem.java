package com.ganada.silsiganmetro.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.sip.SipSession;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ganada.silsiganmetro.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingItem extends FrameLayout {

    @IntDef({VISIBLE, INVISIBLE, GONE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Visibility {}

    private Context context;
    private AttributeSet attrs;

    @BindView(R.id.layoutRoot)
    RelativeLayout layoutRoot;
    @BindView(R.id.txtText)
    TextView txtText;
    @BindView(R.id.txtSubText)
    TextView txtSubText;

    private int visibilityText = VISIBLE;
    private int visibilitySubText = VISIBLE;
    private String strText;
    private String strSubText;

    public SettingItem(Context context) {
        super(context);
        this.context = context;
        this.attrs = null;

        init();
    }

    public SettingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;

        init();
    }

    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingItem);
        visibilityText = a.getInteger(R.styleable.SettingItem_textVisibility, VISIBLE);
        visibilitySubText = a.getInteger(R.styleable.SettingItem_subTextVisibility, VISIBLE);
        strText = a.getString(R.styleable.SettingItem_text);
        strSubText = a.getString(R.styleable.SettingItem_subText);
        View v = li.inflate(R.layout.item_settings, null);
        ButterKnife.bind(this, v);

        setText(strText);
        setSubText(strSubText);
        setTextVisibility(visibilityText);
        setSubTextVisibility(visibilitySubText);

        addView(v);
    }

    public void setText(String text) {
        txtText.setText(text);
    }

    public void setSubText(String text) {
        txtSubText.setText(text);
    }

    public void setOnClickListener(OnClickListener listener) {
        layoutRoot.setOnClickListener(listener);
    }

    public void setTextVisibility(@Visibility int visible) {
        txtText.setVisibility(visible);
    }

    public void setSubTextVisibility(@Visibility int visible) {
        txtSubText.setVisibility(visible);
    }
}
