package com.rocui.util.base.reflect;

import com.rocui.test.TestOne;
import com.rocui.util.base.reflect.each.FieldAnnotaionEach;
import com.rocui.util.base.reflect.each.JannotaionEach;
import com.rocui.util.base.reflect.each.JconstructEach;
import com.rocui.util.base.reflect.each.JfieldEach;
import com.rocui.util.base.reflect.each.JmethodEach;
import com.rocui.util.base.reflect.each.MethodAnnotaionEach;
import com.rocui.util.base.reflect.test.Test;
import com.rocui.util.base.reflect.test.TestImp;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ObjectSnooper {

    private Object object;

    private ObjectSnooper(Object object) {
        this.object = object;
    }

    public static ObjectSnooper snoop(Object object) {
        return new ObjectSnooper(object);
    }

    public static ObjectSnooper snoop(String className) throws Exception {
        Object c = Class.forName(className).newInstance();
        return new ObjectSnooper(c);
    }

    public Object object() {
        return this.object;
    }

    public Object get(String fieldName) {
        Object result = null;
        try {
            Field field = this.object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            result = field.get(object);
        } catch (Exception ex) {
        }
        return result;
    }

    public Object getWithGetter(String fieldName) {
        Object result = null;
        try {
            Field field = this.object.getClass().getDeclaredField(fieldName);
            fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method k = this.object.getClass().getDeclaredMethod(fieldName);
            if (null != k) {
                field.setAccessible(true);
                result = field.get(object);
            }
        } catch (Exception ex) {
        }
        return result;
    }

    public Object gett(String fieldName) {
        Class<?> cla = this.object.getClass();
        Object result = null;
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                field.setAccessible(true);
                result = field.get(object);
                break;
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public Object gettWithGetter(String fieldName) {
        Class<?> cla = this.object.getClass();
        Object result = null;
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                fieldName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method k = this.object.getClass().getDeclaredMethod(fieldName);
                if (null != null) {
                    field.setAccessible(true);
                    result = field.get(object);
                    break;
                }
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public ObjectSnooper setWithSetter(String fieldName, String value) {
        try {
            Field field = this.object.getClass().getDeclaredField(fieldName);
            fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method k = this.object.getClass().getDeclaredMethod(fieldName, value.getClass());
            if (null != k) {
                field.setAccessible(true);
                field.set(object, this.getRightValue(field.getType(), value));
            }
        } catch (Exception ex) {
        }
        return this;
    }

    public ObjectSnooper set(String fieldName, String value) {
        try {
            Field field = this.object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, this.getRightValue(field.getType(), value));
        } catch (Exception ex) {
        }
        return this;
    }

    public ObjectSnooper set(String fieldName, Object value) {
        Field field;
        try {
            field = this.object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, this.getRightValue(field.getType(), value));
        } catch (Exception ex) {
        }
        return this;
    }

    public Object setWithSetter(String fieldName, Object value) {
        Object result = null;
        try {
            Field field = this.object.getClass().getDeclaredField(fieldName);
            fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method k = this.object.getClass().getDeclaredMethod(fieldName, value.getClass());
            if (null != k) {
                field.setAccessible(true);
                result = field.get(object);
            }
        } catch (Exception ex) {
        }
        return result;
    }

    public ObjectSnooper sett(String fieldName, Object value) {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, value);
                break;
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper settWithSetter(String fieldName, Object value) {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method k = this.object.getClass().getDeclaredMethod(fieldName, value.getClass());
                if (null != k) {
                    field.setAccessible(true);
                    field.set(object, value);
                    break;
                }
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper sett(String fieldName, String value) {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, this.getRightValue(field.getType(), value));
                break;
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper settWithSetter(String fieldName, String value) {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            try {
                Field field = cla.getDeclaredField(fieldName);
                fieldName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Method k = this.object.getClass().getDeclaredMethod(fieldName, value.getClass());
                if (null != k) {
                    field.setAccessible(true);
                    field.set(object, this.getRightValue(field.getType(), value));
                    break;
                }
            } catch (Exception ex) {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper set(HashMap<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.set(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper setWithSetter(HashMap<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.setWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper sset(HashMap<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.set(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper sett(HashMap<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.sett(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper settWithSetter(HashMap<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.settWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper ssett(HashMap<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.sett(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper ssettWithSetter(HashMap<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.settWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper set(Map<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.set(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper setWithSetter(Map<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.setWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper sset(Map<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.set(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper ssetWithSetter(Map<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.setWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper sett(Map<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.sett(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper settWithSetter(Map<String, Object> map) {
        for (Entry<String, Object> x : map.entrySet()) {
            this.settWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper ssett(Map<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.sett(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper ssettWithSetter(Map<String, String> map) {
        for (Entry<String, String> x : map.entrySet()) {
            this.settWithSetter(x.getKey(), x.getValue());
        }
        return this;
    }

    public ObjectSnooper fields(JfieldEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Field field : cla.getDeclaredFields()) {
            boolean isbreak = each.each(field);
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper fieldss(JfieldEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            boolean isok = true;
            for (Field field : cla.getDeclaredFields()) {
                boolean isbreak = each.each(field);
                if (isbreak) {
                    isok = false;
                    break;
                }
            }
            if (isok) {
                cla = cla.getSuperclass();
            } else {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper constructor(JconstructEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Constructor field : cla.getDeclaredConstructors()) {
            boolean isbreak = each.each(field);
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper constructors(JconstructEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            boolean isok = true;
            for (Constructor field : cla.getDeclaredConstructors()) {
                boolean isbreak = each.each(field);
                if (isbreak) {
                    isok = false;
                    break;
                }
            }
            if (isok) {
                cla = cla.getSuperclass();
            } else {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper methods(JmethodEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Method metho : cla.getDeclaredMethods()) {
            boolean isbreak = each.each(metho);
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper methodss(JmethodEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            boolean isok = true;
            for (Method metho : cla.getDeclaredMethods()) {
                boolean isbreak = each.each(metho);
                if (isbreak) {
                    isok = false;
                    break;
                }
            }
            if (isok) {
                cla = cla.getSuperclass();
            } else {
                break;
            }
        }
        return this;
    }

    public boolean hasFieldAnnotation(Class clazz) throws NoSuchFieldException {
        boolean result = false;
        for (Field field : this.object.getClass().getDeclaredFields()) {
            boolean isok = false;
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == clazz) {
                    result = true;
                    isok = true;
                    break;
                }
            }
            if (isok) {
                break;
            }
        }
        return result;
    }

    public boolean hasFieldAnnotations(Class clazz) throws NoSuchFieldException {
        boolean result = false;
        Class<?> cla = this.object.getClass();
        boolean issok = false;
        while (cla != null) {
            for (Field field : cla.getDeclaredFields()) {
                boolean isok = false;
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == clazz) {
                        result = true;
                        isok = true;
                        issok = true;
                        break;
                    }
                }
                if (isok) {
                    break;
                }
            }
            if (issok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public boolean hasFieldAnnotation(String fieldName, Class clazz) throws NoSuchFieldException {
        boolean result = false;
        Field field = this.object.getClass().getField(fieldName);
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == clazz) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean hasFieldAnnotations(String fieldName, Class clazz) throws NoSuchFieldException {
        boolean result = false;
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            Field field = cla.getClass().getField(fieldName);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == clazz) {
                    result = true;
                    isok = true;
                    break;
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public ObjectSnooper fieldAnnotations(FieldAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Field field : cla.getDeclaredFields()) {
            Annotation[] cs = field.getDeclaredAnnotations();
            for (Annotation c : cs) {
                boolean isbreak = each.each(field, c);
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotationss(FieldAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            for (Field field : cla.getDeclaredFields()) {
                Annotation[] cs = field.getDeclaredAnnotations();
                for (Annotation c : cs) {
                    boolean isbreak = each.each(field, c);
                    if (isbreak) {
                        isok = true;
                        break;
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotaions(Class clazz, FieldAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Field field : cla.getDeclaredFields()) {
            Annotation[] cs = field.getDeclaredAnnotations();
            for (Annotation c : cs) {
                if (c.annotationType() == clazz) {
                    boolean isbreak = each.each(field, c);
                    if (isbreak) {
                        break;
                    }
                }
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotationss(Class clazz, FieldAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            for (Field field : cla.getDeclaredFields()) {
                Annotation[] cs = field.getDeclaredAnnotations();
                for (Annotation c : cs) {
                    if (c.annotationType() == clazz) {
                        boolean isbreak = each.each(field, c);
                        if (isbreak) {
                            isok = true;
                            break;
                        }
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotation(String fieldName, Class clazz, FieldAnnotaionEach each) throws NoSuchFieldException, Exception {
        Field field = this.object.getClass().getField(fieldName);
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == clazz) {
                boolean isbreak = each.each(field, annotation);
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotations(String fieldName, Class clazz, FieldAnnotaionEach each) throws NoSuchFieldException, Exception {
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            Field field = cla.getClass().getField(fieldName);
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == clazz) {
                    boolean isbreak = each.each(field, annotation);
                    if (isbreak) {
                        isok = true;
                        break;
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotations(String fieldName, FieldAnnotaionEach each) throws Exception {
        Field field = this.object.getClass().getField(fieldName);
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            boolean isbreak = each.each(field, annotation);
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper fieldAnnotationss(String fieldName, FieldAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            Field field = cla.getField(fieldName);
            Annotation[] annotations = field.getAnnotations();
            boolean isok = true;
            for (Annotation annotation : annotations) {
                boolean isbreak = each.each(field, annotation);
                if (isbreak) {
                    isok = false;
                    break;
                }
            }
            if (isok) {
                cla = cla.getSuperclass();
            } else {
                break;
            }
        }
        return this;
    }

    public boolean hasMethodAnnotation(Class clazz) throws NoSuchFieldException {
        boolean result = false;
        for (Method method : this.object.getClass().getDeclaredMethods()) {
            boolean isok = false;
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == clazz) {
                    result = true;
                    isok = true;
                    break;
                }
            }
            if (isok) {
                break;
            }
        }
        return result;
    }

    public boolean hasMethodAnnotations(Class clazz) throws NoSuchFieldException {
        boolean result = false;
        Class<?> cla = this.object.getClass();
        boolean issok = false;
        while (cla != null) {
            for (Method field : cla.getDeclaredMethods()) {
                boolean isok = false;
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation.annotationType() == clazz) {
                        result = true;
                        isok = true;
                        issok = true;
                        break;
                    }
                }
                if (isok) {
                    break;
                }
            }
            if (issok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public ObjectSnooper mehodAnnotations(MethodAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Method field : cla.getDeclaredMethods()) {
            Annotation[] cs = field.getDeclaredAnnotations();
            for (Annotation c : cs) {
                boolean isbreak = each.each(field, c);
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public ObjectSnooper methodAnnotationss(MethodAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            for (Method field : cla.getDeclaredMethods()) {
                Annotation[] cs = field.getDeclaredAnnotations();
                for (Annotation c : cs) {
                    boolean isbreak = each.each(field, c);
                    if (isbreak) {
                        isok = true;
                        break;
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper methodAnnotaions(Class clazz, MethodAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Method field : cla.getDeclaredMethods()) {
            Annotation[] cs = field.getDeclaredAnnotations();
            for (Annotation c : cs) {
                if (c.annotationType() == clazz) {
                    boolean isbreak = each.each(field, c);
                    if (isbreak) {
                        break;
                    }
                }
            }
        }
        return this;
    }

    public ObjectSnooper methodAnnotationss(Class clazz, MethodAnnotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        boolean isok = false;
        while (cla != null) {
            for (Method field : cla.getDeclaredMethods()) {
                Annotation[] cs = field.getDeclaredAnnotations();
                for (Annotation c : cs) {
                    if (c.annotationType() == clazz) {
                        boolean isbreak = each.each(field, c);
                        if (isbreak) {
                            isok = true;
                            break;
                        }
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return this;
    }

    public ObjectSnooper methodAnnotation(String fieldName, Class clazz, FieldAnnotaionEach each) throws NoSuchFieldException, Exception {
        Field field = this.object.getClass().getField(fieldName);
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == clazz) {
                boolean isbreak = each.each(field, annotation);
                if (isbreak) {
                    break;
                }
            }
        }
        return this;
    }

    public ObjectSnooper annotations(JannotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        for (Annotation annotation : cla.getDeclaredAnnotations()) {
            boolean isbreak = each.each(annotation);
            if (isbreak) {
                break;
            }
        }
        return this;
    }

    public ObjectSnooper annotationss(JannotaionEach each) throws Exception {
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            boolean isok = true;
            for (Annotation annotation : cla.getDeclaredAnnotations()) {
                boolean isbreak = each.each(annotation);
                if (isbreak) {
                    isok = false;
                    break;
                }
            }
            if (isok) {
                cla = cla.getSuperclass();
            } else {
                break;
            }
        }
        return this;
    }

    public Annotation annotation(Class clazz) {
        Class<?> cla = this.object.getClass();
        if (cla.isAnnotationPresent(clazz)) {
            return cla.getAnnotation(clazz);
        } else {
            return null;
        }
    }

    public Annotation annotations(Class clazz) {
        Class<?> cla = this.object.getClass();
        Annotation c = null;
        while (cla != null) {
            if (cla.isAnnotationPresent(clazz)) {
                c = cla.getAnnotation(clazz);
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return c;
    }

    public Object proxy(JwrapMethod deal) {
        deal.set(this.object);
        return Proxy.newProxyInstance(this.object.getClass().getClassLoader(), this.object.getClass().getInterfaces(), deal);
    }

    public Object proxy(InvocationHandler deal, String objectName) {
        ObjectSnooper.snoop(deal).set(objectName, this.object);
        return Proxy.newProxyInstance(this.object.getClass().getClassLoader(), this.object.getClass().getInterfaces(), deal);
    }

    public Object proxy(BaseProxy deal) {
        deal.setAgent(this.object);
        return Proxy.newProxyInstance(this.object.getClass().getClassLoader(), this.object.getClass().getInterfaces(), deal);
    }

    private Object getRightValue(Class parType, Object value) {
        String type = parType.getName();
        System.out.println("==="+type);
        System.out.println("==="+value);
        if (value != null) {
            if (type.equals("String")||type.equals("java.lang.String")) {
                return value.toString();
            } else if (type.equals("long")||type.equals("java.lang.Long")) {
                return Long.valueOf(value.toString());
            } else if (type.equals("int")||type.equals("java.lang.Integer")) {
                return Integer.valueOf(value.toString());
            } else if (type.equals("float")||type.equals("java.lang.Float")) {
                return Float.parseFloat(value.toString());
            } else if (type.equals("boolean")||type.equals("java.lang.Boolean")) {
                return Boolean.parseBoolean(value.toString());
            } else if (type.equals("short")||type.equals("java.lang.Short")) {
                return Short.parseShort(value.toString());
            } else {
                return parType.cast(value);
            }
        } else {
            return null;
        }
    }

    public Object trigger(Object... args) throws Exception {
        if (null != args && args.length > 0) {
            String methodName = args[0].toString();
            Object result = null;
            Class<?> cla = this.object.getClass();
            Method[] methods = cla.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] pars = method.getParameterTypes();
                    int length = args.length - 1;
                    System.out.println("===" + pars.length + "----" + length);
                    if (pars.length == length) {
                        try {
                            method.setAccessible(true);
                            if (args.length > 1) {
                                Object[] c = new Object[args.length - 1];
                                for (int i = 1; i < args.length; i++) {
                                    c[i - 1] = args[i];
                                }
                                result = method.invoke(object, c);
                            } else {
                                result = method.invoke(object);
                            }
                            break;
                        } catch (Exception e) {
                            throw e;
                        }
                    }
                }
            }
            return result;
        } else {
            return null;
        }
    }

    public Object triggerr(String methodName, Object... args) {
        Object result = null;
        Class<?> cla = this.object.getClass();
        while (cla != null) {
            Method[] methods = cla.getDeclaredMethods();
            boolean isok = false;
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class<?>[] pars = method.getParameterTypes();
                    int length;
                    if (args == null) {
                        length = 1;
                    } else {
                        length = args.length;
                    }
                    if (pars.length == length) {
                        try {
                            method.setAccessible(true);
                            result = method.invoke(object, args);
                            isok = true;
                            break;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (isok) {
                break;
            } else {
                cla = cla.getSuperclass();
            }
        }
        return result;
    }

    public Object[] toArray() {
        Class<?> cla = this.object.getClass();
        List<Object> list = new ArrayList<Object>();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                list.add(field.get(object));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list.toArray();
    }

    public HashMap<String, Object> toHashMap() {
        Class<?> cla = this.object.getClass();
        HashMap<String, Object> map = new HashMap<String, Object>();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object x;
            try {
                x = field.get(object);
            } catch (Exception ex) {
                x = null;
            }
            map.put(field.getName(), x);
        }
        return map;
    }

    public HashMap<String, Object> toHashMapp() {
        Class<?> cla = this.object.getClass();
        HashMap<String, Object> map = new HashMap<String, Object>();
        while (cla != null) {
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object x;
                try {
                    x = field.get(object);
                } catch (Exception ex) {
                    x = null;
                }
                map.put(field.getName(), x);
            }
            cla = cla.getSuperclass();
        }
        return map;
    }

    public Map<String, Object> toMap() {
        Class<?> cla = this.object.getClass();
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object x;
            try {
                x = field.get(object);
            } catch (Exception ex) {
                x = null;
            }
            map.put(field.getName(), x);
        }
        return map;
    }

    public Map<String, Object> toMapp() {
        Class<?> cla = this.object.getClass();
        Map<String, Object> map = new HashMap<String, Object>();
        while (cla != null) {
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object x;
                try {
                    x = field.get(object);
                } catch (Exception ex) {
                    x = null;
                }
                map.put(field.getName(), x);
            }
            cla = cla.getSuperclass();
        }
        return map;
    }

    public <T> T instance() {
        if (null != this.object) {
            return (T) this.object;
        } else {
            return null;
        }
    }

    public <T> T newInstance() {
        if (null != this.object) {
            try {
                return (T) this.object.getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
//        TestOne one = new TestOne();
//        ObjectSnooper.snoop(one).set("kk", null).sett("two", "nnnnnn").methods(new JmethodEach() {
//            @Override
//            public boolean each(Method method) {
//                System.out.println("-----methid>>>" + method.getName());
//                return false;
//            }
//        });
//        System.out.println(one.getKk());
//        System.out.println(one.getTwo());
//        String cd = null;
//        int cdd = 0;
//        Object c = ObjectSnooper.snoop(one).trigger("getKk");
//        System.out.println(c);
        Test test = (Test) ObjectSnooper.snoop(new TestImp()).proxy(new JwrapMethod() {
            @Override
            public void before() {
                System.out.println("----before---");
            }

            @Override
            public Object after(Object result) {
                System.out.println("----after---");
                return null;
            }
        });
        test.test();
    }
}
