package exts.whats.billing;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import exts.whats.R;
import java.util.Arrays;

public class CreditCardImagesAnimatorFroyo extends CreditCardImagesAnimator {
    private Animation mFadeInAnimation;
    private Animation mFadeOutAnimation;
    private boolean[] mVisible;

    public CreditCardImagesAnimatorFroyo(Context paramContext, ImageView[] paramArrayOfImageView, CreditCardType[] paramArrayOfCreditCardType) {
        super(paramArrayOfImageView, paramArrayOfCreditCardType);
        this.mVisible = new boolean[paramArrayOfImageView.length];
        Arrays.fill(this.mVisible, true);
        this.mFadeInAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.fade_in);
        this.mFadeOutAnimation = AnimationUtils.loadAnimation(paramContext, R.anim.fade_out);
    }

    public void animateToType(CreditCardType paramCreditCardType) {
        if (paramCreditCardType != this.mCurrentType) {
            int i = findIndex(paramCreditCardType);
            if (i != -1) {
                if (!this.mVisible[i]) {
                    this.mImages[i].startAnimation(this.mFadeInAnimation);
                    this.mVisible[i] = true;
                    this.mImages[i].setVisibility(0);
                }
                int j = 0;
                while (j < this.mImages.length) {
                    if (j != i && this.mVisible[j]) {
                        this.mImages[j].startAnimation(this.mFadeOutAnimation);
                        this.mVisible[j] = false;
                        this.mImages[j].setVisibility(4);
                    }
                    j++;
                }
            }
            this.mCurrentType = paramCreditCardType;
        }
    }
}
