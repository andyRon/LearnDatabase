package com.andyron.mysql;

import java.io.IOException;
import java.sql.*;

import com.mysql.cj.jdbc.Driver;

/**
 * https://blog.csdn.net/C3245073527/article/details/122071045
 * @author andyron
 **/
public class MysqlTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xiaohaizi?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true", "root", "33824");
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("Select * From page_demo");
        while (resultSet.next()) {
            System.out.println(resultSet.getString("c3"));
        }
        conn.close();
    }


}
