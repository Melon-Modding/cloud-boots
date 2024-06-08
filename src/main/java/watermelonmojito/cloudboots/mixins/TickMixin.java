package watermelonmojito.cloudboots.mixins;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;
import watermelonmojito.cloudboots.Cloud;

@Mixin(value = Minecraft.class, remap = false)
public class TickMixin {
	@Shadow
	public World theWorld;

	@Shadow
	public EntityPlayerSP thePlayer;

	@Inject(method = "runTick", at = @At("HEAD"))
	void runTick(CallbackInfo ci) {
		//checks if the player is in a world, then runs every tick
		if(!(thePlayer == null) && !(theWorld == null)) Cloud.placeCloud(theWorld, thePlayer);
	}
}
