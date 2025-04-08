package com.example.lab04;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextSettingsUtil {

    public static void applyTextSettings(Context context, View view) {
        SharedPreferences prefs = context.getSharedPreferences("SettingsPrefs", Context.MODE_PRIVATE);
        float textSize = prefs.getFloat("textSize", 16f);
        int colorIndex = prefs.getInt("textColorIndex", 0);
        int textColor = getColorFromIndex(colorIndex);

        applySettingsToView(view, textSize, textColor);
    }

    private static void applySettingsToView(View view, float textSize, int textColor) {
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(textSize);
            ((TextView) view).setTextColor(textColor);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applySettingsToView(viewGroup.getChildAt(i), textSize, textColor);
            }
        }
    }

    private static int getColorFromIndex(int index) {
        switch (index) {
            case 1: return Color.RED;
            case 2: return Color.BLUE;
            case 3: return Color.GREEN;
            default: return Color.BLACK;
        }
    }
}