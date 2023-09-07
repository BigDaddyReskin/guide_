package com.zonek.apps.gfxherror.Activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.zonek.apps.gfxherror.Applications.MyApplication;
import com.zonek.apps.gfxherror.Async.SynContent;
import com.zonek.apps.gfxherror.BuildConfig;
import com.zonek.apps.gfxherror.GFX;
import com.zonek.apps.gfxherror.Helper.Hexing;
import com.zonek.apps.gfxherror.Helper.SettingsPreferences;
import com.zonek.apps.gfxherror.Models.ModelsTips;
import com.zonek.apps.gfxherror.R;
import com.zonek.apps.gfxherror.UI.ImageViews;
import com.zonek.apps.gfxherror.UI.Particles;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */

public class ActivityContent extends AppCompatActivity {

    public static String ExtraPosition = "extraPosition";
    public static String ExtraSize = "extraSize";
    private String getPosition ;
    private int getExtrasize;
    private LinearLayout linearLayout ;
    private LinearLayout.LayoutParams params ;

    private FrameLayout.LayoutParams params2;
    private FrameLayout.LayoutParams paramsButton;
    private Typeface typeface;
    private ImageView back ;
    private LinearLayout searching, failed;
    private Button tryAgain;
    private TextView title ;
    private RelativeLayout relative_next;
    private ImageViews next,preview;
    private int counter;
    private int AdsCounter = 1;
    private Particles particles ;
    private MyApplication myApplication;
    private UnifiedNativeAd nativeAd;
    private NativeAd nativeAdFacebook;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        getPosition = getIntent().getStringExtra(ExtraPosition);
        getExtrasize = getIntent().getIntExtra(ExtraSize, 0);
        counter = Integer.parseInt(getPosition);
        if (BuildConfig.DEBUG){
            Log.d("motya","getExtrasize = "+getExtrasize);
        }
        back = findViewById(R.id.ic_back);
        linearLayout= findViewById(R.id.content);
        searching = findViewById(R.id.searching);
        failed = findViewById(R.id.failed);
        tryAgain = findViewById(R.id.tryAgain);
        title = findViewById(R.id.title);
        relative_next = findViewById(R.id.relative_next);
        next = findViewById(R.id.next);
        preview = findViewById(R.id.preview);
        particles = findViewById(R.id.particles);
        myApplication = (MyApplication)getApplicationContext();
        InitializeCounterNext();
        if (!GFX.ShowNEXT_PREVIEW){
            relative_next.setVisibility(View.GONE);
        }
        if (GFX.useParticles){
            particles.pause();
            particles.setVisibility(View.VISIBLE);
        }
        typeface = Typeface.createFromAsset(getAssets(),GFX.ApplicationFont);
        title.setText(GFX.tips+" "+getPosition);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClicksound();
                finish();


            }
        });
        connectBridge(Integer.parseInt(getPosition));
        InitializeAds();

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

    private void InitializeCounterNext() {

        if (getExtrasize == counter) {
            setVisible(preview, true);
            setVisible(next, false);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialCounter();
                counter++;
                setClicksound();
                if (getExtrasize < counter) {
                    setVisible(next, false);
                } else {
                    setVisible(preview, true);
                    if (counter == getExtrasize) {
                        setVisible(preview, true);
                        setVisible(next, false);
                    }
                    connectBridge(counter);
                }

            }
        });

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitialCounter();
                counter--;
                setClicksound();
                if (counter > 0) {
                    setVisible(next, true);
                    connectBridge(counter);
                    if (counter == 1) {
                        setVisible(preview, false);
                        setVisible(next, true);
                    }
                } else {
                    setVisible(preview, false);
                }

            }
        });

    }

    private void showInterstitialCounter(){
        if (GFX.ShowAdsWhenClickNext) {
            if (AdsCounter < GFX.NumberofAdsCounter) {
                AdsCounter++;
            } else {
                AdsCounter = 0;
                 ShowInterstitial();
            }
        }
    }

    private void connectBridge(int position){
        title.setText(GFX.tips+" "+position);
        linearLayout.removeAllViews();
        try {
            if(GFX.useCRYPTDATA){
                builContent(Hexing.getStrings(GFX.JSONLINK_on),String.valueOf(position));
            }else {
                builContent(GFX.JSONLINK_on,String.valueOf(position));
            }

        }catch (Exception e){

        }
    }

    private void builContent(String link ,String content){
        if (GFX.ONLINE_OFFLINE){
            setBridgeOnline(link,content);
            return;
        }
        setBridgeOffline(content);
    }

    @SuppressLint("StaticFieldLeak")
    private void setBridgeOffline(String position){

        new SynContent(getApplicationContext(),position) {

            @Override
            protected void onDataPreExecute() {
                showLoading(true);
                if (BuildConfig.DEBUG){
                    Log.d("motya","Content Offline loading...");
                }
            }
            @Override
            protected void onDataExecute(String result, List<Object> objects,String status) {
                if (status.equalsIgnoreCase(GFX.Tags.DONE)){
                    setContent(objects);
                    showLoading(false);
                    return;
                }
                showFailed();

            }
        }.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private void setBridgeOnline(String link, String position){

        new SynContent(getApplicationContext(),link,position) {

            @Override
            protected void onDataPreExecute() {
                showLoading(true);
                if (BuildConfig.DEBUG){
                    Log.d("motya","Content Online loading...");
                }
            }
            @Override
            protected void onDataExecute(String Result, List<Object> objects,String status) {
                if (status.equalsIgnoreCase(GFX.Tags.DONE)){
                    setContent(objects);
                    showLoading(false);
                    return;
                }
                showFailed();

            }
        }.execute();

    }

    private void showLoading(boolean isLoading){
        if (isLoading){
            setVisible(linearLayout, false);
            setVisible(searching, true);
            setVisible(failed, false);
            return;
        }
        setVisible(linearLayout, true);
        setVisible(searching, false);
        setVisible(failed, false);
    }

    private void showFailed(){
        setVisible(linearLayout, false);
        setVisible(searching, false);
        setVisible(failed ,true);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClicksound();
                connectBridge(counter);
            }
        });

    }


    private void setContent(List<Object> contentData){

        for (int i = 0 ; i< contentData.size(); i++){

        TextView textView = new TextView(this);
        ImageView imageView = new ImageView(this);
        Button button = new Button(this);
        FrameLayout frameLayout = new FrameLayout(this);
        int wrap = LinearLayout.LayoutParams.WRAP_CONTENT;
        int match = MATCH_PARENT;

        params = new LinearLayout.LayoutParams(match,match);
        params2 = new FrameLayout.LayoutParams(wrap,wrap);
        paramsButton = new FrameLayout.LayoutParams(match,wrap);


        textView.setLayoutParams(params);
        imageView.setLayoutParams(params);
        button.setLayoutParams(paramsButton);
        frameLayout.setLayoutParams(params2);


        params.bottomMargin = 10 ;
        params2.bottomMargin = 20;
        paramsButton.bottomMargin = 30 ;
        paramsButton.gravity = Gravity.CENTER ;
        button.setPadding(20,0,20,0);

        final ModelsTips models = (ModelsTips) contentData.get(i);
        String paths = GFX.assetsPath+ models.getImage();
        if (GFX.ONLINE_OFFLINE){
            Glide.with(getApplicationContext())
                    .load(models.getImage())
                    .override(1000,700)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

        }else {
            Glide.with(getApplicationContext())
                    .load(paths)
                    .override(1000,700)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);

        }

        imageView.setAdjustViewBounds(true);
        textView.setText(models.getContent());
        textView.setPadding(Integer.parseInt(models.getLeft()),10,0,30);

        setStyle(textView, models.getStyle());
        setGravity(textView,models.getGravity());
        textView.setTextColor(Color.parseColor(models.getColor()));
        textView.setTextSize(Float.parseFloat(models.getText_size()));
        textView.setTypeface(typeface);
        button.setText(models.getLinkTitle());
        button.setTextColor(getResources().getColor(R.color.white));
        button.setBackgroundResource(R.drawable.btn_content);
        button.setGravity(Gravity.CENTER);
        button.setTypeface(typeface,Typeface.BOLD);

        if (models.getOrder().equals("image_text")){

            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            linearLayout.addView(frameLayout);

            if (models.getSetNativeAds().equals(GFX.Tags.True)){

                if(GFX.ONLINE_OFFLINE){
                    if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.AdMob)){

                        loadNativeAdMob(frameLayout,myApplication.natAdMob);
                        if (BuildConfig.DEBUG){
                            Log.d("motya","Native ADMOB : "+myApplication.natAdMob);
                        }

                    }else if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.Facebook)){
                        loadNativeAdFB(frameLayout,myApplication.natfacebook);
                        if (BuildConfig.DEBUG){
                            Log.d("motya","Native Facebook : "+myApplication.natfacebook);
                        }
                    }

                }else {
                    if (GFX.useCRYPTDATA){

                        if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                            try {
                                loadNativeAdMob(frameLayout, Hexing.getStrings(GFX.NativeAds_AdMob));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                            try {
                                loadNativeAdFB(frameLayout,Hexing.getStrings(GFX.NativeAds_Facebook));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }else {
                        if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                            loadNativeAdMob(frameLayout,GFX.NativeAds_AdMob);
                        }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                            loadNativeAdFB(frameLayout,GFX.NativeAds_Facebook);
                        }
                    }
                }

            }

            if (models.getIsLink().equals(GFX.Tags.True)){
                linearLayout.addView(button);
            }

        }else if (models.getOrder().equals("text_image")){

            linearLayout.addView(textView);
            linearLayout.addView(imageView);
            linearLayout.addView(frameLayout);

            if (models.getSetNativeAds().equals(GFX.Tags.True)){
                if(GFX.ONLINE_OFFLINE){
                    if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.AdMob)){

                        loadNativeAdMob(frameLayout,myApplication.natAdMob);
                        if (BuildConfig.DEBUG){
                            Log.d("motya","Native ADMOB : "+myApplication.natAdMob);
                        }

                    }else if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.Facebook)){
                        loadNativeAdFB(frameLayout,myApplication.natfacebook);
                        if (BuildConfig.DEBUG){
                            Log.d("motya","Native Facebook : "+myApplication.natfacebook);
                        }
                    }

                }else {
                    if (GFX.useCRYPTDATA){

                        if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                            try {
                                loadNativeAdMob(frameLayout, Hexing.getStrings(GFX.NativeAds_AdMob));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                            try {
                                loadNativeAdFB(frameLayout,Hexing.getStrings(GFX.NativeAds_Facebook));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }else {
                        if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                            loadNativeAdMob(frameLayout,GFX.NativeAds_AdMob);
                        }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                            loadNativeAdFB(frameLayout,GFX.NativeAds_Facebook);
                        }
                    }
                }
            }

            if (models.getIsLink().equals(GFX.Tags.True)){
                linearLayout.addView(button);
            }
        }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setClicksound();
                    if (models.getIsLink().equals(GFX.Tags.True)){
                        openLink(models.getSetLink());
                    }
                }
            });


        }

    }

    private boolean isAvailable(Intent intent) {
        final PackageManager mgr = getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private void setStyle(TextView textView , String style){
        switch (style){
            case "blood":
                textView.setTypeface(null, Typeface.BOLD);
                break;
            case "normal":
                textView.setTypeface(null, Typeface.NORMAL);
                break;
            case "italic":
                textView.setTypeface(null, Typeface.ITALIC);
                break;
        }

    }

    private void setGravity(TextView textView, String gravity){
        switch (gravity){
            case "center":
                textView.setGravity(Gravity.CENTER);
                break;
            case "right":
                textView.setGravity(Gravity.RIGHT);
                break;
            case "left":
                textView.setGravity(Gravity.LEFT);
                break;
        }
    }

    private void openGame(String pkg){
        Intent intent = getPackageManager().getLaunchIntentForPackage(pkg);
        intent.setData(Uri.parse(GFX.Tags.MARKET+pkg));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GFX.Tags.GGAPPS+pkg)));
        }
    }

    private void openLink(String link){

        if (link.startsWith(GFX.Tags.HTTP)||link.startsWith(GFX.Tags.WWW)){
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(link));
            if (isAvailable(intent)){
                startActivity(intent);
                return;
            }
            ShowInterstitial();
            Toast.makeText(getApplicationContext(),GFX.open_link_failed, Toast.LENGTH_SHORT).show();
        }else {
            if (checkAppInDevice(link)){
                openGame(link);
            }else {
                ShowInterstitial();
                Toast.makeText(getApplicationContext(),GFX.open_package_failed, Toast.LENGTH_SHORT).show();
            }
        }

    }

    private boolean checkAppInDevice(String string){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(string  ,PackageManager.GET_ACTIVITIES);

        }catch (PackageManager.NameNotFoundException e){
            return false ;
        }
        return true ;
    }

    private void setVisible(View view, boolean isVisible){
        if (isVisible){
            view.setVisibility(View.VISIBLE);
            return;
        }
        view.setVisibility(View.GONE);
    }

    private void setClicksound(){
        if (GFX.useMusic){
            if (SettingsPreferences.checkSound(getApplicationContext())) {
                myApplication.ClickSounds(getApplicationContext(),R.raw.click);
            }
        }

    }


    private void loadNativeAdMob(final FrameLayout frameLayout, String nativeAds) {

        AdLoader.Builder builder = new AdLoader.Builder(this, nativeAds);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.ad_unified, null);
                inflateAdMob(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
                if (BuildConfig.DEBUG){
                    Log.d("motya","Native AdMob loaded");
                }

            }
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (BuildConfig.DEBUG){
                    Log.d("motya","Native AdMob failed to load");
                }

            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());


    }

    private void inflateAdMob(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getVideoController();


    }

    private void loadNativeAdFB(final FrameLayout frameLayout , String nativeID) {

        final NativeAd nativeAdFacebook = new NativeAd(this, nativeID);
        nativeAdFacebook.setAdListener(new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {}
            @Override
            public void onError(Ad ad, AdError adError) {
                if (BuildConfig.DEBUG){
                    Log.d("motya","Native Facebook Failed to load");
                }
            }
            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAdFacebook == null || nativeAdFacebook != ad) {
                    return;
                }
                try {
                    inflateAdFacebook(frameLayout,nativeAdFacebook);
                }catch (Exception e){

                    if (BuildConfig.DEBUG){
                        Log.d("motya","somthing wrong about native : "+e);
                    }

                }

                if (BuildConfig.DEBUG){
                    Log.d("motya","Native Facebook loaded");
                }
            }
            @Override
            public void onAdClicked(Ad ad) {}
            @Override
            public void onLoggingImpression(Ad ad) {}

        });
        nativeAdFacebook.loadAd();
    }

    private void inflateAdFacebook(FrameLayout frameLayout,NativeAd nativeAd) {

        nativeAd.unregisterView();
        NativeAdLayout nativeAdLayout = new NativeAdLayout(getApplicationContext());
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        // frameLayout.removeAllViews();
        frameLayout.addView(nativeAdLayout);
        View adView = NativeAdView.render(getApplicationContext(), nativeAd);
        nativeAdLayout.addView(adView, new ViewGroup.LayoutParams(MATCH_PARENT, 500));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        myApplication.onDestroyBanner();
        super.onDestroy();
    }

}


