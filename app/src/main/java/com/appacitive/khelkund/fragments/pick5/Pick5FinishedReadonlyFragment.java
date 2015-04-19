package com.appacitive.khelkund.fragments.pick5;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.pick5.Pick5MatchActivity;
import com.appacitive.khelkund.infra.transforms.CircleTransform;
import com.appacitive.khelkund.model.Pick5MatchDetails;
import com.appacitive.khelkund.model.Player;
import com.appacitive.khelkund.infra.TeamHelper;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.io.OutputStream;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Pick5FinishedReadonlyFragment extends Fragment {

    private Pick5MatchDetails mDetails;

    private static final int empty_background_color = Color.parseColor("#ffd3d3d3");

    //  My items

    @InjectView(R.id.my_bowler)
    public ImageView mMyBowler;

    @InjectView(R.id.my_bowler_score)
    public TextView mMyBowlerScore;

    @InjectView(R.id.my_bowler_name)
    public TextView mMyBowlerName;

    @InjectView(R.id.my_batsman)
    public ImageView mMyBatsman;

    @InjectView(R.id.my_batsman_score)
    public TextView mMyBatsmanScore;

    @InjectView(R.id.my_batsman_name)
    public TextView mMyBatsmanName;

    @InjectView(R.id.my_allrounder)
    public ImageView mMyAllRounder;

    @InjectView(R.id.my_allrounder_score)
    public TextView mMyAllRounderScore;

    @InjectView(R.id.my_allrounder_name)
    public TextView mMyAllRounderName;

    @InjectView(R.id.my_wk)
    public ImageView mMyWicketKeeper;

    @InjectView(R.id.my_wk_score)
    public TextView mMyWicketKeeperScore;

    @InjectView(R.id.my_wk_name)
    public TextView mMyWicketKeeperName;

    @InjectView(R.id.my_any)
    public ImageView mMyAny;

    @InjectView(R.id.my_any_score)
    public TextView mMyAnyScore;

    @InjectView(R.id.my_any_name)
    public TextView mMyAnyName;

//    AI items

    @InjectView(R.id.ai_bowler)
    public ImageView mAiBowler;

    @InjectView(R.id.ai_bowler_score)
    public TextView mAiBowlerScore;

    @InjectView(R.id.ai_bowler_name)
    public TextView mAiBowlerName;

    @InjectView(R.id.ai_batsman)
    public ImageView mAiBatsman;

    @InjectView(R.id.ai_batsman_score)
    public TextView mAiBatsmanScore;

    @InjectView(R.id.ai_batsman_name)
    public TextView mAiBatsmanName;

    @InjectView(R.id.ai_allrounder)
    public ImageView mAiAllRounder;

    @InjectView(R.id.ai_allrounder_score)
    public TextView mAiAllRounderScore;

    @InjectView(R.id.ai_allrounder_name)
    public TextView mAiAllRounderName;

    @InjectView(R.id.ai_wk)
    public ImageView mAiWicketKeeper;

    @InjectView(R.id.ai_wk_score)
    public TextView mAiWicketKeeperScore;

    @InjectView(R.id.ai_wk_name)
    public TextView mAiWicketKeeperName;

    @InjectView(R.id.ai_any)
    public ImageView mAiAny;

    @InjectView(R.id.ai_any_score)
    public TextView mAiAnyScore;

    @InjectView(R.id.ai_any_name)
    public TextView mAiAnyName;

    //  result
    @InjectView(R.id.tv_pick5_readonly_result)
    public TextView mResult;

//    @InjectView(R.id.tv_readonly_score)
//    public TextView mScore;

    @InjectView(R.id.btn_readonly_share)
    public Button mShare;


    public Pick5FinishedReadonlyFragment() {
        // Required empty public constructor
    }

    public static Pick5FinishedReadonlyFragment newInstance() {
        Pick5FinishedReadonlyFragment fragment = new Pick5FinishedReadonlyFragment();
        return fragment;
    }

    public Pick5FinishedReadonlyFragment.Pick5Team myTeam = new Pick5Team();
    public Pick5FinishedReadonlyFragment.Pick5Team aiTeam = new Pick5Team();

    public class Pick5Team {
        public Player Batsman;
        public Player Bowler;
        public Player AllRounder;
        public Player WicketKeeper;
        public Player Any;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pick5_finished_readonly, container, false);
        ButterKnife.inject(this, rootView);
        this.mDetails = ((Pick5MatchActivity) getActivity()).getMatchDetails();

        loadPlayers();
        displayResults();
        displayPlayers();

        return rootView;
    }

    private void displayResults() {

        YoYo.with(Techniques.ZoomIn).duration(1000).playOn(mResult);

        if (mDetails.getResult() == 1) {
            mResult.setText("This match is under progress");
//            mScore.setText("TBD");
            return;
        }

        int myWinningCount = 0;
        if (myTeam.Batsman.getPoints() > aiTeam.Batsman.getPoints())
            myWinningCount++;
        if (myTeam.Bowler.getPoints() > aiTeam.Bowler.getPoints())
            myWinningCount++;
        if (myTeam.AllRounder.getPoints() > aiTeam.AllRounder.getPoints())
            myWinningCount++;
        if (myTeam.WicketKeeper.getPoints() > aiTeam.WicketKeeper.getPoints())
            myWinningCount++;
        if (myTeam.Any.getPoints() > aiTeam.Any.getPoints())
            myWinningCount++;

        if (myWinningCount >= 4) {
            mResult.setText("You won this match against Khelkund");
            mResult.setTextColor(getActivity().getResources().getColor(R.color.accent));
        } else {
            mResult.setText("You lost this match to Khelkund");
            mResult.setTextColor(getActivity().getResources().getColor(R.color.primary_dark));
        }

//        mScore.setText(String.valueOf(myTeam.Batsman.getPoints() + myTeam.Bowler.getPoints() + myTeam.AllRounder.getPoints() + myTeam.WicketKeeper.getPoints() + myTeam.Any.getPoints()));
    }

    private void displayPlayers() {
        int myBorderColor = Color.DKGRAY;
        int aiBorderColor = Color.DKGRAY;
        int green = Color.parseColor("#64DD17");
        int red = Color.parseColor("#D50000");

        //  show batsmen details

        if (mDetails.getResult() == 1 || myTeam.Batsman.getPoints() == aiTeam.Batsman.getPoints())
            myBorderColor = aiBorderColor = Color.DKGRAY;
        else {
            myBorderColor = (myTeam.Batsman.getPoints() > aiTeam.Batsman.getPoints()) ? green : red;
            aiBorderColor = (myTeam.Batsman.getPoints() > aiTeam.Batsman.getPoints()) ? red : green;
        }

        Picasso.with(getActivity()).load(myTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Batsman.getShortTeamName())), myBorderColor))
                .into(mMyBatsman);
        mMyBatsmanName.setText(myTeam.Batsman.getDisplayName());
        mMyBatsmanScore.setText(String.valueOf(myTeam.Batsman.getPoints()));

        Picasso.with(getActivity()).load(aiTeam.Batsman.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Batsman.getShortTeamName())), aiBorderColor)).into(mAiBatsman);
        mAiBatsmanScore.setText(String.valueOf(aiTeam.Batsman.getPoints()));
        mAiBatsmanName.setText(aiTeam.Batsman.getDisplayName());

        //  show bowler details

        if (mDetails.getResult() == 1 || myTeam.Bowler.getPoints() == aiTeam.Bowler.getPoints())
            myBorderColor = aiBorderColor = Color.DKGRAY;
        else {
            myBorderColor = (myTeam.Bowler.getPoints() > aiTeam.Bowler.getPoints()) ? green : red;
            aiBorderColor = (myTeam.Bowler.getPoints() > aiTeam.Bowler.getPoints()) ? red : green;
        }

        Picasso.with(getActivity()).load(myTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Bowler.getShortTeamName())), myBorderColor)).into(mMyBowler);
        mMyBowlerName.setText(myTeam.Bowler.getDisplayName());
        mMyBowlerScore.setText(String.valueOf(myTeam.Bowler.getPoints()));

        Picasso.with(getActivity()).load(aiTeam.Bowler.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Bowler.getShortTeamName())), aiBorderColor)).into(mAiBowler);
        mAiBowlerScore.setText(String.valueOf(aiTeam.Bowler.getPoints()));
        mAiBowlerName.setText(aiTeam.Bowler.getDisplayName());

        //  show all rounder details

        if (mDetails.getResult() == 1 || myTeam.AllRounder.getPoints() == aiTeam.AllRounder.getPoints())
            myBorderColor = aiBorderColor = Color.DKGRAY;
        else {
            myBorderColor = (myTeam.AllRounder.getPoints() > aiTeam.AllRounder.getPoints()) ? green : red;
            aiBorderColor = (myTeam.AllRounder.getPoints() > aiTeam.AllRounder.getPoints()) ? red : green;
        }

        Picasso.with(getActivity()).load(myTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.AllRounder.getShortTeamName())), myBorderColor)).into(mMyAllRounder);
        mMyAllRounderName.setText(myTeam.AllRounder.getDisplayName());
        mMyAllRounderScore.setText(String.valueOf(myTeam.AllRounder.getPoints()));

        Picasso.with(getActivity()).load(aiTeam.AllRounder.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.AllRounder.getShortTeamName())), aiBorderColor)).into(mAiAllRounder);
        mAiAllRounderScore.setText(String.valueOf(aiTeam.AllRounder.getPoints()));
        mAiAllRounderName.setText(aiTeam.AllRounder.getDisplayName());

        //  show wicket keeper details

        if (mDetails.getResult() == 1 || myTeam.WicketKeeper.getPoints() == aiTeam.WicketKeeper.getPoints())
            myBorderColor = aiBorderColor = Color.DKGRAY;
        else {
            myBorderColor = (myTeam.WicketKeeper.getPoints() > aiTeam.WicketKeeper.getPoints()) ? green : red;
            aiBorderColor = (myTeam.WicketKeeper.getPoints() > aiTeam.WicketKeeper.getPoints()) ? red : green;
        }

        Picasso.with(getActivity()).load(myTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.WicketKeeper.getShortTeamName())), myBorderColor)).into(mMyWicketKeeper);
        mMyWicketKeeperName.setText(myTeam.WicketKeeper.getDisplayName());
        mMyWicketKeeperScore.setText(String.valueOf(myTeam.WicketKeeper.getPoints()));

        Picasso.with(getActivity()).load(aiTeam.WicketKeeper.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.WicketKeeper.getShortTeamName())), aiBorderColor)).into(mAiWicketKeeper);
        mAiWicketKeeperScore.setText(String.valueOf(aiTeam.WicketKeeper.getPoints()));
        mAiWicketKeeperName.setText(aiTeam.WicketKeeper.getDisplayName());

        //  show any player details

        if (mDetails.getResult() == 1 || myTeam.Any.getPoints() == aiTeam.Any.getPoints())
            myBorderColor = aiBorderColor = Color.DKGRAY;
        else {
            myBorderColor = (myTeam.Any.getPoints() > aiTeam.Any.getPoints()) ? green : red;
            aiBorderColor = (myTeam.Any.getPoints() > aiTeam.Any.getPoints()) ? red : green;
        }

        Picasso.with(getActivity()).load(myTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(myTeam.Any.getShortTeamName())), myBorderColor)).into(mMyAny);
        mMyAnyName.setText(myTeam.Any.getDisplayName());
        mMyAnyScore.setText(String.valueOf(myTeam.Any.getPoints()));

        Picasso.with(getActivity()).load(aiTeam.Any.getImageUrl()).resize(250, 375).centerInside()
                .transform(new CircleTransform(getResources().getColor(TeamHelper.getTeamColor(aiTeam.Any.getShortTeamName())), aiBorderColor)).into(mAiAny);
        mAiAnyScore.setText(String.valueOf(aiTeam.Any.getPoints()));
        mAiAnyName.setText(aiTeam.Any.getDisplayName());
    }

    private void loadPlayers() {
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
            if (myPlayer.getType().equals("Batsman") && myTeam.Batsman == null) {
                myTeam.Batsman = myPlayer;
                aiTeam.Batsman = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("Bowler") && myTeam.Bowler == null) {
                myTeam.Bowler = myPlayer;
                aiTeam.Bowler = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("AllRounder") && myTeam.AllRounder == null) {
                myTeam.AllRounder = myPlayer;
                aiTeam.AllRounder = aiPlayer;
                continue;
            } else if (myPlayer.getType().equals("WicketKeeper") && myTeam.WicketKeeper == null) {
                myTeam.WicketKeeper = myPlayer;
                aiTeam.WicketKeeper = aiPlayer;
                continue;
            } else {
                myTeam.Any = myPlayer;
                aiTeam.Any = aiPlayer;
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_readonly_share)
    public void onShareClick()
    {
        Bitmap bitmap = getScreenBitmap(getActivity());

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

    private Bitmap getScreenBitmap(Activity activity) {

        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width,
                height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap2;

//        Bitmap bitmap;
//        View v1 = (RelativeLayout) getActivity().findViewById(R.id.rl_pick5_readonly_parent);
//        v1.setDrawingCacheEnabled(true);
//        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//        v1.setDrawingCacheEnabled(false);
//        return bitmap;

//        RelativeLayout iv = (RelativeLayout) getActivity().findViewById(R.id.rl_pick5_readonly_parent);
//        Bitmap bitmap = Bitmap.createBitmap(iv.getWidth(), iv.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bitmap);
//        c.drawColor(Color.WHITE);
//        iv.draw(c);
//        return (bitmap);
    }


}
