package cn.nukkit.block;

import cn.nukkit.event.redstone.RedstoneUpdateEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.math.Vector3i;
import cn.nukkit.player.Player;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Identifier;

import static cn.nukkit.block.BlockIds.UNLIT_REDSTONE_TORCH;

/**
 * author: Angelic47
 * Nukkit Project
 */
public class BlockRedstoneTorch extends BlockTorch {

    public BlockRedstoneTorch(Identifier id) {
        super(id);
    }

    @Override
    public int getLightLevel() {
        return 7;
    }

    @Override
    public boolean place(Item item, Block block, Block target, BlockFace face, Vector3f clickPos, Player player) {
        if (!super.place(item, block, target, face, clickPos, player)) {
            return false;
        }

//        if (!checkState()) {
//            BlockFace facing = getFacing().getOpposite();
//            Vector3 pos = getLocation();
//
//            for (BlockFace side : BlockFace.values()) {
//                if (facing == side) {
//                    continue;
//                }
//
//                this.level.updateAround(pos.getSide(side));
//            }
//        }

        checkState();

        return true;
    }

    @Override
    public int getWeakPower(BlockFace side) {
        return getBlockFace() != side ? 15 : 0;
    }

    @Override
    public int getStrongPower(BlockFace side) {
        return side == BlockFace.DOWN ? this.getWeakPower(side) : 0;
    }

    @Override
    public boolean onBreak(Item item) {
        super.onBreak(item);

        Vector3i pos = asVector3i();

        BlockFace face = getBlockFace().getOpposite();

        for (BlockFace side : BlockFace.values()) {
            if (side == face) {
                continue;
            }

            this.level.updateAroundRedstone(pos.getSide(side), null);
        }
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (super.onUpdate(type) == 0) {
            if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE) {
                this.level.scheduleUpdate(this, tickRate());
            } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
                RedstoneUpdateEvent ev = new RedstoneUpdateEvent(this);
                getLevel().getServer().getPluginManager().callEvent(ev);

                if (ev.isCancelled()) {
                    return 0;
                }

                if (checkState()) {
                    return 1;
                }
            }
        }

        return 0;
    }

    protected boolean checkState() {
        if (isPoweredFromSide()) {
            BlockFace face = getBlockFace().getOpposite();
            Vector3i pos = asVector3i();

            this.level.setBlock(pos, Block.get(UNLIT_REDSTONE_TORCH, getDamage()), false, true);

            for (BlockFace side : BlockFace.values()) {
                if (side == face) {
                    continue;
                }

                this.level.updateAroundRedstone(pos.getSide(side), null);
            }

            return true;
        }

        return false;
    }

    protected boolean isPoweredFromSide() {
        BlockFace face = getBlockFace().getOpposite();
        return this.level.isSidePowered(this.asVector3i().getSide(face), face);
    }

    @Override
    public int tickRate() {
        return 2;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }
}
