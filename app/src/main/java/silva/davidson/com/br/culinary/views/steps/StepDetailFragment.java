package silva.davidson.com.br.culinary.views.steps;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.FragmentStepsDetailBinding;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class StepDetailFragment extends Fragment {

    public StepDetailFragment(){}

    private FragmentStepsDetailBinding mBinding;
    private StepsViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps_detail, container,
                false);
        mViewModel = StepsDetailsActivity.obtainViewModel(getActivity());
        mBinding.setViewModel(mViewModel);
        return mBinding.getRoot();
    }
}
