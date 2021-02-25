package org.privately.chat.notifications;

import android.content.Context;
import androidx.annotation.NonNull;

import org.privately.chat.database.RecipientDatabase;
import org.privately.chat.recipients.Recipient;
import org.privately.chat.util.TextSecurePreferences;

public enum ReplyMethod {

  GroupMessage,
  SecureMessage,
  UnsecuredSmsMessage;

  public static @NonNull ReplyMethod forRecipient(Context context, Recipient recipient) {
    if (recipient.isGroup()) {
      return ReplyMethod.GroupMessage;
    } else if (TextSecurePreferences.isPushRegistered(context) && recipient.getRegistered() == RecipientDatabase.RegisteredState.REGISTERED && !recipient.isForceSmsSelection()) {
      return ReplyMethod.SecureMessage;
    } else {
      return ReplyMethod.UnsecuredSmsMessage;
    }
  }
}
