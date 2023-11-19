package net.rainbowcreation.loginer.model;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerDescriptor {
  private final EntityPlayer player;
  
  private final PlayerPos pos;
  
  public PlayerDescriptor(EntityPlayer entity, PlayerPos position) {
    this.player = entity;
    this.pos = position;
  }
  
  public EntityPlayer getPlayer() {
    return this.player;
  }
  
  public PlayerPos getPosition() {
    return this.pos;
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\model\PlayerDescriptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */