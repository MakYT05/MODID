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

        event.shaped()
                .result(ItemsMI.FIRE_AXE)
                .shape(" GN", " BG", " B ")
                .map('N', Items.NETHER_STAR)
                .map('G', Items.GHAST_TEAR)
                .map('B', Items.BLAZE_ROD)
                .register();

        event.shaped()
                .result(ItemsMI.ZEITH_EGG)
                .shape(" L ", "EFT", " D ")
                .map('L', ItemsMI.LIGHTNING_WAND)
                .map('E', ItemsMI.ENDER_SWORD)
                .map('F', ItemsMI.FIRE_AXE)
                .map('T', ItemsMI.TNT_BOW)
                .map('D', ItemsMI.DARK_BRAID)
                .register();
    }
}