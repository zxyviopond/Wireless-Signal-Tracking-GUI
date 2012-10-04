import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import oracle.sdoapi.OraSpatialManager;
import oracle.sdoapi.geom.*;
import oracle.sdoapi.adapter.*;
import oracle.sdoapi.sref.*;
import oracle.sql.STRUCT;


public class populate
{
    Connection mainConnection = null;
    Statement mainStatement = null;
    ResultSet mainResultSet = null;

    /**
     * @throws IOException ***************************/
    public static void main(String[] args) throws IOException
    {
       populate c=new populate(args);
    }

    /**
     * @throws IOException 
     * @throws IOException */
    
    
  
    
   
    public populate(String[] args) throws IOException
    {
        
    	
    	ConnectToDB();		
    	publishBuildingData();
    	Buildinginsert(args[0]);
    	PublishPeopleData();			// publish some data
    	Peopleinsert(args[1]);
        publishAPdata();
        APinsert(args[2]);
		
    }

    
   private void APinsert(String filename) throws IOException{
		// TODO Auto-generated method stub
	   FileReader reader = new FileReader(filename);
 	  BufferedReader br = new BufferedReader(reader);
 	  String s1 = null;
 	  try {
			while((s1 = br.readLine()) != null) {
			  String[] splited;
			  splited=s1.split(", ");
			  String apid=splited[0];
			  pointtype Center=new pointtype();
			  Center.x=Integer.parseInt(splited[1]);
			  Center.y=Integer.parseInt(splited[2]);
			  int radius=Integer.parseInt(splited[3]);
			  String points=Center.x+","+(Center.y+radius)+", "+(Center.x+radius)+","+Center.y+", "+Center.x+","+(Center.y-radius);
			  //System.out.println(points);
				 
		      mainStatement.executeUpdate( "insert into ap values ('"+apid+"' , MDSYS.SDO_GEOMETRY" +
		            							 "( 2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1,1003,4), SDO_ORDINATE_ARRAY("+points+")),"+Center.x+","+Center.y+","+radius+")" );
				  			  
				  
			  
			  
			  }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	 br.close();
 	 reader.close();
 	 System.out.println("Succesfully inserted AP");
		
	}

private void publishAPdata() {
	   try
       {

				// delete previous data from the DB
           System.out.print( "\n ** Deleting previous AP tuples ..." );
           mainStatement.executeUpdate( "delete from ap" );
           System.out.println( ", Deleted AP tuples**" );


				// publish new data
          

       }
       catch( Exception e )
       { System.out.println( " Error 2: " + e.toString() ); }
		
	}


public class pointtype{
    public int x;
    public int y;
    }
    private void Buildinginsert(String filename) throws IOException{
    	
    	FileReader reader = new FileReader(filename);
  	  BufferedReader br = new BufferedReader(reader);
  	  String s1 = null;
  	  try {
			while((s1 = br.readLine()) != null) {
			  
				String[] splited=s1.split(", ");
				  String bid=splited[0];
				  String buildingname=splited[1];
				  int pointnumber=Integer.parseInt(splited[2]);
				  int length=splited.length;
				  //System.out.println(bid+","+buildingname);
				  pointtype[] Points = new pointtype[pointnumber+1];
				  for(int i=1;i<=pointnumber;i++){
					  Points[i]=new pointtype();
					  Points[i].y=Integer.parseInt(splited[length-1-2*(i-1)]);
					  Points[i].x=Integer.parseInt(splited[length-2-2*(i-1)]);
				  }
				  String insertpoints="";
				  for(int i=1;i<pointnumber;i++){
					insertpoints=insertpoints+Points[i].x+","+Points[i].y+",";
				  }
				  insertpoints=insertpoints+Points[pointnumber].x+","+Points[pointnumber].y;
				  
				  String SQL="insert into building values('"+bid+"','"+buildingname+
				  "',MDSYS.SDO_GEOMETRY(2003,null,null,SDO_ELEM_INFO_ARRAY(1,1003,1), SDO_ORDINATE_ARRAY("
				  +insertpoints+")))";
				 
		      mainStatement.executeUpdate(SQL);
				  			  
				  
			  
			  
			  }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	 br.close();
  	 reader.close();
  	 System.out.println("Succesfully inserted building");
  }
    	
		
		
	

	private void publishBuildingData() {
		// TODO Auto-generated method stub
		try
        {

				// delete previous data from the DB
            System.out.print( "\n ** Deleting previous building tuples ..." );
            mainStatement.executeUpdate( "delete from building" );
            System.out.println( ", Deleted building tuples**" );


				// publish new data
           

        }
        catch( Exception e )
        { System.out.println( " Error 2: " + e.toString() ); }
		
	}

	/*****************************/
    
    public void Peopleinsert(String filename) throws IOException{
    	FileReader reader = new FileReader(filename);
    	  BufferedReader br = new BufferedReader(reader);
    	  String s1 = null;
    	  try {
			while((s1 = br.readLine()) != null) {
			  String[] splited;
			  splited=s1.split(", ");
			  
				 
		      mainStatement.executeUpdate( "insert into people values ('"+splited[0]+"' , MDSYS.SDO_GEOMETRY" +
		            							 "( 2001, NULL, MDSYS.SDO_POINT_TYPE("+splited[1]+", "+splited[2]+", NULL), NULL, NULL) ) " );
				  			  
				  
			  
			  
			  }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 br.close();
    	 reader.close();
    	 System.out.println("Succesfully inserted people");
    	
    }
    
    
    public void PublishPeopleData()
    {
        try
        {

				// delete previous data from the DB
            System.out.print( "\n ** Deleting previous people tuples ..." );
            mainStatement.executeUpdate( "delete from people" );
            System.out.println( ", Deleted people tuples. **" );


				// publish new data
           

        }
        catch( Exception e )
        { System.out.println( " Error 2: " + e.toString() ); }
    }

    /*****************************/
    

    /*****************************/
   
	/********************************************/
	/* Connecting to DB							*/
	/********************************************/
	public void ConnectToDB()
 	{
		try
		{
			// loading Oracle Driver
    		System.out.print("Looking for Oracle's jdbc-odbc driver ... ");
	    	DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    	System.out.println(", Loaded.");

			String URL = "jdbc:oracle:thin:@127.0.0.1:1521:dbhomework1";
	    	String userName = "SYSTEM";
	    	String password = "Zxy213213";

	    	System.out.print("Connecting to DB...");
	    	mainConnection = DriverManager.getConnection(URL, userName, password);
	    	System.out.println(", Connected!");

    		mainStatement = mainConnection.createStatement();

   		}
   		catch (Exception e)
   		{
     		System.out.println( "Error while connecting to DB: "+ e.toString() );
     		e.printStackTrace();
     		System.exit(-1);
   		}
 	}



}

