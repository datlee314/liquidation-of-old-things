package com.example.test.LoginAndRegister;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.test.SetUpProfile.SetUpProfile1;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUpTabFragment extends Fragment {

    EditText email, pass, comfirmPass;
    Button register;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;



    DatabaseReference RootRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        email = root.findViewById(R.id.inputEmail);
        pass = root.findViewById(R.id.inputPassword);
        comfirmPass = root.findViewById(R.id.inputConfirmPassword);
        register = root.findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(getActivity());
        mAuth =FirebaseAuth.getInstance();


        RootRef = FirebaseDatabase.getInstance().getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerforAuth();
            }
        });

        return root;
    }
    private void PerforAuth() {
        String inputEmail = email.getText().toString();
        String password = pass.getText().toString();
        String confirmPassword = comfirmPass.getText().toString();

        if (!inputEmail.matches(emailPattern))
        {
            email.setError("Nhập đúng Email");
        }else if(password.isEmpty() || password.length()<6)
        {
            pass.setError("Nhập mật khẩu phù hợp");
        }else if(!password.equals(confirmPassword))
        {
            comfirmPass.setError("Xác nhận mật khẩu sai");
        }else
        {
            progressDialog.setMessage("Đợi tí nha đang đăng ký ....");
            progressDialog.setTitle("Đăng ký");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(inputEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String currentUserID = mAuth.getCurrentUser().getUid();
                        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                String deviceToken = task.getResult();
                                RootRef.child("Users").child(currentUserID).child("device_token")
                                        .setValue(deviceToken);
                            }
                        });


                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(getActivity().getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(),""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent = new Intent(getActivity(), SetUpProfile1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
