package com.appacitive.khelkund.model.viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.events.LogoSelectedEvent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/1/2015.
 */
public class TeamLogoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    @InjectView(R.id.card_view_pick_team_logo)
    public CardView logo;

    public TeamLogoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
        logo.setOnClickListener(null);
        logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LogoSelectedEvent event = new LogoSelectedEvent();
        event.Position = getAdapterPosition();
        BusProvider.getInstance().post(event);
    }
}
