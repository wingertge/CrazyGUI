package com.octagon.crazygui.idea.util;

import com.google.common.base.Defaults;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;

public class DefaultUtils {
    private static boolean aBoolean;
    private static byte aByte;
    private static short aShort;
    private static int anInt;
    private static long aLong;
    private static float aFloat;
    private static double aDouble;

    public static Object getDefaultValue(PsiType parameterType) {
        if(parameterType instanceof PsiPrimitiveType)
            return getPrimitiveDefault(parameterType);
        try {
            return Defaults.defaultValue(Class.forName(parameterType.getCanonicalText()));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Object getPrimitiveDefault(PsiType parameterType) {
        if(parameterType.equals(PsiPrimitiveType.BOOLEAN)) {
            return aBoolean;
        } else if(parameterType.equals(PsiPrimitiveType.BYTE)) {
            return aByte;
        } else if(parameterType.equals(PsiPrimitiveType.SHORT)) {
            return aShort;
        } else if(parameterType.equals(PsiPrimitiveType.INT)) {
            return anInt;
        } else if(parameterType.equals(PsiPrimitiveType.LONG)) {
            return aLong;
        } else if(parameterType.equals(PsiPrimitiveType.FLOAT)) {
            return aFloat;
        } else if(parameterType.equals(PsiPrimitiveType.DOUBLE)) {
            return aDouble;
        }
        return 0;
    }
}
