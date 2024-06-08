package watermelonmojito.cloudboots;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

import java.util.HashMap;


public class Cloud {

	private int x;
	private int y;
	private int z;

	public Cloud(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static HashMap<Integer, Cloud> clouds = new HashMap<>();

	public static void placeCloud(World world, EntityPlayer player) {

		//Get the players feet position
		double playerPosX = player.getPosition(0.5F).xCoord;
		double playerPosY = player.getPosition(0.5F).yCoord - 1;
		double playerPosZ = player.getPosition(0.5F).zCoord;


		//Is the block below the player pos air. If statements are to correct for funky coordinate rounding imprecision
		//If x or z is negative +1, if positive do nothing, leave it the same as nextPlayerPos
		double flooredPlayerPosX = Math.floor(playerPosX);
		double flooredPlayerPosY = Math.floor(playerPosY);
		double flooredPlayerPosZ = Math.floor(playerPosZ);

		//-x -z
		if(playerPosX < 0 && playerPosZ < 0) {
			flooredPlayerPosX = Math.floor(playerPosX + 1);
			flooredPlayerPosZ = Math.floor(playerPosZ + 1);
		}

		//+x -z
		else if (playerPosX > 0 && playerPosZ < 0) {
			flooredPlayerPosZ = Math.floor(playerPosZ + 1);
		}

		//-x +z
		else if (playerPosX < 0 && playerPosZ > 0) {
			flooredPlayerPosX = Math.floor(playerPosX + 1);
		}

		//to resolve issues with negative positions
		if (flooredPlayerPosZ < 0) {
			flooredPlayerPosZ = flooredPlayerPosZ - 1;
		}

		if (flooredPlayerPosX < 0) {
			flooredPlayerPosX = flooredPlayerPosX - 1;
		}

		flooredPlayerPosY = flooredPlayerPosY - 1;


		//Checks if player is sneaking while on cloud
		if(player.isSneaking() && world.getBlock((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ) == Block.blockSnow){
			for(int j = 5; j > 0; j--){
				//Checks for null spots in clouds hashmap
				if(clouds.get(j) == null){continue;}
				//Checks to see which cloud player is standing on
				if(clouds.get(j).x == flooredPlayerPosX && clouds.get(j).y == flooredPlayerPosY && clouds.get(j).z == flooredPlayerPosZ){
					world.setBlockWithNotify(clouds.get(j).x, clouds.get(j).y, clouds.get(j).z, 0);
					Cloud crouchCloud = new Cloud(clouds.get(j).x, clouds.get(j).y - 1, clouds.get(j).z);
					clouds.put(j, crouchCloud);
					world.setBlockWithNotify(clouds.get(j).x, clouds.get(j).y, clouds.get(j).z, 740);
					break;
				}
			}
		}

		//replacing air with cloud
		if (world.getBlock((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ) == null) {
			world.setBlockWithNotify((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ, 740);
			Cloud tempCloud = new Cloud((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ);

				for(int i = 5; i > 0; i--){
					//Checks for null spots in clouds hashmap
					if(clouds.get(i) == null){continue;}
					//removes last cloud from hashmap and world
					if(i == 5){world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0); clouds.remove(i); continue;}
					//shifting over cloud to next key/index in hashmap (copy then remove)
					clouds.put(i+1, clouds.get(i));
					clouds.remove(i);
				}
			clouds.put(1, tempCloud);
		}
		//Checks if player is not on a cloud, if they aren't... then removes all clouds from world and hashmap
		if (world.getBlock((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ) != Block.blockSnow) {
			for(int i = 5; i > 0; i--){
				if(clouds.get(i) == null){continue;}
				world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
			}
			clouds.clear();
		}
	}
}
