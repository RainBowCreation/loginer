package net.rainbowcreation.loginer.guard.datasource.db;

import net.rainbowcreation.loginer.model.IPlayer;

import java.sql.SQLException;

public interface IPlayersDAO<P extends IPlayer> {
  void create(P paramP) throws SQLException;
  
  P find(P paramP) throws SQLException;
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\datasource\db\IPlayersDAO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */