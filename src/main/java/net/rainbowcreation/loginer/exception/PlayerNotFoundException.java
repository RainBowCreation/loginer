package net.rainbowcreation.loginer.exception;

public class PlayerNotFoundException extends LoginException {
  private static final long serialVersionUID = 1L;
  
  public PlayerNotFoundException() {
    super("You're not registered as a player.");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\PlayerNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */