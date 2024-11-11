package Model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

import static RestApis.RequestsIngredients.getIngredientsPojo;

@Data
@AllArgsConstructor
public class PojoIngredients {
    public static List<String> ingredients;
    public static void setData()
    {
        ingredients = getIngredientsPojo();
        System.out.println(ingredients);

    }
}
