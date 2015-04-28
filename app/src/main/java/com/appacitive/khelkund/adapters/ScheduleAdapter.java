package com.appacitive.khelkund.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.widgets.CircleView;
import com.appacitive.khelkund.model.Match;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.model.events.MatchSelectedEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/3/2015.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Match> mMatches;

    private static final DateFormat df = new SimpleDateFormat("dd MMM");
    private static final DateFormat tf  = new SimpleDateFormat("EEE hh:mm a");

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

        holder.date.setText(df.format((match.getStartDate())));
        holder.time.setText(tf.format(toUtc(match.getStartDate())));

        holder.homeLogo.setTitleText(match.getHomeTeamShortName());
        holder.homeLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getHomeTeamShortName())));
        holder.awayLogo.setTitleText(match.getAwayTeamShortName());
        holder.awayLogo.setFillColor(KhelkundApplication.getAppContext().getResources().getColor(TeamHelper.getTeamColor(match.getAwayTeamShortName())));


//        holder.card.setCardBackgroundColor(KhelkundApplication.getAppContext().getResources().getColor(colors[position % 8]));
//        holder.relativeLayout.setOnClickListener(null);
//        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MatchSelectedEvent event = new MatchSelectedEvent();
//                event.MatchId = match.getId();
//                BusProvider.getInstance().post(event);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder
    {
        @InjectView(R.id.iv_pick5_away_logo)
        public CircleView awayLogo;
        @InjectView(R.id.iv_pick5_home_logo)
        public CircleView homeLogo;

        @InjectView(R.id.tv_pick5_play_time)
        public TextView time;
        @InjectView(R.id.tv_pick5_play_date)
        public TextView date;
        @InjectView(R.id.card_view_pick5)
        public CardView card;
        @InjectView(R.id.rl_pick5_match)
        public RelativeLayout relativeLayout;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);

        }
    }

    private Date toUtc(Date date)
    {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(date); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 5);
        cal.add(Calendar.MINUTE, 30);
        return cal.getTime();
    }

    private static final Integer[] colors = new Integer[]{
            R.color.DD, R.color.CSK, R.color.RR, R.color.KXIP, R.color.KKR, R.color.RCB, R.color.MI, R.color.SRH
    };
}