/*
 * Copyright (C) 2017. The JackKnife Open Source Project
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

import android.app.ActivityGroup;
import android.database.sqlite.SQLiteOpenHelper;

import com.lwh.jackknife.ioc.SupportActivity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * In a particular case, you might want to inherit this class and specify it in the application's
 * tag of the manifest file.
 */
public class Application extends android.app.Application {

    /**
     * Only a mirror used to record the activity created.
     */
    private Stack<WeakReference<SupportActivity>> mActivityStacks;

    /**
     * There is only one instance of the program.
     */
    private static Application sApp;

    /**
     * It is attached to the application context and is easy to access anywhere in the program.
     */
    private SQLiteOpenHelper mSQLiteOpenHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        mActivityStacks = new Stack<>();
    }

    /**
     * After attachment, you can get it in {@link #getSQLiteOpenHelper()}.
     *
     * @param helper A helper class to manage database creation and version management.
     */
    public void attach(SQLiteOpenHelper helper){
        this.mSQLiteOpenHelper = helper;
    }

    public SQLiteOpenHelper getSQLiteOpenHelper(){
        return mSQLiteOpenHelper;
    }

    public static Application getInstance(){
        return sApp;
    }

    /**
     * Add the activity to the task stack.
     *
     * @param activity An activity is a single, focused thing that the user can do.  Almost all
     * activities interact with the user, so the Activity class takes care of
     * creating a window for you in which you can place your UI with
     * {@link android.app.Activity#setContentView}.  While activities are often presented to the user
     * as full-screen windows, they can also be used in other ways: as floating
     * windows (via a theme with {@link android.R.attr#windowIsFloating} set)
     * or embedded inside of another activity (using {@link ActivityGroup}).
     */
    /* package */ void pushTask(SupportActivity activity){
        mActivityStacks.add(new WeakReference<>(activity));
    }

    /**
     * Destroy and remove activity from the top of the task stack.
     */
    /* package */ void popTask(){
        WeakReference<SupportActivity> ref = mActivityStacks.pop();
        SupportActivity activity = ref.get();
        activity.finish();
        mActivityStacks.remove(activity);
    }

    /**
     * Destroy and remove all activities in the task stack.
     */
    protected void removeTasks(){
        for (WeakReference<SupportActivity> ref:mActivityStacks){
            SupportActivity activity = ref.get();
            activity.finish();
        }
        mActivityStacks.removeAllElements();
    }
}
