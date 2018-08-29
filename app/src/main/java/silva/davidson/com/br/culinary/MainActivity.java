package silva.davidson.com.br.culinary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.adapter.RecipeRecycleViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityMainBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.recipe.RecipeActivity;

public class MainActivity extends AppCompatActivity implements RecipeRecycleViewAdapter.EventHandler {

    private ActivityMainBinding mMainBinding;
    private RecipeRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setSupportActionBar(mMainBinding.mainToolbar);
        mMainBinding.mainToolbar.setLogo(R.mipmap.ic_culinary);
        mMainBinding.mainToolbar.setTitleTextColor(getResources().getColor(R.color.text_primary));

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);

        viewModel.loadRecipes();

        viewModel.getRecipeMutableLiveData().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                adapter = new RecipeRecycleViewAdapter(getApplication(),
                        MainActivity.this);
                adapter.setRecipes(recipes);
                mMainBinding.recipeList.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onItemClick(Recipe recipe) {
        Snackbar.make(mMainBinding.mainContainer,
                " Recipe selected : " + recipe.getName(), Snackbar.LENGTH_LONG).show();
        openRecypeActivityWithBundleValue(recipe);
    }

    private void openRecypeActivityWithBundleValue(Recipe recipe) {
        Bundle extras = new Bundle();
        extras.putParcelable(RecipeActivity.RECIPE_RECORD, recipe);
        RecipeActivity.startActivity(this, extras);
    }
}
