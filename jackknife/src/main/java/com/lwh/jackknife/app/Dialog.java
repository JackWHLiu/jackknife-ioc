/*
 * Copyright (C) 2017 The JackKnife Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lwh.jackknife.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.lwh.jackknife.ioc.SupportActivity;
import com.lwh.jackknife.ioc.SupportDialog;
import com.lwh.jackknife.ioc.ViewInjector;
import com.lwh.jackknife.ioc.exception.LackInterfaceException;

/**
 * Automatically inject a layout, bind views, and register events for dialogs.
 */

public abstract class Dialog extends android.app.Dialog implements SupportDialog,
        DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

    public Dialog(Context context) {
        super(context);
    }

    public Dialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onCreateView());
        ViewInjector.inject(this);
        initData(this);
        setOnShowListener(this);
        setOnDismissListener(this);
    }

    @Override
    public SupportActivity getDialogActivity() {
        if (getOwnerActivity() instanceof SupportActivity) {
            return (SupportActivity) getOwnerActivity();
        }
        throw new LackInterfaceException("The activity lacks the SupportActivity interface.");
    }

    protected abstract void initData(Dialog dialog);

    @Override
    public View findViewById(int id) {
        return super.findViewById(id);
    }

    /**
     * Seize activity's layout.
     */
    protected abstract View onCreateView();

    /**
     * Called when the dialog is displayed.
     */
    public abstract void onShow(DialogInterface dialog);

    /**
     * Called when the dialog is hidden.
     */
    public abstract void onDismiss(DialogInterface dialog);
}
