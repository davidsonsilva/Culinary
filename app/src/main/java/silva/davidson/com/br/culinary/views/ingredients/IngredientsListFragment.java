package silva.davidson.com.br.culinary.views.ingredients;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.IngredientsRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.FragmentIngredientsBinding;
import silva.davidson.com.br.culinary.model.Ingredient;

public class IngredientsListFragment extends Fragment {

    public static final String INGREDIENTS_RECORD =
            IngredientsListFragment.class.getName().concat(".INGREDIENTS_RECORD");

    private ArrayList<Ingredient> mIngredients;
    private IngredientsRecyclerViewAdapter mAdapter;
    //private ItemListIngredientsBinding mBinding;
    private FragmentIngredientsBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container,
                false);
        if (getContext() != null && mIngredients != null) {

        }
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
