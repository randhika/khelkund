package com.appacitive.khelkund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.PrivateLeagueTeam;
import com.appacitive.khelkund.model.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/14/2015.
 */
public class PrivateLeagueAdapter  extends RecyclerView.Adapter<PrivateLeagueAdapter.PrivateLeagueViewHolder>{

    private List<PrivateLeague> mPrivateLeagues;
    private StorageManager mManager;
    private Team mTeam;
    public PrivateLeagueAdapter(List<PrivateLeague> privateLeagues)
    {
        this.mPrivateLeagues = privateLeagues;
        this.mManager = new StorageManager();
        mTeam = mManager.GetTeam(SharedPreferencesManager.ReadUserId());
    }

    @Override
    public PrivateLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_league, parent, false);
        return new PrivateLeagueViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PrivateLeagueViewHolder holder, int position) {
        PrivateLeague privateLeague = mPrivateLeagues.get(position);
        holder.name.setText(privateLeague.getName());
        holder.playerCount.setText(String.valueOf(privateLeague.getTeams().size()) + " users are playing this league");

        for(PrivateLeagueTeam team : privateLeague.getTeams())
        {
            if(team.getUserTeamId().equals(mTeam.getId()));
            {
                holder.score.setText("Your score is " + team.getTotalPoints());
                holder.rank.setText("Your rank " + team.getRank());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mPrivateLeagues.size();
    }

    public static class PrivateLeagueViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.tv_private_league_name)
        protected TextView name;

        @InjectView(R.id.tv_private_league_rank)
        protected TextView rank;

        @InjectView(R.id.tv_private_league_score)
        protected TextView score;

        @InjectView(R.id.tv_private_league_player_count)
        protected TextView playerCount;


        public PrivateLeagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
