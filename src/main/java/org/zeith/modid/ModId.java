package org.zeith.modid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.init.ItemsHL;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.modid.client.ModEntityRenderers;
import org.zeith.modid.init.EntitiesMI;
import org.zeith.modid.init.ItemsMI;

@Mod(ModId.MOD_ID)
public class ModId
{
	public static final String MOD_ID = "modid";

	@CreativeTab.RegisterTab
	public static final CreativeTab MOD_TAB = new CreativeTab(id("root"),
			builder -> builder
					.icon(() -> ItemsHL.COPPER_GEAR.getDefaultInstance())
					.withTabsBefore(HLConstants.HL_TAB.id())
	);

	public ModId()
	{
		LanguageAdapter.registerMod(MOD_ID);
		ItemsMI.class.getCanonicalName();
		EntitiesMI.class.getCanonicalName();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
	}

	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}

	private void onClientSetup(FMLClientSetupEvent event) { ModEntityRenderers.registerRenderers(); }
}