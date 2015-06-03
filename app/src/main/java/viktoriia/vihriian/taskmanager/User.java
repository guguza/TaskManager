package viktoriia.vihriian.taskmanager;

public class User {
    private static volatile User instance;

    private String login;
    private String password;


    public static User getInstance() {
        User localInstance = instance;
        if(localInstance == null) {
            synchronized (User.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new User();
                }
            }
        }
        return localInstance;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
