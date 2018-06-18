package org.thoughtcrime.securesms;

import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;

public class IsMITMAttackOn {

    public IsMITMAttackOn(){

        if (this.fakeKey == null) {
            ECKeyPair ourKeyPair = Curve.generateKeyPair();
            this.fakeKey = new IdentityKey(ourKeyPair.getPublicKey());
        }
    }

    private static boolean isAttackOn = false;
    private static boolean isTextSent = false;
    private static boolean isSafetyNumberChanged = false;

    private static IdentityKey fakeKey = null;

    public boolean isAttackOn() {
        return isAttackOn;
    }

    public static boolean isTextSent() {
        return isTextSent;
    }

    public static boolean isSafetyNumberChanged() {
        return isSafetyNumberChanged;
    }

    public static IdentityKey getFakeKey() {
        return fakeKey;
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
