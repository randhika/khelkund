package com.appacitive.khelkund.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sathley on 3/27/2015.
 */
public class TeamLogoAdapter extends RecyclerView.Adapter<TeamLogoAdapter.TeamLogoViewHolder> {

    private List<String> mTeamLogos = new ArrayList<String>();

    public TeamLogoAdapter(List<String> logos)
    {
        this.mTeamLogos = logos;
    }

    @Override
    public TeamLogoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_pick_team_logo, parent, false);
        return new TeamLogoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamLogoViewHolder holder, int position) {
        int bitmapId  = KhelkundApplication.getAppContext().getResources().getIdentifier(mTeamLogos.get(position), "drawable", KhelkundApplication.getAppContext().getPackageName());
        Picasso.with(KhelkundApplication.getAppContext()).load(bitmapId).into((ImageView)holder.logo.findViewById(R.id.iv_pick_team_logo));
    }

    @Override
    public int getItemCount() {
        return mTeamLogos.size();
    }

    public static class TeamLogoViewHolder extends RecyclerView.ViewHolder
    {
        public CardView logo;
        public TeamLogoViewHolder(View itemView) {
            super(itemView);
            logo = (CardView) itemView.findViewById(R.id.card_view_pick_team_logo);
        }
    }
}
