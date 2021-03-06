package com.appacitive.khelkund.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.PrivateLeagueTeam;
import com.appacitive.khelkund.model.events.LeaderboardItemClickedEvent;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/15/2015.
 */
public class PrivateLeagueLeaderboardAdapter extends RecyclerView.Adapter<PrivateLeagueLeaderboardAdapter.ViewHolder> {
    private PrivateLeague mLeague;
    private String mUserId;

    public PrivateLeagueLeaderboardAdapter(PrivateLeague league, String userId) {
        this.mLeague = league;
        this.mUserId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        PrivateLeagueTeam team = mLeague.getTeams().get(position);
        if (team.getUserId().equals(mUserId)) {
            return 1;
        }
        return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);

        PrivateLeagueLeaderboardAdapter.ViewHolder holder = new PrivateLeagueLeaderboardAdapter.ViewHolder(itemView);
        if (viewType == 0) {
            int white = KhelkundApplication.getAppContext().getResources().getColor(android.R.color.white);
            holder.mLayout.setCardBackgroundColor(KhelkundApplication.getAppContext().getResources().getColor(R.color.primary_dark));
            holder.rank.setTextColor(white);
            holder.points.setTextColor(white);

            holder.teamName.setTextColor(white);
            holder.userName.setTextColor(white);
            holder.mRankLabel.setTextColor(white);
        }
        else holder.mTrophy.setImageResource(R.drawable.ic_fantasy_black);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PrivateLeagueTeam team = mLeague.getTeams().get(position);
        holder.points.setText(String.valueOf(team.getTotalPoints()));

        holder.userName.setText(String.valueOf(team.getUsername()));
        holder.rank.setText(String.valueOf(team.getRank()));

        if(team.getRank() == 1)
            holder.mTrophy.setVisibility(View.VISIBLE);
        else holder.mTrophy.setVisibility(View.INVISIBLE);

        holder.teamName.setText(String.valueOf(team.getUserTeamName()));

        int bitmapId = KhelkundApplication.getAppContext().getResources().getIdentifier(team.getImageName(), "drawable", KhelkundApplication.getAppContext().getPackageName());
        if (bitmapId > 0)
            Picasso.with(KhelkundApplication.getAppContext()).load(bitmapId).into(holder.logo);

        holder.mLayout.setOnClickListener(null);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new LeaderboardItemClickedEvent(team.getUserId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLeague.getTeams().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_leaderboard_username)
        public TextView userName;

        @InjectView(R.id.tv_leaderboard_teamname)
        public TextView teamName;

        @InjectView(R.id.iv_leaderboard_photo)
        public ImageView logo;

        @InjectView(R.id.tv_leaderboard_points)
        public TextView points;

        @InjectView(R.id.tv_leaderboard_rank)
        public TextView rank;

        @InjectView(R.id.rl_item_leaderboard)
        public CardView mLayout;

        @InjectView(R.id.tv_leaderboard_rank_label)
        public TextView mRankLabel;

        @InjectView(R.id.iv_rank1)
        public ImageView mTrophy;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }
}
