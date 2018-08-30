package silva.davidson.com.br.culinary.adapter;

import android.app.Application;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.ItemRecipeBinding;
import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> recipeArrayList;
    private Application mContext;
    private final WeakReference<EventHandler> mEventHandler;

    public interface EventHandler {
        void onItemClick(Recipe recipe);
    }

    public RecipeRecyclerViewAdapter(Application context, EventHandler eventHandler){
        mContext = context;
        mEventHandler = new WeakReference<>(eventHandler);
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipeArrayList = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemRecipeBinding mRecipeBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.item_recipe, viewGroup, false);
        mRecipeBinding.setEventHandler(mEventHandler);
        return new ViewHolder(mRecipeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        viewHolder.binding(recipeArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeArrayList != null ? recipeArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemRecipeBinding recipeBinding;
        private RecipeViewModel viewModel;

        public ViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding) {
            super(itemRecipeBinding.getRoot());
            recipeBinding = itemRecipeBinding;
        }

        public void binding(Recipe recipe){
            viewModel = new RecipeViewModel(mContext, CulinaryDataBase.getInstance(mContext));
            viewModel.getRecipe().setValue(recipe);
            recipeBinding.setViewModel(viewModel);
            recipeBinding.avatarImage.setImageResource(viewModel.setRoundIcon(recipe.getName()));
            setImageResource(recipe.getName(), recipeBinding.mediaImage);
        }

        private void setImageResource (String recipeValue, ImageView imageView) {
            switch (recipeValue) {
                case "Nutella Pie":
                    imageView.setImageResource(R.mipmap.ic_nutela_pie_foreground);
                    break;
                case "Brownies":
                    imageView.setImageResource(R.mipmap.ic_brownies_foreground);
                    break;
                case "Yellow Cake":
                    imageView.setImageResource(R.mipmap.ic_yellow_cake_foreground);
                    break;
                case "Cheesecake":
                    imageView.setImageResource(R.mipmap.ic_cheesecake_foreground);
                    break;
                default : imageView.setImageResource(R.mipmap.ic_cheesecake_foreground);
                    break;
            }
        }
    }
}
