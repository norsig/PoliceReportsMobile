package com.keniobyte.bruino.minsegapp.utils;

import android.app.Dialog;
import android.app.DialogFragment;
import android.lib.recaptcha.ReCaptcha;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.keniobyte.bruino.minsegapp.R;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author  bruino
 * @version 25/12/16.
 */

public class CaptchaDialog extends DialogFragment {
    @BindView(R.id.edit_text_captcha) EditText captchaEditText;
    @BindView(R.id.captchaProgress) ProgressBar progressBar;
    @BindView(R.id.captcha) ReCaptcha captcha;
    @BindView(R.id.reload) Button reloadCaptchaButton;
    @BindView(R.id.okCaptcha) Button okCaptchaButton;

    @BindString(R.string.API_KEY_CAPCHTA) String apiKeyCaptcha;
    @BindString(R.string.API_KEY_CAPCHTA_VERIFY) String apiKeyCaptchaVerify;

    private Unbinder unbinder;

    private ReCaptcha.OnVerifyAnswerListener onVerifyAnswerListener;
    final ReCaptcha.OnShowChallengeListener onShowChallengeListener = new ReCaptcha.OnShowChallengeListener() {
        @Override
        public void onChallengeShown(boolean shown) {
            progressBar.setVisibility(View.INVISIBLE);
            captchaEditText.setVisibility(View.VISIBLE);
            captchaEditText.setText("");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.captcha_dialog, container, false);
        unbinder = ButterKnife.bind(this, view);

        getDialog().setCanceledOnTouchOutside(false);
        captchaEditText.setVisibility(View.INVISIBLE);
        captcha.setLanguageCode("es");
        captcha.showChallengeAsync(apiKeyCaptcha, onShowChallengeListener);
        okCaptchaButton.setEnabled(false);
        captchaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(getClass().getSimpleName(), "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(getClass().getSimpleName(), "onTextChanged");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()){
                    okCaptchaButton.setEnabled(false);
                } else {
                    okCaptchaButton.setEnabled(true);
                }

            }
        });

        return view;
    }

    @OnClick(R.id.okCaptcha)
    public void okCaptcha(){
        if (captcha.isShown() && !captchaEditText.getText().toString().isEmpty()){
            dismiss();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            captcha.verifyAnswerAsync(apiKeyCaptchaVerify
                    , captchaEditText.getText().toString()
                    , onVerifyAnswerListener);
        }
    }

    @OnClick(R.id.reload)
    public void recharge(){
        captchaEditText.setText("");
        captchaEditText.setVisibility(View.INVISIBLE);
        captcha.showChallengeAsync(apiKeyCaptcha, onShowChallengeListener);
        progressBar.setVisibility(View.VISIBLE);
        okCaptchaButton.setEnabled(false);
    }

    public void setOnVerifyAnswerListener(ReCaptcha.OnVerifyAnswerListener onVerifyAnswerListener) {
        this.onVerifyAnswerListener = onVerifyAnswerListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
