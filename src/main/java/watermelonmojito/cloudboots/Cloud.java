package watermelonmojito.cloudboots;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import watermelonmojito.cloudboots.cloudblocks.CloudBlock;

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

	private static int trailLength = 5;

	public static HashMap<Integer, Cloud> clouds = new HashMap<>();

	public static void removeCloud(World world){
		for(int i = trailLength; i > 0; i--){
			if(clouds.get(i) == null){continue;}
			world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
		}
		clouds.clear();
	}

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


		Block blockAtFlooredPlayerPos = world.getBlock((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ);

		//Checks if player is sneaking while on cloud
		if( player.isSneaking() && blockAtFlooredPlayerPos == CloudBoots.tileBlockCloudStage1 ||
			player.isSneaking() && blockAtFlooredPlayerPos == CloudBoots.tileBlockCloudStage2 ||
			player.isSneaking() && blockAtFlooredPlayerPos == CloudBoots.tileBlockCloudStage3 ||
			player.isSneaking() && blockAtFlooredPlayerPos == CloudBoots.tileBlockCloudStage4 ||
			player.isSneaking() && blockAtFlooredPlayerPos == CloudBoots.tileBlockCloudStage5 ){

			for(int i = trailLength; i > 0; i--){
				//Checks for null spots in clouds hashmap
				if(clouds.get(i) == null){continue;}
				//Checks to see which cloud player is standing on
				if(clouds.get(i).x == flooredPlayerPosX && clouds.get(i).y == flooredPlayerPosY && clouds.get(i).z == flooredPlayerPosZ){
					//Checks to see if the block below is air
					if(world.getBlock(clouds.get(i).x, clouds.get(i).y-1, clouds.get(i).z) == null) {
						//deletes cloud player is standing on and replaces it with new one, one block lower
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
						Cloud crouchCloud = new Cloud(clouds.get(i).x, clouds.get(i).y - 1, clouds.get(i).z);
						clouds.put(i, crouchCloud);
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_1"));
						player.setPos(playerPosX, playerPosY + 0.97, playerPosZ);
						break;
					}
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
					clouds.remove(i);
				}
			}
		}

		//replacing air with cloud
		if (world.getBlock((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ) == null && playerPosY - flooredPlayerPosY > 1.58) {
   			world.setBlockWithNotify((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ, CloudBoots.config.getInt("cloud_block_stage_1"));
			Cloud tempCloud = new Cloud((int) flooredPlayerPosX, (int) flooredPlayerPosY, (int) flooredPlayerPosZ);

				for(int i = trailLength; i > 0; i--){
					//Checks for null spots in clouds hashmap
					if(clouds.get(i) == null){continue;}
					//removes last cloud from hashmap and world
					if(i == trailLength){world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0); clouds.remove(i); continue;}
					//shifting over cloud to next key/index in hashmap (copy then remove)
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_" + (i+1)));
					clouds.put(i+1, clouds.get(i));
					clouds.remove(i);
				}
			clouds.put(1, tempCloud);
		}
		//Checks if player is not on a cloud, if they aren't... then removes all clouds from world and hashmap
		if (blockAtFlooredPlayerPos != CloudBoots.tileBlockCloudStage1 &&
			blockAtFlooredPlayerPos != CloudBoots.tileBlockCloudStage2 &&
			blockAtFlooredPlayerPos != CloudBoots.tileBlockCloudStage3 &&
			blockAtFlooredPlayerPos != CloudBoots.tileBlockCloudStage4 &&
			blockAtFlooredPlayerPos != CloudBoots.tileBlockCloudStage5 &&
			blockAtFlooredPlayerPos != null){

			for(int i = trailLength; i > 0; i--){
				if(clouds.get(i) == null){continue;}
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
			}
			clouds.clear();
		}
	}
}
