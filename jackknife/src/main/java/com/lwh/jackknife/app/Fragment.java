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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwh.jackknife.ioc.bind.BindLayout;
import com.lwh.jackknife.ioc.SupportActivity;
import com.lwh.jackknife.ioc.SupportFragment;
import com.lwh.jackknife.ioc.ViewInjector;
import com.lwh.jackknife.ioc.exception.LackInterfaceException;
import com.lwh.jackknife.ioc.inject.FragmentHandler;

/**
 * Automatically inject a layout, bind views, and register events for fragments.
 */
public abstract class Fragment extends android.app.Fragment implements SupportFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		FragmentHandler handler = new FragmentHandler();
		return handler.inflateLayout(new BindLayout(this));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		ViewInjector.inject(this);
	}

	@Override
	public SupportActivity getFragmentActivity() {
		if (getActivity() instanceof SupportActivity) {
			return (SupportActivity) getActivity();
		}
		throw new LackInterfaceException("The activity lacks the SupportActivity interface.");
	}
}
