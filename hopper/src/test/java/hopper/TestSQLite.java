package hopper;

import java.sql.*;
import org.sqlite.JDBC;
public class TestSQLite
{
	public static void main(String[] args)
	{
		try
		{
			//连接SQLite的JDBC
			Class.forName("org.sqlite.JDBC");
			//建立一个数据库名zieckey.db的连接，如果不存在就在当前目录下创建之
			Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\hopper.db");
			
			conn.close(); //结束数据库的连接
		}
		catch( Exception e )
		{
			e.printStackTrace ( );
		}
	}
}
