package server.factories;

import server.dao.abstractDAO.ActuatorCategoriesDAO;
import server.dao.abstractDAO.ActuatorDAO;
import server.dao.abstractDAO.AmbienceDAO;
import server.dao.abstractDAO.BehaviourDAO;
import server.dao.abstractDAO.CommandDAO;
import server.dao.abstractDAO.ComplexActionDAO;
import server.dao.abstractDAO.EnvironmentVariableDAO;
import server.dao.abstractDAO.RightDAO;
import server.dao.abstractDAO.RoleDAO;
import server.dao.abstractDAO.SensorCategoriesDAO;
import server.dao.abstractDAO.SensorDAO;
import server.dao.abstractDAO.UserDAO;

public abstract class AbstractDAOFactory {
	
    // ==================== //
    // ==== ATTRIBUTES ==== //
    // ==================== //
	public static final int SQLITE_DAO_FACTORY = 0;
	public static final int POSTGRES_DAO_FACTORY = 1;
	public static final int ORACLE_DAO_FACTORY = 2;

	// ================= //
    // ==== METHODS ==== //
    // ================= //
	/**
	 * Return a DAO factory according to the type given (default = SQLiteDAO)
	 * 0 = SQLite DAO
	 * 1 = PostGRES DAO
	 * 1 = Oracle DAO
	 * @param type, int
	 * @return AsbstractDAOFactory
	 */
	public static AbstractDAOFactory getFactory(int type){
		switch(type) { 
			case(SQLITE_DAO_FACTORY):
				return new SQLiteDAOFactory();
			case(POSTGRES_DAO_FACTORY):
				return new PostGreDAOFactory();
			case(ORACLE_DAO_FACTORY):
                return new OracleDAOFactory();
			default:
				return new SQLiteDAOFactory();
		}
	}
	
	// ==== DAOs ==== //
	public abstract UserDAO getUserDAO();
	public abstract ActuatorDAO getActuatorDAO();
	public abstract SensorDAO getSensorDAO();
	public abstract RoleDAO getRoleDAO();
    public abstract SensorCategoriesDAO getSensorCategoriesDAO();
    public abstract ActuatorCategoriesDAO getActuatorCategoriesDAO();
    public abstract BehaviourDAO getBehaviourDAO();
    public abstract AmbienceDAO getAmbienceDAO();
    public abstract CommandDAO getCommandDAO();
    public abstract ComplexActionDAO getComplexActionDAO();
    public abstract RightDAO getRightDAO();
    public abstract EnvironmentVariableDAO getEnvironmentVariableDAO();
}
