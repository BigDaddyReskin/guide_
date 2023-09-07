package com.zonek.apps.gfxherror.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.zonek.apps.gfxherror.Applications.MyApplication;
import com.zonek.apps.gfxherror.GFX;
import com.zonek.apps.gfxherror.Helper.Hexing;
import com.zonek.apps.gfxherror.Helper.SettingsPreferences;
import com.zonek.apps.gfxherror.R;
import com.zonek.apps.gfxherror.UI.ImageViews;
import com.zonek.apps.gfxherror.UI.Particles;

import java.util.List;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */
public class MainActivity extends AppCompatActivity {

    private ImageViews starts,setting,exit;
    private ImageViews share,rate;
    private Particles particles ;
    private MyApplication myApplication;
    private AlertDialog mAlertDialog,exitDialogue ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    private void initializeUI(){
        starts = findViewById(R.id.btn_start);
        setting = findViewById(R.id.btn_setting);
        exit = findViewById(R.id.btn_exit);
        rate = findViewById(R.id.ic_rate);
        share = findViewById(R.id.ic_share);
        particles = findViewById(R.id.particles);
        myApplication = (MyApplication)getApplicationContext();
        if (GFX.useParticles){
            particles.pause();
            particles.setVisibility(View.VISIBLE);
        }
        setEvent();
    }

    private void setEvent(){

        starts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPressed(starts,R.drawable.btn_start,R.drawable.btn_start_pressed);
                startActivity(new Intent(getApplicationContext(), ActivityTips.class));
                ShowInterstitial();

            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPressed(setting,R.drawable.btn_setting,R.drawable.btn_setting_pressed);
                startActivity(new Intent(getApplicationContext(), ActivitySetting.class));
                ShowInterstitial();

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPressed(exit,R.drawable.btn_exit,R.drawable.btn_exit_pressed);
                setExitDialogue();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShareApp();
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRateApp();
            }
        });
    }


    private void InitializeAds(){
        myApplication = (MyApplication)getApplicationContext();
        RelativeLayout view = findViewById(R.id.adView);
        myApplication.setBannerAd(view);
    }

    private void ShowInterstitial(){
        myApplication = (MyApplication)getApplicationContext();
        myApplication.showInterstitial();
    }

    private void AcceptTermsEU() {

        ConsentInformation consentInformation = ConsentInformation.getInstance(this);
        myApplication = (MyApplication)getApplicationContext();
        String[] publisherIds = new String[0];
        if (GFX.ONLINE_OFFLINE){
            publisherIds = new String[]{myApplication.AdMobID};
        }else {
            if (GFX.useCRYPTDATA){
                try {
                    publisherIds = new String[]{Hexing.getStrings(GFX.AdMob_Id)};
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                publisherIds = new String[]{GFX.AdMob_Id};
            }

        }
        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {
                // User's consent status successfully updated.
                switch (consentStatus) {
                    case PERSONALIZED:
                        ConsentInformation.getInstance(getApplicationContext()).setConsentStatus(ConsentStatus.PERSONALIZED);
                        InitializeAds();
                        break;
                    case UNKNOWN:
                        if(ConsentInformation.getInstance(getBaseContext()).isRequestLocationInEeaOrUnknown()){
                            buildAlertDialoguEu();
                        }
                        else {
                            InitializeAds();

                        }
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                InitializeAds();
            }
        });


    }

    private void buildAlertDialoguEu() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_eu, null);
        alertDialog.setView(view).setCancelable(false);
        mAlertDialog = alertDialog.create();
        mAlertDialog.show();
        mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button accepts =  view.findViewById(R.id.accept_policy);
        accepts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAlertDialog.cancel();
                ConsentInformation.getInstance(getApplicationContext()).setConsentStatus(ConsentStatus.PERSONALIZED);
                InitializeAds();
            }

        });
        TextView privacy =  view.findViewById(R.id.consent);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrivacy();

            }
        });
    }

    private void setExitDialogue() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate, null);
        alertDialog.setView(view).setCancelable(false);
        exitDialogue = alertDialog.create();
        exitDialogue.show();
        exitDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button rate =  view.findViewById(R.id.rate_me);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getRateApp();
              exitDialogue.dismiss();
            }

        });
        TextView no =  view.findViewById(R.id.rate_no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();

            }
        });
    }

    private void setPressed(final ImageViews img,final int unpressed,int pressed){
        img.setImageResource(pressed);
        if (GFX.useMusic){
            setClicksound();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                img.setImageResource(unpressed);
            }
        },70);

    }

    private void setClicksound(){
        if (SettingsPreferences.checkSound(getApplicationContext())) {
            myApplication.ClickSounds(getApplicationContext(),R.raw.click);
        }
    }

    private void getPrivacy(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GFX.PrivacyPolicy));
        if (isAvailable(intent)) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),GFX.Tags.NotAvailableMessage, Toast.LENGTH_SHORT).show();
        }
    }
    private void getShareApp(){
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Share Application");
            startActivity(Intent.createChooser(intent, "choose one :"));
        } catch (Exception e) {
        }

    }
    private void getRateApp(){
        try {
            Uri uri = Uri.parse(GFX.Tags.MARKET+ getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GFX.Tags.GGAPPS+getPackageName())));
        }
    }
    private boolean isAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    public void onBackPressed() {
        setExitDialogue();
    }


    @Override
    protected void onDestroy() {
        myApplication.onDestroyBanner();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        AcceptTermsEU();
        super.onResume();
    }
}