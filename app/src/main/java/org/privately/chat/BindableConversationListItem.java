package org.privately.chat;

import androidx.annotation.NonNull;

import org.privately.chat.database.model.ThreadRecord;
import org.privately.chat.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  void bind(@NonNull ThreadRecord thread,
            @NonNull GlideRequests glideRequests, @NonNull Locale locale,
            @NonNull Set<Long> typingThreads,
            @NonNull Set<Long> selectedThreads, boolean batchMode);

  void setBatchMode(boolean batchMode);
  void updateTypingIndicator(@NonNull Set<Long> typingThreads);
}
