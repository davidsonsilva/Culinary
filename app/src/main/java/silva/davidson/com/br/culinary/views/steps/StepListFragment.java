package silva.davidson.com.br.culinary.views.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import silva.davidson.com.br.culinary.views.BaseActivity;

public class StepListFragment extends Fragment implements StepRecyclerViewAdapter.EventHandler {

    public static final String STEPS_RECORD =
            StepListFragment.class.getName().concat(".STEPS_RECORD");

    private ArrayList<Step> mSteps;
    private StepRecyclerViewAdapter mAdapter;
    private FragmentStepsBinding mBinding;
    private StepsViewModel mViewModel;
    private boolean isRuningOnTabletView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container,
                false);

        ViewModelFactory factory = ViewModelFactory.getInstance(getActivity().getApplication());
        mViewModel = ViewModelProviders.of(this, factory).get(StepsViewModel.class);

        isRuningOnTabletView = mBinding.getRoot().findViewById(R.id.container_step_detail) != null;

        if (mSteps != null && getContext() != null) {
            mBinding.include.stepList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mBinding.include.stepList.addItemDecoration(
                    new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            mAdapter = new StepRecyclerViewAdapter(getActivity().getApplication(), this);
            mBinding.include.stepList.setAdapter(mAdapter);
            mViewModel.getStepList().setValue(mSteps);

            if (isRuningOnTabletView) {
                startStepDetailActivity(mSteps.get(0));
            }


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
        startStepDetailActivity(step);
    }

    private void startStepDetailActivity(Step step){

        if (isRuningOnTabletView) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepDetailFragment.STEP_SELECTED, step);
            StepDetailFragment fragment =  new StepDetailFragment();
            fragment.setArguments(arguments);
            if (getFragmentManager() != null) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_step_detail, fragment)
                        .commit();
            }
        } else {

            Bundle extras = new Bundle();
            extras.putParcelableArrayList(StepsDetailsActivity.STEPS_RECORD, mSteps);
            extras.putParcelable(StepsDetailsActivity.STEP_SELECTED, step);
            StepsDetailsActivity.startActivity((BaseActivity) getActivity(), extras);
        }
    }

}
