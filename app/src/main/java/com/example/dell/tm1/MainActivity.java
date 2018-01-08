package com.example.dell.tm1;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Scene;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    Scene loginScene;
    Scene registerScene;
    ViewGroup mSceneRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSceneRoot = (ViewGroup)findViewById(R.id.login_register_scene);
        loginScene = Scene.getSceneForLayout(mSceneRoot,R.layout.login,this);
        registerScene = Scene.getSceneForLayout(mSceneRoot,R.layout.register,this);

    }
}
