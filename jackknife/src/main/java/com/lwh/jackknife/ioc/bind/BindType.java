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

package com.lwh.jackknife.ioc.bind;

import com.lwh.jackknife.ioc.SupportV;
import com.lwh.jackknife.ioc.inject.InjectHandler;

/**
 * Abstraction of all IOC injection functions.
 */
public interface BindType {

    /**
     * Accepts a visitor.
     */
    void accept(InjectHandler handler);

    /**
     * Gets IOC environment.
     */
    SupportV getTarget();
}
