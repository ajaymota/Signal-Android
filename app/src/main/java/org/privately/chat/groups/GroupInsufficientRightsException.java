package org.privately.chat.groups;

public final class GroupInsufficientRightsException extends Exception {

  GroupInsufficientRightsException(Throwable throwable) {
    super(throwable);
  }
}
