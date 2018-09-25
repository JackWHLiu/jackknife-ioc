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

import com.lwh.jackknife.ioc.SupportV;
import com.lwh.jackknife.ioc.bind.BindEvent;
import com.lwh.jackknife.ioc.bind.BindLayout;
import com.lwh.jackknife.ioc.bind.BindView;

/**
 * The default implementation to decrease code.
 */
public abstract class InjectAdapter implements InjectHandler {

    @Override
    public String generateLayoutName(SupportV v) {
        return null;
    }

    @Override
    public void performInject(BindLayout bindLayout) {
    }

    @Override
    public void performInject(BindView bindView) {
    }

    @Override
    public void performInject(BindEvent bindEvent) {
    }
}
