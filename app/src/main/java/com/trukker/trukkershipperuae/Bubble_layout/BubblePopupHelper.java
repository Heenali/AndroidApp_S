package com.trukker.trukkershipperuae.Bubble_layout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.trukker.trukkershipperuae.R;

/**
 * Created by sudamasayuki on 16/05/02.
 */
public class BubblePopupHelper {

    public static PopupWindow create(@NonNull Context context, @NonNull BubbleLayout bubbleLayout) {

        PopupWindow popupWindow = new PopupWindow(context);

        popupWindow.setContentView(bubbleLayout);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // change background color to transparent
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(R.drawable.popup_window_transparent);
        } else {
            drawable = context.getResources().getDrawable(R.drawable.popup_window_transparent);
        }
        popupWindow.setBackgroundDrawable(drawable);

        return popupWindow;
    }

}