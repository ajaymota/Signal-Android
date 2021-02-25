package org.privately.chat.megaphone;

final class SignalPinReminderSchedule implements MegaphoneSchedule {

  @Override
  public boolean shouldDisplay(int seenCount, long lastSeen, long firstVisible, long currentTime) {
//    if (SignalStore.kbsValues().hasOptedOut()) {
//      return false;
//    }
//
//    if (!SignalStore.kbsValues().hasPin()) {
//      return false;
//    }
//
//    if (!SignalStore.pinValues().arePinRemindersEnabled()) {
//      return false;
//    }
//
//    if (!TextSecurePreferences.isPushRegistered(ApplicationDependencies.getApplication())) {
//      return false;
//    }
//
//    long lastSuccessTime = SignalStore.pinValues().getLastSuccessfulEntryTime();
//    long interval        = SignalStore.pinValues().getCurrentInterval();
//
//    return currentTime - lastSuccessTime >= interval;

    return false;
  }
}
