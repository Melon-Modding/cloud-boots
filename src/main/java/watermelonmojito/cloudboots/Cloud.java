package watermelonmojito.cloudboots;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

public class Cloud {

	public static Vec3d roundVec3d(Vec3d vec3d){
		return Vec3d.createVector(Math.round(vec3d.xCoord), Math.floor(vec3d.yCoord), Math.round(vec3d.zCoord));
	}

	public static void placeCloud(World world, EntityPlayer player) {

		//Get the players feet position

		Vec3d playerPos = Vec3d.createVector(player.getPosition(1).xCoord, player.getPosition(1).yCoord - 1, player.getPosition(1).zCoord);

		//Find the difference between the player's position and the last position
		double movementX = player.xo - player.x;
		double movementZ = player.zo - player.z;

		//Has the player moved more than x amount in any horizontal (x,z) direction
		if (movementX < -0 || movementX > 0 || movementZ < -0 || movementZ > 0) {

			//Project players direction forward to the next square
			Vec3d nextPlayerPos = playerPos;

			//keep adding the movement to the players location till the next block is found
			while ((MathHelper.floor_double(playerPos.xCoord) == MathHelper.floor_double(nextPlayerPos.xCoord)) &&
				(MathHelper.floor_double(playerPos.zCoord) == MathHelper.floor_double(nextPlayerPos.zCoord))) {

				nextPlayerPos = Vec3d.createVector(nextPlayerPos.xCoord - movementX, nextPlayerPos.yCoord, nextPlayerPos.zCoord - movementZ);
			}


			//Is the block below the next player pos air, if so fill it in with DIAMOND
			Vec3d blockBelowPos = roundVec3d(nextPlayerPos);

			//to resolve issues with negative positions
			if (blockBelowPos.zCoord < 0) {
				blockBelowPos.zCoord = blockBelowPos.zCoord - 1;
			}
			if (blockBelowPos.xCoord < 0) {
				blockBelowPos.xCoord = blockBelowPos.xCoord - 1;
			}
			blockBelowPos.yCoord = blockBelowPos.yCoord - 1;
			if (world.getBlock((int) blockBelowPos.xCoord, (int) blockBelowPos.yCoord, (int) blockBelowPos.zCoord) == null) {
				world.setBlockWithNotify((int) blockBelowPos.xCoord, (int) blockBelowPos.yCoord, (int) blockBelowPos.zCoord, 740);
			}
		}
    }
}
