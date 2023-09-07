package com.zonek.apps.gfxherror.Applications;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.RawRes;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.zonek.apps.gfxherror.Async.SynAd;
import com.zonek.apps.gfxherror.BuildConfig;
import com.zonek.apps.gfxherror.GFX;
import com.zonek.apps.gfxherror.Helper.Hexing;
import com.zonek.apps.gfxherror.R;
import com.zonek.apps.gfxherror.Helper.SettingsPreferences;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */
public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static MediaPlayer player ;
    public static MediaPlayer mMediaPlayer ;

    private InterstitialAd mInterstitialAdMob;
    private AdView mAdViewAdMob ;
    private RelativeLayout relativeLayout ;

    private com.facebook.ads.InterstitialAd mInterstitialFacebook ;
    private com.facebook.ads.AdView mAdViewFacebook ;

    public String defualtAd = "";
    public String AdMobID = "";
    public String natAdMob = "";
    public String natfacebook = "";
    private String tag = "secret_gfx";


    @Override
    public void onCreate() {
        super.onCreate();
        setContext(getApplicationContext());
        buildRequestAd();
        player = new MediaPlayer();
        mediaPlayerInitializer();
        if (GFX.useMusic){
            CheckMusic();
        }
        if (BuildConfig.DEBUG){
            if (!GFX.ONLINE_OFFLINE){
                if (GFX.useCRYPTDATA){
                    cryptData();
                    return;
                }
                if (BuildConfig.DEBUG){
                    Log.d(tag,"Attention : crypt data not active >>");
                    Log.d(tag,"your App it be compiled by reverse engine !! i recommended to use crypt data");
                }
            }
        }
    }

    private void cryptData(){

        if (BuildConfig.DEBUG){
        try {
            String link = Hexing.setStrings(GFX.JSONLINK_on);
            String admobId = Hexing.setStrings(GFX.AdMob_Id);
            String banner_admob = Hexing.setStrings(GFX.Banner_AdMob);
            String interstitial_admob = Hexing.setStrings(GFX.Interstitial_AdMob);
            String native_admob = Hexing.setStrings(GFX.NativeAds_AdMob);
            String banner_facebook = Hexing.setStrings(GFX.Banner_AdUnit_Facebook);
            String interstitial_facebook = Hexing.setStrings(GFX.Interstitial_AdUnit_Facebook);
            String native_facebook = Hexing.setStrings(GFX.NativeAds_Facebook);

            if (BuildConfig.DEBUG){
                Log.d(tag,"new Link crypt is : "+link);
                Log.d(tag,"*******************AdMob ID crypt***********************");
                Log.d(tag,"AdMob id is : "+admobId);
                Log.d(tag,"Banner AdMob crypt is : "+banner_admob);
                Log.d(tag,"Interstitial AdMob crypt is : "+interstitial_admob);
                Log.d(tag,"Native AdMob crypt is : "+native_admob);
                Log.d(tag,"*******************Facebook ID crypt***********************");
                Log.d(tag,"Banner Facebook crypt is : "+banner_facebook);
                Log.d(tag,"Interstitial Facebook crypt is : "+interstitial_facebook);
                Log.d(tag,"Native Facebook crypt is : "+native_facebook);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        }
    }

    private void buildRequestAd(){

        if (GFX.useONLINE_ADNETWORK){
            try {
                if (GFX.useCRYPTDATA){
                    onAdOnline(Hexing.getStrings(GFX.JSONLINK_on));
                }else {
                    onAdOnline(GFX.JSONLINK_on);
                }
                if (BuildConfig.DEBUG){
                    Log.d("motya","use ads online");
                }

            }catch (Exception e){}
            return;
        }
        onAdOffline();
        if (BuildConfig.DEBUG){
            Log.d("motya","use ads offline");
        }
    }

    private void onAdOffline(){

        if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
            try {

                if (GFX.useCRYPTDATA){
                    RequestAdMobAd(Hexing.getStrings(GFX.Banner_AdMob),
                                  Hexing.getStrings(GFX.Interstitial_AdMob));
                }else {
                    RequestAdMobAd(GFX.Banner_AdMob,GFX.Interstitial_AdMob);
                }

            }catch (Exception e){
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "onAdOnline AdMob Build Failed causes :"+e);
                }
            }
        }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
            try {

                if (GFX.useCRYPTDATA){
                    RequestFacebookAd(
                            Hexing.getStrings(GFX.Banner_AdUnit_Facebook),
                            Hexing.getStrings(GFX.Interstitial_AdUnit_Facebook));
                }else {
                    RequestFacebookAd(GFX.Banner_AdUnit_Facebook,GFX.Interstitial_AdUnit_Facebook);
                }

            }catch (Exception e){
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "onAdOnline Facebook Build Failed causes :"+e);
                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void onAdOnline(String link){
        new SynAd(getApplicationContext(),link) {
            @Override
            protected void onDataPreExecute() {
                if (BuildConfig.DEBUG){
                    Log.d("motya","onAdOnline loading...");
                }
            }

            @Override
            protected void onDataExecute(String result, String status) {
                if (BuildConfig.DEBUG){
                    Log.d("motya","onAdOnline status is "+status);
                }
            }

            @Override
            protected void onAdResult(String networkDefault, String adMobID, String bannerAdMob, String interstitialAdMob, String nativeAdMob, String bannerFacebook, String interstitialFacebook, String nativeFacebook) {

                defualtAd = networkDefault;
                AdMobID = adMobID;
                natAdMob = nativeAdMob;
                natfacebook = nativeFacebook;
                if (networkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                    try {
                        RequestAdMobAd(bannerAdMob,interstitialAdMob);
                    }catch (Exception e){
                        if (BuildConfig.DEBUG){
                            Log.d("motya" , "onAdOnline AdMob Build Failed causes :"+e);
                        }
                    }
                }else if (networkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                    try {
                        RequestFacebookAd(bannerFacebook,interstitialAdMob);
                    }catch (Exception e){
                        if (BuildConfig.DEBUG){
                            Log.d("motya" , "onAdOnline Facebook Build Failed causes :"+e);
                        }
                    }
                }
            }
        }.execute();

    }

    public void RequestAdMobAd(String banner,String Interstitial){
        // Initialize the AdMob Network SDK :
        MobileAds.initialize(this);

        //Load AdMob Interstitial :
        mInterstitialAdMob = new InterstitialAd(this);
        mInterstitialAdMob.setAdUnitId(Interstitial);
        final AdRequest adRequestInterstitial =  new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mInterstitialAdMob.loadAd(adRequestInterstitial);
        mInterstitialAdMob.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Interstitial Loaded");
                }

            }


            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Interstitial onAdFailedToLoad");
                }


            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAdMob.loadAd(adRequestInterstitial);
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Interstitial onAdClosed");
                }

            }
        });



        mAdViewAdMob = new AdView(this);
        mAdViewAdMob.setAdSize(AdSize.SMART_BANNER);
        mAdViewAdMob.setAdUnitId(banner);
        AdRequest adRequestBanner =  new AdRequest
                .Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdViewAdMob.loadAd(adRequestBanner);
        mAdViewAdMob.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Banner on Loaded");
                }

                if (relativeLayout != null){
                    setBannerAd(relativeLayout);
                }

            }
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Failed to load Banner");
                }


            }
        });

    }

    public void RequestFacebookAd(String banner, String Interstitial){
        // Initialize the Audience Network SDK :
        AudienceNetworkAds.initialize(this);

        //Load Facebook BannerAd :
        mAdViewFacebook = new com.facebook.ads.AdView(this, banner, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        mAdViewFacebook.loadAd();
        mAdViewFacebook.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Banner Facebook on Failed Loaded");
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Banner Facebook on Loaded");
                }
                if (relativeLayout != null){
                    setBannerAd(relativeLayout);
                }
            }

            @Override
            public void onAdClicked(Ad ad) {}
            @Override
            public void onLoggingImpression(Ad ad) {}
        });

        //Load Facebook InterstitialAd :
        mInterstitialFacebook = new com.facebook.ads.InterstitialAd(this, Interstitial);
        mInterstitialFacebook.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad){}
            @Override
            public void onInterstitialDismissed(Ad ad){
                mInterstitialFacebook.loadAd();
            }
            @Override
            public void onError(Ad ad, AdError adError){
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Interstitial Facebook on Failed Loaded");
                }

            }
            @Override
            public void onAdClicked(Ad ad){}
            @Override
            public void onLoggingImpression(Ad ad){}
            @Override
            public void onAdLoaded(Ad ad) {
                if (BuildConfig.DEBUG){
                    Log.d("motya" , "Interstitial Facebook Loaded");
                }

            }
        });
        mInterstitialFacebook.loadAd();
    }

    // Show Interstitial After Loading :
    public void showInterstitial(){
        if (GFX.useONLINE_ADNETWORK){
            buildInterstitial(defualtAd);
            if (BuildConfig.DEBUG){
                Log.d("motya","Show Interstitial Online network using is : "+defualtAd);
            }

        }else {
            buildInterstitial(GFX.NetworkDefault);
            if (BuildConfig.DEBUG){
                Log.d("motya","Show Interstitial Offline network using is : "+GFX.NetworkDefault);
            }
        }

    }

    // Show Banner in RelativeLayout :
    public void setBannerAd(RelativeLayout bannerView) {

        if (GFX.useONLINE_ADNETWORK){
            buildBanner(defualtAd,bannerView);
            if (BuildConfig.DEBUG){
                Log.d("motya","Show Banner Online network using is : "+defualtAd);
            }
        }else {
            buildBanner(GFX.NetworkDefault,bannerView);
            if (BuildConfig.DEBUG){
                Log.d("motya","Show Banner Offline network using is : "+GFX.NetworkDefault);
            }
        }

    }

    public void onDestroyBanner(){
       /* if (mAdViewAdMob != null) {
            mAdViewAdMob.destroy();
        }
        if (mAdViewFacebook != null) {
            mAdViewFacebook.destroy();
        }
        if (mInterstitialFacebook != null) {
            mInterstitialFacebook.destroy();
        }*/
    }
    private void buildInterstitial(String checking){

        if (checking.equals(GFX.Tags.AdMob)) {
            // Show Interstitial AdMob After Loading
            if (mInterstitialAdMob != null && mInterstitialAdMob.isLoaded()) {
                mInterstitialAdMob.show();
            }
        } else if (checking.equals(GFX.Tags.Facebook)) {
            // Show Interstitial Facebook After Loading
            if (mInterstitialFacebook != null && mInterstitialFacebook.isAdLoaded()) {
                mInterstitialFacebook.show();
            }
        }

    }

    private void buildBanner(String check,RelativeLayout relativeLayout){

        if (check.equals(GFX.Tags.AdMob)){

            if (mAdViewAdMob == null) {
                return;
            }
            if (mAdViewAdMob.getParent() != null){
                ((ViewGroup) mAdViewAdMob.getParent()).removeView(mAdViewAdMob);
            }
            relativeLayout.removeAllViews();
            relativeLayout.addView(mAdViewAdMob);
            relativeLayout.invalidate();

        }else if (check.equals(GFX.Tags.Facebook)){

            if (mAdViewFacebook == null) {
                return;
            }
            if (mAdViewFacebook.getParent() != null){
                ((ViewGroup) mAdViewFacebook.getParent()).removeView(mAdViewFacebook);
            }
            relativeLayout.removeAllViews();
            relativeLayout.addView(mAdViewFacebook);
            relativeLayout.invalidate();


        }


    }

    public static void mediaPlayerInitializer(){
        try {
            player = MediaPlayer.create(getAppContext(), R.raw.music_bg);
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setLooping(true);
            player.setVolume(1f, 1f);
        } catch (IllegalStateException e) {
            if (BuildConfig.DEBUG){
                Log.d("music","somthing wrong about music "+e);
            }
            e.printStackTrace();
        }
    }

    public static void PlayMusic(){
        try {
            if (SettingsPreferences.checkMusic(mContext)&&!player.isPlaying()) {
                player.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            if (BuildConfig.DEBUG){
                Log.d("music","somthing wrong about music "+e);
            }
            mediaPlayerInitializer();
            player.start();
        }
    }

    public static void ClickSounds(Context context , @RawRes final  int mRaw){
        mMediaPlayer = MediaPlayer.create(getAppContext(), mRaw);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setVolume(0.2f, 0.2f);
        try{
            mMediaPlayer.prepare();

        }catch (Exception e){
            e.printStackTrace();
            if (BuildConfig.DEBUG){
                Log.d("motya","somthing wrong about music "+e);
            }
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mMediaPlayer = null;
            }
        });
        mMediaPlayer.start();
    }

    public static void StopSound(){
        try {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
            }
        }catch (Exception e){
            if (BuildConfig.DEBUG){
                Log.d("music","somthing wrong about music "+e);
            }
        }

    }

    public static void StopMuisc() {
        try {
            if (player.isPlaying()) {
                player.pause();
            }
        }catch (Exception e){
            if (BuildConfig.DEBUG){
                Log.d("music","somthing wrong about music "+e);
            }
        }


    }

    public void CheckMusic(){
        if (SettingsPreferences.checkMusic(getApplicationContext())) {
            try {
                PlayMusic();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }else {
            try {
                StopMuisc();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setContext(Context context) {
        mContext = context;
    }

    public static Context getAppContext() {
        return mContext;
    }



}

