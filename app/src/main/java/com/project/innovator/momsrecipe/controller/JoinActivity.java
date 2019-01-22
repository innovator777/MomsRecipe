package com.project.innovator.momsrecipe.controller;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.innovator.momsrecipe.R;


public class JoinActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button btnjoin;
    EditText editId;
    EditText editPw;
    EditText editRPw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btnjoin = (Button) findViewById(R.id.btnjoin);
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText) findViewById(R.id.editPw);
        editRPw = (EditText) findViewById(R.id.editRPw);

        mAuth = FirebaseAuth.getInstance();

        btnjoin.setOnClickListener(new View.OnClickListener(){

                                       @Override
                                       public void onClick(View v) {
                                           String idData = editId.getText().toString();
                                           String pwData = editPw.getText().toString();
                                           String rpwData = editRPw.getText().toString();

                                           //문자열 길이 받아오기!!
                                           int pwDataLength = pwData.length();
                                           int rpwDataLength = rpwData.length();

//                Toast.makeText(joinActivity.this, idData+pwData+rpwData, Toast.LENGTH_SHORT).show();

                                           if(idData.equals("") || pwData.equals("") || rpwData.equals("")){
                                               Toast.makeText(JoinActivity.this, "빈칸없이 입력하세요.", Toast.LENGTH_LONG).show();

                                           }

                                           else{
                                               if(pwData.equals(rpwData) && pwDataLength>=6 && rpwDataLength>=6) {                    //pwData랑 rpwData 값이 같을 때
                                                   mAuth.createUserWithEmailAndPassword(idData, pwData).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                           if (task.isSuccessful()) {
                                                               FirebaseUser user = mAuth.getCurrentUser();
                                                               Toast.makeText(JoinActivity.this, "가입완료", Toast.LENGTH_SHORT).show();
                                                               finish();

                                                           } else {
                                                               Toast.makeText(JoinActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                   });
                                               }
                                               else if(pwDataLength<6 || rpwDataLength<6){
                                                   Toast.makeText(JoinActivity.this, "비밀번호를 6자리 이상 입력하세요.", Toast.LENGTH_SHORT).show();

                                               }
                                               else{
                                                   Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                                               }
                                           }

                                       }
                                   }
        );
    }
}
