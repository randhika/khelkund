package com.appacitive.khelkund.model.viewholders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.Player;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/28/2015.
 */
public class FilledCardViewHolder extends PlayerCardViewHolder {

    @InjectView(R.id.card_view_filled)
    public CardView cvFilledCard;
    public FilledCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
