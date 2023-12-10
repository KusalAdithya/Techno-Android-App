package com.waka.techno;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.waka.techno.model.User;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private EditText emailText, passwordText;
    private FirebaseAuth firebaseAuth;
    private SignInClient signInClient;
    CoordinatorLayout coordinatorLayout;

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

        coordinatorLayout = fragment.findViewById(R.id.coordinator);

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


        //forgot password btn ----------------------------------------------------------------------
        fragment.findViewById(R.id.forgotPasswordView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                if (email.isEmpty()) {
                    emailText.setError("Firstly enter your email");
                    emailText.requestFocus();
                } else {
                    forgotPassword(email);
                }
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
//onViewCreated end---------------------------------------------------------------------------------///////////////////

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

    private void userLogin(String email, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) {
                                if (!user.isEmailVerified()) {

                                    Snackbar.make(coordinatorLayout, "Please Verify Your Email", Snackbar.LENGTH_LONG)
                                            .setAction("Open Gmail",
                                                    new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            gmailIntent();
                                                        }
                                                    }
                                            ).show();
                                    return;
                                }
                                Toast.makeText(getContext(), "Logged", Toast.LENGTH_SHORT).show();
                                loadFragment(new HomeFragment());
                            }
                        } else {
                            Toast.makeText(getContext(), "Please enter valid details", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    private void forgotPassword(String email) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Snackbar.make(coordinatorLayout, "Please check your email inbox for password reset link", Snackbar.LENGTH_LONG)
                            .setAction("Open Gmail",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            gmailIntent();
                                        }
                                    }
                            ).show();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        emailText.setError("User does not exists.Please register again");
                        emailText.requestFocus();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
    }


    public void gmailIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}