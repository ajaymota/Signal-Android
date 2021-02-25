package org.privately.chat.jobmanager.impl;

import androidx.annotation.NonNull;

import org.privately.chat.jobmanager.Data;
import org.privately.chat.logging.Log;
import org.privately.chat.util.JsonUtils;

import java.io.IOException;

public class JsonDataSerializer implements Data.Serializer {

  private static final String TAG = Log.tag(JsonDataSerializer.class);

  @Override
  public @NonNull String serialize(@NonNull Data data) {
    try {
      return JsonUtils.toJson(data);
    } catch (IOException e) {
      Log.e(TAG, "Failed to serialize to JSON.", e);
      throw new AssertionError(e);
    }
  }

  @Override
  public @NonNull Data deserialize(@NonNull String serialized) {
    try {
      return JsonUtils.fromJson(serialized, Data.class);
    } catch (IOException e) {
      Log.e(TAG, "Failed to deserialize JSON.", e);
      throw new AssertionError(e);
    }
  }
}
