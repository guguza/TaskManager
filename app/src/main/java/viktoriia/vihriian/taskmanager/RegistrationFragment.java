package viktoriia.vihriian.taskmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import viktoriia.vihriian.taskmanager.core_classes.User;
import viktoriia.vihriian.taskmanager.managers.MyFragmentManager;
import viktoriia.vihriian.taskmanager.managers.SharedPreferencesManager;


public class RegistrationFragment extends Fragment implements View.OnClickListener{

    private EditText login;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;
    private SharedPreferencesManager mPrefs;

    private static final String TAG = "RegistrationFragment";

    private final MyFragmentManager myFragmentManager = MyFragmentManager.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        setContainerParams(container);

        login = (EditText) view.findViewById(R.id.et_login);
        password = (EditText) view.findViewById(R.id.et_password);
        confirmPassword = (EditText) view.findViewById(R.id.et_password_confirm);
        registerButton = (Button) view.findViewById(R.id.butt_register);

        mPrefs = SharedPreferencesManager.getInstance(getActivity());

        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.butt_register:
                //save to prefs
                if(checkFields()) {
                   // myFragmentManager.backToPreviousFragment(R.id.fragment_container);
                    myFragmentManager.changeFragment(R.id.fragment_container, new LoginFragment());
                }
                break;
        }
    }

    private void setContainerParams(ViewGroup container) {
        container.setBackgroundColor(getResources().getColor(R.color.accent));
        container.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private boolean checkFields() {

        String fLogin = login.getText().toString();
        String fPassword = password.getText().toString();
        String fPassword2 = confirmPassword.getText().toString();

        if(fLogin.equals("") || fPassword.equals("")
                || fPassword2.equals("")) {
            Toast.makeText(getActivity(), R.string.empty_fields_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!fPassword.equals(fPassword2)) {
            Toast.makeText(getActivity(), R.string.pass_confirmation_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        saveUser(fLogin, fPassword);
        return true;
    }

    private  void saveUser(String uLogin, String uPassword) {
        User user = User.getInstance();
        user.setLogin(uLogin);
        user.setPassword(uPassword);
        if(mPrefs.checkUser(uLogin)) {
            mPrefs.saveUser(user);
            Toast.makeText(getActivity(),R.string.success_registration, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.username_error, Toast.LENGTH_SHORT).show();
        }
    }
}
