package silva.davidson.com.br.culinary.service.task;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.model.Step;

public class SelectWidgetRecipeTaks extends AsyncTask<Void, Void, Recipe> {

    private final CulinaryDataBase mDb;
    private final SelectRecipeCallBack mCallBack;

    public SelectWidgetRecipeTaks(Context context, SelectRecipeCallBack callBack) {
        this.mDb = CulinaryDataBase.getInstance(context);
        this.mCallBack = callBack;
    }

    @Override
    protected Recipe doInBackground(Void... voids) {
        Recipe recipe = mDb.recipeDAO().get();
        if (recipe != null) {
            recipe.setIngredients((ArrayList<Ingredient>) mDb.ingredientDAO().getAll());
            recipe.setSteps((ArrayList<Step>) mDb.stepDAO().getAll());
        } /*else {
            BackingService.getClient().listRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                       @NonNull Response<ArrayList<Recipe>> response) {
                    if (response.isSuccessful()) {
                        //mCallBack.onSelect(response.body());
                        Executor executor = Executors.newSingleThreadScheduledExecutor();
                        final Recipe recipeRecord = response.body().get(0);
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {

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
                                }
                            });

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Recipe>> call,
                                      @NonNull Throwable t) {
                    Log.i("loadRecipes", t.getLocalizedMessage());

                }
            });
        }*/

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
