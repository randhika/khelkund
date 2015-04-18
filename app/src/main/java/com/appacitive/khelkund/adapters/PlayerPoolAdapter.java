package com.appacitive.khelkund.adapters;

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
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.transforms.CircleTransform;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.TeamHelper;
import com.appacitive.khelkund.model.events.PlayerChosenEvent;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

/**
 * Created by sathley on 3/29/2015.
 */
public class PlayerPoolAdapter extends RecyclerView.Adapter<PlayerPoolAdapter.PoolViewHolder>{

    private List<Player> mPlayers;
    public PlayerPoolAdapter(List<Player> players)
    {
        this.mPlayers = players;
    }

    private static final Map<String, String> mTeamNameMappings = new HashMap<String, String>(){{
        put("KXIP", "Punjab");
        put("MI", "Mumbai");
        put("SRH", "Hyderabad");
        put("DD", "Delhi");
        put("RCB", "Bangalore");
        put("RR", "Rajasthan");
        put("CSK", "Chennai");
        put("KKR", "Kolkata");
    }};

    @Override
    public PoolViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player_pool, parent, false);
        return new PoolViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PoolViewHolder holder, final int position) {
        final Player player = mPlayers.get(position);
        holder.name.setText(player.getFirstName() + " " + player.getLastName());
        holder.points.setText(String.valueOf(player.getPoints()));
        holder.team.setText(mTeamNameMappings.get(player.getShortTeamName()));
        holder.price.setText("$ " + String.valueOf(player.getPrice()));
        int totalTeams = SharedPreferencesManager.ReadTotalTeamsCount();
        if(totalTeams != 0)
        {
            holder.popularity.setText(String.valueOf((player.getPopularity() * 100)/totalTeams) + " %");
        }

//        Picasso.with(KhelkundApplication.getAppContext()).load(player.getImageUrl()).placeholder(R.drawable.demo).into(holder.logo);
        Picasso.with(KhelkundApplication.getAppContext())
                .load(player.getImageUrl()).resize(250, 370).centerInside()
                .transform(new CircleTransform(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(player.getShortTeamName()))))
                .into(holder.logo);

        holder.relativeLayout.setOnClickListener(null);
        holder.relativeLayout.setClickable(false);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayerChosenEvent event = new PlayerChosenEvent();
                event.PlayerId = mPlayers.get(position).getId();
                BusProvider.getInstance().post(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public static class PoolViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.tv_squad_name)
        public TextView name;
        @InjectView(R.id.tv_squad_team)
        public TextView team;
        @InjectView(R.id.iv_player_squad_photo)
        public ImageView logo;
        @InjectView(R.id.tv_pool_points)
        public TextView points;
        @InjectView(R.id.tv_pool_popularity)
        public TextView popularity;
        @InjectView(R.id.tv_squad_points)
        public TextView price;
        @InjectView(R.id.rl_item_pool)
        public RelativeLayout relativeLayout;

        public PoolViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
