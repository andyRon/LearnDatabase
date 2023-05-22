package com.andyron;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.UUID;

/**
 * @author andyron
 **/
public class MyTest {
    /**
     * 插入随机数据用于测试
     */
    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/xiaohaizi?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowPublicKeyRetrieval=true", "root", "33824");
        String sql = "insert into single_table(key1,key2,key3,key_part1,key_part2,key_part3,common_field) values(?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
            for (int i = 0; i < 10000; i++) {
                ps.setObject(1, randomStr(4));
                ps.setObject(2, i);
                ps.setObject(3, randomStr(4));
                ps.setObject(4, randomStr(8));
                ps.setObject(5, randomStr(8));
                ps.setObject(6, randomStr(8));
                ps.setObject(7, randomStr(16));

                ps.addBatch();
                ps.executeBatch();
                ps.clearBatch();
            }
            ps.executeBatch();
            ps.clearBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        System.out.println("时间：" + (System.currentTimeMillis() - start));
    }

    public String randomStr(int size) {
        if (size <= 0 || size >64) {
            size = 8;
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }

}
