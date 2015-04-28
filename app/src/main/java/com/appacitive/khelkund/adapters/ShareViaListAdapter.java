package com.appacitive.khelkund.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.navigationdrawer.ShareReferralCodeActivity;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.widgets.CircleView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by sathley on 4/28/2015.
 */
public class ShareViaListAdapter extends RecyclerView.Adapter<ShareViaListAdapter.ViewHolder> {

    private List<ShareReferralCodeActivity.SharePackageDetails> mPackages;
    private Context mContext;

    public ShareViaListAdapter(List<ShareReferralCodeActivity.SharePackageDetails> packages, Context context)
    {
        mPackages = packages;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share_item, parent, false);
        return new ShareViaListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ShareReferralCodeActivity.SharePackageDetails details = mPackages.get(position);
        holder.logo.setImageDrawable(details.logo);
        holder.name.setText(details.appName);
        holder.layout.setOnClickListener(null);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusProvider.getInstance().post(details.packageName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPackages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        @InjectView(R.id.iv_share_app_icon)
        public ImageView logo;

        @InjectView(R.id.tv_share_app_name)
        public TextView name;

        @InjectView(R.id.rl_share_item)
        public RelativeLayout layout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
