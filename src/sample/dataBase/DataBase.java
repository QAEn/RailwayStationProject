package sample.dataBase;

import sample.auxiliary.dataDAO;

import java.sql.*;

public class DataBase implements dataDAO {

    private final String HOST = "localhost";
    private final String PORT = "3306";
    private final String DataBaseName = "RailwayStation";
    private final String LOGIN = "root";
    private final String PASSWORD = "root";

    //переменная для установки подключения к базе данных
    private Connection dbConnection = null;

    /**
     * Метод для подключения к базе данных, возвращеет объект с подключением к базе данных
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection getDataBaseConnection() throws ClassNotFoundException, SQLException {
        //переменная с информацией об определенной безе данных
        String connection = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DataBaseName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return dbConnection = DriverManager.getConnection(connection, LOGIN, PASSWORD);//помещаем подключение в переменную
    }

    /**
     * Метод для проверки работоспособности подключения
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void isConnected() throws ClassNotFoundException, SQLException {
        dbConnection = getDataBaseConnection();
        System.out.println(dbConnection.isValid(1000));
    }

    public boolean regUser (String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `users` (`login`, `password`) VALUES(?, ?)";
        try(Connection connection = getDataBaseConnection();
            PreparedStatement prSt = connection.prepareStatement(sql))
        {
            Statement statement = getDataBaseConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `users` WHERE `login` = '" + login + "' LIMIT 1");
            if(resultSet.next()) return false;

            prSt.setString(1, login);
            prSt.setString(2, password);
            prSt.executeUpdate();
            return true;
        }
    }

    public boolean authUser(String login, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM `users` WHERE `login` = '" + login + "' AND `password` = '" + password + "' LIMIT 1";
        try(Connection connection = getDataBaseConnection())
        {
            Statement statement = connection.createStatement();
            ResultSet res = statement.executeQuery(sql);
            return res.next();
        }
    }

    public ResultSet getTickets() throws SQLException, ClassNotFoundException {
        try {
            String sql = "SELECT `station`, `date`, `time`, `FIO` FROM `tickets`";
            Statement statement = getDataBaseConnection().createStatement();
            ResultSet res = statement.executeQuery(sql);
            return res;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            getDataBaseConnection().close();
        }
        return null;
    }

    public void addTicket(String station, String date, String time, String fio) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO `tickets` (`station`, `date`, `time`, `FIO`) VALUES(?, ?, ?, ?)";
        try(Connection connection = getDataBaseConnection();
            PreparedStatement prSt = connection.prepareStatement(sql))
        {
            prSt.setString(1, station);
            prSt.setString(2, date);
            prSt.setString(3, time);
            prSt.setString(4, fio);
            prSt.executeUpdate();
        }
    }
}