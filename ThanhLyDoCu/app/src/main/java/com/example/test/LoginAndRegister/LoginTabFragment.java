package com.example.test.LoginAndRegister;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.test.ForgetPassword.MakeSelection;
import com.example.test.R;
import com.example.test.User.UserDashBoard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginTabFragment extends Fragment {
    EditText email, pass;
    TextView forgotPass;
    Button login;
    float v=0;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;


    DatabaseReference UserRef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        email = root.findViewById(R.id.inputEmail);
        pass = root.findViewById(R.id.inputPassword);
        forgotPass = root.findViewById(R.id.forgotPassword);
        login = root.findViewById(R.id.btnLogin);

        progressDialog = new ProgressDialog(getActivity());

        //Database
        mAuth = FirebaseAuth.getInstance();


        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perforLogin();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MakeSelection.class);
                startActivity(intent);
            }
        });

        email.setTranslationX(800);
        pass.setTranslationX(800);
        forgotPass.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        pass.setAlpha(v);
        forgotPass.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        forgotPass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        registerInBackground();
        return root;



    }



    private void registerInBackground() {
        FirebaseApp.initializeApp(getActivity());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token = task.getResult();

            }
        });
    }



    private void perforLogin() {
        String inputEmail = email.getText().toString();
        String password = pass.getText().toString();

        if(!isConnected(this)){
            showCustomDialog();
        }

        if (!inputEmail.matches(emailPattern))
        {
            email.setError("Nhập đúng Email");
        }else if(password.isEmpty() || password.length()<6)
        {
            pass.setError("Nhập đúng mật khẩu");
        }else {
            progressDialog.setMessage("Đợi tí nha đang đăng nhập ....");
            progressDialog.setTitle("Đăng nhập");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(inputEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                String deviceToken = task.getResult();
                                UserRef.child(currentUserID).child("device_token")
                                        .setValue(deviceToken)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    progressDialog.dismiss();
                                                    sendUserToNextActivity();
                                                    Toast.makeText(getActivity().getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }
                        });


                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    //Check Internet Connection
    private boolean isConnected(LoginTabFragment loginTabFragment) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn!=null && mobileConn.isConnected())){
            return  true;
        }else{
            return false;
        }
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Bạn chưa kết nối internet kìa?")
                .setCancelable(false)
                .setPositiveButton("Kết nối", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



    private void sendUserToNextActivity() {
        Intent intent = new Intent(getActivity(), UserDashBoard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
