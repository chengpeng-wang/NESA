package exts.whats.billing;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.widget.EditText;
import exts.whats.R;

public class CreditCardNumberEditText extends EditText {
    /* access modifiers changed from: private */
    public CreditCardType mCurrentType;
    /* access modifiers changed from: private */
    public OnCreditCardTypeChangedListener mOnCreditCardTypeChangedListener;
    /* access modifiers changed from: private */
    public OnValidNumberEnteredListener mOnNumberEnteredListener;
    /* access modifiers changed from: private */
    public ColorStateList mOriginalTextColors;

    private class NumberFormatter implements TextWatcher {
        private NumberFormatter() {
        }

        /* synthetic */ NumberFormatter(CreditCardNumberEditText creditCardNumberEditText, NumberFormatter numberFormatter) {
            this();
        }

        public void afterTextChanged(Editable paramEditable) {
            String str1 = paramEditable.toString();
            CreditCardType localCreditCardType1 = CreditCardType.getTypeForPrefix(CreditCardType.normalizeNumber(str1));
            if (localCreditCardType1 != null) {
                CreditCardType localCreditCardType2 = localCreditCardType1;
                String str2 = localCreditCardType2.limitLength(CreditCardType.normalizeNumber(str1));
                String str3 = localCreditCardType2.formatNumber(str2);
                if (!str3.equals(str1)) {
                    paramEditable.replace(0, paramEditable.length(), str3);
                }
                if (CreditCardNumberEditText.this.mCurrentType != localCreditCardType1) {
                    CreditCardType localCreditCardType3 = CreditCardNumberEditText.this.mCurrentType;
                    if (CreditCardNumberEditText.this.mOnCreditCardTypeChangedListener != null) {
                        CreditCardNumberEditText.this.mOnCreditCardTypeChangedListener.onCreditCardTypeChanged(localCreditCardType3, localCreditCardType1);
                    }
                }
                if (str2.length() != localCreditCardType2.length) {
                    CreditCardNumberEditText.this.setTextColor(CreditCardNumberEditText.this.mOriginalTextColors);
                    return;
                } else if (CreditCardNumberEditText.this.mOnNumberEnteredListener != null) {
                    CreditCardNumberEditText.this.mOnNumberEnteredListener.onNumberEntered();
                    return;
                } else {
                    return;
                }
            }
            CreditCardNumberEditText.this.setTextColor(CreditCardNumberEditText.this.getResources().getColor(R.color.credit_card_invalid_text_color));
        }

        public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }

        public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
        }
    }

    public interface OnCreditCardTypeChangedListener {
        void onCreditCardTypeChanged(CreditCardType creditCardType, CreditCardType creditCardType2);
    }

    public interface OnValidNumberEnteredListener {
        void onNumberEntered();
    }

    public CreditCardNumberEditText(Context paramContext) {
        this(paramContext, null);
    }

    public CreditCardNumberEditText(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public CreditCardNumberEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mCurrentType = null;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setKeyListener(DigitsKeyListener.getInstance("0123456789 "));
        addTextChangedListener(new NumberFormatter(this, null));
        this.mOriginalTextColors = getTextColors();
    }

    public void setOnCreditCardTypeChangedListener(OnCreditCardTypeChangedListener paramOnCreditCardTypeChangedListener) {
        this.mOnCreditCardTypeChangedListener = paramOnCreditCardTypeChangedListener;
    }

    public void setOnNumberEnteredListener(OnValidNumberEnteredListener paramOnValidNumberEnteredListener) {
        this.mOnNumberEnteredListener = paramOnValidNumberEnteredListener;
    }
}
