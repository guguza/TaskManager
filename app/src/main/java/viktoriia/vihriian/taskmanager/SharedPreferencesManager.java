package viktoriia.vihriian.taskmanager;

/**
 * Created by Администратор on 31.05.2015.
 */
public class SharedPreferencesManager {
    private static volatile SharedPreferencesManager instance;
    private static final String APP_SETTINGS = "APP_SETTINGS";

    //fields
    private static final String NOTES = "NOTES";
    private static final String ID = "ID"

    public static SharedPreferencesManager getInstance() {
        SharedPreferencesManager localInstance = instance;
        if (localInstance == null) {
            synchronized (SharedPreferencesManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SharedPreferencesManager();
                }
            }
        }
        return localInstance;
    }


}
