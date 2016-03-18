package comm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SQLCommand {
	
    private Connection conn;
    private String driver = "com.mysql.jdbc.Driver";
    
    private String url = "jdbc:mysql://localhost:3306/stockdb?useUnicode=true&characterEncoding=UTF-8";
    private String user = "root";
    private String password = "123456";
    private Statement statement;

	public SQLCommand(String url, String user, String password){
		this.url = url;
		this.user = user;
		this.password = password;
    }
    
	public SQLCommand(){
		
	}
	
	public boolean connect(){
		try{
			conn = DriverManager.getConnection(url, user, password);
			if(conn.isClosed()){
				System.out.println("false connecting to the Database!");
				return false;
			}
			// statement锟斤拷锟斤拷执锟斤拷SQL锟斤拷锟�
			statement = conn.createStatement();
		}
		catch(SQLException e) {   
			e.printStackTrace();   
		} 
		catch(Exception e) {   
			e.printStackTrace();   
		}
		//System.out.println("success connecting to the Database!");
		return true;
	}
	
	public void disConnect(){
		try{
			if(conn.isClosed()){
				return;
			}
			conn.close();
			//System.out.println("success disConnect to the Database!");
		}
		catch(SQLException e) {   
			e.printStackTrace();   
		} 
		catch(Exception e) {   
			e.printStackTrace();   
		}
	}
	
	
	public ResultSet query(String sql){
		ResultSet rs = null;
		try{
			if(statement.isClosed()){		
				return rs;
			}
			//System.out.println("start query data sql = " + sql);
			rs = statement.executeQuery(sql);
		}
		catch(SQLException e) {   
			e.printStackTrace();   
		} 
		catch(Exception e) {   
			e.printStackTrace();   
		}
		return rs;
	

	}
   
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}
}