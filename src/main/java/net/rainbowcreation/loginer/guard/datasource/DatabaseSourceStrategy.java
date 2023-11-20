package net.rainbowcreation.loginer.guard.datasource;

import net.rainbowcreation.loginer.Main;
import net.rainbowcreation.loginer.exception.PlayerAlreadyExistException;
import net.rainbowcreation.loginer.exception.RegistrationException;
import net.rainbowcreation.loginer.guard.datasource.db.IConnectionFactory;
import net.rainbowcreation.loginer.guard.datasource.db.IPlayersDAO;
import net.rainbowcreation.loginer.guard.datasource.db.PlayersDAO;
import net.rainbowcreation.loginer.model.IPlayer;
import net.rainbowcreation.loginer.model.Player;
import java.sql.SQLException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class DatabaseSourceStrategy implements IDataSourceStrategy {
  private final IPlayersDAO<IPlayer> playersDAO;
  
  private static final Logger LOGGER = Main.LOGGER;
  
  public DatabaseSourceStrategy(String table, IConnectionFactory connectionFactory) throws SQLException {
    this.playersDAO = (IPlayersDAO<IPlayer>)new PlayersDAO(table, connectionFactory);
  }
  
  public DatabaseSourceStrategy(IConnectionFactory connectionFactory) throws SQLException {
    this.playersDAO = (IPlayersDAO<IPlayer>)new PlayersDAO("players", connectionFactory);
  }
  
  public IPlayer find(String email, String username) {
    try {
      IPlayer p = this.playersDAO.find((new Player()).setEmail(email).setUsername(username));
      return p;
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
      return null;
    } 
  }
  
  public boolean add(IPlayer player) throws RegistrationException {
    try {
      boolean alreadyExist = exist(player);
      if (alreadyExist)
        throw new PlayerAlreadyExistException(); 
      this.playersDAO.create(player);
      return true;
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
      return false;
    } 
  }
  
  public boolean exist(IPlayer player) {
    try {
      return (this.playersDAO.find(player) != null);
    } catch (SQLException e) {
      LOGGER.catching(Level.ERROR, e);
      return false;
    } 
  }
}