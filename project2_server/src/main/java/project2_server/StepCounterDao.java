package project2_server;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;


//import org.apache.tomcat.jdbc.pool.DataSource;


public class StepCounterDao {
	protected ConnectionManager conn;
	private static StepCounterDao instance = null;
	private final int NO_DATA = -1;
	
	protected StepCounterDao() {
		conn = new ConnectionManager();
	}
	
	public static StepCounterDao getInstance() {
		if (instance == null) {
			instance = new StepCounterDao();
		}
		
		return instance;
	}
	
	public String insert(CountData data) throws Exception {
		
		String insertion = "INSERT INTO Records(UserID, Day, TimeInterval, StepCount)" + "values (?,?,?,?);";
		
		try (Connection connection = conn.getConnection();
			PreparedStatement insertStmt = connection.prepareStatement(insertion)) {
			insertStmt.setInt(1, data.getUserId());
			insertStmt.setInt(2, data.getDay());
			insertStmt.setInt(3, data.getInterval());
			insertStmt.setInt(4, data.getCount());
			insertStmt.executeUpdate();
			
			return "Inserted userID: " + data.getUserId() + " day: " + data.getDay() + 
					" interval: " + data.getInterval() + " step: " + data.getCount();
		} catch (Exception e) {
			
			throw e;
		}

	}
	
	
	public int getCurrent(int userID) throws Exception {
		
		String getCurrDayData = "SELECT Day FROM Records WHERE UserID = ? ORDER BY Day DESC LIMIT 1;";
		int day;
		
		try (Connection connection = conn.getConnection();
			PreparedStatement selectStmt = connection.prepareStatement(getCurrDayData)) {
			
			selectStmt.setInt(1, userID);
			ResultSet result = selectStmt.executeQuery();
			
			if (result.next()) {
				day = result.getInt(1);
				
				return getDay(userID, day);
			}
		} catch (Exception e) {
			throw e;
		}
		return NO_DATA;
	}
	
	
	public int getDay(int userID, int day) throws Exception {
		
		String getDayData = "SELECT SUM(StepCount) FROM Records WHERE UserID = ? AND Day = ?;";
		
		try(Connection connection = conn.getConnection();
				PreparedStatement selectStmt = connection.prepareStatement(getDayData)) {
			
			selectStmt.setInt(1, userID);
			selectStmt.setInt(2, day);
			ResultSet result = selectStmt.executeQuery();
			
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (Exception e) {
			throw e;
		}
		
		return NO_DATA;
	}
	
	
	public List<Integer> getRange(int userID, int day, int range) throws Exception {
		
		List<Integer> counts = new ArrayList<>();
		for (int i = day; i < day + range; ++i) {
			counts.add(getDay(userID, i));
		}
		
		return counts;
	}
	
}
