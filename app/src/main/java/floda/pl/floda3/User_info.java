package floda.pl.floda3;

public class User_info {
    String name;
    String mail;
    String ID;
    String nick;
    String surname;
    boolean su;
    public User_info(String name,String mail,String ID,String nick,String surname,boolean su){
        this.name=name;
        this.mail=mail;
        this.ID=ID;
        this.nick=nick;
        this.surname=surname;
        this.su=su;
    }
    public String getID() {
        return ID;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getNick() {
        return nick;
    }

    public String getSurname() {
        return surname;
    }

    public boolean isSu() {
        return su;
    }
}
