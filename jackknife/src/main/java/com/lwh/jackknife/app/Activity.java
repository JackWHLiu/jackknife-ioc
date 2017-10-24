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

import android.os.Bundle;

import com.lwh.jackknife.ioc.SupportActivity;
import com.lwh.jackknife.ioc.ViewInjector;

import java.lang.reflect.InvocationTargetException;

/**
 * Automatically inject a layout, bind views, and register events for activities.
 */
public abstract class Activity extends android.app.Activity implements SupportActivity {

	/**
	 * If you are using {@link Application}, it is automatically gonna be added to the Application's
	 * task stack when creating the Activity.
	 */
	protected void push(){
		if (getApplication() instanceof Application) {
			Application.getInstance().pushTask(this);
		}
	}

	/**
	 * Equivalent to {@link android.app.Activity#finish()}, the difference is that it is gonna be
	 * removed from the Application's task stack when the activity is destroyed.
	 */
	public void pop(){
		if (getApplication() instanceof Application) {
			Application.getInstance().popTask();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ViewInjector.create().inject(this);
			if (getApplication() instanceof Application){
				push();
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
