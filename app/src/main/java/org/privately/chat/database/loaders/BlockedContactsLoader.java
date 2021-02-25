package org.privately.chat.database.loaders;

import android.content.Context;
import android.database.Cursor;

import org.privately.chat.database.DatabaseFactory;
import org.privately.chat.util.AbstractCursorLoader;

public class BlockedContactsLoader extends AbstractCursorLoader {

  public BlockedContactsLoader(Context context) {
    super(context);
  }

  @Override
  public Cursor getCursor() {
    return DatabaseFactory.getRecipientDatabase(getContext()).getBlocked();
  }

}
