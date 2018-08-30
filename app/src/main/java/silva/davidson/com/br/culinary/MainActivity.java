package silva.davidson.com.br.culinary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.adapter.RecipeRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityMainBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.recipe.RecipeActivity;

public class MainActivity extends AppCompatActivity implements RecipeRecyclerViewAdapter.EventHandler {

    private static final String STATE_RECIPE = MainActivity.class.getName().concat(".STATE_RECIPE");

    private ActivityMainBinding mMainBinding;
    private RecipeRecyclerViewAdapter adapter;
    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mMainBinding.mainToolbar);
        mMainBinding.mainToolbar.setLogo(R.mipmap.ic_culinary);
        mMainBinding.mainToolbar.setTitleTextColor(getResources().getColor(R.color.text_primary));
        mMainBinding.setLifecycleOwner(this);

        mMainBinding.progressBar.setVisibility(View.VISIBLE);

        viewModel.getRecipeMutableLiveData().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                adapter = new RecipeRecyclerViewAdapter(getApplication(),
                        MainActivity.this);
                adapter.setRecipes(recipes);
                mMainBinding.recipeList.setAdapter(adapter);
                mMainBinding.progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(Recipe recipe) {
        viewModel.getRecipe().setValue(recipe);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if(recipe != null) {
                    openRecipeActivityWithBundleValue(recipe);
                }
            }
        });

    }

    private void openRecipeActivityWithBundleValue(Recipe recipe) {
        Bundle extras = new Bundle();
        extras.putParcelable(RecipeActivity.RECIPE_RECORD, recipe);
        RecipeActivity.startActivity(this, extras);
    }

    public RecipeViewModel getViewModel (AppCompatActivity activity){
        final ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
    }

}
