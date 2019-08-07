package com.simibubi.create;

import com.simibubi.create.modules.curiosities.item.TreeFertilizerItem;
import com.simibubi.create.modules.curiosities.placementHandgun.BuilderGunItem;
import com.simibubi.create.modules.curiosities.placementHandgun.BuilderGunItemRenderer;
import com.simibubi.create.modules.curiosities.placementHandgun.BuilderGunModel;
import com.simibubi.create.modules.schematics.item.BlueprintAndQuillItem;
import com.simibubi.create.modules.schematics.item.BlueprintItem;
import com.simibubi.create.modules.symmetry.SymmetryWandItem;
import com.simibubi.create.modules.symmetry.client.SymmetryWandItemRenderer;
import com.simibubi.create.modules.symmetry.client.SymmetryWandModel;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
public enum AllItems {

	SYMMETRY_WAND(new SymmetryWandItem(
			standardProperties().rarity(Rarity.UNCOMMON).setTEISR(() -> () -> withSpecialRenderer("wand")))),
	PLACEMENT_HANDGUN(new BuilderGunItem(
			new Properties().rarity(Rarity.UNCOMMON).setTEISR(() -> () -> withSpecialRenderer("gun")))),

	ANDESITE_ALLOY_CUBE(new Item(standardProperties())), BLAZE_BRASS_CUBE(new Item(standardProperties())),
	CHORUS_CHROME_CUBE(new Item(standardProperties().rarity(Rarity.UNCOMMON))),

	TREE_FERTILIZER(new TreeFertilizerItem(standardProperties())),
	EMPTY_BLUEPRINT(new Item(standardProperties().maxStackSize(1))),
	BLUEPRINT_AND_QUILL(new BlueprintAndQuillItem(standardProperties().maxStackSize(1))),
	BLUEPRINT(new BlueprintItem(standardProperties())),
	
	;

	public Item item;

	private AllItems(Item item) {
		this.item = item;
		this.item.setRegistryName(Create.ID, this.name().toLowerCase());
	}

	public static Properties standardProperties() {
		return new Properties().group(Create.creativeTab);
	}

	public static void registerItems(IForgeRegistry<Item> iForgeRegistry) {
		for (AllItems item : values()) {
			iForgeRegistry.register(item.get());
		}
	}

	public Item get() {
		return item;
	}

	public boolean typeOf(ItemStack stack) {
		return stack.getItem() == item;
	}

	@OnlyIn(Dist.CLIENT)
	public static ItemStackTileEntityRenderer withSpecialRenderer(String renderer) {
		if ("wand".equals(renderer))
			return new SymmetryWandItemRenderer();
		if ("gun".equals(renderer))
			return new BuilderGunItemRenderer();
		return null;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void onModelBake(ModelBakeEvent event) {

		ModelResourceLocation wandLocation = getModelLocation(SYMMETRY_WAND);
		IBakedModel template = event.getModelRegistry().get(wandLocation);
		event.getModelRegistry().put(wandLocation, new SymmetryWandModel(template).loadPartials(event));

		ModelResourceLocation handgunLocation = getModelLocation(PLACEMENT_HANDGUN);
		template = event.getModelRegistry().get(handgunLocation);
		event.getModelRegistry().put(handgunLocation, new BuilderGunModel(template).loadPartials(event));
	}

	protected static ModelResourceLocation getModelLocation(AllItems item) {
		return new ModelResourceLocation(item.item.getRegistryName(), "inventory");
	}

}