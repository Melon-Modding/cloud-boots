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

	private static final int trailLength = 5;

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
		double playerFeetX = player.getPosition(0.5F).xCoord;
		double playerFeetY = player.getPosition(0.5F).yCoord - 1;
		double playerFeetZ = player.getPosition(0.5F).zCoord;

		//Floor it
		double flooredPlayerFeetX = Math.floor(playerFeetX);
		double flooredPlayerFeetY = Math.floor(playerFeetY) - 1;
		double flooredPlayerFeetZ = Math.floor(playerFeetZ);

		//Cast to int
		int intPlayerFeetX = (int) flooredPlayerFeetX;
		int intPlayerFeetY = (int) flooredPlayerFeetY;
		int intPlayerFeetZ = (int) flooredPlayerFeetZ;
		Block blockAtIntPlayerFeet = world.getBlock(intPlayerFeetX, intPlayerFeetY, intPlayerFeetZ);

		//Checks if player is sneaking while on cloud
		if( player.isSneaking() && blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage1 ||
			player.isSneaking() && blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage2 ||
			player.isSneaking() && blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage3 ||
			player.isSneaking() && blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage4 ||
			player.isSneaking() && blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage5 ){

			//if they are, loop through the clouds hashmap iterating backwards from 5 to 1 (trailLength is for testing)
			for(int i = trailLength; i > 0; i--){
				//Checks for null spots in clouds hashmap
				if(clouds.get(i) == null){continue;}
				//Checks to see which cloud player is standing on
				if(clouds.get(i).x == flooredPlayerFeetX && clouds.get(i).y == flooredPlayerFeetY && clouds.get(i).z == flooredPlayerFeetZ){
					//Checks to see if the block below the cloud is air
					if(world.getBlock(clouds.get(i).x, clouds.get(i).y-1, clouds.get(i).z) == null) {
						//if so, deletes cloud player is standing on and replaces it with new one, same stage, one block lower
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
						Cloud crouchCloud = new Cloud(clouds.get(i).x, clouds.get(i).y - 1, clouds.get(i).z);
						clouds.put(i, crouchCloud);
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_" + i));
						player.setPos(playerFeetX, playerFeetY + 0.97, playerFeetZ);
						break;
					}
					//if not, remove the cloud player is standing on from world and hashmap
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
					clouds.remove(i);
				}
			}
		}

		//replacing air with cloud
		if (blockAtIntPlayerFeet == null && playerFeetY - flooredPlayerFeetY > 1.58) {
   			world.setBlockWithNotify(intPlayerFeetX, intPlayerFeetY, intPlayerFeetZ, CloudBoots.config.getInt("cloud_block_stage_1"));
			world.spawnParticle("explode", playerFeetX, playerFeetY-1, playerFeetZ, 0.2, 0, 0.2, 10);
			world.spawnParticle("explode", playerFeetX, playerFeetY-1, playerFeetZ, -0.2, 0, -0.2, 10);
			world.spawnParticle("explode", playerFeetX, playerFeetY-1, playerFeetZ, -0.2, 0, 0.2, 10);
			world.spawnParticle("explode", playerFeetX, playerFeetY-1, playerFeetZ, 0.2, 0, -0.2, 10);
			Cloud tempCloud = new Cloud(intPlayerFeetX, intPlayerFeetY, intPlayerFeetZ);

				for(int i = trailLength; i > 0; i--){
					//Checks for null spots in clouds hashmap
					if(clouds.get(i) == null){continue;}
					//removes last cloud from hashmap and world
					if(i == trailLength){world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0); clouds.remove(i); continue;}
					//shifting over cloud to next key/index in hashmap while also incrementally replacing previous clouds with later stages
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_" + (i+1)));
					clouds.put(i+1, clouds.get(i));
					clouds.remove(i);
				}

			clouds.put(1, tempCloud);
		}

		//Checks if player is not on a cloud, if they aren't... then removes all clouds from world and hashmap
		if (blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage1 &&
			blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage2 &&
			blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage3 &&
			blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage4 &&
			blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage5 &&
			blockAtIntPlayerFeet != null){

			for(int i = trailLength; i > 0; i--){
				if(clouds.get(i) == null){continue;}
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
			}
			clouds.clear();
		}
	}
}
