package watermelonmojito.cloudboots;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

import java.util.ArrayList;

public class Tether {

	private static int x;
	private static int y;
	private static int z;
	private static int size;

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
		return distance(player.x, player.y, player.z, tether.x, tether.y, tether.z) < tether.size;
	}



	//checks if the player is in ANY tether in the tethers hashmap
	public static boolean isInAnyTether(EntityPlayer player){
		for(int i = 1; i <= tethers.size(); i++) {
			if(isInTether(tethers.get(i), player)){return true;}
		}
		return false;
	}



	//places the tether object into the hashmap, allowing it to function
	public static void place(Tether tether){
		tethers.add(tether);
	}

	public static void placeAt(int index, Tether tether){
		tethers.add(index, tether);
	}


	//removes a placed tether
	public static void remove(){

	}

	public static void tetherAtFeet(EntityPlayer player){
		Tether tempTether = new Tether(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ, 16);
		tethers.remove(1);
		tethers.add(1, tempTether);
	}

	//ran on tick for most Tether related stuff
	public static void tickTether(World world, EntityPlayer player){

		if(isInAnyTether(player)){
			Cloud.tickCloud(world, player);
        }
		else{
			return;
		}
	}
}
