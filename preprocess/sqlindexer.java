package preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import voldemort.client.StoreClient;

public class sqlindexer {
	Vector<String> list=new Vector<String>();
	int count=0;
	private Connection connect = null;
	  private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;
	 String root="/media/ruturaj/Data/Music";
	sqlindexer()
	{
		//initialize file list from list.txt
		FileReader fr;
		BufferedReader br;
		System.out.println("Initializing file list...");
		
		try 
		{
			
			fr=new FileReader(root+"/list.txt");
			br=new BufferedReader(fr);
			String line=new String();
			line=br.readLine();
			while(line!=null)
			{
				//System.out.println(line);
				if(!line.equals(".")&&!line.equals(".."))
				{
					list.add(line);
					count++;
				}
				line=br.readLine();
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("\n\n"+count+" entries found.");
		connect();
	}
	public HashMap<String, String> indexAll()
	{
		
		HashMap<String, String> primary=new HashMap<>();
		String parent=new String();
		//distinguish between path change entries & file names
		Iterator<String> j=list.iterator();
		while(j.hasNext())
		{
			String line=j.next();
			if(line.startsWith("./")) // change of parent folder
			{
				int pos=line.lastIndexOf('/');
				parent=root+line.substring(1, line.length()-1);
				//System.out.println("cd "+parent);
				
			}
			else// its file/folder
			{
				String fname=line;
				String path=parent+"/"+fname;
				String type="folder";
				if(path.contains("."))
				{
					if(path.lastIndexOf('.')>=path.length()-5)
						type=path.substring(path.lastIndexOf('.'), path.length());
				}
					
				System.out.println(fname.toLowerCase()+" ---> "+path+"  type="+type);
				try 
				{
					writeDataBase(fname,path,type);
				}
				catch(SQLException ex)
				{
					ResultSet rs;
					try 
					{
						
						//Statement stmt=connection
						rs = statement.executeQuery("select * from primary_index where fname=\""+fname+"\"");
						if(rs.isBeforeFirst())
						statement.executeUpdate("update set path=\""+rs.getString("path")+","+path+"\"");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
	
			}
		}
		
		return primary;
	}
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		sqlindexer si=new sqlindexer();
		si.indexAll();
		
		
	}
	//*********


	void connect()
	{
		try 
		 {
	      // This will load the MySQL driver, each DB has its own driver
	      Class.forName("com.mysql.jdbc.Driver");
	      // Setup the connection with the DB
	      connect = DriverManager.getConnection("jdbc:mysql://localhost/files?"
	              + "user=root&password=desh5489");

	      // Statements allow to issue SQL queries to the database
	      statement = connect.createStatement();
		 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	  
	  public void writeDataBase(String key, String path, String type) throws Exception {
		  
			try
			{
				
		      statement.executeUpdate("insert into primary_index (fname, path, type) values(\""+key+"\",\""+path+"\",\""+type+"\")");
		      //System.out.println("Match="+resultSet.getString("fname"));

		      
		    }
			catch(SQLException ex)
			{
				ResultSet rs;
				try 
				{
					//Statement stmt=connection
					rs = statement.executeQuery("select * from primary_index where fname=\""+key+"\"");
					if(!rs.wasNull())
						{
							statement.executeUpdate("update set path=\""+rs.getString("path")+","+path+"\" where fname=\""+key+"\"");
							System.out.println("Updated "+rs.getString("fname"));
						}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			catch (Exception e) {
		      throw e;
		    } finally {
		      //close();
		    }

		  }
	  private void close() 
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
