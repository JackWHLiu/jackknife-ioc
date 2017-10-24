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

package com.lwh.jackknife.ioc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwh.jackknife.ioc.exception.IllegalViewClassNameException;
import com.lwh.jackknife.ioc.exception.ViewTypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewInjector<V extends SupportV> {

    private final String METHOD_SET_CONTENT_VIEW = "setContentView";
    private final String METHOD_FIND_VIEW_BY_ID = "findViewById";
    private final String METHOD_INFLATE = "inflate";
    private final String METHOD_VALUE = "value";
    private final String UNDERLINE = "_";
    private final String ID = ".R$id";
    private final String LAYOUT = ".R$layout";
    private final String VIEW_TYPE_ERROR = "viewInjected must be an activity or a fragment.";
    private final String VIEW_CLASS_NAME_ERROR = "class name is not ends with \'Activity\' or \'Fragment\'.";
    private final int A = 'A';
    private final int Z = 'Z';

    enum ViewType {
        Activity,
        Fragment,
        UNDECLARED
    }

    private ViewInjector() {
    }

    public static ViewInjector create() {
        return new ViewInjector();
    }

    public void inject(V viewInjected) throws InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException, NoSuchFieldException,
            IllegalAccessException {
        if (viewInjected instanceof SupportActivity) {
            injectLayout(viewInjected);
            injectViews(viewInjected);
            injectEvents(viewInjected);
        } else if (viewInjected instanceof SupportFragment) {
            injectViews(viewInjected);
            injectEvents(viewInjected);
        }
    }

    protected V getActivity(V viewInjected) {
        ViewType viewType = getViewType(viewInjected);
        if (viewType == ViewType.Fragment) {
            viewInjected = (V) ((SupportFragment) viewInjected).getFragmentActivity();
        }else if (viewType == ViewType.UNDECLARED){
            throw new ViewTypeException(VIEW_TYPE_ERROR);
        }
        return viewInjected;
    }

    protected String generateLayoutName(V viewInjected) {
        ViewType viewType = getViewType(viewInjected);
        String suffix = ViewType.Activity.name();
        if (viewType == ViewType.Fragment) {
            suffix = viewType.name();
        }
        StringBuffer sb;
        String layoutName = viewInjected.getClass().getSimpleName();
        if (!layoutName.endsWith(suffix)) {
            throw new IllegalViewClassNameException(VIEW_CLASS_NAME_ERROR);
        } else {
            String name = layoutName.substring(0, layoutName.length() - suffix.length());
            sb = new StringBuffer(suffix.toLowerCase(Locale.ENGLISH));
            for (int i = 0; i < name.length(); i++) {
                if (name.charAt(i) >= A && name.charAt(i) <= Z || i == 0) {
                    sb.append(UNDERLINE);
                }
                sb.append(String.valueOf(name.charAt(i)).toLowerCase(Locale.ENGLISH));
            }
        }
        return sb.toString();
    }

    protected ViewType getViewType(V viewInjected) {
        Class<V> viewClass = (Class<V>) viewInjected.getClass();
        if (SupportActivity.class.isAssignableFrom(viewClass)) {
            return ViewType.Activity;
        } else if (SupportFragment.class.isAssignableFrom(viewClass)) {
            return ViewType.Fragment;
        } else {
            return ViewType.UNDECLARED;
        }
    }

    protected boolean isViewTypeAllowed(ViewType viewType){
        if (viewType != ViewType.UNDECLARED){
            return true;
        }
        return false;
    }

    public final View injectLayout(V viewInjected) throws NoSuchMethodException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException, NoSuchFieldException {
        ViewType viewType = getViewType(viewInjected);
        if (isViewTypeAllowed(viewType)) {
            String layoutName = generateLayoutName(viewInjected);
            ContentView contentView = viewInjected.getClass().getAnnotation(ContentView.class);
            viewInjected = getActivity(viewInjected);
            Class<?> viewClass = viewInjected.getClass();
            String packageName = ((SupportActivity) viewInjected).getPackageName();
            Class<?> layoutClass = Class.forName(packageName + LAYOUT);
            Field field = layoutClass.getDeclaredField(layoutName);
            int layoutId = field.getInt(viewInjected);
            if (contentView != null) {
                layoutId = contentView.value();
            }
            if (viewType == ViewType.Activity) {
                Method method = viewClass.getMethod(METHOD_SET_CONTENT_VIEW, int.class);
                method.invoke(viewInjected, layoutId);
            }
            LayoutInflater inflater = LayoutInflater.from((Context) viewInjected);
            Class<? extends LayoutInflater> inflaterClass = LayoutInflater.class;
            Method inflateMethod = inflaterClass.getDeclaredMethod(METHOD_INFLATE, int.class,
                    ViewGroup.class);
            return (View) inflateMethod.invoke(inflater, layoutId, null);
        }else{
            throw new ViewTypeException(VIEW_TYPE_ERROR);
        }
    }

    public final void injectViews(V viewInjected) throws NoSuchMethodException,
            ClassNotFoundException, InvocationTargetException, IllegalAccessException,
            NoSuchFieldException {
        ViewType viewType = getViewType(viewInjected);
        if (isViewTypeAllowed(viewType)) {
            Class<?> viewClass = viewInjected.getClass();
            Field[] viewFields = viewClass.getDeclaredFields();
            SupportActivity activity = (SupportActivity) getActivity(viewInjected);
            Class<? extends SupportActivity> activityClass = activity.getClass();
            for (Field field : viewFields) {
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (View.class.isAssignableFrom(fieldType)) {
                    ViewIgnore viewIgnore = field.getAnnotation(ViewIgnore.class);
                    if (viewIgnore != null) {
                        continue;
                    }
                    ViewId viewId = field.getAnnotation(ViewId.class);
                    int id = View.NO_ID;
                    if (viewId != null) {
                        id = viewId.value();
                    } else {
                        String packageName = activity.getPackageName();
                        Class<?> idClass = Class.forName(packageName + ID);
                        Field idField = idClass.getDeclaredField(field.getName());
                        try {
                            id = idField.getInt(idField);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                    Method findViewByIdMethod = activityClass.getMethod(METHOD_FIND_VIEW_BY_ID,
                            int.class);
                    Object view = findViewByIdMethod.invoke(activity, id);
                    if (view != null) {
                        field.set(viewInjected, view);
                    }
                }
            }
        }else{
            throw new ViewTypeException(VIEW_TYPE_ERROR);
        }
    }

    public final void injectEvents(V viewInjected)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        ViewType viewType = getViewType(viewInjected);
        if (isViewTypeAllowed(viewType)) {
            Class<?> viewClass = viewInjected.getClass();
            Method[] methods = viewClass.getDeclaredMethods();
            SupportActivity activity = (SupportActivity) getActivity(viewInjected);
            for (Method method : methods) {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    Class<? extends Annotation> annotationType = annotation.annotationType();
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase == null) {
                        continue;
                    }
                    String listenerSetter = eventBase.listenerSetter();
                    Class<?> listenerType = eventBase.listenerType();
                    String callbackMethod = eventBase.callbackMethod();
                    Method valueMethod = annotationType.getDeclaredMethod(METHOD_VALUE);
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    for (int viewId : viewIds) {
                        View view = activity.findViewById(viewId);
                        if (view == null) {
                            continue;
                        }
                        Method setListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        HashMap<String, Method> map = new HashMap();
                        map.put(callbackMethod, method);
                        EventInvocationHandler handler = new EventInvocationHandler(map,
                                viewInjected);
                        Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class<?>[]{listenerType}, handler);
                        setListenerMethod.invoke(view, proxy);
                    }
                }
            }
        }else{
            throw new ViewTypeException(VIEW_TYPE_ERROR);
        }
    }

    private class EventInvocationHandler implements InvocationHandler {

        private Map<String, Method> mCallbackMethodMap;
        private V mViewInjected;

        public EventInvocationHandler(HashMap<String, Method> callbackMethodMap, V view) {
            this.mCallbackMethodMap = callbackMethodMap;
            this.mViewInjected = view;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            Method callbackMethod = mCallbackMethodMap.get(name);
            if (callbackMethod != null) {
                return callbackMethod.invoke(mViewInjected, args);
            }
            return method.invoke(proxy, args);
        }
    }
}
