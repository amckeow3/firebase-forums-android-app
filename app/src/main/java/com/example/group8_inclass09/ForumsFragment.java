package com.example.group8_inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.group8_inclass09.databinding.ForumListItemBinding;
import com.example.group8_inclass09.databinding.FragmentForumsBinding;
import com.example.group8_inclass09.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForumsFragment extends Fragment {
    private static final String TAG = "Forums Fragment: ";
    ForumsFragment.ForumsFragmentListener mListener;
    FragmentForumsBinding binding;
    
    private FirebaseAuth mAuth;

    private static final String ARG_PARAM_USER = "user";
    private static final String ARG_PARAM2 = "param2";

    private String mUser;
    private String mParam2;

    private ArrayList<Forum> forums = new ArrayList<>();
    ForumsListAdapter forumsListAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    public ForumsFragment() {
        // Required empty public constructor
    }


    public static ForumsFragment newInstance(String user) {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USER, String.valueOf(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String name = user.getDisplayName();
        Log.d(TAG, "onCreate Forums Fragment: ");
        Log.d(TAG, "user ---------------- " + user);
        Log.d(TAG, "Logged In User Display Name ---------------- " + name);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForumsBinding.inflate(inflater, container, false);

        getData();

        // Clicking the “Logout” menu button logs out the currently logged in user and
        // replace the Forums Fragment fragment with the Login Fragment.
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mListener.goToLogin();
            }
        });

        binding.buttonNewForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToCreateNewForum();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Forums");
    }

    public void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*
        db.collection("forums")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            forums.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Forum forum = new Forum();
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                forum.setForumId(document.getId());
                                forum.setTitle(document.getString("title"));
                                forum.setForumId(document.getId());
                                forum.setCreator(document.getString("creator"));
                                forum.setCreatorId(document.getString("creatorId"));
                                forum.setDescription(document.getString("description"));
                                forum.setDateTime(document.getDate("dateTime"));
                                forums.add(forum);
                            }
                            Log.d(TAG, "Forums Array List: " + forums);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView = binding.forumsRecyclerView;
                                    recyclerView.setHasFixedSize(true);
                                    linearLayoutManager = new LinearLayoutManager(getContext());
                                    recyclerView.setLayoutManager(linearLayoutManager);
                                    forumsListAdapter = new ForumsListAdapter(forums);
                                    recyclerView.setAdapter(forumsListAdapter);
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: " + task.getException());
                        }
                    }
                });
                */
        db.collection("forums")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forums.clear();
                        for (QueryDocumentSnapshot document: value) {
                            Forum forum = new Forum();
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            forum.setForumId(document.getId());
                            forum.setTitle(document.getString("title"));
                            forum.setForumId(document.getId());
                            forum.setCreator(document.getString("creator"));
                            forum.setCreatorId(document.getString("creatorId"));
                            forum.setDescription(document.getString("description"));
                            forum.setDateTime(document.getDate("dateTime"));
                            forums.add(forum);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView = binding.forumsRecyclerView;
                                recyclerView.setHasFixedSize(true);
                                linearLayoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(linearLayoutManager);
                                forumsListAdapter = new ForumsListAdapter(forums);
                                recyclerView.setAdapter(forumsListAdapter);
                            }
                        });
                    }
                });
    }

    class ForumsListAdapter extends RecyclerView.Adapter<ForumsListAdapter.ForumsViewHolder> {
        ArrayList<Forum> mForums;

        public ForumsListAdapter(ArrayList<Forum> data) {
            this.mForums = data;
        }

        @NonNull
        @Override
        public ForumsListAdapter.ForumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ForumListItemBinding binding = ForumListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ForumsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ForumsListAdapter.ForumsViewHolder holder, int position) {
            Forum forum = mForums.get(position);
            holder.setupUI(forum);
        }

        @Override
        public int getItemCount() {
            return this.mForums.size();
        }

        public class ForumsViewHolder extends RecyclerView.ViewHolder {
            ForumListItemBinding mBinding;
            Forum mForum;

            public ForumsViewHolder(@NonNull ForumListItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Forum forum) {
                mForum = forum;
                mBinding.textViewForumTitle.setText(mForum.title);
                mBinding.textViewCreator.setText(mForum.creator);
                mBinding.textViewForumDescription.setText(mForum.description);
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String userId = user.getUid();
                SimpleDateFormat sdf = new SimpleDateFormat ("MM/dd/yyyy hh:mm a");
                String formattedDate = sdf.format(mForum.dateTime);
                mBinding.textViewDateTime.setText(formattedDate);

                if (userId.matches(mForum.creatorId)) {
                    mBinding.imageFilterViewTrash.setImageResource(R.drawable.rubbish_bin);
                    mBinding.imageFilterViewTrash.setVisibility(View.VISIBLE);
                    mBinding.imageFilterViewTrash.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("forums")
                                    .document(mForum.forumId)
                                    .delete();
                            getData();
                        }
                    });
                } else {
                    mBinding.imageFilterViewTrash.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ForumsFragment.ForumsFragmentListener) context;
    }

    interface ForumsFragmentListener {
        void goToLogin();
        void goToCreateNewForum();
    }
}