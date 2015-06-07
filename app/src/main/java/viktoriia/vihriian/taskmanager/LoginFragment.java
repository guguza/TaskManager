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
import viktoriia.vihriian.taskmanager.tools.SharedPreferencesManager;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "LoginFragment";
    private static SharedPreferencesManager mPrefs;

    private MyFragmentManager myFragmentManager;
    private EditText login;
    private EditText password;
    private Button loginButton;
    private Button registerButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mPrefs = SharedPreferencesManager.getInstance(getActivity());

        login = (EditText) view.findViewById(R.id.et_login);
        password = (EditText) view.findViewById(R.id.et_password);
        loginButton = (Button) view.findViewById(R.id.butt_login);
        registerButton = (Button) view.findViewById(R.id.butt_register);

        myFragmentManager  = MyFragmentManager.getInstance();

        container.setBackgroundColor(getResources().getColor(R.color.accent));
        container.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.butt_login:
                //check user
            if(checkFields()) {
                if(mPrefs.checkUser(login.getText().toString(), password.getText().toString())) {
                    User user = User.getInstance();
                    user.setLogin(login.getText().toString());
                    Toast.makeText(getActivity(), "Hello, " + user.getLogin() + "!", Toast.LENGTH_SHORT).show();
                    navigateTo(new NotesListFragment());
                } else {
                    Toast.makeText(getActivity(), "Incorrect login or password", Toast.LENGTH_SHORT).show();
                }
            }
                break;
            case R.id.butt_register:
                navigateTo(new RegistrationFragment());
                break;
        }
    }

    private boolean checkFields() {
        String fLogin = login.getText().toString();
        String fPassword = password.getText().toString();

        if(fLogin.equals("") || fPassword.equals("")) {
            Toast.makeText(getActivity(), "Fill in all the fields!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void navigateTo(Fragment fragment) {
        myFragmentManager.changeFragment(R.id.fragment_container, fragment);
    }
}
