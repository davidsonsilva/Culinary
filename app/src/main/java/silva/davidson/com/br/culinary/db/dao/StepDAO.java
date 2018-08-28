package silva.davidson.com.br.culinary.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import silva.davidson.com.br.culinary.model.Step;

@Dao
public interface StepDAO {
    @Query("SELECT * FROM step")
    LiveData<List<Step>> getAll();
}
