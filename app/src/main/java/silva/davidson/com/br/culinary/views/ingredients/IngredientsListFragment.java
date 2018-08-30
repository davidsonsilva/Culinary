package silva.davidson.com.br.culinary.views.ingredients;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.IngredientsRecyclerViewAdapter;
import silva.davidson.com.br.culinary.adapter.StepRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.FragmentIngredientsBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.viewModel.IngredientsViewModel;

public class IngredientsListFragment extends Fragment {

    public static final String INGREDIENTS_RECORD =
            IngredientsListFragment.class.getName().concat(".INGREDIENTS_RECORD");

    private ArrayList<Ingredient> mIngredients;
    private IngredientsRecyclerViewAdapter mAdapter;
    private FragmentIngredientsBinding mBinding;
    private IngredientsViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container,
                false);
        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(IngredientsViewModel.class);
        if (getContext() != null && mIngredients != null) {
            mBinding.include.ingredientsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.include.ingredientsList.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mAdapter = new IngredientsRecyclerViewAdapter(getActivity().getApplication());
            mBinding.include.ingredientsList.setAdapter(mAdapter);
            mViewModel.getIngredientsList().setValue(mIngredients);
        }

        mViewModel.getIngredientsList().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                if(ingredients != null) {
                    mAdapter.setIngredientList(ingredients);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(INGREDIENTS_RECORD)) {
            mIngredients = getArguments().getParcelableArrayList(INGREDIENTS_RECORD);
        }
    }
}
