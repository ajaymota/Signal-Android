package org.privately.chat.registration.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.whispersystems.signalservice.api.util.PhoneNumberFormatter;

import java.util.Objects;

public final class NumberViewState implements Parcelable {

  private final String selectedCountryName;
  private final int    countryCode;
  private final String   nationalNumber;

  private NumberViewState(Builder builder) {
    this.selectedCountryName = builder.countryDisplayName;
    this.countryCode         = builder.countryCode;
    this.nationalNumber      = builder.nationalNumber;
  }

  public boolean isValid() {
//    return PhoneNumberFormatter.isValidNumber(getE164Number(), Integer.toString(getCountryCode()));
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 1;
    hash *= 31;
    hash += (Integer.parseInt(nationalNumber) ^ (Integer.parseInt(nationalNumber) >>> 32));
    hash *= 31;
//    hash += selectedCountryName != null ? selectedCountryName.hashCode() : 0;
    return hash;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == null) return false;
    if (obj.getClass() != getClass()) return false;

    NumberViewState other = (NumberViewState) obj;

    return other.countryCode == countryCode &&
           other.nationalNumber == nationalNumber &&
           Objects.equals(other.selectedCountryName, selectedCountryName);
  }

  public static class Builder {
    private String countryDisplayName;
    private int    countryCode;
    private String   nationalNumber;

    public Builder countryCode(int countryCode) {
      this.countryCode = countryCode;
      return this;
    }

    public Builder selectedCountryDisplayName(String countryDisplayName) {
      this.countryDisplayName = countryDisplayName;
      return this;
    }

    public Builder nationalNumber(String nationalNumber) {
      this.nationalNumber = nationalNumber;
      return this;
    }

    public NumberViewState build() {
      return new NumberViewState(this);
    }
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(selectedCountryName);
    parcel.writeInt(countryCode);
    parcel.writeString(nationalNumber);
  }

  public static final Creator<NumberViewState> CREATOR = new Creator<NumberViewState>() {
    @Override
    public NumberViewState createFromParcel(Parcel in) {
      return new Builder().selectedCountryDisplayName(in.readString())
                          .countryCode(in.readInt())
                          .nationalNumber(in.readString())
                          .build();
    }

    @Override
    public NumberViewState[] newArray(int size) {
      return new NumberViewState[size];
    }
  };
}
