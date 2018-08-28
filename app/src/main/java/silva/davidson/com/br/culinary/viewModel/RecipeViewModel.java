package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.service.BackingService;

public class RecipeViewModel extends AndroidViewModel {

    private static CulinaryDataBase mdb;
    private MutableLiveData<ArrayList<Recipe>> mRecipeMutableLiveData = new MutableLiveData<>();
    private BackingService mService;

    private RecipeViewModel(@NonNull Application application) {
        super(application);
    }

    public RecipeViewModel(Application mApplication, CulinaryDataBase db) {
        super(mApplication);
        mdb = db;
    }

    public void loadRecipes() {
        BackingService.getClient().listRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,@NonNull Response<ArrayList<Recipe>> response) {
                if (response.isSuccessful()) {
                    mRecipeMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call,@NonNull Throwable t) {

            }
        });
    }
}
