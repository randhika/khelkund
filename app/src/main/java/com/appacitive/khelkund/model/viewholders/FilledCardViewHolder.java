package com.appacitive.khelkund.model.viewholders;

import android.support.v7.widget.CardView;
import android.view.View;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.model.events.AlreadyOwnedPlayerClickedEvent;
import com.appacitive.khelkund.model.events.CardErrorEvent;
import com.appacitive.khelkund.model.events.NewPlayerAddedEvent;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 3/28/2015.
 */
public class FilledCardViewHolder extends PlayerCardViewHolder {

    @InjectView(R.id.card_view_filled)
    public CardView cvFilledCard;

    public String PlayerId = null;

    public FilledCardViewHolder(View itemView) {
        super(itemView);
        PlayerId = null;
        ButterKnife.inject(this, itemView);
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onErrorAnimation(CardErrorEvent event) {
        if (this.cvFilledCard != null)
            YoYo.with(Techniques.Wobble)
                    .duration(700)
                    .playOn(this.cvFilledCard);
    }

    @Subscribe
    public void alreadyOwnedPlayerClickAnimation(AlreadyOwnedPlayerClickedEvent event)
    {
        if(this.PlayerId != null && event.PlayerId.equals(this.PlayerId) && this.cvFilledCard != null)
            YoYo.with(Techniques.Bounce)
            .duration(700)
            .playOn(this.cvFilledCard);
    }

    @Subscribe
    public void newPlayerAddedAnimation(NewPlayerAddedEvent event)
    {
        if(this.cvFilledCard != null && this.PlayerId != null && event.PlayerId.equals(this.PlayerId))
            YoYo.with(Techniques.RubberBand)
                    .duration(1200)
                    .playOn(this.cvFilledCard);
    }
}
