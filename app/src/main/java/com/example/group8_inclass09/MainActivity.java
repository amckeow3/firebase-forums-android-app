package com.example.group8_inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener,
        NewForumFragment.NewForumFragmentListener, ForumsFragment.ForumsFragmentListener {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "login-fragment")
                    .commit();
        } else {
            String name = user.getDisplayName();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new ForumsFragment(), "forum-fragment")
                    .commit();
        }
    }

    @Override
    public void goToAccountRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegisterFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToForumsList() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ForumsFragment(), "forums-fragment")
                .commit();
    }

    @Override
    public void cancelRegistration() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToCreateNewForum() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new NewForumFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToForumsList() {
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void createNewAndUpdateList() {
        getSupportFragmentManager().popBackStack();
    }
}