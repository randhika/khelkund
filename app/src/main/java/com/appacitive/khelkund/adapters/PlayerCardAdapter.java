package com.appacitive.khelkund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sathley on 3/26/2015.
 */
public class PlayerCardAdapter extends RecyclerView.Adapter<PlayerCardAdapter.ViewHolder> {
    private String[] mDataset;
    private int mCount;
    private List<Player> mPlayers;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.iv_player_photo);
        }
    }

    public PlayerCardAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    public PlayerCardAdapter(List<Player> players, int count) {
        this.mCount = count;
        this.mPlayers = players;
    }

    @Override
    public PlayerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_player_card, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(KhelkundApplication.getAppContext()).load(mDataset[position]).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
