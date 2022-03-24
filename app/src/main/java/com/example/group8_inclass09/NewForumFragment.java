package com.example.group8_inclass09;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group8_inclass09.databinding.FragmentForumsBinding;
import com.example.group8_inclass09.databinding.FragmentNewForumBinding;

public class NewForumFragment extends Fragment {
    NewForumFragment.NewForumFragmentListener mListener;
    FragmentNewForumBinding binding;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                } else if (description.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please enter a description", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (NewForumFragment.NewForumFragmentListener) context;
    }

    interface NewForumFragmentListener {
        void goBackToForumsList();
    }
}