package org.privately.chat.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.jobs.PushNotificationReceiveJob;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    ApplicationDependencies.getJobManager().add(new PushNotificationReceiveJob(context));
  }
}
