package com.appacitive.khelkund.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.model.Player;
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

    @Override
    public Pick5ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick5_card, parent, false);
        return new Pick5ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Pick5ViewHolder holder, int position) {
        Match match = mMatches.get(position);
        holder.awayName.setText(match.getAwayTeamName());
        holder.homeName.setText(match.getHomeTeamName());
        DateFormat df = new SimpleDateFormat("dd, MMMM yyyy");
        holder.date.setText(df.format(match.getStartDate()));

        Picasso.with(KhelkundApplication.getAppContext()).load(getTeamLogo(match.getAwayTeamShortName())).into(holder.awayLogo);
        Picasso.with(KhelkundApplication.getAppContext()).load(getTeamLogo(match.getHomeTeamShortName())).into(holder.homeLogo);
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

        return R.drawable.broncos;
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
        @InjectView(R.id.iv_pick5_away_logo)
        public ImageView awayLogo;
        @InjectView(R.id.iv_pick5_home_logo)
        public ImageView homeLogo;
        @InjectView(R.id.tv_pick5_date)
        public TextView date;

        public Pick5ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
