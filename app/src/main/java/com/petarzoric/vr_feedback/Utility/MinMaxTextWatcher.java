package com.petarzoric.vr_feedback.Utility;

/**
 * credits: stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
 */

import android.text.TextWatcher;


public abstract class MinMaxTextWatcher implements TextWatcher {
    int min, max;
    public MinMaxTextWatcher(int min, int max) {
        super();
        this.min = min;
        this.max = max;
    }

}
