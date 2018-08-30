package silva.davidson.com.br.culinary.adapter;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import silva.davidson.com.br.culinary.model.Ingredient;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter
        <IngredientsRecyclerViewAdapter.ViewHolder> {


    private List<Ingredient> mIngredientList;
    private Application mContext;

    public IngredientsRecyclerViewAdapter(){

    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
