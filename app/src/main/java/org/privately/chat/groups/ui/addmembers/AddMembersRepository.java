package org.privately.chat.groups.ui.addmembers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;

import org.privately.chat.contacts.SelectedContact;
import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.recipients.RecipientId;
import org.privately.chat.util.concurrent.SignalExecutors;

class AddMembersRepository {

  private final Context context;

  AddMembersRepository() {
    this.context = ApplicationDependencies.getApplication();
  }

  void getOrCreateRecipientId(@NonNull SelectedContact selectedContact, @NonNull Consumer<RecipientId> consumer) {
    SignalExecutors.BOUNDED.execute(() -> consumer.accept(selectedContact.getOrCreateRecipientId(context)));
  }
}
