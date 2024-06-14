package watermelonmojito.cloudboots;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

import java.util.ArrayList;

public class Tether {

	private static int x;
	private static int y;
	private static int z;
	private static int size;
	public static boolean wasOutOfTether;

	//Constructor
	public Tether(int x, int y, int z, int size){
		Tether.x = x;
		Tether.y = y;
		Tether.z = z;
		Tether.size = size;
	}

	//array for all active/placed tethers
	public static ArrayList<Tether> tethers = new ArrayList<>();

	//method for distance between two 3-dimensional points
	static double distance(double x1, double y1, double z1, float x2, float y2, float z2) {
        return Math.pow((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2)), 0.5);
	}

	//checks if the player is in a tether
	public static boolean isInTether(Tether tether, EntityPlayer player){
		if(tether != null) {
			return distance(player.x, player.y, player.z, tether.x, tether.y, tether.z) < tether.size;
		}
        return false;
    }

	//checks if the player is in ANY tether in the tethers hashmap
	public static boolean isInAnyTether(EntityPlayer player){
		if(!tethers.isEmpty()) {
			for (int i = 0; i <= tethers.size()-1; i++) {
				if (isInTether(tethers.get(i), player)) {
					return true;
				}
			}
		}
        return false;
    }

	//creates a constant tether in index 0 that follows the player
	public static void tetherAtFeet() {
		if (PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage1 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage2 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage3 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage4 &&
			PlayerInfo.blockAtIntPlayerFeet != CloudBoots.tileBlockCloudStage5 &&
			PlayerInfo.blockAtIntPlayerFeet != null){

			Tether tempTether = new Tether(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ, 16);

			if(!tethers.isEmpty()){tethers.remove(0);}
			tethers.add(0, tempTether);
		}
	}

	//ran on tick for most Tether related stuff
	public static void tickTether(World world, EntityPlayer player){

		wasOutOfTether = Cloud.outOfTether;

		if(isInAnyTether(player)){
			Cloud.tickInTether(world, player);
        }
		else{
			Cloud.tickOutOfTether(world, player);
		}

		if(wasOutOfTether && Cloud.inTether){
			Cloud.refresh(world);
		}


		tetherAtFeet();
	}
}
