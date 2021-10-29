package com.koreait.server;

import java.sql.*;

public class DbUtils {
    public static DbUtils dbUtils; // static 필드인데 heap영역에 저장??
    private DbUtils(){} // 외부에서는 객체화 할수없음
    public static  DbUtils getInstance(){
        if(dbUtils==null){
            dbUtils = new DbUtils();
        }
        return dbUtils; // 객체는 heap영역에 올라간다
    }//싱글톤?
    public Connection getCon(){
        final String URL = "jdbc:mysql://localhost:3308/son";
        final String USER_NAME = "root";
        final String USER_PW = "koreait";
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL,USER_NAME,USER_PW); // try-catch로 예외를 잡거나, throws SQLException 로 던진다
            System.out.println("DB연결성공");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DB연결 실패");
        }
        return con;
    }

    public void close(Connection con, PreparedStatement ps, ResultSet rs){
        //crud를 계속할때 커넥션은 항상필요, preparedStatment(쿼리문 실행당담)은 항상필요, ResultSet은 읽기했는 결과가 담겼을때 쓰임(select결과가 담김))
        //con이 ps객체를 만들고 ps객체가 rs를 만듦, 생성순서 -> 닫을때순서 <-
        if(rs!=null){
            try { rs.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
        if(ps!=null){
            try { ps.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
        if(con!=null){
            try { con.close(); }
            catch (SQLException e) { e.printStackTrace(); }
        }
        /*
        try {
            rs.close();
            ps.close();
            con.close(); // 이 구조는 하나가 에러가 난다면 나머지는 닫아지지 않으니 하나하나 예외처리를 해줘야한다
        } catch (SQLException e) {
            e.printStackTrace();
        }

         */
    }
    public void close(Connection con,PreparedStatement ps) {
        close(con,ps,null); // 오버로딩할때 이렇게 null을 주어주면 편함
    }
}
