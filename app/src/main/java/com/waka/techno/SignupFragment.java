package com.waka.techno;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.EdgeToEdgeUtils;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignupFragment extends Fragment {

    private EditText emailText, passwordText;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        emailText = fragment.findViewById(R.id.emailTextSignup);
        passwordText = fragment.findViewById(R.id.passwordTextSignup);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // signup btn -----------------------------------------------------------------------------
        fragment.findViewById(R.id.signupBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) { //TextUtils.isEmpty(email)
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                    emailText.setError("Valid email is required");
                    emailText.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                    passwordText.requestFocus();
                } else if (password.length() <= 5) {
                    passwordText.setError("Should be ar least 5 characters");
                    passwordText.requestFocus();
                } else {
                    userRegistration(email, password);
                }
            }
        });

        // login btn -----------------------------------------------------------------------
        fragment.findViewById(R.id.loginTextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new LoginFragment());
            }
        });
    }

    // register with firebase auth -----------------------------------------------------------------
    private void userRegistration(String email, String password) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            currentUser.sendEmailVerification();
                            Toast.makeText(getContext(), "Registration Success! Please Verify Your Email", Toast.LENGTH_LONG).show();
                            loadFragment(new LoginFragment());
                        } else {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthUserCollisionException e){
                                Toast.makeText(getContext(), "User is already registered. Use another email", Toast.LENGTH_SHORT).show();
                            }catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // load fragment method ------------------------------------------------------------------------
    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}