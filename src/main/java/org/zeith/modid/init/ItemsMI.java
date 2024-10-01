package org.zeith.modid.init;

import net.minecraft.world.item.*;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.modid.ModId;
import org.zeith.modid.custom.EnderSwordItem;
import org.zeith.modid.custom.FireAxeItem;
import org.zeith.modid.custom.LightningWandItem;
import org.zeith.modid.custom.ZeithEggItem;

import static org.zeith.modid.init.EntitiesMI.ZEITH_MOB;

@SimplyRegister
public interface ItemsMI {
	@RegistryName("lightning_wand")
	Item LIGHTNING_WAND = ModId.MOD_TAB.add(new LightningWandItem(new Item.Properties().durability(10).rarity(Rarity.EPIC)));

	@RegistryName("ender_sword")
	Item ENDER_SWORD = ModId.MOD_TAB.add(new EnderSwordItem(Tiers.DIAMOND, 10, 2F, new Item.Properties().durability(2000).rarity(Rarity.EPIC)));

	@RegistryName("fire_axe")
	Item FIRE_AXE = ModId.MOD_TAB.add(new FireAxeItem(Tiers.DIAMOND, 6, 1F, new Item.Properties().durability(2000).rarity(Rarity.EPIC)));

	@RegistryName("zeith_egg")
	Item ZEITH_EGG = ModId.MOD_TAB.add(new ZeithEggItem(ZEITH_MOB, new Item.Properties().durability(10).rarity(Rarity.EPIC)));}