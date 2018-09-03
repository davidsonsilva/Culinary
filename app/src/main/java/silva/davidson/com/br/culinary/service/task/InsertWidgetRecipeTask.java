package silva.davidson.com.br.culinary.service.task;

import android.content.Context;
import android.os.AsyncTask;

import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.model.Step;

public class InsertWidgetRecipeTask extends AsyncTask<Recipe, Void, Recipe> {

    private final CulinaryDataBase mDb;
    private final InsertRecipeCallBack mCallBack;

    public interface InsertRecipeCallBack{
        void onInsert(Recipe recipe);
    }

    public InsertWidgetRecipeTask(Context context, InsertRecipeCallBack callBack) {
        mDb = CulinaryDataBase.getInstance(context);
        mCallBack = callBack;
    }

    @Override
    protected Recipe doInBackground(Recipe... recipes) {

        Recipe recipeRecord = recipes[0];

        mDb.recipeDAO().clear();
        mDb.recipeDAO().insert(recipeRecord);

        for(Ingredient i : recipeRecord.getIngredients()){
            i.setRecipeId(recipeRecord.getId());
        }
        mDb.ingredientDAO().insertAll(recipeRecord.getIngredients());

        for (Step s :recipeRecord.getSteps()){
             s.setRecipeId(recipeRecord.getId());
        }
        mDb.stepDAO().insertAll(recipeRecord.getSteps());

        return recipeRecord;
    }

    @Override
    protected void onPostExecute(Recipe recipe) {
        super.onPostExecute(recipe);
        if(mCallBack != null) {
            mCallBack.onInsert(recipe);
        }
    }
}
