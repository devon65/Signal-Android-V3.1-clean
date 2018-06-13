//Elham code starts here
//Creates connection to Man in the Middle Attack Server
package org.thoughtcrime.securesms;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class ManInMiddleService extends IntentService {

    IsMITMAttackOn isMITMAttackOn = new IsMITMAttackOn();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     **/
    public ManInMiddleService() {
        super("ManInMiddleService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Socket clientSocket;

        try {
            clientSocket = new Socket("192.168.0.110", 3000);

            while (!isMITMAttackOn.isAttackOn()) {
                int isMiddleManAttackOn = 0;
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                isMiddleManAttackOn = dataInputStream.readByte();

                if (isMiddleManAttackOn == 1) {
                    this.isMITMAttackOn.setAttackOn(true);
                    //resetLocalKey();
                    //setAllRecordsToUnverified();
                    /*IdentityKeyUtil keyCreator = new IdentityKeyUtil();
                    keyCreator.generateIdentityKeys(ManInMiddleService.this);*/
                    //IdentityKeyUtil.generateIdentityKeys(this);
                    clientSocket.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setAllRecordsToUnverified() {
        final long             time             = System.currentTimeMillis();
        final SmsDatabase smsDatabase      = DatabaseFactory.getSmsDatabase(ManInMiddleService.this);
        final IdentityDatabase           identityDatabase = DatabaseFactory.getIdentityDatabase(ManInMiddleService.this);
        final TextSecureIdentityKeyStore identityKeyStore = new TextSecureIdentityKeyStore(ManInMiddleService.this);

        // Grab every contact in the database
        IdentityDatabase.IdentityReader identityReader = identityDatabase.readerFor(identityDatabase.getIdentities());
        ArrayList<IdentityDatabase.IdentityRecord> allContacts = new ArrayList<>();
        if (identityReader != null) {
            while (true) {
                IdentityDatabase.IdentityRecord nextContact = identityReader.getNext();
                if (nextContact == null) break;
                allContacts.add(nextContact);
            }
        }

        synchronized (SESSION_LOCK) {
            for (IdentityDatabase.IdentityRecord identityRecord : allContacts) {
                // TODO IRL now we are getting the "your safety number with [contact] has changed" twice

                // Change contact to be unverified (copied and modified from ConversationActivity.java)
                /*Note: even though we change the security key below, the contact must be marked
                as VERIFIED or UNVERIFIED in order to bring about all notifications*/
                identityDatabase.setVerified(identityRecord.getAddress(),
                        identityRecord.getIdentityKey(),
                        IdentityDatabase.VerifiedStatus.UNVERIFIED);



                // Change identity key
//                ECKeyPair ourKeyPair = Curve.generateKeyPair();
//                IdentityKey ik = new IdentityKey(ourKeyPair.getPublicKey());
//                identityKeyStore.saveIdentity(new SignalProtocolAddress(identityRecord.getAddress().serialize(), 1), ik, true);
                // TODO IRL further experimenting on this to make sure it's working. Did closing the app reset the keys?

                // I think if we can change the keys for our own phone, it will better simulate the MITM attack
                // Second text warning is called through the ConversationItem.setBodyText

                IncomingTextMessage           incoming         = new IncomingTextMessage(identityRecord.getAddress(), 1, time, null, Optional.absent(), 0);
                IncomingIdentityUpdateMessage individualUpdate = new IncomingIdentityUpdateMessage(incoming);
                Optional<InsertResult>        insertResult     = smsDatabase.insertMessageInbox(individualUpdate);

                if (insertResult.isPresent()) {
                    MessageNotifier.updateNotification(ManInMiddleService.this, insertResult.get().getThreadId());
                }


            }
        }


    }

    private void resetLocalKey() {
        MasterSecret masterSecret;
        PassphraseActivity passphraseActivity = new PassphraseActivity() {
            @Override
            protected void cleanup() {
            }
        };

        String passphrase = "";
        masterSecret = MasterSecretUtil.generateMasterSecret(ManInMiddleService.this,
                passphrase);

        MasterSecretUtil.generateAsymmetricMasterSecret(ManInMiddleService.this, masterSecret);
        IdentityKeyUtil.generateIdentityKeys(ManInMiddleService.this);
        VersionTracker.updateLastSeenVersion(ManInMiddleService.this);

        TextSecurePreferences.setLastExperienceVersionCode(ManInMiddleService.this, Util.getCurrentApkReleaseVersion(ManInMiddleService.this));
        TextSecurePreferences.setPasswordDisabled(ManInMiddleService.this, true);
        TextSecurePreferences.setReadReceiptsEnabled(ManInMiddleService.this, true);

        passphraseActivity.setMasterSecret(masterSecret);
    }

    private class SecretGenerator extends AsyncTask<String, Void, Void>{
        private MasterSecret masterSecret;
        PassphraseActivity passphraseActivity = new PassphraseActivity() {
            @Override
            protected void cleanup() {}
        };

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {

            String passphrase = params[0];
            masterSecret      = MasterSecretUtil.generateMasterSecret(ManInMiddleService.this,
                    passphrase);

            MasterSecretUtil.generateAsymmetricMasterSecret(ManInMiddleService.this, masterSecret);
            IdentityKeyUtil.generateIdentityKeys(ManInMiddleService.this);
            VersionTracker.updateLastSeenVersion(ManInMiddleService.this);

            TextSecurePreferences.setLastExperienceVersionCode(ManInMiddleService.this, Util.getCurrentApkReleaseVersion(ManInMiddleService.this));
            TextSecurePreferences.setPasswordDisabled(ManInMiddleService.this, true);
            TextSecurePreferences.setReadReceiptsEnabled(ManInMiddleService.this, true);

            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            passphraseActivity.setMasterSecret(masterSecret);
        }
    }
}
//Elham code ends here