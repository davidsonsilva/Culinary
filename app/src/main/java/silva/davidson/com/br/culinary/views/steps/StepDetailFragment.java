package silva.davidson.com.br.culinary.views.steps;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.FragmentStepsDetailBinding;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class StepDetailFragment extends Fragment implements StepsViewModel.PlayerLifeCycle {

    public static final String STEP_SELECTED = StepDetailFragment.class.getName().concat(".STEP_SELECTED");
    private static final String PLAYER_STATE = StepDetailFragment.class.getName().concat(".PLAYER_STATE");


    private FragmentStepsDetailBinding mBinding;
    private StepsViewModel mViewModel;

    private ExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private Long mPlayerPosition;
    private Step mCurrentStep;
    public StepDetailFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps_detail, container,
                false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = StepsDetailsActivity.obtainViewModel(getActivity());
        mBinding.setViewModel(mViewModel);
        mViewModel.setPlayerLifeCycle(this);

        if (getArguments() != null) {
            if (getArguments().containsKey(STEP_SELECTED)) {
                mCurrentStep = getArguments().getParcelable(STEP_SELECTED);
                mViewModel.getCurrentStep().setValue(mCurrentStep);
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STEP_SELECTED)) {
                mCurrentStep = savedInstanceState.getParcelable(STEP_SELECTED);
                mViewModel.getCurrentStep().setValue(mCurrentStep);
            }
            if (savedInstanceState.containsKey(PLAYER_STATE)) {
                mPlayerPosition = savedInstanceState.getLong(PLAYER_STATE);
            }
        }

        setupToolbar();
    }

    private void setupToolbar() {
        if (mViewModel.getCurrentStep().getValue() != null) {
            AppCompatActivity activity = (AppCompatActivity) this.getActivity();
            if (activity != null) {
                activity.setTitle(mViewModel.getCurrentStep().getValue().getShortDescription());
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_SELECTED, mViewModel.getCurrentStep().getValue());
        if (mExoPlayer != null) {
            outState.putLong(PLAYER_STATE, mExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    @Override
    public void onSetupPlayer(SimpleExoPlayer exoPlayer) {
        if (isAdded()) {
            mExoPlayer = exoPlayer;
            mExoPlayer.addListener(new MyPlayerListener());
            if (mMediaSession == null) {
                initializeMediaSession();
            }
            if (mPlayerPosition != null) {
                mExoPlayer.seekTo(mPlayerPosition);
                mPlayerPosition = null;
            }
        }
    }

    private class MyPlayerListener implements Player.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) { }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) { }

        @Override
        public void onLoadingChanged(boolean isLoading) { }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if (mExoPlayer != null) {
                if ((playbackState == Player.STATE_READY) && playWhenReady) {
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                            mExoPlayer.getCurrentPosition(), 1f);
                } else if ((playbackState == Player.STATE_READY)) {
                    mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                            mExoPlayer.getCurrentPosition(), 1f);
                }
                mMediaSession.setPlaybackState(mStateBuilder.build());
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) { }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }

        @Override
        public void onPlayerError(ExoPlaybackException error) { }

        @Override
        public void onPositionDiscontinuity(int reason) {  }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) { }

        @Override
        public void onSeekProcessed() { }
    }

    private void initializeMediaSession() {
        if (getContext() != null) {
            mMediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
            mMediaSession.setFlags(
                    MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                            MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mMediaSession.setMediaButtonReceiver(null);

            mStateBuilder = new PlaybackStateCompat.Builder()
                    .setActions(
                            PlaybackStateCompat.ACTION_PLAY |
                                    PlaybackStateCompat.ACTION_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                    PlaybackStateCompat.ACTION_PLAY_PAUSE);

            mMediaSession.setPlaybackState(mStateBuilder.build());
            mMediaSession.setCallback(new MySessionCallback());
            mMediaSession.setActive(true);
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(true);
            }
        }

        @Override
        public void onPause() {
            if (mExoPlayer != null) {
                mExoPlayer.setPlayWhenReady(false);
            }
        }

        @Override
        public void onSkipToPrevious() {
            if (mExoPlayer != null) {
                mExoPlayer.seekTo(0);
            }
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

/*    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, boolean orientationLandscape) {
        view.getLayoutParams().height = orientationLandscape ? RelativeLayout.LayoutParams.MATCH_PARENT : 600;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.setOrientationLandscape(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
    }*/

}
