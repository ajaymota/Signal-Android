package org.privately.chat.profiles.edit;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.util.Consumer;

import org.privately.chat.database.DatabaseFactory;
import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.jobs.MultiDeviceProfileContentUpdateJob;
import org.privately.chat.jobs.MultiDeviceProfileKeyUpdateJob;
import org.privately.chat.jobs.ProfileUploadJob;
import org.privately.chat.logging.Log;
import org.privately.chat.profiles.AvatarHelper;
import org.privately.chat.profiles.ProfileMediaConstraints;
import org.privately.chat.profiles.ProfileName;
import org.privately.chat.profiles.SystemProfileUtil;
import org.privately.chat.recipients.Recipient;
import org.privately.chat.recipients.RecipientId;
import org.privately.chat.util.ProfileUtil;
import org.privately.chat.util.TextSecurePreferences;
import org.privately.chat.util.Util;
import org.privately.chat.util.concurrent.ListenableFuture;
import org.privately.chat.util.concurrent.SignalExecutors;
import org.privately.chat.util.concurrent.SimpleTask;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.profiles.SignalServiceProfile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class EditSelfProfileRepository implements EditProfileRepository {

  private static final String TAG = Log.tag(EditSelfProfileRepository.class);

  private final Context context;
  private final boolean excludeSystem;

  EditSelfProfileRepository(@NonNull Context context, boolean excludeSystem) {
    this.context        = context.getApplicationContext();
    this.excludeSystem  = excludeSystem;
  }

  @Override
  public void getCurrentProfileName(@NonNull Consumer<ProfileName> profileNameConsumer) {
    ProfileName storedProfileName = Recipient.self().getProfileName();
    if (!storedProfileName.isEmpty()) {
      profileNameConsumer.accept(storedProfileName);
    } else if (!excludeSystem) {
      SystemProfileUtil.getSystemProfileName(context).addListener(new ListenableFuture.Listener<String>() {
        @Override
        public void onSuccess(String result) {
          if (!TextUtils.isEmpty(result)) {
            profileNameConsumer.accept(ProfileName.fromSerialized(result));
          } else {
            profileNameConsumer.accept(storedProfileName);
          }
        }

        @Override
        public void onFailure(ExecutionException e) {
          Log.w(TAG, e);
          profileNameConsumer.accept(storedProfileName);
        }
      });
    } else {
      profileNameConsumer.accept(storedProfileName);
    }
  }

  @Override
  public void getCurrentAvatar(@NonNull Consumer<byte[]> avatarConsumer) {
    RecipientId selfId = Recipient.self().getId();

    if (AvatarHelper.hasAvatar(context, selfId)) {
      SimpleTask.run(() -> {
        try {
          return Util.readFully(AvatarHelper.getAvatar(context, selfId));
        } catch (IOException e) {
          Log.w(TAG, e);
          return null;
        }
      }, avatarConsumer::accept);
    } else if (!excludeSystem) {
      SystemProfileUtil.getSystemProfileAvatar(context, new ProfileMediaConstraints()).addListener(new ListenableFuture.Listener<byte[]>() {
        @Override
        public void onSuccess(byte[] result) {
          avatarConsumer.accept(result);
        }

        @Override
        public void onFailure(ExecutionException e) {
          Log.w(TAG, e);
          avatarConsumer.accept(null);
        }
      });
    }
  }

  @Override
  public void getCurrentDisplayName(@NonNull Consumer<String> displayNameConsumer) {
    displayNameConsumer.accept("");
  }

  @Override
  public void getCurrentName(@NonNull Consumer<String> nameConsumer) {
    nameConsumer.accept("");
  }

  @Override
  public void uploadProfile(@NonNull ProfileName profileName,
                            @NonNull String displayName,
                            boolean displayNameChanged,
                            @Nullable byte[] avatar,
                            boolean avatarChanged,
                            @NonNull Consumer<UploadResult> uploadResultConsumer)
  {
    SimpleTask.run(() -> {
      DatabaseFactory.getRecipientDatabase(context).setProfileName(Recipient.self().getId(), profileName);

      if (avatarChanged) {
        try {
          AvatarHelper.setAvatar(context, Recipient.self().getId(), avatar != null ? new ByteArrayInputStream(avatar) : null);
        } catch (IOException e) {
          return UploadResult.ERROR_IO;
        }
      }

      ApplicationDependencies.getJobManager()
                             .startChain(new ProfileUploadJob())
                             .then(Arrays.asList(new MultiDeviceProfileKeyUpdateJob(), new MultiDeviceProfileContentUpdateJob()))
                             .enqueue();

      return UploadResult.SUCCESS;
    }, uploadResultConsumer::accept);
  }

  @Override
  public void getCurrentUsername(@NonNull Consumer<Optional<String>> callback) {
    callback.accept(Optional.fromNullable(TextSecurePreferences.getLocalUsername(context)));
    SignalExecutors.UNBOUNDED.execute(() -> callback.accept(getUsernameInternal()));
  }

  @WorkerThread
  private @NonNull Optional<String> getUsernameInternal() {
    try {
      SignalServiceProfile profile = ProfileUtil.retrieveProfile(context, Recipient.self(), SignalServiceProfile.RequestType.PROFILE).get(5, TimeUnit.SECONDS).getProfile();
      TextSecurePreferences.setLocalUsername(context, profile.getUsername());
      DatabaseFactory.getRecipientDatabase(context).setUsername(Recipient.self().getId(), profile.getUsername());
    } catch (TimeoutException | InterruptedException | ExecutionException e) {
      Log.w(TAG, "Failed to retrieve username remotely! Using locally-cached version.");
    }
    return Optional.fromNullable(TextSecurePreferences.getLocalUsername(context));
  }
}
