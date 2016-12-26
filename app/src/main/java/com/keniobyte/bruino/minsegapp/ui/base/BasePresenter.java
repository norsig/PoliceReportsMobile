package com.keniobyte.bruino.minsegapp.ui.base;

import android.os.Bundle;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;

/**
 * Created by bruino on 26/12/16.
 */

public class BasePresenter<ViewType> extends RxPresenter<ViewType> {
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }
}
