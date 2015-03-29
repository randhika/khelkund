package com.appacitive.khelkund.model.viewholders;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import com.appacitive.khelkund.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/28/2015.
 */
public class EmptyCardViewHolder extends PlayerCardViewHolder {

    @InjectView(R.id.card_view_empty)
    public CardView cvEmptyCard;

    public EmptyCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
