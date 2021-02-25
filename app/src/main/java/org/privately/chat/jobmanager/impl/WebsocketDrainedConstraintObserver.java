package org.privately.chat.jobmanager.impl;

import androidx.annotation.NonNull;

import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.jobmanager.ConstraintObserver;

/**
 * An observer for {@link WebsocketDrainedConstraint}. Will fire when the
 * {@link org.privately.chat.messages.InitialMessageRetriever} is caught up.
 */
public class WebsocketDrainedConstraintObserver implements ConstraintObserver {

  private static final String REASON = WebsocketDrainedConstraintObserver.class.getSimpleName();

  private volatile Notifier notifier;

  public WebsocketDrainedConstraintObserver() {
    ApplicationDependencies.getInitialMessageRetriever().addListener(() -> {
      if (notifier != null) {
        notifier.onConstraintMet(REASON);
      }
    });
  }

  @Override
  public void register(@NonNull Notifier notifier) {
    this.notifier = notifier;
  }
}
