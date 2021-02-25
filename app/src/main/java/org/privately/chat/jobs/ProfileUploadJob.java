package org.privately.chat.jobs;

import android.content.Context;

import androidx.annotation.NonNull;

import org.signal.zkgroup.profiles.ProfileKey;
import org.privately.chat.crypto.ProfileKeyUtil;
import org.privately.chat.database.DatabaseFactory;
import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.jobmanager.Data;
import org.privately.chat.jobmanager.Job;
import org.privately.chat.jobmanager.impl.NetworkConstraint;
import org.privately.chat.logging.Log;
import org.privately.chat.profiles.AvatarHelper;
import org.privately.chat.profiles.ProfileName;
import org.privately.chat.recipients.Recipient;
import org.privately.chat.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.util.StreamDetails;

public final class ProfileUploadJob extends BaseJob {

  private static final String TAG = Log.tag(ProfileUploadJob.class);

  public static final String KEY = "ProfileUploadJob";

  public static final String QUEUE = "ProfileAlteration";

  private final Context                     context;
  private final SignalServiceAccountManager accountManager;

  public ProfileUploadJob() {
    this(new Job.Parameters.Builder()
                            .addConstraint(NetworkConstraint.KEY)
                            .setQueue(QUEUE)
                            .setLifespan(Parameters.IMMORTAL)
                            .setMaxAttempts(Parameters.UNLIMITED)
                            .setMaxInstances(1)
                            .build());
  }

  private ProfileUploadJob(@NonNull Parameters parameters) {
    super(parameters);

    this.context        = ApplicationDependencies.getApplication();
    this.accountManager = ApplicationDependencies.getSignalServiceAccountManager();
  }

  @Override
  protected void onRun() throws Exception {
    if (!TextSecurePreferences.isPushRegistered(context)) {
      Log.w(TAG, "Not registered. Skipping.");
      return;
    }

    ProfileKey  profileKey  = ProfileKeyUtil.getSelfProfileKey();
    ProfileName profileName = Recipient.self().getProfileName();
    String      avatarPath;

    try (StreamDetails avatar = AvatarHelper.getSelfProfileAvatarStream(context)) {
      avatarPath = accountManager.setVersionedProfile(Recipient.self().getUuid().get(), profileKey, profileName.serialize(), avatar).orNull();
    }

    DatabaseFactory.getRecipientDatabase(context).setProfileAvatar(Recipient.self().getId(), avatarPath);
  }

  @Override
  protected boolean onShouldRetry(@NonNull Exception e) {
    return true;
  }

  @Override
  public @NonNull Data serialize() {
    return Data.EMPTY;
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onFailure() {
  }

  public static class Factory implements Job.Factory<ProfileUploadJob> {

    @Override
    public @NonNull ProfileUploadJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new ProfileUploadJob(parameters);
    }
  }
}
