package com.appacitive.khelkund.fragments.pick5;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5MatchActivity;
import com.appacitive.khelkund.adapters.Pick5TeamAdapter;
import com.appacitive.khelkund.infra.APCallback;
import com.appacitive.khelkund.infra.BusProvider;
import com.appacitive.khelkund.infra.Http;
import com.appacitive.khelkund.infra.SharedPreferencesManager;
import com.appacitive.khelkund.infra.SnackBarManager;
import com.appacitive.khelkund.infra.StorageManager;
import com.appacitive.khelkund.infra.Urls;
import com.appacitive.khelkund.infra.widgets.CircleView;
import com.appacitive.khelkund.infra.widgets.carousel.Carousel;
import com.appacitive.khelkund.model.MatchStatistic;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.model.PlayerMatchStatistic;
import com.appacitive.khelkund.model.events.pick5.Pick5PlayerClickedEvent;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5FinishedReadonlyFragment extends Fragment {

    private Pick5MatchDetails mDetails;
    private MatchStatistic mMatchStatistic;

    private static final int green = Color.parseColor("#43A047");
    private static final int red = Color.parseColor("#F44336");
    private static final int grey = Color.parseColor("#757575");

    @InjectView(R.id.iv_pick5_you)
    public CircleImageView mImageYou;

    @InjectView(R.id.iv_pick5_khelkund)
    public CircleImageView mImageKhelkund;

    @InjectView(R.id.breadcrumb_btsm)
    public CircleView mBreadcrumbBtsm;

    @InjectView(R.id.breadcrumb_bwlr)
    public CircleView mBreadcrumbBwlr;

    @InjectView(R.id.breadcrumb_ar)
    public CircleView mBreadcrumbAr;

    @InjectView(R.id.breadcrumb_wk)
    public CircleView mBreadcrumbWk;

    @InjectView(R.id.breadcrumb_any)
    public CircleView mBreadcrumbAny;

    @InjectView(R.id.my_carousel)
    public Carousel mMyCarousel;

    @InjectView(R.id.ai_carousel)
    public Carousel mAiCarousel;

    @InjectView(R.id.tv_pick5_result)
    public TextView mResult;

    private StorageManager manager;

    private Pick5TeamAdapter mMyAdapter;
    private Pick5TeamAdapter mAiAdapter;


    public Pick5FinishedReadonlyFragment() {
        // Required empty public constructor
    }

    public static Pick5FinishedReadonlyFragment newInstance() {
        Pick5FinishedReadonlyFragment fragment = new Pick5FinishedReadonlyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick5_finished_readonly, container, false);
        ButterKnife.inject(this, view);
        manager = new StorageManager();

        fetchAndDisplayUserImage();

        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();
        initAdapters();

        loadPlayers();

        displayResults();

        mMyCarousel.setAdapter(mMyAdapter);
        mMyCarousel.setSelection(2);
        mMyCarousel.setSlowDownCoefficient(Integer.MAX_VALUE);
        mMyCarousel.setSpacing(0.6f);

        mAiCarousel.setAdapter(mAiAdapter);
        mAiCarousel.setSelection(2);
        mAiCarousel.setSlowDownCoefficient(Integer.MAX_VALUE);
        mAiCarousel.setSpacing(0.6f);

        return view;
    }

    private void initAdapters() {
        mMyAdapter = new Pick5TeamAdapter(getActivity(), true, true);
        mAiAdapter = new Pick5TeamAdapter(getActivity(), true, true);
    }

    private void fetchAndDisplayUserImage() {
        StorageManager manager = new StorageManager();
        Bitmap me = manager.FetchImage(SharedPreferencesManager.ReadUserId());
        if (me != null)
            mImageYou.setImageBitmap(me);
    }

    private void displayResults() {

        if (mDetails.getResult() == 1) {
            mResult.setText("Match in Progress");
            return;
        }

        int myWinningCount = 0;

        if (mMyAdapter.mTeam[0].getPoints() > mAiAdapter.mTeam[0].getPoints()) {
            myWinningCount++;
            mBreadcrumbBtsm.setFillColor(green);
        } else mBreadcrumbBtsm.setFillColor(red);
        mBreadcrumbBtsm.setSubtitleText(String.format("%s-%s", String.valueOf(mMyAdapter.mTeam[0].getPoints()), String.valueOf(mAiAdapter.mTeam[0].getPoints())));

        if (mMyAdapter.mTeam[1].getPoints() > mAiAdapter.mTeam[1].getPoints()) {
            myWinningCount++;
            mBreadcrumbBwlr.setFillColor(green);
        } else mBreadcrumbBwlr.setFillColor(red);
        mBreadcrumbBwlr.setSubtitleText(String.format("%s-%s", String.valueOf(mMyAdapter.mTeam[1].getPoints()), String.valueOf(mAiAdapter.mTeam[1].getPoints())));

        if (mMyAdapter.mTeam[2].getPoints() > mAiAdapter.mTeam[2].getPoints()) {
            myWinningCount++;
            mBreadcrumbAr.setFillColor(green);
        } else mBreadcrumbAr.setFillColor(red);
        mBreadcrumbAr.setSubtitleText(String.format("%s-%s", String.valueOf(mMyAdapter.mTeam[2].getPoints()), String.valueOf(mAiAdapter.mTeam[2].getPoints())));

        if (mMyAdapter.mTeam[3].getPoints() > mAiAdapter.mTeam[3].getPoints()) {
            myWinningCount++;
            mBreadcrumbWk.setFillColor(green);
        } else mBreadcrumbWk.setFillColor(red);
        mBreadcrumbWk.setSubtitleText(String.format("%s-%s", String.valueOf(mMyAdapter.mTeam[3].getPoints()), String.valueOf(mAiAdapter.mTeam[3].getPoints())));

        if (mMyAdapter.mTeam[4].getPoints() > mAiAdapter.mTeam[4].getPoints()) {
            myWinningCount++;
            mBreadcrumbAny.setFillColor(green);
        } else mBreadcrumbAny.setFillColor(red);
        mBreadcrumbAny.setSubtitleText(String.format("%s-%s", String.valueOf(mMyAdapter.mTeam[4].getPoints()), String.valueOf(mAiAdapter.mTeam[4].getPoints())));

        if (myWinningCount >= 4) {
            mResult.setText("YOU WIN!");
            mImageYou.setBorderColor(green);
            mImageKhelkund.setBorderColor(red);

        } else {
            mResult.setText("YOU LOST!");
            mImageYou.setBorderColor(red);
            mImageKhelkund.setBorderColor(green);
        }
    }

    private boolean loadPlayers() {
        if (mDetails == null || mDetails.getPlayers() == null || mDetails.getPlayers().size() != 5 || mDetails.getAppPlayers().size() != 5)
            return false;
        List<Player> myPlayers = mDetails.getPlayers();
        for (Player myPlayer : myPlayers) {
            String aiPlayerId = mDetails.getPlayerMappings().get(myPlayer.getId());
            Player aiPlayer = null;
            for (Player aiP : mDetails.getAppPlayers()) {
                if (aiP.getId().equals(aiPlayerId)) {
                    aiPlayer = aiP;
                    break;
                }
            }
            if (myPlayer.getType().equals("Batsman") && mMyAdapter.mTeam[0] == null) {
                mMyAdapter.mTeam[0] = myPlayer;
                mAiAdapter.mTeam[0] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("Bowler") && mMyAdapter.mTeam[1] == null) {
                mMyAdapter.mTeam[1] = myPlayer;
                mAiAdapter.mTeam[1] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("AllRounder") && mMyAdapter.mTeam[2] == null) {
                mMyAdapter.mTeam[2] = myPlayer;
                mAiAdapter.mTeam[2] = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("WicketKeeper") && mMyAdapter.mTeam[3] == null) {
                mMyAdapter.mTeam[3] = myPlayer;
                mAiAdapter.mTeam[3] = aiPlayer;
                continue;
            } else {
                mMyAdapter.mTeam[4] = myPlayer;
                mAiAdapter.mTeam[4] = aiPlayer;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_pick5_share)
    public void onShareClick() {
        Bitmap bitmap = getScreenBitmap();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Khelkund Pick'em 5");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outputStream;
        try {
            outputStream = getActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        share.putExtra(Intent.EXTRA_TEXT, String.format("Check out my squad on Khelkund's Pick'em 5. You can play too. Get the app here %s", getActivity().getResources().getString(R.string.SHORT_APP_URL)));

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share team using"));
    }

    private Bitmap getScreenBitmap() {

        RelativeLayout view = (RelativeLayout) getActivity().findViewById(R.id.pick5_parent_layout);
        Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas1 = new Canvas(b);
        canvas1.drawColor(getResources().getColor(R.color.background_material_light));
        view.draw(canvas1);
        return b;

    }

    @Subscribe
    public void onPlayerClicked(Pick5PlayerClickedEvent event) {
        if (mMatchStatistic != null)
            showPlayerStat(event.player);
        else
            fetchStatistics(event.player);
    }

    private void fetchStatistics(Player player) {
        StorageManager manager = new StorageManager();
        mMatchStatistic = manager.GetMatchStatistic(mDetails.getMatchDetails().getId(), SharedPreferencesManager.ReadUserId());
        if (mMatchStatistic != null)
            showPlayerStat(player);
        else fetchStatisticsFromServer(player);
    }

    private void fetchStatisticsFromServer(final Player player) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Fetching player statistics");
        final String userId = SharedPreferencesManager.ReadUserId();
        Http http = new Http();
        http.get(Urls.MatchUrls.getScoreboardUrl(mDetails.getMatchDetails().getId(), userId), new HashMap<String, String>(), new APCallback() {
            @Override
            public void success(JSONObject result) {
                dialog.dismiss();
                if (result.optJSONObject("Error") != null) {
                    SnackBarManager.showError(result.optJSONObject("Error").optString("ErrorMessage"), getActivity());
                    return;
                }
                mMatchStatistic = new MatchStatistic(result);
                mMatchStatistic.setUserId(userId);

                if(mMatchStatistic.getPlayerStatistics() != null)
                {
                    for (PlayerMatchStatistic statistic : mMatchStatistic.getPlayerStatistics())
                        statistic.setMatchId(mDetails.getMatchDetails().getId());

                }
                StorageManager manager = new StorageManager();
                manager.SaveMatchStatistic(mMatchStatistic);

                showPlayerStat(player);
            }

            @Override
            public void failure(Exception e) {
                dialog.dismiss();
                SnackBarManager.showError("Unable to fetch player statistics at the moment.", getActivity());
            }
        });
    }

    private void showPlayerStat(Player player) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        PlayerMatchStatistic statistic = null;
        for (PlayerMatchStatistic playerMatchStatistic : mMatchStatistic.getPlayerStatistics()) {
            if (playerMatchStatistic.getPlayerId().equals(player.getId())) {
                statistic = playerMatchStatistic;
                break;
            }

        }
//        Pick5PlayerDialogFragment playerStatDialog = Pick5PlayerDialogFragment.newInstance(player, statistic);
//        playerStatDialog.show(fm, "player_details");
    }


    @Override
    public void onResume() {
        super.onResume();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        BusProvider.getInstance().unregister(this);
    }

}
