package org.zeith.modid.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.modid.ModId;
import org.zeith.modid.custom.LightningWandItem;

@SimplyRegister
public interface ItemsMI {
	@RegistryName("lightning_wand")
	Item LIGHTNING_WAND = ModId.MOD_TAB.add(new LightningWandItem(new Item.Properties().durability(10).rarity(Rarity.EPIC)));
}