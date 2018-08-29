package silva.davidson.com.br.culinary.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import silva.davidson.com.br.culinary.db.CulinaryDataBase;
import silva.davidson.com.br.culinary.viewModel.RecipeViewModel;
import silva.davidson.com.br.culinary.viewModel.StepsViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulinaryDataBase mdb;
    private final Application mApplication;
    private static volatile ViewModelFactory INSTANCE;

    private  ViewModelFactory(CulinaryDataBase db, Application application) {
        this.mdb = db;
        this.mApplication = application;
    }

    public static ViewModelFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(CulinaryDataBase.getInstance(application
                            .getApplicationContext()),
                            application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RecipeViewModel.class)) {
            return (T) new RecipeViewModel(mApplication, mdb);
        } else if (modelClass.isAssignableFrom(StepsViewModel.class)) {
            return (T) new StepsViewModel(mApplication);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

