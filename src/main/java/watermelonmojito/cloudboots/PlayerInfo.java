package watermelonmojito.cloudboots;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

public class PlayerInfo {

	public static double playerFeetX;
	public static double playerFeetY;
	public static double playerFeetZ;

	public static double flooredPlayerFeetX;
	public static double flooredPlayerFeetY;
	public static double flooredPlayerFeetZ;

	public static int intPlayerFeetX;
	public static int intPlayerFeetY;
	public static int intPlayerFeetZ;

	public static Block blockAtIntPlayerFeet;


	public static void tickPlayerInfo(World world, EntityPlayer player){

		//Get the players feet position
		playerFeetX = player.getPosition(0.5F).xCoord;
		playerFeetY = player.getPosition(0.5F).yCoord - 1;
		playerFeetZ = player.getPosition(0.5F).zCoord;

		//Floor it
		flooredPlayerFeetX = Math.floor(playerFeetX);
		flooredPlayerFeetY = Math.floor(playerFeetY) - 1;
		flooredPlayerFeetZ = Math.floor(playerFeetZ);

		//Cast to int
		intPlayerFeetX = (int) flooredPlayerFeetX;
		intPlayerFeetY = (int) flooredPlayerFeetY;
		intPlayerFeetZ = (int) flooredPlayerFeetZ;

		//Get the Block at players feet position
		blockAtIntPlayerFeet = world.getBlock(intPlayerFeetX, intPlayerFeetY, intPlayerFeetZ);
	}
}
