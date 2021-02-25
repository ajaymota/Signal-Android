package org.privately.chat.groups;

public final class GroupNotAMemberException extends Exception {

  public GroupNotAMemberException(Throwable throwable) {
    super(throwable);
  }

  GroupNotAMemberException() {
  }
}
