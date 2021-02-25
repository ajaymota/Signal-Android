package org.privately.chat.lock.v2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import org.privately.chat.BaseActivity;
import org.privately.chat.PassphrasePromptActivity;
import org.privately.chat.R;
import org.privately.chat.dependencies.ApplicationDependencies;
import org.privately.chat.service.KeyCachingService;
import org.privately.chat.util.DynamicRegistrationTheme;
import org.privately.chat.util.DynamicTheme;

public class KbsMigrationActivity extends BaseActivity {

  public static final int REQUEST_NEW_PIN = CreateKbsPinActivity.REQUEST_NEW_PIN;

  private final DynamicTheme dynamicTheme = new DynamicRegistrationTheme();

  public static Intent createIntent() {
    return new Intent(ApplicationDependencies.getApplication(), KbsMigrationActivity.class);
  }

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);

    if (KeyCachingService.isLocked(this)) {
      startActivity(getPromptPassphraseIntent());
      finish();
      return;
    }

    dynamicTheme.onCreate(this);

    setContentView(R.layout.kbs_migration_activity);
  }

  @Override
  public void onResume() {
    super.onResume();
    dynamicTheme.onResume(this);
  }

  private Intent getPromptPassphraseIntent() {
    return getRoutedIntent(PassphrasePromptActivity.class, getIntent());
  }

  private Intent getRoutedIntent(Class<?> destination, @Nullable Intent nextIntent) {
    final Intent intent = new Intent(this, destination);
    if (nextIntent != null)   intent.putExtra("next_intent", nextIntent);
    return intent;
  }
}
