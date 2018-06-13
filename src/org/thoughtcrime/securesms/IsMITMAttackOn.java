package org.thoughtcrime.securesms;

public class IsMITMAttackOn {
    private static boolean isAttackOn = false;

    public boolean isAttackOn() {
        return isAttackOn;
    }

    public void setAttackOn(boolean attackOn) {
        isAttackOn = attackOn;
    }
}
