package silva.davidson.com.br.culinary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.adapter.RecipeRecycleViewAdapter;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;

public class MainActivity extends AppCompatActivity {

    private RecipeRecycleViewAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        RecipeViewModel viewModel = ViewModelProviders.of(this, factory).get(RecipeViewModel.class);
        mRecyclerView = findViewById(R.id.recipe_list);
        viewModel.loadRecipes();
        viewModel.getRecipeMutableLiveData().observe(this, new Observer<ArrayList<Recipe>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Recipe> recipes) {
                adapter = new RecipeRecycleViewAdapter(getApplication());
                adapter.setRecipes(recipes);
                mRecyclerView.setAdapter(adapter);
            }
        });
    }
}
