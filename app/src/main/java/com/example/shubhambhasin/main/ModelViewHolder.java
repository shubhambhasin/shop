package com.example.shubhambhasin.main;

/**
 * Created by Shubham Bhasin on 26-Mar-16.
 */

import android.widget.CheckBox;
import android.widget.TextView;

public class ModelViewHolder {

    private CheckBox checkBox;
    private TextView textView;

    public ModelViewHolder()
    {
    }

    public ModelViewHolder(TextView textView, CheckBox checkBox)
    {
        this.checkBox = checkBox;
        this.textView = textView;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }

    public TextView getTextView()
    {
        return textView;
    }

    public void setTextView(TextView textView)
    {
        this.textView = textView;
    }
}
