import java.io.*;  
import java.util.Scanner; 
import java.sql.SQLException;
import java.sql.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter; 
class first{


	public static void main(String[] args)
	{
		String jdbcURL = "jdbc:postgresql://localhost:5432/{Db_name}";
		String username = "{user_name}";
		String password = "{password}";
		Statement stmt = null;
		Statement stmt1 = null;
			try
			{
				//Connecting to the postgresql Db

				Connection connection = DriverManager.getConnection(jdbcURL,username,password);
				System.out.println("Connected to postgresql server");

				//Creating the districts table
				
				String createStatement = "create table if not exists states(id SERIAL primary key,s_name VARCHAR unique)";
				stmt = connection.createStatement();
				stmt.executeUpdate(createStatement);
				stmt.close();
				try{
					BufferedReader bufferedReader = new BufferedReader(new FileReader("{csv_file_path}"));
					BufferedReader bufferedReader1 = new BufferedReader(new FileReader("{csv_file_path}"));
					String input;
					int count =0 ;
					String title1 = bufferedReader1.readLine();
					String title = bufferedReader.readLine();
					String[] titlearr = title.split(",");
					
					//Creating a districts table if it does not exist
				
					String createDist = "create table if not exists districts(";	
					int b = 0;
					for(String a:titlearr)
					{	
						if(a.contains(" "))
						{
							String[] arr = a.split(" ");
							a = String.join("_",arr);	
						}
						createDist += a;
						if(b == 0 | b == 1)
						{
							createDist += " INT"; 		
						}
						else if(b==2)
						{
							createDist += " VARCHAR,";	
						}
						else
						{
							createDist += " BIGINT,";	
						}
						if(b == 0)
						{
							createDist += " Primary key,";
						}
						if(b==1)
						{
							createDist += ",";	
						}
						b++;
					}
					createDist += "constraint fk_state foreign key(State_name) references states(id));";
					
					stmt1 = connection.createStatement();
					stmt1.executeUpdate(createDist);
					stmt1.close();
					int cl = 0;

					while((input=bufferedReader1.readLine())!=null)
					{
						String insertStatement = "insert into districts values(";
						String[] titlearr1 = input.split(",");
						int c = 0;
						for(String a:titlearr1)
						{ 
							if(c==1)
							{	
								String insertState = "insert into states values(DEFAULT,\'" + a + "\')" + "on conflict do nothing";
								Statement stmt4 = null;
								stmt4 = connection.createStatement();
								stmt4.executeUpdate(insertState);
								stmt4.close();
								a = "(Select id from states where s_name=\'"+ a +"\')";			
							}
							if(c==2)
							{
								a = "\'" + a + "\'";	
							}
							if(c == titlearr1.length -1)
							{
								insertStatement += a;	
							}
							else
								insertStatement += a + ',';							
							c++;
						}
						insertStatement += " )on conflict do nothing;";

						Statement stmt2 = null;
						stmt2 = connection.createStatement();
						stmt2.executeUpdate(insertStatement);
						stmt2.close();
					}
				}catch(Exception e)
				{
					System.out.println(e);
				}

			}catch(SQLException e){
				System.out.println("Error in connection");
				e.printStackTrace();
			}

				
		
	}
}