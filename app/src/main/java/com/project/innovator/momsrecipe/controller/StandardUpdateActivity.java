package com.project.innovator.momsrecipe.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.models.Standard;

import java.util.ArrayList;
import java.util.HashMap;

public class StandardUpdateActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //   private final  int GALLERY_CODE=1112;
    final int SELECT_IMAGE = 100;

    private ArrayList<Bitmap> mDataset;
    //key랑 value값 저장(HashMap)
    HashMap<String, String> images = new HashMap<>();
    String standardName;
    Button buttonPhoto;
    Button buttonUpdate;
    EditText editStandard;
    ImageView imageStandard;
    String standardText;
    //image uri 담을 공간
    Uri uri = null;
    ImageView imageInvisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_update);

        buttonPhoto = (Button) findViewById(R.id.btnPhoto);
        editStandard = (EditText) findViewById(R.id.editStandard);
        imageStandard = (ImageView) findViewById(R.id.imageStandard);
        buttonUpdate = (Button) findViewById(R.id.btnUpdate);
        imageInvisible=(ImageView) findViewById(R.id.invisibleImage);


        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                standardText = editStandard.getText().toString();
////                //터치 안먹힘
//                getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (standardText.equals("") || uri == null) {
                    Log.d("tag", "if");
                    Toast.makeText(StandardUpdateActivity.this, "이미지와 기준 모두 작성해주세요.", Toast.LENGTH_SHORT).show();

                } else {

                    //현재 user를 가져오는 부분
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //user의 키를 가져옴. 여기서 한번만 사용 (항상 새로운 키가 반환됨)
                    String key = FirebaseUtils.getStandardAutoKey(user.getUid());

                    Standard standard = new Standard(standardText, user.getUid() + "_" + key + "_1.jpg");
                    FirebaseUtils.addStandardData(user.getUid(), key, standard);

                    final ProgressDialog progressDialog = new ProgressDialog(StandardUpdateActivity.this);
                    progressDialog.setTitle("업로드중");
                    progressDialog.show();

                    //업로드위해.
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    //참조 만드려고.
                    StorageReference storageReference = storage.getReference();

                    UploadTask uploadTask = null;
                    //image 파일명 만들기
                    StorageReference storageReference1 = storageReference.child(user.getUid() + "_" + key + "_1.jpg");
                    uploadTask = storageReference1.putFile(uri);
                    // Standard standard = new Standard(images, standardText);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            progressDialog.dismiss();
                            //업로드실패
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //업로드
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //업로드중
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("업로드 중" + ((int) progress) + "%...");
                        }
                    });
                }
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("태그", "사진");
                try {
                    uri = data.getData();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap resized = BitmapFactory.decodeFile(getPathFromUri(data.getData()), options);
                    imageStandard.setImageBitmap(resized);
                    imageInvisible.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

//    private void sendPickture(Uri imgUri) {
//        String imagePath = getRealPathFromURI(imgUri);
//        ExifInterface exif = null;
//
//        try {
//            exif=new ExifInterface(imagePath);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int exitfOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        int exitfDegree = exifOrientationToDegrees(exifOrientation);
//
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);        //경로통해 비트맵 전환
//        imageStandard.setImageBitmap(rotate(bitmap, exifDegree));         //이미지뷰 비트맵 넣기
//    }
//
//        //사진 정방향대로 회전??
//    private Bitmap rotate(Bitmap src, float degree){
//        Matrix matrix = new Matrix();
//
//        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix,true);
//    }

    //사진 절대경로 구하기
//    private String getRealPathFromURI(Uri contentUri){
//        int column_index=0;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = getContentResolver().query(contentUri, proj,null,null,null);
//        if (cursor.moveToFirst()){
//            column_index=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        }
//        return  cursor.getString(column_index);
//    }

    //사진 절대경로 구하기
    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
    }
}
