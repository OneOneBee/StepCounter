package project2_server;

import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//import java.util.Properties;
import java.util.logging.*;

//import org.apache.tomcat.jdbc.pool.DataSource;
//import org.apache.tomcat.jdbc.pool.PoolProperties;

public class ConnectionManager {
	private static final String username = "audreyniu";
	private static final String password = "neucs6650";
	private static final String host = "cs6650p2db2.cqiiwgdmulxt.us-west-2.rds.amazonaws.com";
	private static final String port = "3306";
	private static final String scheme = "StepCounter";

	public Connection getConnection() throws Exception {
//		try {
//			PoolProperties poolProps = new PoolProperties();
//			String connectionURL = "jdbc:mysql://" + ConnectionManager.host + ":" + ConnectionManager.port + "/" + ConnectionManager.scheme;
//			poolProps.setUrl(connectionURL);
//			poolProps.setDriverClassName("com.mysql.jdbc.Driver");
//			poolProps.setUsername(ConnectionManager.username);
//			poolProps.setPassword(ConnectionManager.password);
//			
//			DataSource dataSource = new DataSource();
//			dataSource.setPoolProperties(poolProps);
//			
//			return dataSource;
//			
//		} catch (Exception e) {
//			throw e;
//		}

		try {
			Properties connProperties = new Properties();
			connProperties.put("user", ConnectionManager.username);
			connProperties.put("password", ConnectionManager.password);
			connProperties.put("useSSL", "false");
			
			String connectionURL = "jdbc:mysql://" + ConnectionManager.host + ":" + ConnectionManager.port + "/" + ConnectionManager.scheme;
			
			Connection connection = null;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, connProperties);
			
			return connection;
			
		} catch (SQLException e) {
			Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}
	
//    public Connection getConnection() throws SQLException {
//        Connection connection = null;
//        try {
//            Properties connectionProperties = new Properties();
//            connectionProperties.put("user", ConnectionManager.username);
//            connectionProperties.put("password", ConnectionManager.password);
//            connectionProperties.put("useSSL", "false");
//            // Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
//            // Otherwise, Tomcat may have issues loading libraries in the proper order.
//            // One alternative is calling this in the HttpServlet init() override.
//            try {
//                Class.forName("com.mysql.jdbc.Driver");
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
//                throw new SQLException(ex);
//            }
//            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + ConnectionManager.host + ":" +
//                    ConnectionManager.port + "/" + ConnectionManager.scheme, connectionProperties);
//        } catch (SQLException ex) {
//            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
//            throw ex;
//        }
//        return connection;
//    }
	
	public void closeConnection(Connection connection) throws Exception {
		try {
			connection.close();
		} catch (SQLException e) {
			Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            throw e;
		} catch (Exception e) {
			Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
	}
	
}
