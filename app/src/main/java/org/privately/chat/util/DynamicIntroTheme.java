package org.privately.chat.util;

import androidx.annotation.StyleRes;

import org.privately.chat.R;

public class DynamicIntroTheme extends DynamicTheme {

  protected @StyleRes int getLightThemeStyle() {
    return R.style.TextSecure_LightIntroTheme;
  }

  protected @StyleRes int getDarkThemeStyle() {
    return R.style.TextSecure_DarkIntroTheme;
  }
}
