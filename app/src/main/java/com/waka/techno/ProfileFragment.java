package com.waka.techno;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.waka.techno.model.User;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private TextView userNameView;
    private ImageView userDpView;
    private EditText emailText, fNameText, lNameText, mobileText, address1Text, address2Text, address3Text;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private Uri imgUri;
    private String imgUrl;
    private String fName, lName, mobile, address1, address2, address3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        userNameView = fragment.findViewById(R.id.textView5);
        userDpView = fragment.findViewById(R.id.userDpView);
        emailText = fragment.findViewById(R.id.emailTextProfile);
        fNameText = fragment.findViewById(R.id.fnameTextProfile);
        lNameText = fragment.findViewById(R.id.lnameTextProfile);
        mobileText = fragment.findViewById(R.id.mobileTextProfile);
        address1Text = fragment.findViewById(R.id.addressText1Profile);
        address2Text = fragment.findViewById(R.id.addressText2Profile);
        address3Text = fragment.findViewById(R.id.cityTextProfile);


        //get data ---------------------------------------------------------------------------------
        db.collection("users").document(firebaseAuth.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<com.google.firebase.firestore.DocumentSnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            User user = documentSnapshot.toObject(User.class);

                            fNameText.setText(user.getFirstName());
                            lNameText.setText(user.getLastName());
                            mobileText.setText(user.getMobile());
                            address1Text.setText(user.getAddressNo());
                            address2Text.setText(user.getAddressLine());
                            address3Text.setText(user.getCity());

                            if (user.getUserDp() != null) {
                                Glide.with(requireContext()).load(user.getUserDp()).circleCrop().into(userDpView);
                            } else {
                                userDpView.setImageResource(R.drawable.user_non_dp);
                            }

                        }

                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Data getting Fail", Toast.LENGTH_SHORT).show();
            }
        });



        //back Button ------------------------------------------------------------------------------
        fragment.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new HomeFragment());
//                fragment.findViewById(R.id.bottomNavHome).setSelected(true);
            }
        });

        //user name view ---------------------------------------------------------------------------
        if (firebaseUser != null) {
            userNameView.setText("Hello \n" + firebaseUser.getEmail());

            //google account image
            if (firebaseUser.getPhotoUrl() != null) {
                Glide.with(requireContext()).load(firebaseUser.getPhotoUrl()).circleCrop().into(userDpView);
            } else {
                userDpView.setImageResource(R.drawable.user_non_dp);
            }

            emailText.setText(firebaseUser.getEmail());
            emailText.setEnabled(false);
        } else {
            userNameView.setText("Hello User");
        }


        //dp btn------------------------------------------------------------------------------------
        userDpView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));

                    }
                }
        );


        //update btn -------------------------------------------------------------------------------
        fragment.findViewById(R.id.updateBtnProfile).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fName = fNameText.getText().toString();
                        lName = lNameText.getText().toString();
                        mobile = mobileText.getText().toString();
                        address1 = address1Text.getText().toString();
                        address2 = address2Text.getText().toString();
                        address3 = address3Text.getText().toString();

                        String mobileRegex = "[0][0-9]{9}";
                        Matcher mobileMatcher;
                        Pattern mobilePattern = Pattern.compile(mobileRegex);
                        mobileMatcher = mobilePattern.matcher(mobile);

                        if (fName.isEmpty()) {
                            fNameText.setError("Please enter your first name");
                            fNameText.requestFocus();
                        } else if (lName.isEmpty()) {
                            lNameText.setError("Please enter your last name");
                            lNameText.requestFocus();
                        } else if (mobile.isEmpty()) {
                            mobileText.setError("Please enter your mobile number");
                            mobileText.requestFocus();
                        } else if (!mobileMatcher.matches()) {
                            mobileText.setError("Please enter your valid mobile number");
                            mobileText.requestFocus();
                        } else if (address1.isEmpty()) {
                            address1Text.setError("Please enter your address no");
                            address1Text.requestFocus();
                        } else if (address2.isEmpty()) {
                            address2Text.setError("Please enter your address line");
                            address2Text.requestFocus();
                        } else if (address3.isEmpty()) {
                            address3Text.setError("Please enter your city");
                            address3Text.requestFocus();
                        } else {

                            //add user to db
                            if (imgUri != null) { //set dp
                                imageUpload();

                            } else { // not set dp

                                User user = new User();
                                user.setEmail(firebaseUser.getEmail());
                                user.setFirstName(fName);
                                user.setLastName(lName);
                                user.setMobile(mobile);
                                user.setAddressNo(address1);
                                user.setAddressLine(address2);
                                user.setCity(address3);

                                db.collection("users").document(firebaseUser.getUid())
                                        .set(user)
                                        .addOnSuccessListener(
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                                        loadFragment(new ProfileFragment());
                                                    }
                                                }
                                        ).addOnFailureListener(
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Update Fail.", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                        );

                            }


                        }


                    }
                }
        );

//        fragment.findViewById(R.id.deleteBtnProfile).setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog.Builder alert =new AlertDialog.Builder(getContext());
//                        alert.setTitle("Warning").setMessage("Do you want to delete your Techno account")
//                                .setIcon(android.R.drawable.ic_dialog_alert)
//                                .setPositiveButton(android.R.string.yes, new DialogInterface().On
//
//
//
//                                        .OnClickListener() {
//
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        Toast.makeText(MainActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
//                                    }})
//                                .setNegativeButton(android.R.string.no, null).show();
//                    }
//                }
//        );
    }


    // image upload and user add to db -------------------------------------------------------------
    private void imageUpload() {
        String path = imgUri.getPath() + UUID.randomUUID().toString();
        StorageReference reference = storageReference = FirebaseStorage.getInstance().getReference()
                .child("userImages/").child(path);
        reference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete()) ;
                        imgUrl = uriTask.getResult().toString();

                        //add to db ----------------------------------------------------------------
                        User user = new User();
                        user.setEmail(firebaseUser.getEmail());
                        user.setFirstName(fName);
                        user.setLastName(lName);
                        user.setMobile(mobile);
                        user.setAddressNo(address1);
                        user.setAddressLine(address2);
                        user.setCity(address3);
                        user.setUserDp(imgUrl);

                        db.collection("users").document(firebaseUser.getUid())
                                .set(user)
                                .addOnSuccessListener(
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                                loadFragment(new ProfileFragment());
                                            }
                                        }
                                ).addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Update Fail.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                );
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Image add Fail.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imgUri = result.getData().getData();
                        Glide.with(requireContext()).load(imgUri).circleCrop().into(userDpView);
                    }
                }
            }
    );

    public void loadFragment(Fragment fragment) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}