package com.example.group8_inclass09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener,
        NewForumFragment.NewForumFragmentListener, ForumsFragment.ForumsFragmentListener {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rootView, new LoginFragment(), "login-fragment")
                .commit();
    }

    @Override
    public void goToAccountRegistration() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new RegisterFragment(), "registration-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelRegistration() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void Logout() {

    }

    @Override
    public void goBackToForumsList() {
        getSupportFragmentManager().popBackStack();
    }
}