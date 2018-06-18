//Elham code starts here
//Creates connection to Man in the Middle Attack Server
//if server is not on, it will wait 10 seconds and try again
//It also reconnects after receiving the on signal and disconnecting
package org.thoughtcrime.securesms;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import org.thoughtcrime.securesms.crypto.IdentityKeyUtil;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.crypto.MasterSecretUtil;
import org.thoughtcrime.securesms.crypto.storage.TextSecureIdentityKeyStore;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.IdentityDatabase;
import org.thoughtcrime.securesms.database.MessagingDatabase.InsertResult;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.notifications.MessageNotifier;
import org.thoughtcrime.securesms.sms.IncomingIdentityUpdateMessage;
import org.thoughtcrime.securesms.sms.IncomingTextMessage;
import org.thoughtcrime.securesms.util.TextSecurePreferences;
import org.thoughtcrime.securesms.util.Util;
import org.thoughtcrime.securesms.util.VersionTracker;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.util.guava.Optional;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static org.whispersystems.libsignal.SessionCipher.SESSION_LOCK;

public class ManInMiddleService extends IntentService {

    private IsMITMAttackOn isMITMAttackOn = new IsMITMAttackOn(ManInMiddleService.this);
    private static final String serverHost = "192.168.0.110";

    public ManInMiddleService() {
        super("ManInMiddleService");
    }

    //Tries to connect to the server every ten seconds until successful
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while(true){
            connectToServerLoop();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Connects to server multiple times
    private void connectToServerLoop(){
        Socket clientSocket;

        try {
            clientSocket = new Socket(serverHost, 3000);

            while (true) {
                if(clientSocket == null){
                    clientSocket = new Socket(serverHost, 3000);
                }
                int isMiddleManAttackOn = 0;
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                isMiddleManAttackOn = dataInputStream.readByte();

                if (isMiddleManAttackOn == 1) {
                    this.isMITMAttackOn.setIsAttackOn(true);
                    this.isMITMAttackOn.setIsSafetyNumberChanged(true);
                    clientSocket.close();
                    clientSocket = null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //Connects to server multiple times
    private void connectToServerSingle(){
        Socket clientSocket;

        try {
            clientSocket = new Socket(serverHost, 3000);

            while (!isMITMAttackOn.isAttackOn()) {
                int isMiddleManAttackOn = 0;
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                isMiddleManAttackOn = dataInputStream.readByte();

                if (isMiddleManAttackOn == 1) {
                    this.isMITMAttackOn.setIsAttackOn(true);
                    this.isMITMAttackOn.setIsSafetyNumberChanged(true);
                    clientSocket.close();
                    clientSocket = null;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
//Elham code ends here