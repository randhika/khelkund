package com.appacitive.khelkund.model.viewholders;

import android.support.v7.widget.CardView;
import android.view.View;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.events.CardErrorEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.otto.Subscribe;

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
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onErrorAnimation(CardErrorEvent event) {
        if (cvEmptyCard != null)
            YoYo.with(Techniques.Wobble)
                    .duration(700)
                    .playOn(cvEmptyCard);
    }
}
