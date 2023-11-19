package net.rainbowcreation.loginer.model;

public interface IPlayer {
  boolean isBanned();
  
  IPlayer setBanned(boolean paramBoolean);
  
  IPlayer setPassword(String paramString);
  
  String getPassword();
  
  IPlayer setEmail(String paramString);
  
  String getEmail();
  
  IPlayer setUsername(String paramString);
  
  String getUsername();
  
  boolean isPremium();
  
  IPlayer setUuid(String paramString);
  
  String getUuid();
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\model\IPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */