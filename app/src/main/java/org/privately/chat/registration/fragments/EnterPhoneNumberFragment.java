package org.privately.chat.registration.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dd.CircularProgressButton;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;
import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

import org.privately.chat.R;
import org.privately.chat.components.LabeledEditText;
import org.privately.chat.components.registration.VerificationCodeView;
import org.privately.chat.components.registration.VerificationPinKeyboard;
import org.privately.chat.logging.Log;
import org.privately.chat.registration.service.CodeVerificationRequest;
import org.privately.chat.registration.service.RegistrationCodeRequest;
import org.privately.chat.registration.service.RegistrationService;
import org.privately.chat.registration.viewmodel.NumberViewState;
import org.privately.chat.registration.viewmodel.RegistrationViewModel;
import org.privately.chat.util.Dialogs;
import org.privately.chat.util.PlayServicesUtil;
import org.privately.chat.util.concurrent.AssertedSuccessListener;
import org.whispersystems.signalservice.internal.contacts.entities.TokenResponse;

public final class EnterPhoneNumberFragment extends BaseRegistrationFragment {

  private static final String TAG = Log.tag(EnterPhoneNumberFragment.class);

//  private LabeledEditText        countryCode;
  private VerificationCodeView number;
//  private ArrayAdapter<String>   countrySpinnerAdapter;
//  private AsYouTypeFormatter     countryFormatter;
  private CircularProgressButton register;
//  private Spinner                countrySpinner;
  private View                   cancel;
//  private ScrollView             scrollView;

  private VerificationPinKeyboard keyboard;
  private boolean                 autoCompleting;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_registration_enter_phone_number, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    setDebugLogSubmitMultiTapView(view.findViewById(R.id.verify_header));

//    countryCode    = view.findViewById(R.id.country_code);
    number         = view.findViewById(R.id.number);
//    countrySpinner = view.findViewById(R.id.country_spinner);
    cancel         = view.findViewById(R.id.cancel_button);
//    scrollView     = view.findViewById(R.id.scroll_view);
    register       = view.findViewById(R.id.registerButton);

    keyboard             = view.findViewById(R.id.keyboard);
    autoCompleting      = false;

    connectKeyboard(number, keyboard);

//    initializeSpinner(countrySpinner);

    RegistrationViewModel model  = getModel();

    register.setOnClickListener(v -> handleRegister(requireContext(), number, model));

    if (isReregister()) {
      cancel.setVisibility(View.VISIBLE);
      cancel.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
    } else {
      cancel.setVisibility(View.GONE);
    }

//    initNumber(number);

//    countryCode.getInput().addTextChangedListener(new CountryCodeChangedListener());

    if (model.hasCaptchaToken()) {
      handleRegister(requireContext(), number, model);
    }

//    countryCode.getInput().setImeOptions(EditorInfo.IME_ACTION_NEXT);
  }

  private void connectKeyboard(VerificationCodeView verificationCodeView, VerificationPinKeyboard keyboard) {
    keyboard.setOnKeyPressListener(key -> {
      if (!autoCompleting) {
        if (key >= 0) {
          verificationCodeView.append(key);
        } else {
          verificationCodeView.delete();
        }
      }
    });
  }

  private void handleRegister(@NonNull Context context, VerificationCodeView verificationCodeView, RegistrationViewModel model) {

//    if (TextUtils.isEmpty(countryCode.getText())) {
//      Toast.makeText(context, getString(R.string.RegistrationActivity_you_must_specify_your_country_code), Toast.LENGTH_LONG).show();
//      return;
//    }

//    if (TextUtils.isEmpty(this.number.getText())) {
//      Toast.makeText(context, getString(R.string.RegistrationActivity_you_must_specify_your_phone_number), Toast.LENGTH_LONG).show();
//      return;
//    }

//    final NumberViewState number     = getModel().getNumber();
    final String          e164number = verificationCodeView.getText();

    Log.i(TAG, "BLAH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    Log.i(TAG, e164number);

    model.onNumberEntered(e164number);

//    if (!number.isValid()) {
//      Dialogs.showAlertDialog(context,
//        getString(R.string.RegistrationActivity_invalid_number),
//        String.format(getString(R.string.RegistrationActivity_the_number_you_specified_s_is_invalid), e164number));
//      return;
//    }

    PlayServicesUtil.PlayServicesStatus fcmStatus = PlayServicesUtil.getPlayServicesStatus(context);

    if (fcmStatus == PlayServicesUtil.PlayServicesStatus.SUCCESS) {
      handleRequestVerification(context, e164number, true);
    } else if (fcmStatus == PlayServicesUtil.PlayServicesStatus.MISSING) {
      handlePromptForNoPlayServices(context, e164number);
    } else if (fcmStatus == PlayServicesUtil.PlayServicesStatus.NEEDS_UPDATE) {
      GoogleApiAvailability.getInstance().getErrorDialog(requireActivity(), ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, 0).show();
    } else {
      Dialogs.showAlertDialog(context, getString(R.string.RegistrationActivity_play_services_error),
        getString(R.string.RegistrationActivity_google_play_services_is_updating_or_unavailable));
    }
  }

  private void handleRequestVerification(@NonNull Context context, @NonNull String e164number, boolean fcmSupported) {
    setSpinning(register);
    disableAllEntries();

    if (fcmSupported) {
      SmsRetrieverClient client = SmsRetriever.getClient(context);
      Task<Void>         task   = client.startSmsRetriever();

      task.addOnSuccessListener(none -> {
        Log.i(TAG, "Successfully registered SMS listener.");
        requestVerificationCode(e164number, RegistrationCodeRequest.Mode.SMS_WITH_LISTENER);
      });

      task.addOnFailureListener(e -> {
        Log.w(TAG, "Failed to register SMS listener.", e);
        requestVerificationCode(e164number, RegistrationCodeRequest.Mode.SMS_WITHOUT_LISTENER);
      });
    } else {
      Log.i(TAG, "FCM is not supported, using no SMS listener");
      requestVerificationCode(e164number, RegistrationCodeRequest.Mode.SMS_WITHOUT_LISTENER);
    }
  }

  private void disableAllEntries() {
//    countryCode.setEnabled(false);
    number.setEnabled(false);
//    countrySpinner.setEnabled(false);
    cancel.setVisibility(View.GONE);
  }

  private void enableAllEntries() {
//    countryCode.setEnabled(true);
    number.setEnabled(true);
//    countrySpinner.setEnabled(true);
    if (isReregister()) {
      cancel.setVisibility(View.VISIBLE);
    }
  }

  private void requestVerificationCode(String e164number, @NonNull RegistrationCodeRequest.Mode mode) {
    RegistrationViewModel model   = getModel();
    model.onNumberEntered(e164number);
    String                captcha = model.getCaptchaToken();
    model.clearCaptchaResponse();

    NavController navController = Navigation.findNavController(register);

    if (!model.getRequestLimiter().canRequest(mode, e164number, System.currentTimeMillis())) {
      Log.i(TAG, "Local rate limited");
      navController.navigate(EnterPhoneNumberFragmentDirections.actionEnterVerificationCode());
      cancelSpinning(register);
      enableAllEntries();
      return;
    }

    RegistrationService registrationService = RegistrationService.getInstance(e164number, model.getRegistrationSecret());

    registrationService.requestVerificationCode(requireActivity(), mode, captcha,
      new RegistrationCodeRequest.SmsVerificationCodeCallback() {

        @Override
        public void onNeedCaptcha() {
          if (getContext() == null) {
            Log.i(TAG, "Got onNeedCaptcha response, but fragment is no longer attached.");
            return;
          }
          navController.navigate(EnterPhoneNumberFragmentDirections.actionRequestCaptcha());
          cancelSpinning(register);
          enableAllEntries();
          model.getRequestLimiter().onUnsuccessfulRequest();
          model.updateLimiter();
        }

        @Override
        public void requestSent(@Nullable String fcmToken) {
          if (getContext() == null) {
            Log.i(TAG, "Got requestSent response, but fragment is no longer attached.");
            return;
          }
          model.setFcmToken(fcmToken);
          model.markASuccessfulAttempt();
          navController.navigate(EnterPhoneNumberFragmentDirections.actionEnterVerificationCode());
          cancelSpinning(register);
          enableAllEntries();
          model.getRequestLimiter().onSuccessfulRequest(mode, e164number, System.currentTimeMillis());
          model.updateLimiter();
        }

        @Override
        public void onRateLimited() {
          Toast.makeText(register.getContext(), R.string.RegistrationActivity_rate_limited_to_service, Toast.LENGTH_LONG).show();
          cancelSpinning(register);
          enableAllEntries();
          model.getRequestLimiter().onUnsuccessfulRequest();
          model.updateLimiter();
        }

        @Override
        public void onError() {
          Toast.makeText(register.getContext(), R.string.RegistrationActivity_unable_to_connect_to_service, Toast.LENGTH_LONG).show();
          cancelSpinning(register);
          enableAllEntries();
          model.getRequestLimiter().onUnsuccessfulRequest();
          model.updateLimiter();
        }
      });
  }

  private void handlePromptForNoPlayServices(@NonNull Context context, @NonNull String e164number) {
    new AlertDialog.Builder(context)
                   .setTitle(R.string.RegistrationActivity_missing_google_play_services)
                   .setMessage(R.string.RegistrationActivity_this_device_is_missing_google_play_services)
                   .setPositiveButton(R.string.RegistrationActivity_i_understand, (dialog1, which) -> handleRequestVerification(context, e164number, false))
                   .setNegativeButton(android.R.string.cancel, null)
                   .show();
  }
}
