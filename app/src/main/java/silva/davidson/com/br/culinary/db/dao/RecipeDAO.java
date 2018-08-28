package silva.davidson.com.br.culinary.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import silva.davidson.com.br.culinary.model.Recipe;

@Dao
public interface RecipeDAO {
    @Query("SELECT * FROM recipe LIMIT 1")
    LiveData<Recipe> get();

    @Insert
    void insert(Recipe recipe);

    @Query("DELETE FROM recipe")
    void clear();
}
