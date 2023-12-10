package com.waka.techno;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.internal.EdgeToEdgeUtils;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.waka.techno.model.User;

public class SignupFragment extends Fragment {

    private EditText emailText, passwordText;
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;

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

        // signup btn -----------------------------------------------------------------------------
        fragment.findViewById(R.id.signupBtnSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if (email.isEmpty()) { //TextUtils.isEmpty(email)
                    Toast.makeText(getContext(), "Please enter your email", Toast.LENGTH_LONG).show();
                    emailText.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Valid email is required");
                    emailText.requestFocus();
                } else if (password.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a password", Toast.LENGTH_LONG).show();
                    passwordText.requestFocus();
                } else if (password.length() < 5) {
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

        // google sign in ---------------------------------------------------------------------------
        firebaseAuth = FirebaseAuth.getInstance();
        signInClient = Identity.getSignInClient(requireActivity().getApplicationContext());

        fragment.findViewById(R.id.googleBtnLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSignInIntentRequest signInIntentRequest = GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.web_client_id)).build();

                signInClient.getSignInIntent(signInIntentRequest)
                        .addOnSuccessListener(new OnSuccessListener<PendingIntent>() {
                            @Override
                            public void onSuccess(PendingIntent pendingIntent) {
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest
                                        .Builder(pendingIntent).build();
                                signInLauncher.launch(intentSenderRequest);
                                Toast.makeText(getContext(), "Select a Google Account", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Logging with Google Failed", Toast.LENGTH_LONG).show();
                            }
                        });
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

                            User user = new User();
                            user.setEmail(email);

                            currentUser.sendEmailVerification();
                            Toast.makeText(getContext(), "Registration Success! Please Verify Your Email", Toast.LENGTH_LONG).show();
                            loadFragment(new LoginFragment());

                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Toast.makeText(getContext(), "User is already registered. Use another email", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //google sign in methods -----------------------------------------------------------------------
    private final ActivityResultLauncher<IntentSenderRequest> signInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            handleSignInResult(o.getData());
                        }
                    });

    private void handleSignInResult(Intent intent) {
        try {
            SignInCredential signInCredential = signInClient.getSignInCredentialFromIntent(intent);
            String idToken = signInCredential.getGoogleIdToken();
            firebaseAuthWithGoogle(idToken);
        } catch (ApiException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        Task<AuthResult> authResultTask = firebaseAuth.signInWithCredential(authCredential);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                    if (currentUser != null) {
                        loadFragment(new HomeFragment());
                        Toast.makeText(getContext(), "Logged with Google Account", Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getContext(), "User is already registered. Use another email", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Logging With Google Fail", Toast.LENGTH_LONG).show();
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