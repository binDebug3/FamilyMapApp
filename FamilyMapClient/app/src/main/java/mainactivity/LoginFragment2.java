package mainactivity;


import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Result.EventAllResult;
import edu.byu.cs240.dallinstewart.familymapclient.CreateToast;
import edu.byu.cs240.dallinstewart.familymapclient.R;
import model.Settings;
import network.ServerProxy;

import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoginResult;
import Result.PersonAllResult;
import Result.RegisterResult;
import model.DataCache;
import Model.Person;

public class LoginFragment2 extends Fragment {
    private final boolean autoLogin = false;
    private final boolean standardServer = true;
    private static final String USERNAME_KEY = "0";
    private static final String PERSON_ID_KEY = "6";
    private static final String AUTHTOKEN_KEY = "7";
    private static final String SUCCESS_KEY = "8";
    private static final String MESSAGE_KEY = "9";
    public static final String TAG = "LoginKey";
    private String gender = "m";

    private Listener listener;

    public interface Listener {
        void notifyDone();
    }
    public void registerListener(LoginFragment2.Listener listener) {
        this.listener = listener;
    }

    public LoginFragment2() {
        // Required empty public constructor
    }
    public static LoginFragment2 newInstance() {
        LoginFragment2 fragment = new LoginFragment2();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate(Bundle) called");
        View view = inflater.inflate(R.layout.fragment_login2, container, false);
        //make button variables and disable the buttons
        Button loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        setSettings();
        //set listeners to log in or register when the user is reader
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loginClick(view); }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registerClick(view);}
        });
        //make textview variables to receive input
        TextView serverHostBox = view.findViewById(R.id.serverHostField);
        TextView serverPortBox = view.findViewById(R.id.serverPortField);
        TextView usernameBox = view.findViewById(R.id.usernameField);
        TextView passwordBox = view.findViewById(R.id.passwordField);
        TextView firstNameBox = view.findViewById(R.id.firstNameField);
        TextView lastNameBox = view.findViewById(R.id.lastNameField);
        TextView emailBox = view.findViewById(R.id.emailAddressField);

        //build a text watcher to determine when to enable the log in and register buttons
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!usernameBox.getText().toString().equals("") &&
                        !passwordBox.getText().toString().equals("") &&
                        !serverHostBox.getText().toString().equals("") &&
                        !serverPortBox.getText().toString().equals("")) {
                    loginButton.setEnabled(true);
                    if (!emailBox.getText().toString().equals("") &&
                            !firstNameBox.getText().toString().equals("") &&
                            !lastNameBox.getText().toString().equals("")) {
                        registerButton.setEnabled(true);
                    } else {
                        registerButton.setEnabled(false);
                    }
                } else {
                    loginButton.setEnabled(false);
                    registerButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        };
        //add listeners to every input box
        usernameBox.addTextChangedListener(textWatcher);
        passwordBox.addTextChangedListener(textWatcher);
        serverHostBox.addTextChangedListener(textWatcher);
        serverPortBox.addTextChangedListener(textWatcher);
        firstNameBox.addTextChangedListener(textWatcher);
        lastNameBox.addTextChangedListener(textWatcher);
        emailBox.addTextChangedListener(textWatcher);
        //wire up the gender radio button
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioMale:
                        gender = "m";
                        break;
                    case R.id.radioFemale:
                        gender = "f";
                        break;
                }
            }
        });
        //automatic login functionality for debugging
        if (standardServer) {
            serverHostBox.setText("10.0.2.2");
            serverPortBox.setText("8080");
        }
        if (autoLogin) {
            usernameBox.setText("sheila");
            passwordBox.setText("parker");
            loginClick(view);
        }
        return view;
    }

    private void loginClick(View view) {
        Log.d(TAG, "loginClick called");
        CreateToast.showToast(getContext(), "Signing in...");
        EditText[] userInfo = getText(view);
        if (loginIsValid(userInfo)) {
            try {
                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        //Display a toast based on the success of the log in
                        //and notify the main activity to open the map fragment
                        String success = bundle.getString(SUCCESS_KEY, "false");
                        if (success.equals("true")) {
                            getPersonData(userInfo);
                            listener.notifyDone();
                        } else {
                            CreateToast.showToast(getContext(), "Incorrect username or password");
                        }
                    }
                };
                LoginTask task = new LoginTask(uiThreadMessageHandler, userInfo);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            CreateToast.showToast(getContext(),"Please fill the first four fields");
        }
    }

    private void registerClick(View view) {
        Log.d(TAG, "registerClick called");
        CreateToast.showToast(getContext(), "Registering...");
        EditText[] userInfo = getText(view);
        if (registerIsValid(userInfo)) {
            try {
                Handler uiThreadMessageHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        //Display a toast based on the success of the register
                        //and notify the main activity to open the map fragment
                        String success = bundle.getString(SUCCESS_KEY, "false");
                        if (success.equals("true")) {
                            getPersonData(userInfo);
                        } else {
                            CreateToast.showToast(getContext(), "Username already registered");
                        }
                        listener.notifyDone();
                    }
                };
                RegisterTask task = new RegisterTask(uiThreadMessageHandler, userInfo, gender);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(task);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            CreateToast.showToast(getContext(),"Please fill every field");
        }
    }

    private void getPersonData(EditText[] userInfo) {
        //get person elements about the current user from the data cache to display a toast
        //with the user's information
        Log.d(TAG, "getPersonData called");
        String personID = DataCache.getInstance().getCurrUserID();
        String authToken = DataCache.getInstance().getCurrAuthToken();
        Handler uiThreadMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                message.getData();
                String userID = DataCache.getInstance().getCurrUserID();
                Person user = DataCache.getInstance().getPersonByID(userID);
                DataCache.getInstance().setCurrFirst(user.getFirstName());
                DataCache.getInstance().setCurrLast(user.getLastName());
                String toastMessage = "Hello, " + DataCache.getInstance().getCurrFirst() +
                        " " + DataCache.getInstance().getCurrLast();
                CreateToast.showToast(getContext(), toastMessage);

            }
        };
        PersonTask task = new PersonTask(uiThreadMessageHandler, userInfo[5].getText().toString(),
                userInfo[6].getText().toString(), personID, authToken);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }

    //helper method to get all the input from the user once they click a button
    private EditText[] getText(View view) {
        Log.d(TAG, "getText called");
        int amountUserInfo = 7;
        EditText[] userInfo = new EditText[amountUserInfo];
        userInfo[0] = view.findViewById(R.id.usernameField);
        userInfo[1] = view.findViewById(R.id.passwordField);
        userInfo[2] = view.findViewById(R.id.firstNameField);
        userInfo[3] = view.findViewById(R.id.lastNameField);
        userInfo[4] = view.findViewById(R.id.emailAddressField);
        userInfo[5] = view.findViewById(R.id.serverHostField);
        userInfo[6] = view.findViewById(R.id.serverPortField);
        return userInfo;
    }
    //helper method to ensure that the correct boxes have been filled for logging in
    private boolean loginIsValid(EditText[] userInfo) {
        Log.d(TAG, "loginIsValid called");
        int[] importantIndex = {0,1,5,6};
        for (int index : importantIndex) {
            if (userInfo[index].getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }
    //helper method to ensure that the correct boxes have been filled for registering
    private boolean registerIsValid(EditText[] userInfo) {
        Log.d(TAG, "registerIsValid called");
        for (EditText entry : userInfo) {
            if (entry.getText().toString().equals("")) {
                return false;
            }
        }
        return !gender.equals("");
    }
    //set default settings
    private void setSettings() {
        Settings.getInstance().setLifeStoryLines(true);
        Settings.getInstance().setFamilyTreeLines(true);
        Settings.getInstance().setFatherSide(true);
        Settings.getInstance().setMotherSide(true);
        Settings.getInstance().setSpouseLines(true);
        Settings.getInstance().setMaleEvents(true);
        Settings.getInstance().setFemaleEvents(true);
    }



    /* ------------------------ BACKGROUND TASKS ---------------------------------------- */

    public static class LoginTask implements Runnable {
        private final Handler messageHandler;
        private final EditText[] userInfo;

        public LoginTask(Handler messageHandler, EditText[] userInfo) {
            Log.d(TAG, "LoginTask constructed");
            this.messageHandler = messageHandler;
            this.userInfo = userInfo;
        }

        @Override
        public void run() {
            Log.d(TAG, "LoginTask method run called");
            //set variables from input for the login request
            String username = userInfo[0].getText().toString();
            String password = userInfo[1].getText().toString();
            String serverHost = userInfo[5].getText().toString();
            String serverPort = userInfo[6].getText().toString();

            //call the server proxy to log in
            LoginRequest request = new LoginRequest(username, password);
            LoginResult result = ServerProxy.postUserLogin(serverHost, serverPort, request);
            //update the data cache and send message
            if (result != null) {
                DataCache.getInstance().setCurrUserID(result.getPersonID());
                DataCache.getInstance().setCurrAuthToken(result.getAuthToken());
                sendMessage(result);
            }
        }

        private void sendMessage(LoginResult result) {
            Message message = Message.obtain();
            //add login information to the message and send it
            Bundle messageBundle = new Bundle();
            messageBundle.putString(AUTHTOKEN_KEY, result.getAuthToken());
            messageBundle.putString(USERNAME_KEY, result.getUsername());
            messageBundle.putString(PERSON_ID_KEY, result.getPersonID());
            messageBundle.putString(MESSAGE_KEY, result.getMessage());
            if (result.isSuccess()) {
                messageBundle.putString(SUCCESS_KEY, "true");
            } else {
                messageBundle.putString(SUCCESS_KEY, "false");
            }

            Log.d(TAG, "LoginTask sendMessage called");
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    public static class RegisterTask implements Runnable {
        private final Handler messageHandler;
        private final EditText[] userInfo;
        private final String gender;

        public RegisterTask(Handler messageHandler, EditText[] userInfo, String gender) {
            Log.d(TAG, "RegisterTask constructed");
            this.messageHandler = messageHandler;
            this.userInfo = userInfo;
            this.gender = gender;
        }

        @Override
        public void run() {
            Log.d(TAG, "RegisterTask run method called");
            //set variables from input for the register request
            String username = userInfo[0].getText().toString();
            String password = userInfo[1].getText().toString();
            String email = userInfo[4].getText().toString();
            String firstName = userInfo[2].getText().toString();
            String lastName = userInfo[3].getText().toString();
            //gender is already set
            String serverHost = userInfo[5].getText().toString();
            String serverPort = userInfo[6].getText().toString();
            //call the server proxy to register
            RegisterRequest request = new RegisterRequest(username, password, email, firstName, lastName, gender);
            RegisterResult result = ServerProxy.postRegisterUser(serverHost, serverPort, request);
            //update the data cache and send message
            if (result != null) {
                DataCache.getInstance().setCurrAuthToken(result.getAuthToken());
                sendMessage(result);
            }
        }
        private void sendMessage(RegisterResult result) {
            Message message = Message.obtain();
            //add register information to the message and send it
            Bundle messageBundle = new Bundle();
            messageBundle.putString(AUTHTOKEN_KEY, result.getAuthToken());
            messageBundle.putString(USERNAME_KEY, result.getUserName());
            messageBundle.putString(PERSON_ID_KEY, result.getPersonID());
            messageBundle.putString(MESSAGE_KEY, result.getMessage());
            if (result.isSuccess()) {
                messageBundle.putString(SUCCESS_KEY, "true");
            } else {
                messageBundle.putString(SUCCESS_KEY, "false");
            }

            Log.d(TAG, "RegisterTask sendMessage called");
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    public static class PersonTask implements Runnable  {
        private Handler messageHandler = null;
        String personID;
        String authToken;
        String serverHost;
        String serverProxy;
        PersonAllResult people;
        EventAllResult events;

        public PersonTask() {
            new PersonTask(null,"","","","");
        }
        public PersonTask(Handler messageHandler, String serverHost, String serverProxy,
                          String personID, String authToken) {
            Log.d(TAG, "PersonTask constructed");
            this.messageHandler = messageHandler;
            this.serverHost = serverHost;
            this.serverProxy = serverProxy;
            this.personID = personID;
            this.authToken = authToken;
            people = null;
            events = null;
        }

        @Override
        public void run() {
            Log.d(TAG, "PersonTask run method called");
            //get events from the database and update the data cache
            events = ServerProxy.getEvents(serverHost,serverProxy, authToken);
            if (events != null) {
                DataCache.getInstance().insertEventsByUser(personID, events.getEvents());
            }
            //get people from the database and update the data cache
            people = ServerProxy.getPersons(serverHost, serverProxy, personID, authToken);
            if (people != null) {
                DataCache.getInstance().insertPersonsByUser(personID, people.getPersons());
                sendMessage(people);
            }
        }

        private void sendMessage(PersonAllResult result) {
            Message message = Message.obtain();
            //add success information to the message and send it
            Bundle messageBundle = new Bundle();
            messageBundle.putString(MESSAGE_KEY, result.getMessage());
            if (result.isSuccess()) {
                messageBundle.putString(SUCCESS_KEY, "true");
            } else {
                messageBundle.putString(SUCCESS_KEY, "false");
            }
            Log.d(TAG, "PersonTask sendMessage called");
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }
}