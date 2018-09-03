package silva.davidson.com.br.culinary.views.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Ingredient;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private List<Ingredient> mRecipesIgredients;

    public RecipeRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mRecipesIgredients = CulinaryDataBase.getInstance(mContext.getApplicationContext()).ingredientDAO().getAll();
    }

    @Override
    public void onDestroy() {
        mRecipesIgredients = null;
    }

    @Override
    public int getCount() {
        return mRecipesIgredients != null ? mRecipesIgredients.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = mRecipesIgredients.get(position);
        RemoteViews remoteVies = new RemoteViews(mContext.getPackageName(),
                R.layout.item_widget_recipe_ingredient);
        remoteVies.setTextViewText(R.id.widget_recipe_ingredient_title, ingredient.getIngredient());
        remoteVies.setTextViewText(R.id.widget_recipe_ingredient_measure,
                mContext.getString(R.string.recipe_ingredient_list_quantity,
                        String.valueOf(ingredient.getQuantity()),
                        ingredient.getMeasure()));
        return remoteVies;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
