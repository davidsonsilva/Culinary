package silva.davidson.com.br.culinary.adapter;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.ItemListIngredientsBinding;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.viewModel.IngredientsViewModel;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter
        <IngredientsRecyclerViewAdapter.ViewHolder> {


    private List<Ingredient> mIngredientList;
    private Application mContext;

    public IngredientsRecyclerViewAdapter(Application context){
        mContext = context;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListIngredientsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.item_list_ingredients, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.binding(mIngredientList.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredientList != null ? mIngredientList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListIngredientsBinding mBinding;
        private IngredientsViewModel mViewModel;

        public ViewHolder(ItemListIngredientsBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
            mViewModel = new IngredientsViewModel(mContext);
        }

        public void binding(Ingredient ingredient) {
            mViewModel.getIngredients().setValue(ingredient);
            mBinding.setViewModel(mViewModel);
            setupIcons(mBinding.imgViewMeasure, ingredient.getMeasure());
        }

        private void setupIcons(ImageView imageView, String measure){
            switch (measure) {
                case "TSP": imageView.setImageResource(R.mipmap.ic_tsp_spoon_round);
                    break;
                case "CUP": imageView.setImageResource(R.mipmap.ic_cup_round);
                    break;
                case "TBLSP": imageView.setImageResource(R.mipmap.ic_tbls_spoon_round);
                    break;
                case "OZ" : imageView.setImageResource(R.mipmap.ic_oz_round);
                    break;
                case "UNIT": imageView.setImageResource(R.mipmap.ic_unit_round);
                    break;
                default: imageView.setImageResource(R.mipmap.ic_measure_round);
                    break;
            }
        }
    }
}
