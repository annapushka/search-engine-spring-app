import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection
{
    private static Connection connection;

    private static String dbName = "search_engine";
    private static String dbUser = "root";
    private static String dbPass = "20a0020b00";

    private static StringBuilder insertQuery = new StringBuilder();

    public static Connection getConnection()
    {
        if(connection == null)
        {
            try {
                connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName, dbUser, dbPass);
                connection.createStatement().execute("DROP TABLE IF EXISTS search_engine");
                connection.createStatement().execute("CREATE TABLE search_engine(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "path TEXT NOT NULL, " +
                        "code INT NOT NULL, " +
                        "content MEDIUMTEXT NOT NULL, " +
                        "PRIMARY KEY(id))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }


    public static void executeMultiInsert() throws SQLException
    {
        String sql = "INSERT INTO search_engine(path, code, content) " +
                "VALUES" + insertQuery.toString();
        DBConnection.getConnection().createStatement().execute(sql);
    }

    public static void countWebPageData(Page page)
    {
        page.getParentList().forEach(webpage ->{
            insertQuery.append((insertQuery.length() == 0 ? "" : ",") + " (" + webpage.toString() + ")");
            try {
                executeMultiInsert();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
