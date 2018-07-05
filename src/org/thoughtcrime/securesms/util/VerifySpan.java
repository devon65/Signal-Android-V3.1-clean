package org.thoughtcrime.securesms.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.style.ClickableSpan;
import android.view.View;

import org.thoughtcrime.securesms.VerifyIdentityActivity;
import org.thoughtcrime.securesms.crypto.IdentityKeyParcelable;
import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.IdentityDatabase;
import org.thoughtcrime.securesms.database.documents.IdentityKeyMismatch;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.util.guava.Optional;

public class VerifySpan extends ClickableSpan {

  private final Context     context;
  private final Address     address;
  private final IdentityKey identityKey;

  public VerifySpan(@NonNull Context context, @NonNull IdentityKeyMismatch mismatch) {
    this.context     = context;
    this.address     = mismatch.getAddress();
    this.identityKey = mismatch.getIdentityKey();
  }

  public VerifySpan(@NonNull Context context, @NonNull Address address, @NonNull IdentityKey identityKey) {
    this.context     = context;
    this.address     = address;
    this.identityKey = identityKey;
  }

  @Override
  public void onClick(View widget) {
    Intent intent = new Intent(context, VerifyIdentityActivity.class);
    intent.putExtra(VerifyIdentityActivity.ADDRESS_EXTRA, address);
    intent.putExtra(VerifyIdentityActivity.IDENTITY_EXTRA, new IdentityKeyParcelable(identityKey));

    //Devon code starts: changed the boolean that is passed to the VerifyIdentityActivity so that it
    //reflects the current verifiedStatus of the given identityRecord. This is necessary for users
    //who may click on the back button and then go forward again to make sure the verification
    //actually worked

    intent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, false);
    //Optional<IdentityDatabase.IdentityRecord> identityRecord = DatabaseFactory.getIdentityDatabase(context).getIdentity(address);
    //intent.putExtra(VerifyIdentityActivity.VERIFIED_EXTRA, identityRecord.get().getVerifiedStatus() == IdentityDatabase.VerifiedStatus.VERIFIED);

    //Devon code ends

    context.startActivity(intent);
  }
}
