package floda.pl.floda3.spring;

import floda.pl.floda3.User_infos;

public interface connection {
    User_infos getLoginAbout(String login, String password);
    User_infos getLoginAboutID(String ID);
}
