package silva.davidson.com.br.culinary.service.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.model.Step;

public class SelectWidgetRecipeTask extends AsyncTask<Void, Void, Recipe> {

    private final CulinaryDataBase mDb;
    private final SelectRecipeCallBack mCallBack;

    public SelectWidgetRecipeTask(Context context, SelectRecipeCallBack callBack) {
        this.mDb = CulinaryDataBase.getInstance(context);
        this.mCallBack = callBack;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {
        Recipe recipe = mDb.recipeDAO().get();
        if (recipe != null) {
            recipe.setIngredients((ArrayList<Ingredient>) mDb.ingredientDAO().getAll());
            recipe.setSteps((ArrayList<Step>) mDb.stepDAO().getAll());
        }
        return recipe;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        if(mCallBack != null ){
            mCallBack.onSelect(recipe);
        }
    }

    public interface SelectRecipeCallBack {
        void onSelect(Recipe recipe);
    }


}
