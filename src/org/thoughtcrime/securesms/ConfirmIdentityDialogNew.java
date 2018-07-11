//Devon newAuth code starts: Most of this file was taken from ConfirmIdentityDialog.java, but
//it was more convenient to copy over the code and make the major revisions. This will replace
//ConfirmIdentityDialog.java for the new version of the Authentication Ceremony

package org.thoughtcrime.securesms;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import org.thoughtcrime.securesms.crypto.IdentityKeyParcelable;
import org.thoughtcrime.securesms.crypto.storage.TextSecureIdentityKeyStore;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.IdentityDatabase;
import org.thoughtcrime.securesms.database.MmsDatabase;
import org.thoughtcrime.securesms.database.MmsSmsDatabase;
import org.thoughtcrime.securesms.database.PushDatabase;
import org.thoughtcrime.securesms.database.SmsDatabase;
import org.thoughtcrime.securesms.database.documents.IdentityKeyMismatch;
import org.thoughtcrime.securesms.jobs.PushDecryptJob;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.sms.MessageSender;
import org.thoughtcrime.securesms.util.Base64;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;
import org.whispersystems.signalservice.internal.push.SignalServiceProtos;

import java.io.IOException;

import static org.whispersystems.libsignal.SessionCipher.SESSION_LOCK;

public class ConfirmIdentityDialogNew extends AlertDialog {

    @SuppressWarnings("unused")
    private static final String TAG = ConfirmIdentityDialogNew.class.getSimpleName();

    private OnClickListener callback;

    public ConfirmIdentityDialogNew(Context context,
                                 Long threadId,
                                 IdentityKeyMismatch mismatch)
    {
        super(context);

        Recipient recipient       = Recipient.from(context, mismatch.getAddress(), false);
        String          name            = recipient.toShortString();
        String          introduction    = String.format(context.getString(R.string.ConfirmIdentityDialog_your_safety_number_with_s_has_changed), name, name);

        SpannableString spannableString = new SpannableString(introduction);

        setTitle(name);
        setMessage(spannableString);

        setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.ConfirmIdentityDialog_accept), new ConfirmIdentityDialogNew.AcceptListener(threadId, mismatch, recipient.getAddress()));
        setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.ConfirmIdentityDialog_verify_later),               new ConfirmIdentityDialogNew.CancelListener());
    }

    @Override
    public void show() {
        super.show();
        ((TextView)this.findViewById(android.R.id.message))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setCallback(OnClickListener callback) {
        this.callback = callback;
    }

    private class AcceptListener implements OnClickListener {

        private final long                  threadId;
        private final IdentityKeyMismatch   mismatch;
        private final Address               address;
        private final IdentityKey           identityKey;

        private AcceptListener(Long threadId, IdentityKeyMismatch mismatch, Address address) {
            this.threadId = threadId;
            this.mismatch      = mismatch;
            this.address       = address;
            this.identityKey   = mismatch.getIdentityKey();
        }

        @SuppressLint("StaticFieldLeak")
        @Override
        public void onClick(DialogInterface dialog, int which) {

            Intent intent = new Intent(getContext(), VerifyIdentityActivity.class);
            intent.putExtra(VerifyIdentityActivity.ADDRESS_EXTRA, address);
            intent.putExtra(VerifyIdentityActivity.IDENTITY_EXTRA, new IdentityKeyParcelable(identityKey));

            //Devon code starts: changed the boolean that is passed to the VerifyIdentityActivity so that it
            //reflects the current verifiedStatus of the given identityRecord. This is necessary for users
            //who may click on the back button and then go forward again to make sure the verification
            //actually worked

            intent.putExtra(VerifyIdentityActivity.THREAD_ID_EXTRA, this.threadId);

            //intent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, false);
            Optional<IdentityDatabase.IdentityRecord> identityRecord = DatabaseFactory.getIdentityDatabase(getContext()).getIdentity(address);
            intent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, identityRecord.get().getVerifiedStatus() == IdentityDatabase.VerifiedStatus.VERIFIED);

            //Devon code ends

            getContext().startActivity(intent);

            if (callback != null) callback.onClick(null, 0);
        }
    }

    private class CancelListener implements OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (callback != null) callback.onClick(null, 0);
        }
    }

}

//Devon code ends