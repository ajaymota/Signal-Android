package org.privately.chat.sms;

import org.privately.chat.recipients.RecipientId;
import org.whispersystems.libsignal.util.guava.Optional;

public class IncomingJoinedMessage extends IncomingTextMessage {

  public IncomingJoinedMessage(RecipientId sender) {
    super(sender, 1, System.currentTimeMillis(), -1, null, Optional.absent(), 0, false);
  }

  @Override
  public boolean isJoined() {
    return true;
  }

  @Override
  public boolean isSecureMessage() {
    return true;
  }

}
