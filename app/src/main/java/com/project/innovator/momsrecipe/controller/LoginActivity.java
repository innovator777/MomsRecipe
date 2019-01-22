package com.project.innovator.momsrecipe.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.innovator.momsrecipe.R;

public class LoginActivity extends AppCompatActivity{

    private  FirebaseAuth mAuth;

    ImageView imageView;
    Animation flowAnim;
    RelativeLayout relativeLayout;
    Animation idanim;

    EditText idEdit;
    EditText pwEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        Button btnjoinActivity = (Button) findViewById(R.id.btnjoin);
        Button btnlogin = (Button) findViewById(R.id.btnLogin);
        idEdit=(EditText) findViewById(R.id.idEdit);
        pwEdit=(EditText) findViewById(R.id.pwEdit);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idData = idEdit.getText().toString();
                String pwData = pwEdit.getText().toString();

                int pwEditData = pwEdit.length();

                if(idData.equals("")||pwData.equals("")){
                    Toast.makeText(LoginActivity.this, "빈칸없이 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(pwEditData>=6){
                        mAuth.signInWithEmailAndPassword(idData, pwData).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    android.util.Log.d("Login Test", "success");
                                    Toast.makeText(LoginActivity.this, "로그인", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "아이디나 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                                    android.util.Log.d("Login Test", "fail");
                                }
                            }
                        });


                    }
                    else {
                        Toast.makeText(LoginActivity.this, "비밀번호를 6자리 이상 입력하세요.", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });



        btnjoinActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);

            }
        });

        imageView = (ImageView) findViewById(R.id.imageMom);
        flowAnim = AnimationUtils.loadAnimation(this, R.anim.flow);
        relativeLayout = (RelativeLayout) findViewById(R.id.idlayout);
        idanim = AnimationUtils.loadAnimation(this, R.anim.idanim);


        flowAnim.setAnimationListener(new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //애니메이션 종료
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        idanim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //애니메이션 부분
        imageView.startAnimation(flowAnim);
        relativeLayout.startAnimation(idanim);
    }
}
