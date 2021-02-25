package org.privately.chat.components.webrtc;

import android.media.AudioManager;

import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.util.ServiceUtil;

class WebRtcCallRepository {

  private final AudioManager audioManager;

  WebRtcCallRepository() {
    this.audioManager = ServiceUtil.getAudioManager(ApplicationDependencies.getApplication());
  }

  WebRtcAudioOutput getAudioOutput() {
    if (audioManager.isBluetoothScoOn()) {
      return WebRtcAudioOutput.HEADSET;
    } else if (audioManager.isSpeakerphoneOn()) {
      return WebRtcAudioOutput.SPEAKER;
    } else {
      return WebRtcAudioOutput.HANDSET;
    }
  }
}
