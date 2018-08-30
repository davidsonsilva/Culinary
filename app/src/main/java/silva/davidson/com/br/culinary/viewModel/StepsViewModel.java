package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.utils.ImageHelper;

public class StepsViewModel extends AndroidViewModel {

    private static final String APPLICATION_ID = "silva.davidson.com.br.culinary";
    private static PlayerLifeCycle mPlayerLifeCycle;

    private MutableLiveData<List<Step>> mStepList = new MutableLiveData<>();
    private MutableLiveData<Step> mStep = new MutableLiveData<>();

    private MutableLiveData<Integer> mStepSelectedId = new MutableLiveData<>();
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

    public MutableLiveData<Integer> getStepSelectedId() {
        return mStepSelectedId;
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

    public boolean isHasThumbnail() {
        if (getCurrentStep().getValue() != null) {
            return !isHasVideo() && getCurrentStep().getValue().getThumbnailURL() != null
                    && !getCurrentStep().getValue().getThumbnailURL().isEmpty();
        }

        return false;
    }


    public boolean isHasVideo() {
        if (getCurrentStep().getValue() != null) {
            return getCurrentStep().getValue().getVideoURL() != null &&
                    !getCurrentStep().getValue().getVideoURL().isEmpty();
        }
        return false;
    }

    public static Step getStepById(List<Step> steps, int stepId) {
        for (Step step : steps) {
            if (step.getId() == stepId) {
                return step;
            }
        }
        return null;
    }
}
