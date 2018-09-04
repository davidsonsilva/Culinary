package silva.davidson.com.br.culinary.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import silva.davidson.com.br.culinary.model.Ingredient;

public class IngredientsViewModel extends AndroidViewModel {

    private MutableLiveData<List<Ingredient>> mIngredientsList = new MutableLiveData<>();
    private MutableLiveData<Ingredient> mIngredients = new MutableLiveData<>();

    public IngredientsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Ingredient> getIngredients() {
        return mIngredients;
    }

    public MutableLiveData<List<Ingredient>> getIngredientsList() {
        return mIngredientsList;
    }

    public String getQuantityValue(){
        return mIngredients.getValue() != null ? mIngredients.getValue().getQuantity().toString() : "";
    }
}
