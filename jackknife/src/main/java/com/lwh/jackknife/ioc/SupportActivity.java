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

package com.lwh.jackknife.ioc;

import android.app.Activity;

/**
 * Let an activity implements it, for example, {@link android.app.Activity} and so on.
 */
public interface SupportActivity extends SupportV {

    /**
     * {@link Activity#getPackageName()}
     */
    String getPackageName();

    /**
     * {@link Activity#finish()}
     */
    void finish();
}
