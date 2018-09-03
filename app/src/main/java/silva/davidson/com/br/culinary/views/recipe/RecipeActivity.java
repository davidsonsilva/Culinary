package silva.davidson.com.br.culinary.views.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.RecipeRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityRecipeBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.service.task.InsertWidgetRecipeTask;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.BaseActivity;

public class RecipeActivity extends BaseActivity {

    public static final String RECIPE_RECORD =  RecipeActivity.class.getName().concat(".RECIPE_RECORD");
    private ActivityRecipeBinding mBinding;
    private Recipe mRecipe;
    private RecipeViewAdapter mAdapter;
    private RecipeViewModel viewModel;


    public static void startActivity(BaseActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, RecipeActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
        setSupportActionBar(mBinding.recipeToolbar);
        mBinding.recipeToolbar.setTitleTextColor(getResources().getColor(R.color.text_primary));

        setupViewModel();

        if(getIntent().getExtras() != null && getIntent().hasExtra(RECIPE_RECORD)) {
            mRecipe = getIntent().getParcelableExtra(RECIPE_RECORD);
            viewModel.getRecipe().setValue(mRecipe);
        } else {
            finish();
        }

    }

    private void setupViewModel() {
        final ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        viewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {

                if (recipe != null) {

                    mBinding.detailsPager.setAdapter(new RecipeViewAdapter(getSupportFragmentManager(),
                            RecipeActivity.this, recipe));
                    mBinding.detailsTab.setupWithViewPager(mBinding.detailsPager);

                    setupActionBar();

                    putBackGroundImage();

                }
            }
        });
    }

    private void putBackGroundImage() {
        switch (mRecipe.getName()) {
            case "Nutella Pie":
                mBinding.recipeImage.setImageResource(R.mipmap.ic_nutela_pie_foreground);
                break;
            case "Brownies":
                mBinding.recipeImage.setImageResource(R.mipmap.ic_brownies_foreground);
                break;
            case "Yellow Cake":
                mBinding.recipeImage.setImageResource(R.mipmap.ic_yellow_cake_foreground);
                break;
            case "Cheesecake":
                mBinding.recipeImage.setImageResource(R.mipmap.ic_cheesecake_foreground);
                break;
            default : mBinding.recipeImage.setImageResource(R.mipmap.ic_cheesecake_foreground);
                break;
        }
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mRecipe.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
