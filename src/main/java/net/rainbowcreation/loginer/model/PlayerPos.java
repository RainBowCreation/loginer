package net.rainbowcreation.loginer.model;

import net.minecraft.util.math.BlockPos;

public class PlayerPos {
  private final BlockPos pos;
  
  private final float yaw;
  
  private final float pitch;
  
  public PlayerPos(BlockPos pos, float yaw, float pitch) {
    this.pos = pos;
    this.yaw = yaw;
    this.pitch = pitch;
  }
  
  public BlockPos getPosition() {
    return this.pos;
  }
  
  public float getYaw() {
    return this.yaw;
  }
  
  public float getPitch() {
    return this.pitch;
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\model\PlayerPos.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */