package net.rainbowcreation.loginer.exception;

public class WrongPasswordException extends LoginException {
  private static final long serialVersionUID = 1L;
  
  public WrongPasswordException() {
    super("Your password is incorrect. Please retry.");
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\exception\WrongPasswordException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */