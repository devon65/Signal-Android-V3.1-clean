package org.thoughtcrime.securesms;

public class IsMITMAttackOn {
    private static boolean isAttackOn = false;
    private static boolean isTextSent = true;

    public boolean isAttackOn() {
        return isAttackOn;
    }

    public static boolean isIsTextSent() {
        return isTextSent;
    }

    public static void setIsTextSent(boolean isTextSent) {
        IsMITMAttackOn.isTextSent = isTextSent;
    }

    public void setAttackOn(boolean attackOn) {
        isAttackOn = attackOn;
    }
}
