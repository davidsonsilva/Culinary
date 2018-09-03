package silva.davidson.com.br.culinary.views.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Recipe;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private List<Recipe> mRecipes;

    public RecipeRemoteViewsFactory(Context context) {
        this.mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mRecipes = CulinaryDataBase.getInstance(mContext.getApplicationContext()).recipeDAO().getAll();
    }

    @Override
    public void onDestroy() {
        mRecipes = null;
    }

    @Override
    public int getCount() {
        return mRecipes != null ? mRecipes.size() : 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Recipe recipe = mRecipes.get(position);
        RemoteViews remoteVies = new RemoteViews(mContext.getPackageName(), R.layout.item_widget_recipe);
        //TODO: Incluir os valores dos campos da tela do widget
        remoteVies.setTextViewText(R.id.widget_recipe_title, recipe.getName());
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
