package silva.davidson.com.br.culinary.views.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.SplashScreenActivity;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.service.task.SelectWidgetRecipeTask;
import silva.davidson.com.br.culinary.views.recipe.RecipeActivity;

public class RecipesWidgetProvider extends AppWidgetProvider {

    private static final int INTENT_REQUEST_CODE = 0;

    public static void update(Context context) {
        Intent intent = new Intent(context, RecipesWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(context).getAppWidgetIds(
                new ComponentName(context, RecipesWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        context.sendBroadcast(intent);
    }

    @Override
    public void onUpdate(final Context context,
                         final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        new SelectWidgetRecipeTask(context, new SelectWidgetRecipeTask.SelectRecipeCallBack() {
            @Override
            public void onSelect(Recipe recipe) {
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
                }
            }
        }).execute();
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId, Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_recipes);
        views.setEmptyView(R.id.widget_recipe_list, R.id.widget_empty_layout);
        views.setRemoteAdapter(R.id.widget_recipe_list,
                new Intent(context, ListWidgetFactoryService.class));

        if (recipe != null) {
            views.setTextViewText(R.id.widget_title, recipe.getName());

            Bundle extras = new Bundle();
            extras.putParcelable(RecipeActivity.RECIPE_RECORD, recipe);
            Intent appIntent = new Intent(context, RecipeActivity.class);
            appIntent.putExtras(extras);
            PendingIntent appPendingIntent = PendingIntent
                    .getActivity(context, INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_recipe_layout, appPendingIntent);
        } else {
            Intent appIntent = new Intent(context, SplashScreenActivity.class);
            PendingIntent appPendingIntent = PendingIntent
                    .getActivity(context, INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_empty_layout, appPendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_list);
    }
}
