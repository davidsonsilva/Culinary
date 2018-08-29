package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.RecipeRecycleViewAdapter;
import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.service.BackingService;
import silva.davidson.com.br.culinary.utils.VectorDrawableUtils;

public class RecipeViewModel extends AndroidViewModel {

    private static CulinaryDataBase mdb;
    private MutableLiveData<ArrayList<Recipe>> mRecipeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Recipe> mRecipe = new MutableLiveData<>();

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
                    public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {
                        if (response.isSuccessful()) {
                            mRecipeMutableLiveData.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                        Log.i("loadRecipes", t.getLocalizedMessage());
                    }
                });
     }

    public MutableLiveData<ArrayList<Recipe>> getRecipeMutableLiveData() {
        return mRecipeMutableLiveData;
    }

    public MutableLiveData<Recipe> getRecipe() {
        return mRecipe;
    }

    public String getIngredientValue(){
        return String.format("%s" , getRecipe().getValue().getIngredients().size());
    }

    public String getStepsValue(){
        return String.format("%s" , getRecipe().getValue().getSteps().size());
    }

    public String getServingValue(){
        return String.format("%s" ,getRecipe().getValue().getServings());
    }

    public int setRoundIcon(String recipeValue) {
        //String statusRegex = status.replaceAll("\\W+\\d+", "");
        switch (recipeValue) {
            case "Nutella Pie":
                return  R.mipmap.ic_nutela_pie_round;
            case "Brownies":
                return R.mipmap.ic_brownies_round;
            case "Yellow Cake":
                return R.mipmap.ic_yellow_cake_round;
            case "Cheesecake":
                return R.mipmap.ic_cheesecake_round;
            default : return R.drawable.ic_avatar_40dp;
        }
    }
}
