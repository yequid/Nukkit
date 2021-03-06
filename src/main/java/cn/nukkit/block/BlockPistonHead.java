package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Identifier;

import static cn.nukkit.block.BlockIds.AIR;

/**
 * @author CreeperFace
 */
public class BlockPistonHead extends BlockTransparent {

    public BlockPistonHead(Identifier id) {
        super(id);
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[0];
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(AIR), true, true);
        Block piston = getSide(getFacing().getOpposite());

        if (piston instanceof BlockPistonBase && ((BlockPistonBase) piston).getFacing() == this.getFacing()) {
            piston.onBreak(item);
        }
        return true;
    }

    public BlockFace getFacing() {
        return BlockFace.fromIndex(this.getDamage()).getOpposite();
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public Item toItem() {
        return Item.get(AIR, 0, 0);
    }
}
