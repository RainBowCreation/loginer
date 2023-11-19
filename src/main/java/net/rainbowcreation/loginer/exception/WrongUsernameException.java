package net.rainbowcreation.loginer.exception;

public class WrongUsernameException extends LoginException {
  private static final long serialVersionUID = 1L;
  
  public WrongUsernameException() {
    super("Your username doesn't match with the server credentials");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\WrongUsernameException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */