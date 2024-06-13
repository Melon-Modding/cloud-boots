package watermelonmojito.cloudboots;

import java.util.HashMap;

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

	//hashmap for all active/placed tethers
	public static HashMap<Integer, Tether> tethers = new HashMap<>();

	//method for distance between two 3-dimensional points
	static double distance(float x1, float y1, float z1, float x2, float y2, float z2) {
        return Math.pow((Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2)), 0.5);
	}

	//physically places the tether object into the world, allowing it to function
	public static void place(){

	}

	//removes a placed tether
	public static void remove(){

	}
}
