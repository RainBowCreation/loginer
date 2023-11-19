package net.rainbowcreation.loginer.guard;

import net.rainbowcreation.loginer.guard.payload.LoginPayload;
import net.rainbowcreation.loginer.guard.payload.RegistrationPayload;
import net.rainbowcreation.loginer.model.IPlayer;
import net.rainbowcreation.loginer.model.Player;

public class PlayerFactory {
  public static IPlayer createFromRegistrationPayload(RegistrationPayload payload) {
    Player player = new Player();
    player.setUsername(payload.getUsername());
    player.setEmail(payload.getEmail());
    player.setUuid(payload.getUuid());
    player.setPassword(payload.getPassword());
    return (IPlayer)player;
  }
  
  public static RegistrationPayload createRegistrationFactoryFromPlayer(IPlayer player) {
    RegistrationPayload payload = new RegistrationPayload();
    payload.setUsername(player.getUsername());
    payload.setEmail(player.getEmail());
    payload.setUuid(player.getUuid());
    payload.setPassword(player.getPassword());
    payload.setPasswordConfirmation(player.getPassword());
    return payload;
  }
  
  public static IPlayer createFromLoginPayload(LoginPayload payload) {
    Player player = new Player();
    player.setUuid(payload.getUuid());
    player.setEmail(payload.getEmail());
    player.setUsername(payload.getUsername());
    player.setPassword(payload.getPassword());
    return (IPlayer)player;
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\PlayerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */