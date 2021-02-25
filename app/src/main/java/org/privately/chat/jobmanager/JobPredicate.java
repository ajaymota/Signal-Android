package org.privately.chat.jobmanager;

import androidx.annotation.NonNull;

import org.privately.chat.jobmanager.persistence.JobSpec;

public interface JobPredicate {
  JobPredicate NONE = jobSpec -> true;

  boolean shouldRun(@NonNull JobSpec jobSpec);
}
