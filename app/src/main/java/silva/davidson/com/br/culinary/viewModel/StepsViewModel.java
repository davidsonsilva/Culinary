package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import silva.davidson.com.br.culinary.model.Step;

public class StepsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Step>> mStepList = new MutableLiveData<>();
    private MutableLiveData<Step> mStep = new MutableLiveData<>();

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
}
