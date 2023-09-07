package com.zonek.apps.gfxherror.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.zonek.apps.gfxherror.GFX;
import com.zonek.apps.gfxherror.Holders.HolderList;
import com.zonek.apps.gfxherror.Holders.UnifiedNativeAdViewHolder;
import com.zonek.apps.gfxherror.Models.ModelsTips;
import com.zonek.apps.gfxherror.R;

import java.util.List;

/**
 * This project create by SAID MOTYA on 06/17/2020.
 * contact on Facebook : https://web.facebook.com/motya.said
 * contact on Email : zonek.app@hotmail.com or zonek.app@gmail.com
 * it a free code source for member secret gfx
 */

public class AdapterTips extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int View_List = 0;
    private static final int View_NativeAd = 1;
    private Context context;
    private List<Object> objects;
    private ModelsTips modelsTips;
    private HolderList holderList ;
    private OnClickItems onClickItems ;
    private String paths ;

    public AdapterTips(Context context, List<Object> objects) {
        this.context = context;
        this.objects = objects;
    }

    public void setOnClickItems(OnClickItems onClickItems) {
        this.onClickItems = onClickItems;
    }
    public interface OnClickItems{
        void onClick(View view, List<Object> objects, int position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case View_NativeAd:
                return new UnifiedNativeAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_ad_unified, parent,false));
            case View_List:
                // Fall through.
            default:
                return new HolderList(LayoutInflater.from(parent.getContext()),parent);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        setBindView(holder,position);
    }


    private void setBindView(@NonNull RecyclerView.ViewHolder holder, final int position){

        final int viewType = getItemViewType(position);
        switch (viewType) {
            case View_NativeAd:
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) objects.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case View_List:
            default:
                onBindData(holder, position);

        }




    }

    private void onBindData(@NonNull RecyclerView.ViewHolder holder, final int position){

        holderList = (HolderList)holder;
        modelsTips = (ModelsTips)objects.get(position);
        paths = GFX.assetsPath+ GFX.FOLDER_PREVIEW+"/"+modelsTips.getPreview() ;

        holderList.title.setText(""+modelsTips.getTitle());
        holderList.counter.setText(""+modelsTips.getPosition());
        holderList.relativeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickItems !=null){
                    onClickItems.onClick(v,objects,position);
                }
            }
        });

        holderList.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (modelsTips.getPreview().startsWith(GFX.Tags.HTTP)){
            Glide.with(context)
                    .load(modelsTips.getPreview())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holderList.preview);
            return;
        }
        Glide.with(context)
                .load(paths)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holderList.preview);


    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = objects.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return View_NativeAd;
        }
        return View_List;
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }


    private void populateNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        NativeAd.Image icon = nativeAd.getIcon();
        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
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
    }


}
