package org.thoughtcrime.securesms;

import android.content.Context;
import android.content.SharedPreferences;

import org.thoughtcrime.securesms.backup.BackupProtos;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;

public class IsMITMAttackOn {

    private static final String attackBoolString = "attackBoolean";
    private static final String safetyNumberBoolString = "safetyNumberBoolean";

    private static boolean attackOn = false;
    private static boolean safetyNumberChanged = false;

    private static SharedPreferences sharedPref = null;
    private static SharedPreferences.Editor prefEditor = null;

    private static IdentityKey fakeKey = null;


    public IsMITMAttackOn(Context context){

        this.sharedPref = context.getSharedPreferences(
                "preferencesMITM", Context.MODE_PRIVATE);
        this.prefEditor = sharedPref.edit();

        initializeBooleans();

        if (this.fakeKey == null) {
            ECKeyPair ourKeyPair = Curve.generateKeyPair();
            this.fakeKey = new IdentityKey(ourKeyPair.getPublicKey());
        }
    }


    private void initializeBooleans() {
        this.attackOn = sharedPref.getBoolean(attackBoolString, false);
        this.safetyNumberChanged = sharedPref.getBoolean(safetyNumberBoolString, false);
    }

    public boolean isAttackOn() {
        return attackOn;
    }

    public static boolean isSafetyNumberChanged() {
        return safetyNumberChanged;
    }

    public static IdentityKey getFakeKey() {
        return fakeKey;
    }

    public static void setIsSafetyNumberChanged(boolean isSafetyNumberChanged) {
        IsMITMAttackOn.safetyNumberChanged = isSafetyNumberChanged;

        prefEditor.putBoolean(safetyNumberBoolString, isSafetyNumberChanged);
    }

    public static void setIsAttackOn(boolean isAttackOn) {
        IsMITMAttackOn.attackOn = isAttackOn;

        prefEditor.putBoolean(attackBoolString, attackOn);
    }
}
