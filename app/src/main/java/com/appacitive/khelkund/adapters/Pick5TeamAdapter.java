package com.appacitive.khelkund.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.events.pick5.Pick5PlayerClickedEvent;
import com.squareup.picasso.Picasso;

/**
 * Created by sathley on 4/23/2015.
 */
public class Pick5TeamAdapter extends BaseAdapter {
    public Player[] mTeam = new Player[5];
    private Context mContext;
    private boolean mAttachListeners;
    private boolean mIsReadonly;

    public Pick5TeamAdapter(Context context, boolean attachListener, boolean readonly) {
        this.mContext = context;
        mAttachListeners = attachListener;
        mIsReadonly = readonly;
    }

    @Override
    public int getCount() {
        return mTeam.length;
    }

    @Override
    public Player getItem(int position) {
        return mTeam[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_pick5_squad_card, viewGroup, false);
        }

        final Player player = getItem(position);

        ImageView photo = (ImageView) view.findViewById(R.id.iv_player_photo);
        TextView nameTop = (TextView) view.findViewById(R.id.tv_card_nameTop);
        TextView nameBottom = (TextView) view.findViewById(R.id.tv_card_nameBottom);
        RelativeLayout cardView = (RelativeLayout) view.findViewById(R.id.card_view_filled);

        if (mIsReadonly)
            cardView.setAlpha(0.6f);
        String nameText = "";
        if (player != null) {
            Picasso.with(mContext).load(player.getImageUrl()).fit().centerInside().placeholder(R.drawable.demo).into(photo);
            nameText = (player.getDisplayName());
        } else {
            if (mAttachListeners == true)
                photo.setImageResource(R.drawable.demoadd);
            else
                photo.setImageResource(R.drawable.demo);

            switch (position) {
                case 0: {
                    nameText = "BTSM";
                    break;
                }
                case 1: {
                    nameText = "BWLR";
                    break;
                }
                case 2: {
                    nameText = "ALL ROUNDER";
                    break;
                }
                case 3: {
                    nameText = "WK";
                    break;
                }
                default: {
                    nameText = "WILDCARD";
                    break;
                }
            }
        }

        if (position % 2 == 0)
            nameTop.setText(nameText);
        else
            nameBottom.setText(nameText);

        if (position > 2)
            photo.setScaleX(-1);

        if (mAttachListeners == true) {
            cardView.setOnClickListener(null);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pick5PlayerClickedEvent event = new Pick5PlayerClickedEvent(position);
                    event.player = player;
                    BusProvider.getInstance().post(event);
                }
            });
        }
        return view;

    }
}
