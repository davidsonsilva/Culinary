package silva.davidson.com.br.culinary.views.recipe;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.RecipeRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.ActivityRecipeBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.service.task.InsertWidgetRecipeTask;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.views.BaseActivity;
import silva.davidson.com.br.culinary.views.widget.RecipesWidgetProvider;

public class RecipeActivity extends BaseActivity implements
        InsertWidgetRecipeTask.InsertRecipeCallBack, View.OnClickListener {

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

        mBinding.fabButtonWidget.setOnClickListener(this);

        mBinding.detailsTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mBinding.fabButtonWidget.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mBinding.fabButtonWidget.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

    @Override
    public void onInsert(Recipe recipe) {
        if (recipe != null) {
            RecipesWidgetProvider.update(this);
            Snackbar saved = Snackbar.make(mBinding.getRoot(), getString(R.string.add_recipe_widget_sucsses)
                    , Snackbar.LENGTH_SHORT);
            ((TextView) saved.getView().findViewById(android.support.design.R.id.snackbar_text))
                    .setTextColor(getResources().getColor(android.R.color.white));
            saved.show();
        }
    }

    @Override
    public void onClick(View v) {
        if(mRecipe != null) {
            new InsertWidgetRecipeTask(getApplicationContext(), this).execute(mRecipe);
        } else {
            Snackbar error = Snackbar.make(mBinding.getRoot(), getString(R.string.add_recipe_widget_error)
            , Snackbar.LENGTH_SHORT);
            ((TextView) error.getView().findViewById(android.support.design.R.id.snackbar_text))
                .setTextColor(getResources().getColor(android.R.color.white));
            error.show();
        }
    }
}
