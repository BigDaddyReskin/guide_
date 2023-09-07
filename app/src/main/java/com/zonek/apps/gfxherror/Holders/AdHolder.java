package com.zonek.apps.gfxherror.Holders;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.zonek.apps.gfxherror.R;


/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */
public class AdHolder extends RecyclerView.ViewHolder {

    public NativeAdLayout nativeAdLayout;
    public MediaView mvAdMedia;
    public MediaView ivAdIcon;
    public TextView tvAdTitle;
    public TextView tvAdBody;
    public TextView tvAdSocialContext;
    public TextView tvAdSponsoredLabel;
    public Button btnAdCallToAction;
    public LinearLayout adChoicesContainer;

    public AdHolder(NativeAdLayout adLayout) {
        super(adLayout);

        nativeAdLayout = adLayout;
        mvAdMedia = adLayout.findViewById(R.id.native_ad_media);
        tvAdTitle = adLayout.findViewById(R.id.native_ad_title);
        tvAdBody = adLayout.findViewById(R.id.native_ad_body);
        tvAdSocialContext = adLayout.findViewById(R.id.native_ad_social_context);
        tvAdSponsoredLabel = adLayout.findViewById(R.id.native_ad_sponsored_label);
        btnAdCallToAction = adLayout.findViewById(R.id.native_ad_call_to_action);
        ivAdIcon = adLayout.findViewById(R.id.native_ad_icon);
        adChoicesContainer = adLayout.findViewById(R.id.ad_choices_container);
    }
}