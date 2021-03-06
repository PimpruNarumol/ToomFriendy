package narumolpimpru.com.hitachi.toomfriendy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }   //Constructor


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Register Controller
        registerController();

        //Login Controller
        loginController();


    }   //Main Method

    private  void  loginController()
    {
        Button button = getView().findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userEditText = getView().findViewById(R.id.edtUser);
                EditText passwordEditText = getView().findViewById(R.id.edtPassword);

                String userString = userEditText.getText().toString().trim();
                String passwordString = passwordEditText.getText().toString().trim();

                MyAlert myAlert = new MyAlert(getActivity());
                if (userString.isEmpty() || passwordString.isEmpty()) {
                    myAlert.normalDialog(getString(R.string.title_space),getString(R.string.message_space));
                } else {
                    checkUserAndPass(userString, passwordString);
                }
            }
        });
    }

    private void checkUserAndPass(String userString, String passwordString) {
        MyAlert myAlert = new MyAlert(getActivity());
        MyConstant myConstant = new MyConstant();
        boolean b = true;   //True ==> User False, False ==> User True
        String truePassword = null;
        String nameString = null;

        try {
            GetAllDataThread getAllDataThread = new GetAllDataThread(getActivity());
            getAllDataThread.execute(myConstant.getUrlGetAllUser());

            String jsonString = getAllDataThread.get();//Read result
            Log.d("4DecV2","jsonString ==> " +jsonString);

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (userString.equals(jsonObject.getString("User"))) {
                    b = false;
                    truePassword = jsonObject.getString("Password");
                    nameString = jsonObject.getString("Name");
                }   //if
            }   //for
            if (b) {
                myAlert.normalDialog("User False" ,"No " + userString + " in my Database");
            } else if (passwordString.equals(truePassword)) {
                Toast.makeText(getActivity(), "Welcome " + nameString, Toast.LENGTH_SHORT).show();
            } else {
                myAlert.normalDialog("Password False","Please Try Again");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }   //try

    }

    private void registerController() {
        TextView textView = getView().findViewById(R.id.txtRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Replace Fragment
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contentMainFragment, new RegisterFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }   //CreateView

}   //Main Class
