package com.koreait.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {//Data Access Object

    public static void main(String[] args) {
//        StudentVO vo = new StudentVO();
        //vo.setNm("aa");
//        vo.setSno(2);
        //vo.setAddr("대림동");
        //vo.setAge(99);
        List<StudentVO> list = selStudentList();
        for(StudentVO vov: list ){
            System.out.printf("%d - %s\n",vov.getSno(),vov.getNm());
        }
        StudentVO param = new StudentVO();
        param.setSno(5);
        StudentVO vo = selStudent(param);
        System.out.println("sno : "+ "");
    }
    public static DbUtils dbUtils = DbUtils.getInstance();
    //student에 값 넣는 메소드(insert)
    public static int insStudent(StudentVO vo){ //스프링은 메소드명, insert문만 넣으면된다
        int result = 0;
        Connection con = null;
        PreparedStatement ps = null; // 스코프 문제 -> try랑 finally는 같은 스코프를 사용하지않는다
        String sql = "INSERT INTO t_student2"+
                "(nm,age,addr)"+
                "VALUES"+
//                String.format("('%s','%s','%s'",vo.getNm(),vo.getAge(),vo.getAddr());
        //       이걸 해야하나, 귀찮고 힘드니 PreparedStatement를 이용
                "(?,?,?)";

        try {
            con = dbUtils.getCon();
            ps = con.prepareStatement(sql);
            ps.setString(1,vo.getNm());
            ps.setInt(2,vo.getAge());
            ps.setString(3, vo.getAddr());
            result = ps.executeUpdate();//커리문을 실행, 영향을 미친 레코드 수
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtils.close(con,ps);
        }
        return result;
    }
    //todo select는 조금이따
    public static int updStudent(StudentVO vo){
        Connection con = null;
        PreparedStatement ps = null;
        String sql = " UPDATE t_student2 "+
                " SET nm=?, age = ?, addr = ? "+
                "WHERE sno = ?";
        try {
            con = dbUtils.getCon(); // 커넥션을 얻어온다(통신)
            ps=con.prepareStatement(sql); // 가져온 커넥션을 ps객체에 넣어준다
            ps.setString(1,vo.getNm());
            ps.setInt(2,vo.getAge());
            ps.setString(3,vo.getNm());
            ps.setInt(4,vo.getSno());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtils.close(con, ps);
        }
        return 0;
    }
    public static int delStudent(StudentVO vo){
        Connection con = null;
        PreparedStatement ps = null;
        String sql = " DELETE from t_student2 "+
                " where sno = ? ";
        try {
            con = dbUtils.getCon();
            ps = con.prepareStatement(sql);
            ps.setInt(1,vo.getSno());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbUtils.close(con,ps);
        }
        return 0;
    }
    public static List<StudentVO> selStudentList(){ //여러값을 한곳에 담아서 리턴하고, 파라미터를 받지 않겟다
        List<StudentVO> list = new ArrayList();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT sno, nm FROM t_student2";
        try{
            con = dbUtils.getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){// 줄 가르키키 (리턴값 true/false) - > 가르켯더니 레코드가 잇다? true, 없다? false
                StudentVO vo = new StudentVO();
                vo.setSno(rs.getInt("sno"));
                vo.setNm(rs.getString("nm"));
                list.add(vo);
            }
        }catch(Exception e){

        }finally {
            dbUtils.close(con,ps,rs);
        }
        return list;
    }
    public static StudentVO selStudent(StudentVO vo){
        StudentVO result = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM t_student2 WHERE sno = ?";
        try{
            con = dbUtils.getCon();
            ps = con.prepareStatement(sql);
            ps.setInt(1,vo.getSno());
            rs = ps.executeQuery();
            if(rs.next()){
                result = new StudentVO();
                result.setSno(rs.getInt("sno"));
                result.setNm(rs.getString("nm"));
                result.setAddr(rs.getString("addr"));
                result.setAge(rs.getInt("age"));
            }else{

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            dbUtils.close(con,ps,rs);
        }
        return result;
    }

}

//insert vs update => pk값 유무
