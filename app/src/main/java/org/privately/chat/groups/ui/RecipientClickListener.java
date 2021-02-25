package org.privately.chat.groups.ui;

import androidx.annotation.NonNull;

import org.privately.chat.recipients.Recipient;

public interface RecipientClickListener {
  void onClick(@NonNull Recipient recipient);
}
