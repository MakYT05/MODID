package org.zeith.modid;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.LanguageAdapter;
import org.zeith.hammerlib.core.init.ItemsHL;
import org.zeith.hammerlib.proxy.HLConstants;
import org.zeith.modid.client.ModEntityRenderers;
import org.zeith.modid.custom.ZeithMob;
import org.zeith.modid.init.EntitiesMI;
import org.zeith.modid.init.RecipesMI;

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

	public ModId(IEventBus bus)
	{
		LanguageAdapter.registerMod(MOD_ID);

		bus.addListener(ModId::clientSetup);
		bus.addListener(ModId::entityAttributes);
	}

	@SubscribeEvent
	public static void entityAttributes(EntityAttributeCreationEvent event) { event.put(EntitiesMI.ZEITH_MOB, ZeithMob.createAttributes().build()); }

	private static void clientSetup(Event event) { ModEntityRenderers.registerRenderers(); }

	public static ResourceLocation id(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}