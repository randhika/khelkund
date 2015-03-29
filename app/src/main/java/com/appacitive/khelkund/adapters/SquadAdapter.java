package com.appacitive.khelkund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by sathley on 3/26/2015.
 */
public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.SquadViewHolder> {
    private RealmList<Player> mPlayers;
    public SquadAdapter(RealmList<Player> players)
    {
        this.mPlayers = players;
    }
    @Override
    public SquadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_squad, parent, false);
        return new SquadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SquadViewHolder holder, int position) {
        Player player = mPlayers.get(position);
        holder.name.setText(player.getDisplayName());
        holder.points.setText(String.valueOf(player.getPoints()));
        holder.match.setText(player.getNextOpponent());
        Picasso.with(KhelkundApplication.getAppContext()).load(player.getImageUrl()).into(holder.logo);
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public static class SquadViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView name;
        protected TextView match;
        protected ImageView logo;
        protected TextView points;

        public SquadViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.iv_player_squad_photo);
            name = (TextView) itemView.findViewById(R.id.tv_squad_name);
            match = (TextView) itemView.findViewById(R.id.tv_squad_team);
            points = (TextView) itemView.findViewById(R.id.tv_squad_points);
        }
    }
}
