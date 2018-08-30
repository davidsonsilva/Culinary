package silva.davidson.com.br.culinary.views.steps;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import silva.davidson.com.br.culinary.R;
import silva.davidson.com.br.culinary.databinding.ActivityStepDetailBinding;
import silva.davidson.com.br.culinary.factory.ViewModelFactory;
import silva.davidson.com.br.culinary.model.Step;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class StepsDetailsActivity extends AppCompatActivity {

    public static final String STEPS_RECORD =  StepsDetailsActivity.class.getName().concat(".STEP_RECORD");
    public static final String STEP_SELECTED =  StepsDetailsActivity.class.getName().concat(".STEP_SELECTED");
    private static final String FRAGMENT_PAGE = StepsDetailsActivity.class.getName();

    private StepDetailFragment mStepDetailFragment;

    public static void startActivity(AppCompatActivity activity, Bundle extras) {
        activity.startActivity(new Intent(activity, StepsDetailsActivity.class).putExtras(extras));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStepDetailBinding mBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_step_detail);
        setSupportActionBar(mBinding.toolbarStepDetail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewModelFactory factory = ViewModelFactory.getInstance(getApplication());
        final StepsViewModel mViewModel = ViewModelProviders.of(this, factory).get(StepsViewModel.class);

        if (getIntent().getExtras() != null
                && getIntent().hasExtra(STEPS_RECORD)
                && getIntent().hasExtra(STEP_SELECTED)) {

            if (savedInstanceState != null && savedInstanceState.containsKey(FRAGMENT_PAGE)) {
                mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState, FRAGMENT_PAGE);
            }

            ArrayList<Step> steps = getIntent().getParcelableArrayListExtra(STEPS_RECORD);

            mViewModel.getCurrentStep().setValue((Step)getIntent().getParcelableExtra(STEP_SELECTED));

            setupFragment();

            mBinding.setLifecycleOwner(this);

        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupFragment(){
        if (mStepDetailFragment == null) {
            mStepDetailFragment = new StepDetailFragment();
        }
        if (!mStepDetailFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_step_detail, mStepDetailFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_PAGE, mStepDetailFragment);
    }

    public static StepsViewModel obtainViewModel(FragmentActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return ViewModelProviders.of(activity, factory).get(StepsViewModel.class);
    }

}
