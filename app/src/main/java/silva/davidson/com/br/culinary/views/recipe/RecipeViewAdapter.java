package silva.davidson.com.br.culinary.views.recipe;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.views.steps.StepListFragment;

public class RecipeViewAdapter extends FragmentPagerAdapter {

    private final String[] titlesTabs;
    private final Recipe mRecipe;

    public RecipeViewAdapter(FragmentManager fm, Context context, Recipe recipe) {
        super(fm);
        titlesTabs = context.getResources().getStringArray(R.array.tabs_title);
        mRecipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle stepArguments = new Bundle();
                stepArguments.putParcelableArrayList(
                        StepListFragment.STEPS_RECORD, mRecipe.getSteps());
                StepListFragment stepFragment =  new StepListFragment();
                stepFragment.setArguments(stepArguments);
                return stepFragment;
            /*case 1:
                Bundle ingredientArguments = new Bundle();
                ingredientArguments.putParcelableArrayList(
                        IngredientListFragment.BUNDLE_INGREDIENTS, mRecipe.getIngredients());
                IngredientListFragment ingredientListFragment =  new IngredientListFragment();
                ingredientListFragment.setArguments(ingredientArguments);
                return ingredientListFragment;*/
            default:
                return new StepListFragment();
        }
        //return null;
    }

    @Override
    public int getCount() {
        return titlesTabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titlesTabs[position];
    }
}
