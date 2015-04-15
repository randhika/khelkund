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
import com.appacitive.khelkund.infra.widgets.CircleView;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.TeamHelper;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/31/2015.
 */
public class Pick5Adapter extends RecyclerView.Adapter<Pick5Adapter.Pick5ViewHolder> {

    private List<Match> mMatches;

    public Pick5Adapter(List<Match> matches)
    {
        this.mMatches = matches;
    }

    DateFormat df = new SimpleDateFormat("dd, MMMM yyyy");

    @Override
    public Pick5ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick5_card, parent, false);
        return new Pick5ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Pick5ViewHolder holder, int position) {
        final Match match = mMatches.get(position);
        holder.awayName.setText(match.getAwayTeamName());
        holder.homeName.setText(match.getHomeTeamName());

        holder.date.setText(df.format(match.getStartDate()));
        holder.venue.setText(match.getVenue());
//        Picasso.with(KhelkundApplication.getAppContext()).load(TeamHelper.getTeamLogo(match.getAwayTeamShortName())).into(holder.awayLogo);
//        Picasso.with(KhelkundApplication.getAppContext()).load(TeamHelper.getTeamLogo(match.getHomeTeamShortName())).into(holder.homeLogo);

        holder.homeLogo.setTitleText(match.getHomeTeamShortName());
        holder.homeLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getHomeTeamShortName())));
        holder.awayLogo.setTitleText(match.getAwayTeamShortName());
        holder.awayLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getAwayTeamShortName())));

        holder.relativeLayout.setBackgroundResource(R.drawable.background);
        holder.card.setCardElevation(8);
        holder.card.setPreventCornerOverlap(true);

        holder.card.setOnClickListener(null);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchSelectedEvent event = new MatchSelectedEvent();
                event.MatchId = match.getId();
                BusProvider.getInstance().post(event);
            }
        });

    }



    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public static class Pick5ViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.tv_pick5_away_team_name)
        public TextView awayName;
        @InjectView(R.id.tv_pick5_home_team_name)
        public TextView homeName;
//        @InjectView(R.id.iv_pick5_away_logo)
//        public ImageView awayLogo;
//        @InjectView(R.id.iv_pick5_home_logo)
//        public ImageView homeLogo;

        @InjectView(R.id.iv_pick5_away_logo)
        public CircleView awayLogo;
        @InjectView(R.id.iv_pick5_home_logo)
        public CircleView homeLogo;

        @InjectView(R.id.tv_pick5_play_date)
        public TextView date;
        @InjectView(R.id.tv_pick5_venue)
        public TextView venue;
        @InjectView(R.id.card_view_pick5)
        public CardView card;
        @InjectView(R.id.rl_pick5_match)
        public RelativeLayout relativeLayout;

        public Pick5ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
