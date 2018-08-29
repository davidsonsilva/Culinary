package silva.davidson.com.br.culinary.adapter;

import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.ItemListStepsBinding;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {

    private final WeakReference<StepRecyclerViewAdapter.EventHandler> mEventHandler;
    private List<Step> mStepsList;
    private Application mContext;


    public StepRecyclerViewAdapter(Application context, EventHandler eventHandler) {
        mContext = context;
        mEventHandler = new WeakReference<>(eventHandler);
    }

    public void setStepsList(List<Step> mStepsList) {
        this.mStepsList = mStepsList;
        notifyDataSetChanged();
    }

    public interface EventHandler{
        void onItemClick(Step step);
    }

    @NonNull
    @Override
    public StepRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemListStepsBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_list_steps, parent, false);
        binding.setEventHandler(mEventHandler);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StepRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.binding(mStepsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mStepsList != null ? mStepsList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemListStepsBinding mBinding;
        private StepsViewModel mViewModel;

        public ViewHolder(ItemListStepsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mViewModel = new StepsViewModel(mContext);
            mBinding.setViewModel(mViewModel);
        }

        public void binding(Step step){
            mViewModel.getStep().setValue(step);
        }
    }
}
