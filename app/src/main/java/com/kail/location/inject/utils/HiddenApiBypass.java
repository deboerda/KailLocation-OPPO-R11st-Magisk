package com.kail.location.inject.utils;

import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HiddenApiBypass {
    public static Method classForNameMethod;
    public static Method classGetDeclaredMethod;
    static Class vmRuntimeClass;
    static Method setHiddenApiExemptionsMethod;
    static Object vmRuntime;

    static {
        try {
            classGetDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Method declaredMethod = Class.class.getDeclaredMethod("forName", String.class);
            classForNameMethod = declaredMethod;
            Class cls = (Class) declaredMethod.invoke(null, "dalvik.system.VMRuntime");
            vmRuntimeClass = cls;
            setHiddenApiExemptionsMethod = (Method) classGetDeclaredMethod.invoke(cls, "setHiddenApiExemptions", new Class[]{String[].class});
            vmRuntime = ((Method) classGetDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null)).invoke(null, new Object[0]);
        } catch (Exception e) {
            InjectLog.e("HiddenApiBypass", "static init failed: error getting VMRuntime methods", e);
        }
    }

    public static void setHiddenApiExemptions(String... signaturePrefixes) throws IllegalAccessException, InvocationTargetException {
        setHiddenApiExemptionsMethod.invoke(vmRuntime, signaturePrefixes);
    }

    public static boolean bypassHiddenApiRestrictions() {
        if (Build.VERSION.SDK_INT < 28) {
            return true;
        }
        try {
            setHiddenApiExemptions("Landroid/", "Lcom/android/", "Ljava/lang/", "Ldalvik/system/", "Llibcore/io/");
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
