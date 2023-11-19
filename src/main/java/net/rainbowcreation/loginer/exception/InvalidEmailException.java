package net.rainbowcreation.loginer.exception;

public class InvalidEmailException extends RegistrationException {
  private static final long serialVersionUID = 1L;
  
  public InvalidEmailException() {
    super("Your email is incorrect. Please retry");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\InvalidEmailException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */