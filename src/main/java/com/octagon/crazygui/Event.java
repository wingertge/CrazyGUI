package com.octagon.crazygui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Event<T> {
    private List<T> listeners = new ArrayList<>();

    public void subscribe(T listener) {
        listeners.add(listener);
    }

    public void unsubscribe(T listener) {
        listeners.remove(listener);
    }

    public void fire(String methodName, Object... params) {
        Class<?>[] classes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            classes[i] = params[i].getClass();
        }
        for(T listener : listeners) {
            try {
                Method method = listener.getClass().getMethod(methodName, classes);
                method.setAccessible(true);
                method.invoke(listener, params);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                //Ignore listener
            }
        }
    }
}
