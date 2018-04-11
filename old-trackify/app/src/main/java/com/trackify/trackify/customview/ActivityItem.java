package com.trackify.trackify.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trackify.trackify.R;

public class ActivityItem extends RelativeLayout {

    public enum Type {
        TRANSPORT,
        BIKING,
        WALKING,
        HOME,
        STUDYING
    }

    private Context context;

    private ImageView icon;
    private RelativeLayout iconContainer;
    private TextView activityTitle;
    private TextView activityDesciption;


    public ActivityItem(Context context) {
        super(context);
        init(context);
    }

    public ActivityItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActivityItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.custom_activity_item, this, true);

        this.icon = findViewById(R.id.timeline_icon);
        this.activityDesciption = findViewById(R.id.activity_description);
        this.activityTitle = findViewById(R.id.activity_title);
        this.iconContainer = findViewById(R.id.timeline_icon_container);

    }


    public void setData(Type type, String titleText) {
        this.activityTitle.setText(titleText);

        switch (type) {
            case HOME:
                this.icon.setImageDrawable(getResources().getDrawable(R.drawable.home));
                this.activityDesciption.setText("Home");
                this.iconContainer.getLayoutParams().height = 200;
                this.iconContainer.getLayoutParams().width = 200;
                break;
            case BIKING:
                this.icon.setImageDrawable(getResources().getDrawable(R.drawable.bicycle));
                this.activityDesciption.setText("Biking");
                this.iconContainer.getLayoutParams().height = 165;
                this.iconContainer.getLayoutParams().width = 165;
                break;
            case WALKING:
                this.icon.setImageDrawable(getResources().getDrawable(R.drawable.walking));
                this.activityDesciption.setText("Walking");
                this.iconContainer.getLayoutParams().height = 165;
                this.iconContainer.getLayoutParams().width = 165;
                break;
            case TRANSPORT:
                this.icon.setImageDrawable(getResources().getDrawable(R.drawable.train));
                this.activityDesciption.setText("Transport");
                this.iconContainer.getLayoutParams().height =  165;
                this.iconContainer.getLayoutParams().width = 165;
                break;
            case STUDYING:
                this.icon.setImageDrawable(getResources().getDrawable(R.drawable.book));
                this.activityDesciption.setText("Studying");
                this.iconContainer.getLayoutParams().height = 200;
                this.iconContainer.getLayoutParams().width = 200;
                break;
        }
    }
}
