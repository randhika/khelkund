package com.appacitive.khelkund.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.KhelkundApplication;
import com.appacitive.khelkund.infra.TeamHelper;
import com.appacitive.khelkund.model.Player;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.internal.TwitterApiConstants;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by sathley on 4/20/2015.
 */
public class LeaderboardTeamAdapter extends BaseAdapter {
    private final List<Player> mPlayers;
    private final Context mContext;

    public LeaderboardTeamAdapter(Context context, List<Player> players)
    {
        mPlayers = players;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPlayers.size();
    }

    @Override
    public Player getItem(int i) {
        return mPlayers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_pick5_player_card, viewGroup, false);
        }

        final Player player = getItem(i);

        ImageView photo =  (ImageView) view.findViewById(R.id.iv_player_photo);
        TextView name = (TextView) view.findViewById(R.id.tv_card_name);
        CardView cardView = (CardView) view.findViewById(R.id.card_view_filled);

        cardView.setCardBackgroundColor(mContext.getResources().getColor(TeamHelper.getTeamColor(player.getShortTeamName())));
        Picasso.with(mContext).load(player.getImageUrl()).fit().centerInside().placeholder(R.drawable.demo).into(photo);
        name.setText(player.getDisplayName());

        return view;
    }
}
