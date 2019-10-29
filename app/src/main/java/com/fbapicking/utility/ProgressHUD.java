package com.fbapicking.utility;

import com.fbapicking.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgressHUD extends Dialog {
    static Typeface font;

    // Initialize progress dialog
    private ProgressHUD(Context context) {
        super(context);
    }

    // Initialize progress dialog with theme
    private ProgressHUD(Context context, int theme) {
        super(context, theme);
    }

    // Handle window focus changed
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
        spinner.start();
    }

    // Method to set message
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {
            findViewById(R.id.message).setVisibility(View.VISIBLE);
            TextView txt = (TextView) findViewById(R.id.message);
            txt.setText(message);
            txt.invalidate();
        }
    }

    // Method to show the progress dialog
    public static ProgressHUD show(Context context, CharSequence message, boolean indeterminate,
                                   boolean cancelable, OnCancelListener cancelListener) {
        font = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLight.ttf");

        ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_hud);

        if (message == null || message.length() == 0) {
            dialog.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) dialog.findViewById(R.id.message);
            txt.setText(message);
            txt.setTypeface(font);
        }
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.3f;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }
}
