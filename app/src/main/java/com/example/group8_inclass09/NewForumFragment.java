package com.example.group8_inclass09;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group8_inclass09.databinding.FragmentForumsBinding;
import com.example.group8_inclass09.databinding.FragmentNewForumBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class NewForumFragment extends Fragment {
    private static final String TAG = "New Forum Fragment: ";
    NewForumFragment.NewForumFragmentListener mListener;
    FragmentNewForumBinding binding;

    private FirebaseAuth mAuth;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewForumFragment() {
        // Required empty public constructor
    }

    public static NewForumFragment newInstance(String param1, String param2) {
        NewForumFragment fragment = new NewForumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "onCreate New Forum Fragment: ");
        Log.d(TAG, "user = " + user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNewForumBinding.inflate(inflater, container, false);

        // Clicking the “Cancel” button should pops the back stack and returns back to the Forums Fragment.
        binding.textViewCancelNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goBackToForumsList();
            }
        });

        // Clicking the “Submit” button check if all the fields are entered
        binding.buttonSubmitNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.editTextTitle.getText().toString();
                String description = binding.editTextDescription.getText().toString();

                // An alert dialog is displayed if any of the of the required fields are missing or empty
                if (title.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                    builder.setTitle("Missing field")
                            .setMessage("Please enter a forum title")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: Ok Clicked");
                                }
                            });
                    builder.create().show();
                } else if (description.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());
                    builder.setTitle("Missing field")
                            .setMessage("Please enter a forum description")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: Ok Clicked");
                                }
                            });
                    builder.create().show();
                } else { // The app stores a new forum on Firestore and displays the Forums Fragment upon a successful update
                    createData();
                    mListener.createNewAndUpdateList();
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("New Forum");
    }

    private void createData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        String nameId = user.getUid();

        HashMap<String, Object> forum = new HashMap<>();

        String title = binding.editTextTitle.getText().toString();
        String description = binding.editTextDescription.getText().toString();
        forum.put("title", title);
        forum.put("description", description);
        forum.put("creator", name);
        forum.put("creatorId", nameId);
        forum.put("dateTime", Timestamp.now());

        db.collection("forums")
                .add(forum)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding new forum" + e);
                    }
                });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewForumFragment.NewForumFragmentListener) context;
    }

    interface NewForumFragmentListener {
        void goBackToForumsList();
        void createNewAndUpdateList();
    }
}