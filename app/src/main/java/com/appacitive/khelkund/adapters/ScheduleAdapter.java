package com.appacitive.khelkund.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/3/2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Match> mMatches;

    public ScheduleAdapter(List<Match> matches)
    {
        this.mMatches = matches;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick5_card, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        final Match match = mMatches.get(position);
        holder.awayName.setText(match.getAwayTeamName());
        holder.homeName.setText(match.getHomeTeamName());
        DateFormat df = new SimpleDateFormat("dd, MMMM yyyy");
        holder.date.setText(df.format(match.getStartDate()));
        holder.venue.setText(match.getVenue());
        Picasso.with(KhelkundApplication.getAppContext()).load(getTeamLogo(match.getAwayTeamShortName())).into(holder.awayLogo);
        Picasso.with(KhelkundApplication.getAppContext()).load(getTeamLogo(match.getHomeTeamShortName())).into(holder.homeLogo);
        holder.relativeLayout.setBackgroundResource(R.drawable.background);
        holder.card.setCardElevation(8);
        holder.card.setPreventCornerOverlap(true);
    }

    private int getTeamLogo(String teamName)
    {
        if(teamName.equals("KKR"))
            return R.drawable.kkr;
        if(teamName.equals("MI"))
            return R.drawable.mi;
        if(teamName.equals("SRH"))
            return R.drawable.srh;
        if(teamName.equals("KXIP"))
            return R.drawable.kxip;
        if(teamName.equals("DD"))
            return R.drawable.dd;
        if(teamName.equals("CSK"))
            return R.drawable.csk;
        if(teamName.equals("RR"))
            return R.drawable.rr;
        if(teamName.equals("RCB"))
            return R.drawable.rcb;

        return R.drawable.rcb;
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.tv_pick5_away_team_name)
        public TextView awayName;
        @InjectView(R.id.tv_pick5_home_team_name)
        public TextView homeName;
        @InjectView(R.id.iv_pick5_away_logo)
        public ImageView awayLogo;
        @InjectView(R.id.iv_pick5_home_logo)
        public ImageView homeLogo;
        @InjectView(R.id.tv_pick5_date)
        public TextView date;
        @InjectView(R.id.tv_pick5_venue)
        public TextView venue;
        @InjectView(R.id.card_view_pick5)
        public CardView card;
        @InjectView(R.id.rl_pick5_match)
        public RelativeLayout relativeLayout;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}