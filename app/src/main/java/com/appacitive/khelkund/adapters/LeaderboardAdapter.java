package com.appacitive.khelkund.adapters;

import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.LeaderboardScore;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/2/2015.
 */
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<LeaderboardScore> mScores;
    private String mUserId;

    public LeaderboardAdapter(List<LeaderboardScore> scores, String userId)
    {
        this.mScores = scores;
        this.mUserId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LeaderboardScore score = mScores.get(position);
        holder.points.setText(String.valueOf(score.getPoints()));
        holder.teamName.setText(String.valueOf(score.getTeamName()));
        holder.userName.setText(String.valueOf(score.getUserName()));
        holder.rank.setText(String.valueOf(score.getRank()));

        if(score.getUserId().equals(mUserId))
            holder.mLayout.setBackgroundColor(Color.CYAN);
        else
            holder.mLayout.setBackgroundColor(Color.WHITE);

        int bitmapId = KhelkundApplication.getAppContext().getResources().getIdentifier(score.getUserTeamImage(), "drawable", KhelkundApplication.getAppContext().getPackageName());
        if(bitmapId > 0)
            Picasso.with(KhelkundApplication.getAppContext()).load(bitmapId).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return mScores.size();
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
        public RelativeLayout mLayout;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.inject(this, v);
        }
    }
}
