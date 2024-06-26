package com.noclip.marcinmalysz.pixfight;

import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;

public class ProgressDialog extends AppCompatDialog {

    ProgressDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init();
    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.waiting_dialog);
        Window window = getWindow();

        if (window != null) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setDimAmount(0.5f);
            setOnDismissListener(dialog -> window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND));
        }
    }
}
