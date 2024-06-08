package watermelonmojito.cloudboots.mixins;
import com.google.j2objc.annotations.Property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import watermelonmojito.cloudboots.Cloud;
import watermelonmojito.cloudboots.CloudBoots;

@Mixin(value = Minecraft.class, remap = false)
public class TickMixin {
	@Shadow
	public World theWorld;

	@Shadow
	public EntityPlayerSP thePlayer;


	public boolean bootsEquipped = false;

	@Inject(method = "runTick", at = @At("HEAD"))
	void runTick(CallbackInfo ci) {
		//checks if the player is in a world, then runs every tick
		if (thePlayer != null && theWorld != null){
			if(thePlayer.inventory.armorItemInSlot(0) != null && thePlayer.inventory.armorItemInSlot(0).itemID == CloudBoots.armorItemCloudBoots.id) {
				bootsEquipped = true;
				Cloud.placeCloud(theWorld, thePlayer);
			} else if (bootsEquipped) {
				bootsEquipped = false;
				Cloud.removeCloud(theWorld);
			}
		}
	}
}
