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

import android.app.ActivityGroup;

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
    protected Stack<WeakReference<? extends SupportActivity>> mActivityStacks;

    /**
     * There is only one instance of the program.
     */
    private static Application sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityStacks = new Stack<>();
        sApp = this;
    }

    /**
     * Get a unique instance.
     */
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
    public void pushTask(SupportActivity activity) {
        mActivityStacks.add(new WeakReference<>(activity));
    }

    /**
     * Destroy and remove activity from the top of the task stack.
     */
    public void popTask() {
        WeakReference<? extends SupportActivity> ref = mActivityStacks.peek();
        if (ref != null) {
            mActivityStacks.pop();
        }
    }

    /**
     * Destroy and remove all activities in the task stack.
     */
    public void close() {
        for (WeakReference<? extends SupportActivity> ref:mActivityStacks) {
            if (ref != null) {
                SupportActivity activity = ref.get();
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }

    public void closeRetainBottom() {
        int size = mActivityStacks.size();
        for (int i=size-1;i > 0;i--) {
            WeakReference<? extends SupportActivity> ref = mActivityStacks.get(i);
            if (ref != null) {
                SupportActivity activity = ref.get();
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * Destroy all of the activities, and then kill processes.
     *
     * @see Application#close()
     */
    public void forceClose() {
        close();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
