package silva.davidson.com.br.culinary.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import silva.davidson.com.br.culinary.model.Step;

@Dao
public interface StepDAO {

    @Query("SELECT * FROM step")
    List<Step> getAll();

    @Insert
    void insertAll(List<Step> steps);
}
