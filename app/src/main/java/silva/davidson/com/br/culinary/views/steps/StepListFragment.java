package silva.davidson.com.br.culinary.views.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.adapter.StepRecyclerViewAdapter;
import silva.davidson.com.br.culinary.databinding.FragmentStepsBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class StepListFragment extends Fragment implements StepRecyclerViewAdapter.EventHandler {

    public static final String STEPS_RECORD =
            StepListFragment.class.getName().concat(".STEPS_RECORD");

    private ArrayList<Step> mSteps;
    private StepRecyclerViewAdapter mAdapter;
    private FragmentStepsBinding mBinding;
    private StepsViewModel mViewModel;
    //private boolean isTabletView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container,
                false);

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(StepsViewModel.class);

        if (mSteps != null && getContext() != null) {
            mBinding.include.stepList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.include.stepList.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mAdapter = new StepRecyclerViewAdapter(getActivity().getApplication(), this);
            mBinding.include.stepList.setAdapter(mAdapter);
            mViewModel.getStepList().setValue(mSteps);
        }

        mViewModel.getStepList().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if(steps != null) {
                    mAdapter.setStepsList(steps);
                }
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(STEPS_RECORD)) {
            mSteps = getArguments().getParcelableArrayList(STEPS_RECORD);
        }
    }

    @Override
    public void onItemClick(Step step) {
        Snackbar snackbar =
        Snackbar.make(mBinding.getRoot(),
                "Steps selected :" + step.getShortDescription(), Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text))
        .setTextColor(getResources().getColor(android.R.color.white));
        snackbar.show();
    }

}
