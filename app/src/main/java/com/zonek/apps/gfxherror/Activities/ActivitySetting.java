package com.zonek.apps.gfxherror.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zonek.apps.gfxherror.Applications.MyApplication;
import com.zonek.apps.gfxherror.BuildConfig;
import com.zonek.apps.gfxherror.GFX;
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

public class ActivitySetting extends AppCompatActivity {


    private ImageViews music_on ,music_off ,sound_on , sound_off;
    private ImageViews back;
    private TextView version, privacy,moreapps;
    private Particles particles ;
    private MyApplication myApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initializeUI();
        InitializeAds();

    }


    private void InitializeAds(){
        myApplication = (MyApplication)getApplicationContext();
        RelativeLayout view = findViewById(R.id.adView);
        myApplication.setBannerAd(view);
    }


    private void initializeUI(){

        myApplication =(MyApplication)getApplicationContext();
        music_on = findViewById(R.id.music_on);
        music_off = findViewById(R.id.music_off);
        sound_on = findViewById(R.id.sound_on);
        sound_off = findViewById(R.id.sound_off);
        version = findViewById(R.id.version_text);
        privacy = findViewById(R.id.privacy_text);
        moreapps = findViewById(R.id.moreapps);
        version.setText(""+BuildConfig.VERSION_NAME);
        particles = findViewById(R.id.particles);
        back = findViewById(R.id.ic_back);
        if (GFX.useParticles){
            particles.pause();
            particles.setVisibility(View.VISIBLE);
        }

        CheckMusic();
        CheckSound();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPrivacy();
                setAnimation(privacy);
            }
        });
        moreapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMoreApps();
                setAnimation(moreapps);
            }
        });

        music_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVisible(music_on)){
                    setVisible(music_off,true);
                    setVisible(music_on,false);
                    SettingsPreferences.setMusic(getApplicationContext(), false);
                    if (GFX.useMusic){
                        myApplication.StopMuisc();
                    }
                }
            }
        });

        music_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible(music_off)){
                    setVisible(music_off,false);
                    setVisible(music_on,true);
                    SettingsPreferences.setMusic(getApplicationContext(), true);
                    if (GFX.useMusic){
                        myApplication.PlayMusic();
                    }

                }
            }
        });

        sound_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isVisible(sound_on)){
                    setClicksound();
                    setVisible(sound_off,true);
                    setVisible(sound_on,false);
                    SettingsPreferences.setSound(getApplicationContext(), false);
                    if (GFX.useMusic){
                        myApplication.StopSound();
                    }

                }
            }
        });

        sound_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible(sound_off)){
                    setVisible(sound_off,false);
                    setVisible(sound_on,true);
                    SettingsPreferences.setSound(getApplicationContext(), true);
                }
            }
        });


    }

    private void getMoreApps(){

        Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(GFX.Tags.GGDEV+GFX.DeveloperName));
        if (isAvailable(mIntent)) {
            startActivity(mIntent);
        } else {
            Toast.makeText(getApplicationContext(),GFX.Tags.NotAvailableMessage, Toast.LENGTH_SHORT).show();
        }


    }

    public boolean isAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void getPrivacy(){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GFX.PrivacyPolicy));
        if (isAvailable(intent)) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),GFX.Tags.NotAvailableMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAnimation(final View view){
        view.setScaleX(0.9f);
        view.setScaleY(0.9f);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        },50);
    }

    public void CheckSound(){

        if (SettingsPreferences.checkSound(getApplicationContext())) {
            try {
                setVisible(sound_on,true);
                setVisible(sound_off,false);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }else {
            try {
                setVisible(sound_off,true);
                setVisible(sound_on,false);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }
    public void CheckMusic(){

        if (SettingsPreferences.checkMusic(getApplicationContext())) {
            try {
                setVisible(music_on,true);
                setVisible(music_off,false);
                if (GFX.useMusic){
                    myApplication.PlayMusic();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }else {
            try {
                setVisible(music_off,true);
                setVisible(music_on,false);
                if (GFX.useMusic){
                    myApplication.StopMuisc();
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void setVisible(View view, boolean isVisible){
        if (isVisible){
            view.setVisibility(View.VISIBLE);
            return;
        }
        view.setVisibility(View.GONE);
    }

    private boolean isVisible(View view){
        return view.getVisibility()==View.VISIBLE;
    }



    private void setClicksound(){
        if (GFX.useMusic){
            if (SettingsPreferences.checkSound(getApplicationContext())) {
                myApplication.ClickSounds(getApplicationContext(),R.raw.click);
            }
        }

    }
    @Override
    protected void onDestroy() {
        myApplication.onDestroyBanner();
        super.onDestroy();
    }

}