package watermelonmojito.cloudboots.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import watermelonmojito.cloudboots.PlayerInfo;
import watermelonmojito.cloudboots.Tether;

public class BlockTether extends Block {

	public BlockTether(String key, int id) {super(key, id, Material.stone);}


	private static Tether thisTether;

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		thisTether = new Tether(PlayerInfo.intPlayerFeetX, PlayerInfo.intPlayerFeetY, PlayerInfo.intPlayerFeetZ, 32);
		Tether.tethers.add(thisTether);
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		super.onBlockRemoved(world, x, y, z, data);
		Tether.tethers.remove(thisTether);
	}
}
