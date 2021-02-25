package org.privately.chat.lock.v2;

import android.content.Context;

import androidx.annotation.NonNull;

import org.privately.chat.keyvalue.SignalStore;
import org.privately.chat.util.TextSecurePreferences;

public final class RegistrationLockUtil {

  private RegistrationLockUtil() {}

  public static boolean userHasRegistrationLock(@NonNull Context context) {
    return TextSecurePreferences.isV1RegistrationLockEnabled(context) || SignalStore.kbsValues().isV2RegistrationLockEnabled();
  }
}
