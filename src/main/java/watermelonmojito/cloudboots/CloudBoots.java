package watermelonmojito.cloudboots;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.util.helper.TexturePackJsonHelper;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.material.ArmorMaterial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.ArmorHelper;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ConfigHandler;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

import java.util.Properties;
import java.util.function.Function;


public class CloudBoots implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "cloudboots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final ConfigHandler config;

	//config id option
	static {
		Properties prop = new Properties();
		prop.setProperty("ids.cloud_boots", "32200");
		config = new ConfigHandler(MOD_ID, prop);
	}

	public static ArmorMaterial armorMaterialCloudBoots;
	public static Item armorItemCloudBoots;

	private void initializeArmorMaterials() {
		armorMaterialCloudBoots = ArmorHelper.createArmorMaterial
			("cloudboots", "cloud_boots",256,0.0F,0.0F,0.0F, 140.0F);
	}

	private void initializeItems() {
		armorItemCloudBoots = new ItemBuilder(MOD_ID).setIcon("cloudboots:item/cloud_boots").build(new ItemArmor(MOD_ID + ".cloud_boots", config.getInt("ids.cloud_boots"), armorMaterialCloudBoots, 3));
	}

    @Override
    public void onInitialize() {
		initializeArmorMaterials();
		initializeItems();
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
