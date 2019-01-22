package com.project.innovator.momsrecipe.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.innovator.momsrecipe.FirebaseUtils;
import com.project.innovator.momsrecipe.R;
import com.project.innovator.momsrecipe.adapter.ImageAdapter;
import com.project.innovator.momsrecipe.models.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeInputActivity extends AppCompatActivity {

    final int SELECT_IMAGE = 100;

    private ImageView imgView;
    private EditText rNameEdt, rIngredientsEdt, rContentsEdt;
    private Button rImgChooseBtn;
    private Button rSaveBtn, rCancelBtn;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Bitmap> mDataset;
    private ArrayList<Uri> mDataPath; //?
    private String foodName, ingredients, contents;
    private FirebaseAuth mAuth;         //login
    private HashMap<String, String> images = new HashMap<>();
    private Uri file[];
    private Activity beforeActivity = RecipeFragment.recipeFragmentActivity;
//    private boolean isUploadFinished = true;


    //private DatabaseReference mDatabase, mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_input);

        mDataset = new ArrayList<>();
        mDataPath = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ImageAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        imgView = (ImageView)findViewById(R.id.imgView);
        rImgChooseBtn = (Button)findViewById(R.id.rImgChooseBtn);
        rImgChooseBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });

        rNameEdt = (EditText)findViewById(R.id.rNameEdt);
        rIngredientsEdt = (EditText)findViewById(R.id.rIngredientsEdt);
        rContentsEdt = (EditText)findViewById(R.id.rContentsEdt);
        rSaveBtn = (Button)findViewById(R.id.rSaveBtn);

        rSaveBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFilled = true;
                foodName = rNameEdt.getText().toString();
                ingredients = rIngredientsEdt.getText().toString();
                contents = rContentsEdt.getText().toString();
                if(foodName.equals("")){
                    isFilled = false;
                    Toast.makeText(getApplicationContext(), "음식명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(ingredients.equals("")){
                    isFilled = false;
                    Toast.makeText(getApplicationContext(), "재료명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

                if(isFilled == true){
                    //saving in RealtimeDatabase
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String recipeKey = FirebaseUtils.getRecipeAutoKey(user.getUid());

                    //사진 안넣었을 때 예외 처리 수정한 부분
                    if(mDataset.size() != 0) {
                        getUri();
                        for (int i = 0; i < mDataPath.size(); i++) {
                            images.put("image" + (i + 1), user.getUid() + "_" + recipeKey + "_" + (i + 1) + "." + getFileExtension(file[i]));
                        }
                    }
                    else{
                        file = null;
                        images.put("image1", "noImage");
                    }

                    Recipe recipe = new Recipe(foodName, ingredients, "", contents, images);
                    FirebaseUtils.addRecipeData(user.getUid(), recipeKey, recipe);

                    //saving in storage
                    uploadFile();

                    //if saved, erase beforeActivity
                    beforeActivity.finish();
                }
            }
        });

        rCancelBtn = (Button)findViewById(R.id.rCancelBtn);
        rCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeInputActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //Upload 버튼 막기 부분(미완성)
        super.onBackPressed();
        /*
        if(isUploadFinished){
            super.onBackPressed();
        }
        */
    }

    private void getUri(){
        file = new Uri[mDataPath.size()];
        for(int i=0; i<mDataPath.size(); i++){
            file[i] = mDataPath.get(i);
        }
    }

    private void uploadFile(){
        if(file != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로딩 중");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            UploadTask uploadTask = null;
            for(int i=0; i<file.length; i++) {
                StorageReference imageRef = storageRef.child(images.get("image" + (i+1)));
                uploadTask = imageRef.putFile(file[i]);
            }
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
//                    isUploadFinished = true;
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
//                    isUploadFinished = true;
                    Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecipeInputActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("업로드 중 " + ((int) progress) + "%...");
//                    isUploadFinished = false;
                }
            });
        } else{
            //no file is selected -> need to display an error
            //수정한 처리 부분
            Toast.makeText(getApplicationContext(), "업로드 완료", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RecipeInputActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_IMAGE){
            if(resultCode == Activity.RESULT_OK){
                try{
                    //Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    String imagePath = getPathFromUri(data.getData());
                    Bitmap resized = BitmapFactory.decodeFile(imagePath, options);
                    //Bitmap resized = BitmapFactory.decodeFile(getPathFromUri(data.getData()), options);

                    //image rotate
                    ExifInterface exif = null;
                    try{
                        exif = new ExifInterface(imagePath);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap bmRotated = rotateBitmap(resized, orientation);

                    //mDataset.add(resized);
                    mDataset.add(bmRotated);
                    mAdapter.notifyDataSetChanged();
                    //Log.v("태그", "Uri()=" + data.getData());
                    mDataPath.add(data.getData());
                    //Log.v("태그", "path()=" + getPathFromUri(data.getData()));

                    //imgView.setImageBitmap(image_bitmap);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPathFromUri(Uri uri){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null );
        cursor.moveToNext();
        String path = cursor.getString( cursor.getColumnIndex( "_data" ) );
        cursor.close();

        return path;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation){
        Matrix matrix = new Matrix();
        switch (orientation){
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try{
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();   //메모리 반환
            return bmRotated;
        } catch (OutOfMemoryError e){
            e.printStackTrace();
            return null;
        }
    }
}
