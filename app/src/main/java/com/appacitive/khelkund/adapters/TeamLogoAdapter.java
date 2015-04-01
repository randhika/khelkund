package com.appacitive.khelkund.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.events.LogoSelectedEvent;
import com.appacitive.khelkund.model.viewholders.TeamLogoViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/27/2015.
 */
public class TeamLogoAdapter extends RecyclerView.Adapter<TeamLogoViewHolder> {

    private List<String> mTeamLogos = new ArrayList<String>();
    private int mSelectedPosition;

    public void setmSelectedPosition(int position)
    {
        this.mSelectedPosition = position;
    }

    public TeamLogoAdapter(List<String> logos, int selectedPosition)
    {
        this.mTeamLogos = logos;
        this.mSelectedPosition = selectedPosition;
    }

    @Override
    public TeamLogoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_pick_team_logo, parent, false);
        return new TeamLogoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TeamLogoViewHolder holder, final int position) {
        int bitmapId  = KhelkundApplication.getAppContext().getResources().getIdentifier(mTeamLogos.get(position), "drawable", KhelkundApplication.getAppContext().getPackageName());
        Picasso.with(KhelkundApplication.getAppContext()).load(bitmapId).into((ImageView) holder.logo.findViewById(R.id.iv_pick_team_logo));
        if(position == mSelectedPosition)
        {
            holder.logo.setBackgroundColor(KhelkundApplication.getAppContext().getResources().getColor(R.color.accent));
        }
        else {
            holder.logo.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mTeamLogos.size();
    }


}
