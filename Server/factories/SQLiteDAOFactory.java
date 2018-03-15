package factories;

import java.sql.Connection;

import dao.ActuatorDAO;
import dao.DriverConnection;
import dao.SQLiteActuatorDAO;
import dao.SQLiteUserDAO;
import dao.UserDAO;

public class SQLiteDAOFactory extends AbstractDAOFactory{
    protected Connection connect = DriverConnection.getInstance(DriverConnection.SQLITE_DRIVER); 

    @Override
	public UserDAO getUserDAO() {
		return new SQLiteUserDAO(connect);
	}

    @Override
    public ActuatorDAO getActuatorDAO() {
        return new SQLiteActuatorDAO(connect);
    }
	
}
