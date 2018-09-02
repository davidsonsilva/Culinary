package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.utils.ImageHelper;

public class StepsViewModel extends AndroidViewModel {

    public interface StepsEventHandler {
        void nextPositionClick();
        void previousPositionClick();
    }

    private static final String APPLICATION_ID = "silva.davidson.com.br.culinary";
    private static PlayerLifeCycle mPlayerLifeCycle;

    @Nullable
    private StepsEventHandler stepsEventHandler;

    private MutableLiveData<List<Step>> mStepList = new MutableLiveData<>();

    private MutableLiveData<Step> mStep = new MutableLiveData<>();

    private int mStepSelectedId;

    private MutableLiveData<Step> mCurrentStep = new MutableLiveData<>();

    public StepsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Step> getStep() {
        return mStep;
    }

    public MutableLiveData<List<Step>> getStepList() {
        return mStepList;
    }

    public String getStepId(){
        return getStep().getValue().getId().toString();
    }

    public MutableLiveData<Step> getCurrentStep() {
        return mCurrentStep;
    }

    @BindingAdapter("bind:videoUrl")
    public static void loadVideo(PlayerView view, String videoURL) {
        if (videoURL == null || videoURL.isEmpty()) {
            return;
        }

        Context context = view.getContext();
        Uri videoUri = Uri.parse(videoURL);
        DefaultDataSourceFactory sourceFactory = new DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, APPLICATION_ID),
                new DefaultBandwidthMeter());

        SimpleExoPlayer exoPlayer = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
        exoPlayer.prepare(new ExtractorMediaSource.Factory(sourceFactory).createMediaSource(videoUri));
        exoPlayer.setPlayWhenReady(true);

        view.setDefaultArtwork(
                BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_chef_cooking_foreground));
        view.setPlayer(exoPlayer);

        if (mPlayerLifeCycle != null) {
            mPlayerLifeCycle.onSetupPlayer(exoPlayer);
        }
    }

    @BindingAdapter("bind:thumbnailUrl")
    public static void loadThumbnail(ImageView view, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            ImageHelper.loadImage(view.getContext(), imageUrl, view);
        } else {
            view.setImageResource(R.drawable.ic_chef_cooking_foreground);
        }
    }

    public interface PlayerLifeCycle {
        void onSetupPlayer(SimpleExoPlayer exoPlayer);
    }

    /**
     * Verifica se existe dados de thumbnail no registro
     * @return boolean
     */
    public boolean isHasThumbnail() {
        if (getCurrentStep().getValue() != null) {
            return !isHasVideo() && getCurrentStep().getValue().getThumbnailURL() != null
                    && !getCurrentStep().getValue().getThumbnailURL().isEmpty();
        }

        return false;
    }

    /**
     * Verifica se existe link de vídeo na receita
     * @return boolean
     */
    public boolean isHasVideo() {
        if (getCurrentStep().getValue() != null) {
            return getCurrentStep().getValue().getVideoURL() != null &&
                    !getCurrentStep().getValue().getVideoURL().isEmpty();
        }
        return false;
    }

    public void setPlayerLifeCycle(PlayerLifeCycle mPlayerLifeCycle) {
        StepsViewModel.mPlayerLifeCycle = mPlayerLifeCycle;
    }

    public boolean isPrevStepEnable() {
        return mCurrentStep.getValue() != null && mCurrentStep.getValue().getId() > 0;
    }

    public boolean isNextStepEnable() {
        return (mCurrentStep.getValue() != null && mCurrentStep.getValue().getId() >= 0)
                && (mStepList.getValue() != null
                && (mCurrentStep.getValue().getId() < mStepList.getValue().size() - 1));
    }

    public void nextStep() {
        if (stepsEventHandler != null) {
            if (isNextStepEnable()) {
                Step currentStep = mStepList.getValue().get(
                        mCurrentStep.getValue().getId() + 1);
                mCurrentStep.setValue(currentStep);
            }
        }
    }

    public void prevStep() {
        if (stepsEventHandler != null) {
            if (isPrevStepEnable()) {
                Step currentStep = mStepList.getValue().get(
                        mCurrentStep.getValue().getId() - 1);
                mCurrentStep.setValue(currentStep);
                //return currentStep;
            }
        }
        //return null;
    }

    public void setStepsEventHandler(StepsEventHandler stepsEventHandler) {
        this.stepsEventHandler = stepsEventHandler;
    }
}
