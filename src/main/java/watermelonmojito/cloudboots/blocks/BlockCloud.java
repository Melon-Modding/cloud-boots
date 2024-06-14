package watermelonmojito.cloudboots.blocks;
import net.minecraft.core.block.BlockTransparent;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockCloud extends BlockTransparent {

	public BlockCloud(String key, int id) {
		super(key, id, Material.air);
	}

	@Override
	public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
		float f = 0.005f;
		return AABB.getBoundingBoxFromPool(x, y, z, x + 1, (float)(y + 1) - f, z + 1);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.xd *= 0.4;
		entity.zd *= 0.4;
	}

	@Override
	public int getRenderBlockPass() {
		return 1;
	}

}
