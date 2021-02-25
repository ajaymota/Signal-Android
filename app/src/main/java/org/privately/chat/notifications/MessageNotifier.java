package org.privately.chat.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.recipients.Recipient;
import org.privately.chat.util.concurrent.SignalExecutors;

public interface MessageNotifier {
  void setVisibleThread(long threadId);
  void clearVisibleThread();
  void setLastDesktopActivityTimestamp(long timestamp);
  void notifyMessageDeliveryFailed(Context context, Recipient recipient, long threadId);
  void cancelDelayedNotifications();
  void updateNotification(@NonNull Context context);
  void updateNotification(@NonNull Context context, long threadId);
  void updateNotification(@NonNull Context context, long threadId, boolean signal);
  void updateNotification(@NonNull Context context, long threadId, boolean signal, int reminderCount);
  void clearReminder(@NonNull Context context);


  class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
      SignalExecutors.BOUNDED.execute(() -> {
        int reminderCount = intent.getIntExtra("reminder_count", 0);
        ApplicationDependencies.getMessageNotifier().updateNotification(context, -1, true, reminderCount + 1);
      });
    }
  }
}
