package silva.davidson.com.br.culinary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.adapter.RecipeRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityMainBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.test.SimpleIdlingResource;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.BaseActivity;
import silva.davidson.com.br.culinary.views.recipe.RecipeActivity;

public class MainActivity extends BaseActivity implements RecipeRecyclerViewAdapter.EventHandler
        , View.OnClickListener
        , RecipeViewModel.LoadRecipeCallBack {

    private static final String STATE_RECIPE = MainActivity.class.getName().concat(".STATE_RECIPE");

    private ActivityMainBinding mMainBinding;
    private RecipeRecyclerViewAdapter adapter;
    private ArrayList<Recipe> mRecipes;
    private RecipeViewModel viewModel;
    private SimpleIdlingResource mIdlingResource;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mMainBinding.mainToolbar);
        //For test
        mIdlingResource = new SimpleIdlingResource();

        int spanCount = 1;
        if (getResources().getBoolean(R.bool.isRuningOnTablet)) {
            spanCount = 3;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 2;
        }

        mMainBinding.recipeList.setLayoutManager(new GridLayoutManager(this, spanCount));
        mMainBinding.mainToolbar.setLogo(R.mipmap.ic_culinary);
        mMainBinding.mainToolbar.setTitleTextColor(getResources().getColor(R.color.text_primary));
        mMainBinding.progressBar.setVisibility(View.VISIBLE);

        if (isDeviceConnected()) {

            setupViewModel();

            if (savedInstanceState != null && savedInstanceState.containsKey(STATE_RECIPE)) {
                mRecipes = savedInstanceState.getParcelableArrayList(STATE_RECIPE);
                viewModel.getRecipeMutableLiveData().setValue(mRecipes);
            }

        } else {
            showError();
        }
    }

    private void setupViewModel() {
        final ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        viewModel.setLoadRecipeCallBack(this);

        viewModel.getRecipeMutableLiveData().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                if (recipes != null && !recipes.isEmpty()) {
                    mRecipes = recipes;
                    adapter = new RecipeRecyclerViewAdapter(getApplication(),
                            MainActivity.this);
                    adapter.setRecipes(recipes);
                    mMainBinding.recipeList.setAdapter(adapter);
                    mMainBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onItemClick(Recipe recipe) {
        openRecipeActivityWithBundleValue(recipe);
    }

    private void openRecipeActivityWithBundleValue(Recipe recipe) {
        Bundle extras = new Bundle();
        extras.putParcelable(RecipeActivity.RECIPE_RECORD, recipe);
        RecipeActivity.startActivity(this, extras);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(STATE_RECIPE, mRecipes);
        super.onSaveInstanceState(bundle);
    }

    private void showError() {
        mMainBinding.progressBar.setVisibility(View.GONE);
        mMainBinding.imageViewError.setVisibility(View.VISIBLE);

        Snackbar error = Snackbar.make(
                mMainBinding.mainContainer,
                R.string.conection_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_main_connection_action, this);
        ((TextView) error.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(getResources().getColor(android.R.color.white));

        error.show();
    }

    @Override
    public void onClick(View v) {
        if (isDeviceConnected()) {
            setupViewModel();
            mMainBinding.imageViewError.setVisibility(View.GONE);
        } else {
            showError();
        }
    }

    @Override
    public void onError(Throwable t) {
        mMainBinding.progressBar.setVisibility(View.GONE);
        mMainBinding.imageViewError.setVisibility(View.VISIBLE);
        mMainBinding.imageViewError.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_chef_cooking_foreground));
        Snackbar error = Snackbar.make(
                mMainBinding.mainContainer,
                t.getMessage(),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_main_connection_action, this);
        ((TextView) error.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(getResources().getColor(android.R.color.holo_red_light));
        error.show();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return mIdlingResource;
    }
}
