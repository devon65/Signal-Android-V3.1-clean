package org.thoughtcrime.securesms;

public class IsMITMAttackOn {
    private static boolean isAttackOn = false;
    private static boolean isTextSent = false;
    private static boolean isSafetyNumberChanged = false;

    public boolean isAttackOn() {
        return isAttackOn;
    }

    public static boolean isTextSent() {
        return isTextSent;
    }

    public static boolean isSafetyNumberChanged() {
        return isSafetyNumberChanged;
    }

    public static void setIsSafetyNumberChanged(boolean isSafetyNumberChanged) {
        IsMITMAttackOn.isSafetyNumberChanged = isSafetyNumberChanged;
    }

    public static void setIsTextSent(boolean isTextSent) {
        IsMITMAttackOn.isTextSent = isTextSent;
    }

    public void setIsAttackOn(boolean attackOn) {
        isAttackOn = attackOn;
    }
}
