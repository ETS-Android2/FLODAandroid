package floda.pl.floda3.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import floda.pl.floda3.User_infos;

public class Database_connection implements connection {
    Database_connection(){}
    @Autowired
    private DriverManagerDataSource dataSource;
    @Autowired
    public void setdataSource(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }



    @Override
    public User_infos getLoginAbout(String login, String password) {
        String sql="select ID,Nick,Name,Surname,Email,su from floda_user_detail where blocked=0 and activated=1 and Nick=? and password=md5(?)";
        Connection conn=null;
        try{
            conn=dataSource.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setString(1,login);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();
            ps.close();
            User_infos usr= null;
            if(rs.next()){
                usr=new User_infos(rs.getString("Name"),rs.getString("Email"),rs.getString("ID"),rs.getString("Nick"),rs.getString("Surname"),rs.getBoolean("su"));
            }
            rs.close();
            return usr;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    public User_infos getLoginAboutID(String ID) {
        String sql="select ID,Nick,Name,Surname,Email,su from floda_user_detail where ID=?";
        Connection conn=null;
        try{
            conn=dataSource.getConnection();
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setString(1,ID);
            ResultSet rs = ps.executeQuery();
            ps.close();
            User_infos usr= null;
            if(rs.next()){
                usr=new User_infos(rs.getString("Name"),rs.getString("Email"),rs.getString("ID"),rs.getString("Nick"),rs.getString("Surname"),rs.getBoolean("su"));
            }
            rs.close();
            return usr;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
