package org.privately.chat.phonenumbers;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.ShortNumberInfo;

import org.privately.chat.groups.GroupId;
import org.privately.chat.logging.Log;
import org.privately.chat.util.TextSecurePreferences;
import org.privately.chat.util.Util;
import org.whispersystems.libsignal.util.Pair;
import org.whispersystems.libsignal.util.guava.Optional;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberFormatter {

  private static final String TAG = PhoneNumberFormatter.class.getSimpleName();

  private static final Set<String> SHORT_COUNTRIES = new HashSet<String>() {{
    add("NU");
    add("TK");
    add("NC");
    add("AC");
  }};

  private static final Pattern US_NO_AREACODE = Pattern.compile("^(\\d{7})$");
  private static final Pattern BR_NO_AREACODE = Pattern.compile("^(9?\\d{8})$");

  private static final AtomicReference<Pair<String, PhoneNumberFormatter>> cachedFormatter = new AtomicReference<>();

  private final Optional<PhoneNumber> localNumber;

  private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
  private final Pattern         ALPHA_PATTERN   = Pattern.compile("[a-zA-Z]");


  public static @NonNull PhoneNumberFormatter get(Context context) {
    String localNumber = TextSecurePreferences.getLocalNumber(context);

    if (!TextUtils.isEmpty(localNumber)) {
      Pair<String, PhoneNumberFormatter> cached = cachedFormatter.get();

      if (cached != null && cached.first().equals(localNumber)) return cached.second();

      PhoneNumberFormatter formatter = new PhoneNumberFormatter(localNumber);
      cachedFormatter.set(new Pair<>(localNumber, formatter));

      return formatter;
    } else {
      return new PhoneNumberFormatter(Util.getSimCountryIso(context).or("US"), true);
    }
  }

  PhoneNumberFormatter(@NonNull String localNumberString) {
    try {
//      Phonenumber.PhoneNumber libNumber   = phoneNumberUtil.parse(localNumberString, null);
//      int                     countryCode = libNumber.getCountryCode();

      this.localNumber       = Optional.of(new PhoneNumber(localNumberString));
//      this.localCountryCode  = phoneNumberUtil.getRegionCodeForNumber(libNumber);
    } catch (Exception e) {
      Log.w(TAG, localNumberString);
      throw new AssertionError(e);
    }
  }

  PhoneNumberFormatter(@NonNull String localCountryCode, boolean countryCode) {
    this.localNumber      = Optional.absent();
  }

  public String format(@Nullable String number) {
//    if (number == null)                       return "Unknown";
//    if (GroupId.isEncodedGroup(number))     return number;
//    if (ALPHA_PATTERN.matcher(number).find()) return number.trim();
//
//    String bareNumber = number.replaceAll("[^0-9+]", "");
//
//    if (bareNumber.length() == 0) {
//      if (number.trim().length() == 0) return "Unknown";
//      else                             return number.trim();
//    }
//
//    // libphonenumber doesn't seem to be correct for Germany and Finland
//    if (bareNumber.length() <= 6 && ("DE".equals(localCountryCode) || "FI".equals(localCountryCode) || "SK".equals(localCountryCode))) {
//      return bareNumber;
//    }
//
//    // libphonenumber seems incorrect for Russia and a few other countries with 4 digit short codes.
//    if (bareNumber.length() <= 4 && !SHORT_COUNTRIES.contains(localCountryCode)) {
//      return bareNumber;
//    }
//
//    if (isShortCode(bareNumber, localCountryCode)) {
//      return bareNumber;
//    }
//
//    String processedNumber = applyAreaCodeRules(localNumber, bareNumber);
//
//    try {
//      Phonenumber.PhoneNumber parsedNumber = phoneNumberUtil.parse(processedNumber, localCountryCode);
//      return phoneNumberUtil.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
//    } catch (NumberParseException e) {
//      Log.w(TAG, e);
//      if (bareNumber.charAt(0) == '+')
//        return bareNumber;
//
//      String localNumberImprecise = localNumber.isPresent() ? localNumber.get().getE164Number() : "";
//
//      if (localNumberImprecise.charAt(0) == '+')
//        localNumberImprecise = localNumberImprecise.substring(1);
//
//      if (localNumberImprecise.length() == bareNumber.length() || bareNumber.length() > localNumberImprecise.length())
//        return "+" + number;
//
//      int difference = localNumberImprecise.length() - bareNumber.length();
//
//      return "+" + localNumberImprecise.substring(0, difference) + bareNumber;
//    }
    return number;
  }

  private static class PhoneNumber {
    private final String           e164Number;

    PhoneNumber(String e164Number) {
      this.e164Number  = e164Number;
    }

    String getE164Number() {
      return e164Number;
    }
  }
}
