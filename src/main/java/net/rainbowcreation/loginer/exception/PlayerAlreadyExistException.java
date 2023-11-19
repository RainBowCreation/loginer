package net.rainbowcreation.loginer.exception;

public class PlayerAlreadyExistException extends RegistrationException {
  private static final long serialVersionUID = 1L;
  
  public PlayerAlreadyExistException() {
    super("This player is already registered");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\PlayerAlreadyExistException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */