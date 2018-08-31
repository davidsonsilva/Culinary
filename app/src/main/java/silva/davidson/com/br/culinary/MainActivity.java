package silva.davidson.com.br.culinary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.adapter.RecipeRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityMainBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.BaseActivity;
import silva.davidson.com.br.culinary.views.recipe.RecipeActivity;

public class MainActivity extends BaseActivity implements RecipeRecyclerViewAdapter.EventHandler
        , View.OnClickListener{

    private static final String STATE_RECIPE = MainActivity.class.getName().concat(".STATE_RECIPE");

    private ActivityMainBinding mMainBinding;
    private RecipeRecyclerViewAdapter adapter;
    private RecipeViewModel viewModel;
    private ArrayList<Recipe> mRecipes;

    public static void startActivity(BaseActivity activity) {
        activity.startActivity(
                new Intent(activity, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mMainBinding.mainToolbar);
        mMainBinding.mainToolbar.setLogo(R.mipmap.ic_culinary);
        mMainBinding.mainToolbar.setTitleTextColor(getResources().getColor(R.color.text_primary));

        mMainBinding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getRecipeMutableLiveData().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                if(recipes != null && !recipes.isEmpty()){
                    adapter = new RecipeRecyclerViewAdapter(getApplication(),
                            MainActivity.this);
                    adapter.setRecipes(recipes);
                    mMainBinding.recipeList.setAdapter(adapter);
                    mMainBinding.progressBar.setVisibility(View.GONE);
                }
            }
        });

        if(savedInstanceState != null && savedInstanceState.containsKey(STATE_RECIPE)) {
            mRecipes = savedInstanceState.getParcelableArrayList(STATE_RECIPE);
            adapter.setRecipes(mRecipes);
        }
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
        Snackbar error = Snackbar.make(
                mMainBinding.mainContainer,
                R.string.conection_error,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.error_main_connection_action, this);
        ((TextView) error.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(getResources().getColor(android.R.color.white));    }

    @Override
    public void onClick(View v) {

    }
}
