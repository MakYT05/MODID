package org.zeith.modid.init;

import net.minecraft.world.item.Items;
import org.zeith.hammerlib.annotations.ProvideRecipes;
import org.zeith.hammerlib.api.IRecipeProvider;
import org.zeith.hammerlib.event.recipe.RegisterRecipesEvent;

@ProvideRecipes
public class RecipesMI implements IRecipeProvider {

    @Override
    public void provideRecipes(RegisterRecipesEvent event) {
        event.shaped()
                .result(ItemsMI.LIGHTNING_WAND)
                .shape(" N ", " B ", " B ")
                .map('N', Items.NETHER_STAR)
                .map('B', Items.BLAZE_ROD)
                .register();
    }
}