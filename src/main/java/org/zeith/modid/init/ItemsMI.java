package org.zeith.modid.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.modid.ModId;
import org.zeith.modid.custom.EnderSwordItem;
import org.zeith.modid.custom.LightningWandItem;

@SimplyRegister
public interface ItemsMI {
	@RegistryName("lightning_wand")
	Item LIGHTNING_WAND = ModId.MOD_TAB.add(new LightningWandItem(new Item.Properties().durability(10).rarity(Rarity.EPIC)));

	@RegistryName("ender_sword")
	Item ENDER_SWORD = ModId.MOD_TAB.add(new EnderSwordItem(Tiers.DIAMOND, 10, 2F, new Item.Properties().durability(2000).rarity(Rarity.EPIC)));
}