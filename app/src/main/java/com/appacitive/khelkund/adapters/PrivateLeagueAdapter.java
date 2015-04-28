package com.appacitive.khelkund.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.model.PrivateLeague;
import com.appacitive.khelkund.model.PrivateLeagueTeam;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueDeleteEvent;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueSelectedEvent;
import com.appacitive.khelkund.model.events.privateleague.PrivateLeagueShareEvent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/14/2015.
 */
public class PrivateLeagueAdapter  extends RecyclerView.Adapter<PrivateLeagueAdapter.PrivateLeagueViewHolder>{

    private List<PrivateLeague> mPrivateLeagues;
    private StorageManager mManager;
    private String mUserId;
    public PrivateLeagueAdapter(List<PrivateLeague> privateLeagues, String userId)
    {
        this.mPrivateLeagues = privateLeagues;
        this.mManager = new StorageManager();
        this.mUserId = userId;
    }

    public void ResetLeagues(List<PrivateLeague> privateLeagues)
    {
        this.mPrivateLeagues = privateLeagues;
    }

    @Override
    public PrivateLeagueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_private_league, parent, false);
        return new PrivateLeagueViewHolder(itemView);
    }

    private static final Integer[] colors = new Integer[]{
            Color.parseColor("#9575CD"), Color.parseColor("#EF5350"),Color.parseColor("#BA68C8"),Color.parseColor("#009688"),Color.parseColor("#827717"),Color.parseColor("#A1887F")
    };

    @Override
    public void onBindViewHolder(PrivateLeagueViewHolder holder, int position) {
        final PrivateLeague privateLeague = mPrivateLeagues.get(position);
        holder.name.setText(privateLeague.getName());
        holder.playerCount.setText(String.valueOf(privateLeague.getTotalRecords()) + " user(s) are playing in this league");
        holder.code.setText("Private League Code : " + privateLeague.getCode());

        setViewHolderListeners(holder, privateLeague);
        holder.card.setCardBackgroundColor((colors[position % colors.length]));
        for(PrivateLeagueTeam team : privateLeague.getTeams())
        {
            if(mUserId.equals(team.getUserId()))
            {
                holder.score.setText("Your score is " + team.getTotalPoints());
                holder.rank.setText("You are rank " + team.getRank());
                break;
            }
        }

    }

    private void setViewHolderListeners(PrivateLeagueViewHolder holder, final PrivateLeague privateLeague) {
        holder.itemView.setOnClickListener(null);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new PrivateLeagueSelectedEvent(privateLeague.getId()));
            }
        });
        holder.share.setOnClickListener(null);
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new PrivateLeagueShareEvent(privateLeague.getId()));
            }
        });

        holder.delete.setOnClickListener(null);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(new PrivateLeagueDeleteEvent(privateLeague.getId()));
            }
        });
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

        @InjectView(R.id.tv_private_league_code)
        protected TextView code;

        @InjectView(R.id.tv_private_league_score)
        protected TextView score;

        @InjectView(R.id.tv_private_league_player_count)
        protected TextView playerCount;

        @InjectView(R.id.iv_privateleague_share)
        protected ImageView share;

        @InjectView(R.id.iv_private_league_delete)
        protected ImageView delete;

        @InjectView(R.id.card_view_privateleague)
        protected CardView card;


        public PrivateLeagueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
