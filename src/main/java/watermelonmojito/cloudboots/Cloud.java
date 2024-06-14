package watermelonmojito.cloudboots;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;


public class Cloud {

	private final int x;
	private final int y;
	private final int z;
	private static int sneakCooldown = 0;
	private static int stageCounter = 2;
	public static boolean inTether;
	public static boolean outOfTether;

	public Cloud(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	//dev tool, doesn't really work just for testing
	private static final int trailLength = 5;



	//hashmap for all active clouds
	public static HashMap<Integer, Cloud> clouds = new HashMap<>();



	//custom particles for clouds
	private static void cloudParticle(World world, double x, double y, double z){
		world.spawnParticle("explode", x, y-1, z, 0.2, 0, 0.2, 10);
		world.spawnParticle("explode", x, y-1, z, -0.2, 0, -0.2, 10);
		world.spawnParticle("explode", x, y-1, z, -0.2, 0, 0.2, 10);
		world.spawnParticle("explode", x, y-1, z, 0.2, 0, -0.2, 10);
	}



	//removes all clouds from hashmap and world
	public static void removeClouds(World world){
		for(int i = trailLength; i > 0; i--){
			if(clouds.get(i) == null){continue;}
			world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
		}
		clouds.clear();
	}

	//sets all clouds stages equal to their value in the hashmap. Does not physically move anything in the world
	public static void refresh(World world){
		HashMap<Integer, Cloud> refreshClouds = new HashMap<>();
		for(int i = trailLength; i > 0; i--){
			if(clouds.get(i) == null || world.getBlock(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z) == null){continue;}
			String cloudKey = world.getBlock(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z).getKey();
			int cloudStage = Character.getNumericValue(cloudKey.charAt(cloudKey.length() - 1));
			refreshClouds.put(cloudStage, clouds.get(i));
		}
		for(int i = trailLength; i > 0; i--){
			clouds.replace(i, refreshClouds.get(i));
		}
	}


	//ran on tick for most cloud related stuff
	public static void tickInTether(World world, EntityPlayer player) {

		inTether = true;
		outOfTether = false;
		sneakCooldown++;

		//Checks if player is sneaking while on cloud or air
		if( player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage1 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage2 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage3 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage4 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage5 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == null && sneakCooldown >= 5 ){

			//reset cooldown
			sneakCooldown = 0;
			//if they are, loop through the clouds hashmap iterating backwards from 5 to 1 (trailLength is for testing)
			for(int i = trailLength; i > 0; i--){
				//Checks for null spots in clouds hashmap
				if(clouds.get(i) == null){continue;}
				//Checks to see which cloud player is standing on
				if(clouds.get(i).x == PlayerInfo.flooredPlayerFeetX && clouds.get(i).y == PlayerInfo.flooredPlayerFeetY && clouds.get(i).z == PlayerInfo.flooredPlayerFeetZ){
					//Checks to see if the block below the cloud is air
					if(world.getBlock(clouds.get(i).x, clouds.get(i).y-1, clouds.get(i).z) == null) {
						//if so, deletes cloud player is standing on and replaces it with new one, same stage, one block lower
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
						Cloud crouchCloud = new Cloud(clouds.get(i).x, clouds.get(i).y - 1, clouds.get(i).z);
						clouds.put(i, crouchCloud);
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_" + i));
						cloudParticle(world, PlayerInfo.playerFeetX, PlayerInfo.playerFeetY, PlayerInfo.playerFeetZ);
						player.setPos(PlayerInfo.playerFeetX, PlayerInfo.playerFeetY + 0.97, PlayerInfo.playerFeetZ);
						break;
					}
					//if not, remove the cloud player is standing on from world and hashmap
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
					clouds.remove(i);
				}
			}
		}
		else{
			//replacing air with cloud
			if (PlayerInfo.blockAtIntPlayerFeet == null && PlayerInfo.playerFeetY - PlayerInfo.flooredPlayerFeetY > 1.58) {
				world.setBlockWithNotify(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ, CloudBoots.config.getInt("cloud_block_stage_1"));
				cloudParticle(world, PlayerInfo.playerFeetX, PlayerInfo.playerFeetY, PlayerInfo.playerFeetZ);
				Cloud tempCloud = new Cloud(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ);


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
		}

		//Checks if player is not on a cloud, if they aren't... then removes all clouds from world and hashmap
		if (PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage1 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage2 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage3 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage4 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage5 &&
			PlayerInfo.blockAtIntPlayerFeet != null){

			for(int i = trailLength; i > 0; i--){
				if(clouds.get(i) == null){continue;}
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
			}
			clouds.clear();
		}

		//
		stageCounter = 2;
	}

	public static void tickOutOfTether(World world, EntityPlayer player) {
		outOfTether = true;
		inTether = false;
		sneakCooldown++;

		//Checks if player is sneaking while on cloud or air
		if( player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage1 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage2 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage3 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage4 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == CloudBoots.tileBlockCloudStage5 && sneakCooldown >= 5 ||
			player.isSneaking() && PlayerInfo.blockAtIntPlayerFeet == null && sneakCooldown >= 5 ){

			//reset cooldown
			sneakCooldown = 0;
			//if they are, loop through the clouds hashmap iterating backwards from 5 to 1 (trailLength is for testing)
			for(int i = trailLength; i > 0; i--){
				//Checks for null spots in clouds hashmap
				if(clouds.get(i) == null){continue;}
				//Checks to see which cloud player is standing on
				if(clouds.get(i).x == PlayerInfo.flooredPlayerFeetX && clouds.get(i).y == PlayerInfo.flooredPlayerFeetY && clouds.get(i).z == PlayerInfo.flooredPlayerFeetZ){
					//Checks to see if the block below the cloud is air
					if(world.getBlock(clouds.get(i).x, clouds.get(i).y-1, clouds.get(i).z) == null) {
						//if so, deletes cloud player is standing on and replaces it with new one, same stage, one block lower
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
						Cloud crouchCloud = new Cloud(clouds.get(i).x, clouds.get(i).y - 1, clouds.get(i).z);
						clouds.put(i, crouchCloud);
						world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, CloudBoots.config.getInt("cloud_block_stage_" + i));
						cloudParticle(world, PlayerInfo.playerFeetX, PlayerInfo.playerFeetY, PlayerInfo.playerFeetZ);
						player.setPos(PlayerInfo.playerFeetX, PlayerInfo.playerFeetY + 0.97, PlayerInfo.playerFeetZ);
						break;
					}
					//if not, remove the cloud player is standing on from world and hashmap
					world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
					clouds.remove(i);
				}
			}
		}
		else{
			//replacing air with cloud
			if(PlayerInfo.blockAtIntPlayerFeet == null && PlayerInfo.playerFeetY - PlayerInfo.flooredPlayerFeetY > 1.58){
				if(stageCounter <= 5) {
					world.setBlockWithNotify(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ, CloudBoots.config.getInt("cloud_block_stage_" + stageCounter));
					cloudParticle(world, PlayerInfo.playerFeetX, PlayerInfo.playerFeetY, PlayerInfo.playerFeetZ);
					for(int i = trailLength; i > 0; i--){
						if(clouds.get(i) == null){continue;}
						if(i == trailLength){world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0); clouds.remove(i); continue;}
						clouds.put(i+1, clouds.get(i));
						clouds.remove(i);
					}
					clouds.put(1, new Cloud(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ));
					stageCounter++;
				}
			}
		}

		//Checks if player is not on a cloud, if they aren't... then removes all clouds from world and hashmap
		if (PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage1 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage2 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage3 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage4 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage5 &&
			PlayerInfo.blockAtIntPlayerFeet != null){

			for(int i = trailLength; i > 0; i--){
				if(clouds.get(i) == null){continue;}
				world.setBlockWithNotify(clouds.get(i).x, clouds.get(i).y, clouds.get(i).z, 0);
			}
			clouds.clear();
		}
	}
}
