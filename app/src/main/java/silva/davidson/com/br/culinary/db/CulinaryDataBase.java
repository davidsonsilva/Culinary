package silva.davidson.com.br.culinary.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import silva.davidson.com.br.culinary.db.dao.IngredientDAO;
import silva.davidson.com.br.culinary.db.dao.RecipeDAO;
import silva.davidson.com.br.culinary.db.dao.StepDAO;
import silva.davidson.com.br.culinary.model.Ingredient;
import silva.davidson.com.br.culinary.model.Recipe;
import silva.davidson.com.br.culinary.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1)
public abstract class CulinaryDataBase extends RoomDatabase {

    private static CulinaryDataBase INSTANCE;

    public abstract RecipeDAO recipeDAO();
    public abstract IngredientDAO ingredientDAO();
    public abstract StepDAO stepDAO();

    public static CulinaryDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, CulinaryDataBase.class,
                    "culinary.db").build();
        }
        return INSTANCE;
    }
}
