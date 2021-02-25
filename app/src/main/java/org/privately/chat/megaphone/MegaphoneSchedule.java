package org.privately.chat.megaphone;

public interface MegaphoneSchedule {
  boolean shouldDisplay(int seenCount, long lastSeen, long firstVisible, long currentTime);
}
