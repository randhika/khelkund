package com.appacitive.khelkund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.infra.transforms.CircleTransform;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;

/**
 * Created by sathley on 3/26/2015.
 */
public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.SquadViewHolder> {
    private RealmList<Player> mPlayers;
    private String mCaptainId;

    public SquadAdapter(RealmList<Player> players, String captainId) {
        this.mPlayers = players;
        this.mCaptainId = captainId;
    }

    private static final Map<String, String> mTeamNameMappings = new HashMap<String, String>() {{
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
    public SquadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_squad, parent, false);
        return new SquadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SquadViewHolder holder, int position) {
        Player player = mPlayers.get(position);
        holder.name.setText(player.getFirstName() + " " + player.getLastName());
        holder.points.setText(String.valueOf(player.getPoints()));
        holder.team.setText(mTeamNameMappings.get(player.getShortTeamName()));

        switch (player.getType()) {
            case "Batsman": {
                holder.type.setImageResource(R.drawable.batsman);
                break;
            }
            case "Bowler": {
                holder.type.setImageResource(R.drawable.bowler);
                break;
            }
            case "AllRounder": {
                holder.type.setImageResource(R.drawable.allrounder);
                break;
            }
            case "WicketKeeper": {
                holder.type.setImageResource(R.drawable.wicketkeeper);
                break;
            }
        }
        Picasso.with(KhelkundApplication.getAppContext())
                .load(player.getImageUrl()).resize(250, 370).centerInside()
                .transform(new CircleTransform(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(player.getShortTeamName()))))
                .into(holder.logo);
        if (mCaptainId!= null && mCaptainId.equals(player.getId()))
            holder.isCaptain.setVisibility(View.VISIBLE);
        else holder.isCaptain.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public static class SquadViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView team;
        protected ImageView logo;
        protected TextView points;
        protected ImageView type;
        protected TextView isCaptain;

        public SquadViewHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.iv_player_squad_photo);
            name = (TextView) itemView.findViewById(R.id.tv_squad_name);
            team = (TextView) itemView.findViewById(R.id.tv_squad_team);
            points = (TextView) itemView.findViewById(R.id.tv_squad_points);
            type = (ImageView) itemView.findViewById(R.id.iv_squad_type);
            isCaptain = (TextView) itemView.findViewById(R.id.tv_squad_captain);
        }
    }
}
