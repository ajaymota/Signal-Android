package org.privately.chat.groups.ui.addtogroup;

import android.content.Context;

import androidx.annotation.NonNull;

import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.groups.GroupChangeBusyException;
import org.privately.chat.groups.GroupChangeFailedException;
import org.privately.chat.groups.GroupId;
import org.privately.chat.groups.GroupInsufficientRightsException;
import org.privately.chat.groups.GroupManager;
import org.privately.chat.groups.GroupNotAMemberException;
import org.privately.chat.groups.MembershipNotSuitableForV2Exception;
import org.privately.chat.groups.ui.GroupChangeErrorCallback;
import org.privately.chat.groups.ui.GroupChangeFailureReason;
import org.privately.chat.logging.Log;
import org.privately.chat.recipients.Recipient;
import org.privately.chat.recipients.RecipientId;
import org.privately.chat.util.concurrent.SignalExecutors;

import java.io.IOException;
import java.util.Collections;

final class AddToGroupRepository {

  private static final String TAG = Log.tag(AddToGroupRepository.class);

  private final Context context;

  AddToGroupRepository() {
    this.context = ApplicationDependencies.getApplication();
  }

  public void add(@NonNull RecipientId recipientId,
                  @NonNull Recipient groupRecipient,
                  @NonNull GroupChangeErrorCallback error,
                  @NonNull Runnable success)
  {
    SignalExecutors.UNBOUNDED.execute(() -> {
      try {
        GroupId.Push pushGroupId = groupRecipient.requireGroupId().requirePush();

        GroupManager.addMembers(context, pushGroupId, Collections.singletonList(recipientId));

        success.run();
      } catch (GroupInsufficientRightsException | GroupNotAMemberException e) {
        Log.w(TAG, e);
        error.onError(GroupChangeFailureReason.NO_RIGHTS);
      } catch (GroupChangeFailedException | GroupChangeBusyException | IOException e) {
        Log.w(TAG, e);
        error.onError(GroupChangeFailureReason.OTHER);
      } catch (MembershipNotSuitableForV2Exception e) {
        Log.w(TAG, e);
        error.onError(GroupChangeFailureReason.NOT_CAPABLE);
      }
    });
  }
}
