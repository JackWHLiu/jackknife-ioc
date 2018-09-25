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

import android.os.Bundle;
import android.view.View;

import com.lwh.jackknife.ioc.SupportActivity;
import com.lwh.jackknife.ioc.ViewInjector;

/**
 * Automatically inject a layout, bind views, and register events for activities.
 */
public abstract class Activity extends android.app.Activity implements SupportActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewInjector.inject(this);
		push();
	}

	@Override
	protected void onDestroy() {
		pop();
		super.onDestroy();
	}

	/**
	 * If you are using {@link Application}, it is automatically gonna be added to the Application's
	 * task stack when creating the Activity.
	 */
	private void push() {
		if (getApplication() instanceof Application) {
			Application.getInstance().pushTask(this);
		}
	}

	/**
	 * Equivalent to {@link android.app.Activity#finish()}, the difference is that it is gonna be
	 * removed from the Application's task stack when the activity is destroyed.
	 */
	private void pop() {
		if (getApplication() instanceof Application) {
			Application.getInstance().popTask();
		}
	}

	@Override
	public View findViewById(int id) {
		return super.findViewById(id);
	}
}
