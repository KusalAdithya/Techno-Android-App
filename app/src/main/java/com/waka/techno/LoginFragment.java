package com.waka.techno;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private EditText emailText, passwordText;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        emailText = fragment.findViewById(R.id.emailText);
        passwordText = fragment.findViewById(R.id.passwordText);

        // log in btn ------------------------------------------------------------------------------
        fragment.findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { //TextUtils.isEmpty(email)
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                    emailText.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your password", Toast.LENGTH_LONG).show();
                    passwordText.requestFocus();
                } else {
                    userLogin(email, password);
                }
            }
        });

        // sign up btn -----------------------------------------------------------------------------
        fragment.findViewById(R.id.signupTextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new SignupFragment());
            }
        });
    }

    private void userLogin(String email, String password){
      firebaseAuth = FirebaseAuth.getInstance();
      firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
              new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         FirebaseUser user  = firebaseAuth.getCurrentUser();
                         if (user != null) {
                             if (!user.isEmailVerified()) {
                                 Toast.makeText(getContext(), "Please Verify Your Email", Toast.LENGTH_LONG).show();
                                 return;
                             }
                             loadFragment(new ProfileFragment()); //loadFragment(new HomeFragment());
                         }
                     }else{
                         Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                     }
                  }
              }
      );
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}