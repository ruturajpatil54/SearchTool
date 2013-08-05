package preprocess;

import java.util.*;
import java.io.*;

import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
/* This class takes output of "ls -aR" & indexes every file/folder entry
 * to a primary index
 * Key						Value
 * filename/foldername 		path
 */
public class indexer 
{
	Vector<String> list=new Vector<String>();
	int count=0;
	 StoreClient<String, String> primaryclient,secondaryclient;
	 String root="/media/ruturaj/Data/Music";
	indexer()
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
				System.out.println(line);
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
	}
	void connect()
	{
		//connect to primary store as client
				//String bootStrapUrlP = "tcp://localhost:6664";
				String bootStrapUrlP = "tcp://localhost:6666";
				int maxThreads = 300;
			    ClientConfig clientConfig = new ClientConfig();
			    clientConfig.setMaxThreads(maxThreads);
			    clientConfig.setMaxConnectionsPerNode(maxThreads);
			    
			    clientConfig.setBootstrapUrls(bootStrapUrlP);
			    String storeNameP = "primary";
			    //String storeNameG = "test";
			    StoreClientFactory factory = new SocketStoreClientFactory(clientConfig);
			    primaryclient= factory.getStoreClient(storeNameP);
			    primaryclient.put("test", "primary");
			    
			   
	}
	public HashMap<String, String> indexAll()
	{
		connect(); // connect to Voldemort-Store
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
				System.out.println(fname.toLowerCase()+" ---> "+path);
				
				
				primaryclient.put(fname.toLowerCase(), path.trim());
			}
		}
		
		return primary;
	}
	HashMap<String, String> secondaryIndexer(HashMap<String, String> primary)
	{
		HashMap<String, String> secondary=new HashMap<>();
		
		return secondary;
	}
	public static void main(String[] args) 
	{
		
		indexer i=new indexer();
		HashMap<String, String> primary=i.indexAll();	// generate primary index
		HashMap<String, String> secondary=i.secondaryIndexer(primary); // generate secondary index
		
	}

}
