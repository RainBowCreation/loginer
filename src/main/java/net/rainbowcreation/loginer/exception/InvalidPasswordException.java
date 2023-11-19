package net.rainbowcreation.loginer.exception;

public class InvalidPasswordException extends RegistrationException {
  private static final long serialVersionUID = 1L;
  
  public InvalidPasswordException() {
    super("Password too short !");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\InvalidPasswordException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */