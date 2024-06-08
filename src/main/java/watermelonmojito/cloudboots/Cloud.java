package watermelonmojito.cloudboots;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;

public class Cloud {

	public static void placeCloud(World world, EntityPlayer player) {

		//Get the players feet position
		double playerPosX = player.getPosition(0.5F).xCoord;
		double playerPosY = player.getPosition(0.5F).yCoord - 1;
		double playerPosZ = player.getPosition(0.5F).zCoord;

		//Find the difference between the player's position and the last position
		double movementX = player.xo - player.x;
		double movementZ = player.zo - player.z;

		//Has the player moved more than x amount in any horizontal (x,z) direction
		if (movementX < -0 || movementX > 0 || movementZ < -0 || movementZ > 0) {

			//Project players direction forward to the next square
			double nextPlayerPosX = playerPosX;
			double nextPlayerPosY = playerPosY;
			double nextPlayerPosZ = playerPosZ;

			//keep adding the movement to the players location till the next block is found
/*			while ((MathHelper.floor_double(playerPosX) == MathHelper.floor_double(nextPlayerPosX)) &&
				   (MathHelper.floor_double(playerPosZ) == MathHelper.floor_double(nextPlayerPosZ))) {

				nextPlayerPosX = nextPlayerPosX - movementX;
				nextPlayerPosZ = nextPlayerPosZ - movementZ;
			}*/


			//Is the block below the next player pos air. If statements are to correct for funky coordinate rounding imprecision
			//If x or z is negative +1, if positive do nothing, leave it the same as nextPlayerPos
			double blockBelowPosX = Math.floor(nextPlayerPosX);
			double blockBelowPosY = Math.floor(nextPlayerPosY);
			double blockBelowPosZ = Math.floor(nextPlayerPosZ);

			//-x -z
			if(playerPosX < 0 && playerPosZ < 0) {
				blockBelowPosX = Math.floor(nextPlayerPosX + 1);
				blockBelowPosZ = Math.floor(nextPlayerPosZ + 1);
			}
			//+x -z
			else if (playerPosX > 0 && playerPosZ < 0) {
				blockBelowPosZ = Math.floor(nextPlayerPosZ + 1);
			}
			//-x +z
			else if (playerPosX < 0 && playerPosZ > 0) {
				blockBelowPosX = Math.floor(nextPlayerPosX + 1);
			}

			//to resolve issues with negative positions
			if (blockBelowPosZ < 0) {
				blockBelowPosZ = blockBelowPosZ - 1;
			}
			if (blockBelowPosX < 0) {
				blockBelowPosX = blockBelowPosX - 1;
			}
			blockBelowPosY = blockBelowPosY - 1;
			if (world.getBlock((int) blockBelowPosX, (int) blockBelowPosY, (int) blockBelowPosZ) == null) {
				world.setBlockWithNotify((int) blockBelowPosX, (int) blockBelowPosY, (int) blockBelowPosZ, 740);
			}
		}
    }
}
