package silva.davidson.com.br.culinary.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.model.Step;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM recipe LIMIT 1")
    Recipe get();

    @Query("SELECT * FROM recipe")
    List<Recipe> getAll();

    @Insert
    void insert(Recipe recipe);

    @Insert
    void insertAll(Recipe recipe, List<Step> steps, List<Ingredient> ingredients);

    @Query("DELETE FROM recipe")
    void clear();
}
