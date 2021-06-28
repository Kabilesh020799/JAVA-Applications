import javax.swing.*;
import java.sql.SQLException;
import java.sql.*;
import java.awt.List;
import java.io.*;
import javax.swing.table.TableColumn;
import java.util.*;
import javax.swing.table.DefaultTableModel;
public class second extends Thread
{
	JFrame f;
	second()
	{
		String jdbcURL = "jdbc:postgresql://localhost:5432/{Db_name}";
		String username = "{user_name}";
		String password = "{password}";
		try
		{
			//Connecting to Db	
	
			Connection connection = DriverManager.getConnection(jdbcURL,username,password);
			System.out.println("Connected to postgresql server");

			//Getting data from states table

			Statement stmt = null;
			stmt = connection.createStatement();
			ResultSet rs1 = stmt.executeQuery("select * from districts d,states s where d.state_name=s.id order by d.district_code ");
			ResultSetMetaData rsmd = rs1.getMetaData();
			int columnCount = rsmd.getColumnCount();
			String[] colname = new String[columnCount];
			int k = 0;
			
			//Defining a Jtable to display the datas

			DefaultTableModel model = new DefaultTableModel();
			JTable jt = new JTable(model);
			f = new JFrame();
    			jt.setBounds(300,400,200,300);          
    			JScrollPane sp=new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
    			jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jt.setAutoscrolls(true);
			f.add(sp);
    			f.setSize(1000,1000);
    			f.setVisible(true); 
			for(int i=1;i<=columnCount;i++)
			{
				String name = rsmd.getColumnName(i);
				model.addColumn(name);
				colname[k++] = name;
			}
			Statement stmt1 = connection.createStatement();
			ResultSet rs = stmt1.executeQuery("select count(*) from districts");
			rs.next();
			int rowSize = rs.getInt("count");
			String[][] s = new String[rowSize][];
			int j =0;

			while(rs1.next())
			{
				String[] row = new String[columnCount];
				for(int iC=1;iC<=columnCount;iC++)
				{
					Object obj = rs1.getObject(iC);
					row[iC-1] = (obj==null)?null:obj.toString();
				}
				model.addRow(row);
				System.out.println("done");
				try		
				{
					Thread.sleep(50);
				}catch(InterruptedException e)
				{
					System.out.println(e);
				}
				//s[j++] = row;	
			}
			for(int ind3=0;ind3<columnCount;ind3++)
			{
				TableColumn column = jt.getColumnModel().getColumn(ind3);
				column.setMinWidth(50);
				column.setMaxWidth(50);
				column.setPreferredWidth(50);
			}   

		}catch(SQLException e)
		{
			System.out.println("Error in connection");
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
		new second();	
	}
}