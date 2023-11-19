package net.rainbowcreation.loginer.exception;

public class WrongPasswordConfirmationException extends RegistrationException {
  private static final long serialVersionUID = 1L;
  
  public WrongPasswordConfirmationException() {
    super("Password confirmation doesn't match. Please retry");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\WrongPasswordConfirmationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */