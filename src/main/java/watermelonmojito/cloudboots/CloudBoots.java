package watermelonmojito.cloudboots;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import watermelonmojito.cloudboots.cloudblocks.CloudBlock;

import java.util.Properties;


public class CloudBoots implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "cloudboots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigHandler config;

	//config id option
	static {
		Properties prop = new Properties();
		prop.setProperty("cloudboots", "32200");
		prop.setProperty("cloud_block_stage_1", "16301");
		prop.setProperty("cloud_block_stage_2", "16302");
		prop.setProperty("cloud_block_stage_3", "16303");
		prop.setProperty("cloud_block_stage_4", "16304");
		prop.setProperty("cloud_block_stage_5", "16305");
		config = new ConfigHandler(MOD_ID, prop);
	}

	public static ArmorMaterial armorMaterialCloudBoots;
	public static Item armorItemCloudBoots;
	public static Block tileBlockCloudStage1;
	public static Block tileBlockCloudStage2;
	public static Block tileBlockCloudStage3;
	public static Block tileBlockCloudStage4;
	public static Block tileBlockCloudStage5;

	private void initializeArmorMaterials() {
		armorMaterialCloudBoots = ArmorHelper.createArmorMaterial
			("cloudboots", "cloudboots",256,0.0F,0.0F,0.0F, 140.0F);
	}

	private void initializeItems() {
		armorItemCloudBoots = new ItemBuilder(MOD_ID).setIcon("cloudboots:item/cloud_boots").build(new ItemArmor("armor.boots.cloudboots", config.getInt("cloudboots"), armorMaterialCloudBoots, 3));
	}

	private void initializeBlocks(){
		tileBlockCloudStage1 = new BlockBuilder(MOD_ID).setSideTextures("cloudboots:block/cloud1").setTopBottomTextures("cloudboots:block/cloud1").build(new CloudBlock("cloud_block_stage_1", config.getInt("cloud_block_stage_1")));
		tileBlockCloudStage2 = new BlockBuilder(MOD_ID).setSideTextures("cloudboots:block/cloud2").setTopBottomTextures("cloudboots:block/cloud2").build(new CloudBlock("cloud_block_stage_2", config.getInt("cloud_block_stage_2")));
		tileBlockCloudStage3 = new BlockBuilder(MOD_ID).setSideTextures("cloudboots:block/cloud3").setTopBottomTextures("cloudboots:block/cloud3").build(new CloudBlock("cloud_block_stage_3", config.getInt("cloud_block_stage_3")));
		tileBlockCloudStage4 = new BlockBuilder(MOD_ID).setSideTextures("cloudboots:block/cloud4").setTopBottomTextures("cloudboots:block/cloud4").build(new CloudBlock("cloud_block_stage_4", config.getInt("cloud_block_stage_4")));
		tileBlockCloudStage5 = new BlockBuilder(MOD_ID).setSideTextures("cloudboots:block/cloud5").setTopBottomTextures("cloudboots:block/cloud5").build(new CloudBlock("cloud_block_stage_5", config.getInt("cloud_block_stage_5")));
	}

    @Override
    public void onInitialize() {
		initializeArmorMaterials();
		initializeItems();
		initializeBlocks();
		LOGGER.info(MOD_ID + " initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}

	@Override
	public void initNamespaces() {

	}
}
