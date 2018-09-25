/*
 * Copyright (C) 2018 The JackKnife Open Source Project
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

package com.lwh.jackknife.ioc.inject;

import android.view.ViewGroup;

import com.lwh.jackknife.ioc.bind.BindEvent;
import com.lwh.jackknife.ioc.bind.BindLayout;
import com.lwh.jackknife.ioc.bind.BindView;
import com.lwh.jackknife.ioc.SupportV;

/**
 * Abstract visitors to deal with some injection-related functionality.
 */
public interface InjectHandler {

    /**
     * ASCII code index of character 'A', it is 65.
     */
    int A_INDEX = 'A';

    /**
     * ASCII code index of character 'Z', it is 90.
     */
    int Z_INDEX = 'Z';

    /**
     * The inner class named 'id' of R.
     */
    String R_ID = ".R$id";

    /**
     * The inner class named 'layout' of R.
     */
    String R_LAYOUT = ".R$layout";

    /**
     * Used to splice an XML file name.
     */
    String UNDERLINE = "_";

    /**
     * {@link android.app.Activity#setContentView(int)}
     */
    String METHOD_SET_CONTENT_VIEW = "setContentView";

    /**
     * {@link android.app.Activity#findViewById(int)}
     */
    String METHOD_FIND_VIEW_BY_ID = "findViewById";

    /**
     * {@link android.view.LayoutInflater#inflate(int, ViewGroup)}
     */
    String METHOD_INFLATE = "inflate";

    /**
     * The default method for annotations.
     */
    String METHOD_VALUE = "value";

    /**
     * You can override this method to complete the mapping of custom XML files。
     *
     * @return Something like 'activity_main' stands for activity_main.xml
     */
    String generateLayoutName(SupportV v);

    /**
     * Define an abstract method to inject layout.
     *
     * @param bindLayout The element to handle binding layout.
     */
    void performInject(BindLayout bindLayout);

    /**
     * Define an abstract method to inject views.
     *
     * @param bindView The element to handle binding views.
     */
    void performInject(BindView bindView);

    /**
     * Define an abstract method to inject events.
     *
     * @param bindEvent The element to handle binding events.
     */
    void performInject(BindEvent bindEvent);
}
