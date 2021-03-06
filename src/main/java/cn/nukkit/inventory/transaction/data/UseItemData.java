package cn.nukkit.inventory.transaction.data;

import cn.nukkit.item.Item;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.math.Vector3i;
import lombok.ToString;

/**
 * @author CreeperFace
 */
@ToString
public class UseItemData implements TransactionData {

    public int actionType;
    public Vector3i blockPos;
    public BlockFace face;
    public int hotbarSlot;
    public Item itemInHand;
    public Vector3f playerPos;
    public Vector3f clickPos;
    public int blockRuntimeId;
}
