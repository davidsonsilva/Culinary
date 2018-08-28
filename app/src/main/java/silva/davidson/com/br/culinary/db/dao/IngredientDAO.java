package silva.davidson.com.br.culinary.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import silva.davidson.com.br.culinary.model.Ingredient;

@Dao
public interface IngredientDAO {
    @Query("SELECT * FROM ingredient")
    LiveData<List<Ingredient>> getAll();

    @Insert
    void insertAll(List<Ingredient> ingredients);
}
