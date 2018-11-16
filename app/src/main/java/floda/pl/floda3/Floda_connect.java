package floda.pl.floda3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import floda.pl.floda3.spring.Database_connection;
public class Floda_connect {
    Database_connection conn;
    ApplicationContext conext;
    String login,password;
    User_infos usr;
    Floda_connect(){
         conext = new ClassPathXmlApplicationContext("Spring-module.xml");
         conn = (Database_connection) conext.getBean("DatabaseGetter");

    }
    void getDataFromBase(){

    }
    public void usrLogin(String login,String password){
        this.login=login;
        this.password=password;
        usr= conn.getLoginAbout(login,password);
    }
    public void usrLoginID(String ID){
        usr= conn.getLoginAboutID(ID);
    }
    public User_infos getUsr() {
        return usr;
    }
}
