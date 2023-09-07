package com.zonek.apps.gfxherror.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.zonek.apps.gfxherror.Adapters.AdapterTips;
import com.zonek.apps.gfxherror.Adapters.AdapterTipsF;
import com.zonek.apps.gfxherror.Applications.MyApplication;
import com.zonek.apps.gfxherror.Async.SynData;
import com.zonek.apps.gfxherror.BuildConfig;
import com.zonek.apps.gfxherror.GFX;
import com.zonek.apps.gfxherror.Helper.Hexing;
import com.zonek.apps.gfxherror.Helper.SettingsPreferences;
import com.zonek.apps.gfxherror.Models.ModelsTips;
import com.zonek.apps.gfxherror.R;
import com.zonek.apps.gfxherror.UI.ImageViews;
import com.zonek.apps.gfxherror.UI.Particles;

import java.util.ArrayList;
import java.util.List;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
*/

public class ActivityTips extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageViews back;
    private AdapterTips adapterTips ;
    private AdapterTipsF adapterTipsF;
    private LinearLayout searching, failed;
    private Button tryAgain;
    private Particles particles ;
    private List<UnifiedNativeAd> mNativeAds = new ArrayList<>();
    private AdLoader adLoader;
    private NativeAdsManager mNativeAdsManager;
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initializeUI();

    }

    private void initializeUI(){

        recyclerView = findViewById(R.id.recyclerviewTips);
        back = findViewById(R.id.ic_back);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        searching = findViewById(R.id.searching);
        failed = findViewById(R.id.failed);
        tryAgain = findViewById(R.id.tryAgain);
        particles = findViewById(R.id.particles);
        myApplication = (MyApplication)getApplicationContext();
        if (GFX.useParticles){
            particles.pause();
            particles.setVisibility(View.VISIBLE);
        }

        fetchDataOffline();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClicksound();
                finish();
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

    private void fetchDataOffline(){
        if (GFX.ONLINE_OFFLINE){
            if (GFX.useCRYPTDATA){
                try {
                    setDataOnline(Hexing.getStrings(GFX.JSONLINK_on));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                setDataOnline(GFX.JSONLINK_on);
            }
            return;
        }
        setDataOffline();
    }


    @SuppressLint("StaticFieldLeak")
    private void setDataOffline(){

        new SynData(getApplicationContext()) {
            @Override
            protected void onDataPreExecute() {
                showLoading(true);
                if (BuildConfig.DEBUG){
                    Log.d("motya","Fetch Offline loading...");
                }
            }

            @Override
            protected void onDataExecute(String result,List<Object> objects,String status,int size) {
                if (status.equalsIgnoreCase(GFX.Tags.DONE)){
                    getAdapterData(objects,size);
                    showLoading(false);
                    return;
                }
                showFailed();
            }

        }.execute();

    }


    @SuppressLint("StaticFieldLeak")
    private void setDataOnline(String link){

        new SynData(getApplicationContext(),link) {
            @Override
            protected void onDataPreExecute() {
                showLoading(true);
                if (BuildConfig.DEBUG){
                    Log.d("motya","Fetch Online loading...");
                }
            }
            @Override
            protected void onDataExecute(String result,List<Object> objects,String status,int size) {
                if (status.equalsIgnoreCase(GFX.Tags.DONE)){

                    if (GFX.showNativeAdInList){

                        if (GFX.useONLINE_ADNETWORK){
                            if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.AdMob)){
                                loadNatAdmob(myApplication.natAdMob,objects,size);
                            }else if (myApplication.defualtAd.equalsIgnoreCase(GFX.Tags.Facebook)){
                                loadNatFacebook(myApplication.natfacebook,objects,size);
                            }
                        }else {
                            if (GFX.useCRYPTDATA){

                                if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                                    try {
                                        loadNatAdmob(Hexing.getStrings(GFX.NativeAds_AdMob),objects,size);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                                    try {
                                        loadNatAdmob(Hexing.getStrings(GFX.NativeAds_Facebook),objects,size);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else {
                                if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.AdMob)){
                                    loadNatAdmob(GFX.NativeAds_AdMob,objects,size);
                                }else if (GFX.NetworkDefault.equalsIgnoreCase(GFX.Tags.Facebook)){
                                    loadNatAdmob(GFX.NativeAds_Facebook,objects,size);
                                }
                            }
                        }

                    }else {
                        getAdapterData(objects,size);
                        showFailed();
                    }

                }

            }


        }.execute();

    }


    private void loadNatAdmob(final String nativeId,final List<Object> objects,final int size){

        mNativeAds.clear();;
        if (GFX.forceNative){
            AdLoader.Builder builder = new AdLoader.Builder(getApplicationContext(), nativeId);
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            mNativeAds.add(unifiedNativeAd);
                            if (BuildConfig.DEBUG){
                                Log.d("motya","Native AdMob loaded");
                            }

                            mNativeAds.add(unifiedNativeAd);
                            if (BuildConfig.DEBUG){
                                Log.d("motya","Native AdMob loaded");
                            }
                            if (!adLoader.isLoading()) {
                                getAdapterData(objects,size);
                                showLoading(false);
                                insertAdsInMenuItems(objects);
                            }

                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            if (BuildConfig.DEBUG){
                                Log.d("motya","Native AdMob Failed to load");
                            }
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(objects);
                            }
                        }
                    }).build();
            int number = objects.size() / GFX.ListSize_division_nativeAd;
            adLoader.loadAds(new AdRequest.Builder().build(),number);
           // adLoader.loadAds(new AdRequest.Builder().build(),GFX.number_of_native_admob);



        }else {
            getAdapterData(objects,size);
            showLoading(false);
            AdLoader.Builder builder = new AdLoader.Builder(getApplicationContext(), nativeId);
            adLoader = builder.forUnifiedNativeAd(
                    new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            mNativeAds.add(unifiedNativeAd);
                            if (BuildConfig.DEBUG){
                                Log.d("motya","Native AdMob loaded");
                            }
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(objects);
                            }
                        }
                    }).withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int errorCode) {
                            if (BuildConfig.DEBUG){
                                Log.d("motya","Native AdMob Failed to load");
                            }
                            if (!adLoader.isLoading()) {
                                insertAdsInMenuItems(objects);
                            }
                        }
                    }).build();
            int number = objects.size() / GFX.ListSize_division_nativeAd;
            adLoader.loadAds(new AdRequest.Builder().build(), number);
            //adLoader.loadAds(new AdRequest.Builder().build(),GFX.number_of_native_admob);
        }

    }

    private void loadNatFacebook(String nativeId, final List<Object> objects,final int size){

        if (GFX.forceNative){
            mNativeAdsManager = new NativeAdsManager(getApplicationContext(), nativeId, 5);
            mNativeAdsManager.loadAds();
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    getAdapterDataFb(objects,size,mNativeAdsManager);
                    showLoading(false);
                    if (BuildConfig.DEBUG){
                        Log.d("motya","Native Facebook loaded");
                    }
                }
                @Override
                public void onAdError(AdError adError) {
                    if (BuildConfig.DEBUG){
                        Log.d("motya","Native Facebook Failed to load");
                    }
                    getAdapterData(objects,size);
                    showLoading(false);
                }
            });

        }else {
            mNativeAdsManager = new NativeAdsManager(getApplicationContext(), nativeId, 5);
            mNativeAdsManager.loadAds();
            mNativeAdsManager.setListener(new NativeAdsManager.Listener() {
                @Override
                public void onAdsLoaded() {
                    if (BuildConfig.DEBUG){
                        Log.d("motya","Native Facebook loaded");}
                }
                @Override
                public void onAdError(AdError adError) {
                    if (BuildConfig.DEBUG){
                        Log.d("motya","Native Facebook Failed to load");}
                    getAdapterData(objects,size);
                }
            });
            showLoading(false);
            getAdapterDataFb(objects,size,mNativeAdsManager);
        }





    }

    private void getAdapterData(List<Object> objects, final int size){
        adapterTips = new AdapterTips(getApplicationContext(),objects);
        recyclerView.setAdapter(adapterTips);
        adapterTips.setOnClickItems(new AdapterTips.OnClickItems() {
            @Override
            public void onClick(View view, List<Object> objects, int position) {

                ModelsTips modelsTips = (ModelsTips)objects.get(position);
                String preview = modelsTips.getPreview();
                String jsonPosition = modelsTips.getPosition();
                Intent intent = new Intent(getApplicationContext(),ActivityContent.class);
                intent.putExtra(ActivityContent.ExtraPosition, jsonPosition);
                intent.putExtra(ActivityContent.ExtraSize , size);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ShowInterstitial();
                setClicksound();


            }
        });
    }

    private void getAdapterDataFb(List<Object> objects, final int size,NativeAdsManager mNativeAdsManager){
        adapterTipsF = new AdapterTipsF(getApplicationContext(),objects,mNativeAdsManager);
        recyclerView.setAdapter(adapterTipsF);
        adapterTipsF.setOnClickItems(new AdapterTipsF.OnClickItems() {
            @Override
            public void onClick(View view, List<Object> objects, int position) {

                ModelsTips modelsTips = (ModelsTips)objects.get(position);
                String preview = modelsTips.getPreview();
                String jsonPosition = modelsTips.getPosition();
                Intent intent = new Intent(getApplicationContext(),ActivityContent.class);
                intent.putExtra(ActivityContent.ExtraPosition, jsonPosition);
                intent.putExtra(ActivityContent.ExtraSize , size);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ShowInterstitial();
                setClicksound();


            }
        });
    }

    private void showLoading(boolean isLoading){
        if (isLoading){
            setVisible(recyclerView, false);
            setVisible(searching, true);
            setVisible(failed, false);
            return;
        }
        setVisible(recyclerView, true);
        setVisible(searching, false);
        setVisible(failed, false);
    }

    private void showFailed(){
        setVisible(recyclerView, false);
        setVisible(searching, false);
        setVisible(failed ,true);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClicksound();
                fetchDataOffline();
            }
        });

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

    private void insertAdsInMenuItems(List<Object> fetchList) {
        if (mNativeAds.size() <= 0) {
            return;
        }

        try {
            int offset = (fetchList.size() / mNativeAds.size()) + 1;
            int index = 1;
            for (UnifiedNativeAd ad : mNativeAds) {
                fetchList.add(index, ad);
                index = index + offset;
            }
            adapterTips.notifyDataSetChanged();

        }catch (Exception e){
            if (BuildConfig.DEBUG){
                Log.d("motya","Native not replace correct : "+e);
            }
        }


    }


    @Override
    protected void onDestroy() {
        myApplication.onDestroyBanner();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        InitializeAds();
        super.onResume();
    }
}