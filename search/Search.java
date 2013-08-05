package search;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import UI.UI;
import java.util.*;

public class Search extends Thread
{

	/**
	 * As input is typed in search box
	 * 1. retrieve similar filenames from search history
	 * 2. if results from history < 10
	 * 		- retrieve similar filenames from index
	 * 3. on clicking "Search"
	 * 	  -clear previous results
	 * 	  -if it is exact filename,
	 * 		- retrieve using '=' & append to top of the list
	 * 
	 * 	  -append results with similar filenames
	 *    -display results
	 * 	  -update search history
	 * 
	 * 	   
	 */
	
	int count=0;
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet ;
	public Search()
	{
		super("Search Thread");
		   // This will load the MySQL driver, each DB has its own driver
	      try {
			Class.forName("com.mysql.jdbc.Driver");
			 connect = DriverManager.getConnection("jdbc:mysql://localhost/files?"
		              + "user=root&password=desh5489");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      // Setup the connection with the DB
	     start();
	}
	public void run()
	{
		System.out.println("Thread created");
		try {
			UI.result=searchDataBase(UI.query, UI.result, UI.mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	public Vector<String[]> searchDataBase(String query, Vector<String[]> result, int mode) throws Exception 
	{
		System.out.println("Searching...");
		  
		try 
		 {
	   

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
	      // Result set get the result of the SQL query
	      switch(mode)
	      {
	      	case 1: resultSet = statement.executeQuery("select * from primary_index where fname = \"%"+query+"%\"");
	      			break;
	      	case 2:	resultSet = statement.executeQuery("select * from primary_index where fname like \"%"+query+"%\"");
	      			break;
	      }
	      try
	      {
	    	  while(resultSet.next()&&resultSet.isClosed()==false)
				{
		    	  	if(!resultSet.wasNull()&&!resultSet.isAfterLast())
		    	  	{
		    	  		if((resultSet.getString("fname")!=null)&&(resultSet.getString("path")!=null)&&(resultSet.getString("type")!=null))
		    	  		{
		    	  			String s[]={resultSet.getString("fname"),resultSet.getString("path"),resultSet.getString("type")};
		    	  			result.add(s);
		    	  		}
						//System.out.println("Match-->"+s[0]+"-"+s[1]+"-"+s[2]);
							
		    	  	}
					
		  
				}
	      }
	      catch(SQLException | NullPointerException e)
	      {
	    	  
	      }
		
	      
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      //close();
	    }
		System.out.println("Done!");
		return result;
	  }
	public void close() 
	  {
		    try 
		    {
		      if (resultSet != null) 
		      {
		        resultSet.close();
		      }

		      if (statement != null) 
		      {
		        statement.close();
		      }

		      if (connect != null) 
		      {
		        connect.close();
		      }
		    } catch (Exception e) 
		    {

		    }
	 }
	

}
