package com.project.innovator.momsrecipe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.project.innovator.momsrecipe.models.Recipe;
import com.project.innovator.momsrecipe.models.Standard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseUtils {

    private static String TAG = FirebaseUtils.class.getName();

    private static final String baseKey = "users";
    private static final String standardKey = "standard";
    private static final String recipeKey = "recipe";
    private static final String refrigeratorKey = "refrigerator";
    private static final long defaultImageSize = 1024 * 1024 * 5;

    public interface RecipeCallback {
        public void getRecipeDataCallback(List<Recipe> recipes);
        public void cancelledCallback(DatabaseError error);
    }

    public interface StandardCallback {
        public void getStandardDataCallback(List<Standard> standards);
        public void cancelledCallback(DatabaseError error);
    }

    public interface ImageCallback {
        public void getImageDataCallback(boolean success, Bitmap bitmap);
    }

    public static FirebaseDatabase getDatabase(){
        return FirebaseDatabase.getInstance();
    }

    public static DatabaseReference getDBRootReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getDBBaseRerference() {
        return getDBRootReference().child(baseKey);
    }

    public static DatabaseReference getDBUIDReference(String uid) {
        return getDBBaseRerference().child(uid);
    }

    public static DatabaseReference getDBStandardReference(String uid) {
        return getDBUIDReference(uid).child(standardKey);
    }

    public static DatabaseReference getDBRecipeReference(String uid) {
        return getDBUIDReference(uid).child(recipeKey);
    }

    public static DatabaseReference getDBRefrigeratorReference(String uid) {
        return getDBUIDReference(uid).child(refrigeratorKey);
    }

    public static DatabaseReference getDBTargetChildReference(DatabaseReference reference, String target) {
        return reference.child(target);
    }

    private static DatabaseReference getDBTargetChildReference(DatabaseReference reference, List<String> childs) {
        if(reference == null &&(childs.isEmpty() || childs == null))
            return reference;
        else {
            DatabaseReference r = getDBTargetChildReference(reference, childs.get(0));
            childs.remove(0);
            return getDBTargetChildReference(r, childs);
        }
    }

    public static DatabaseReference getDBTargetReferenceWithList(DatabaseReference reference, List<String> childs) {
        DatabaseReference resultReference = null;
        if (!childs.isEmpty() && childs != null) {
            if(reference == null) {
                resultReference = getDBTargetChildReference(getDBRootReference(), childs);
            }
            else {
                resultReference = getDBTargetChildReference(reference, childs);
            }
        }
        return resultReference;
    }

    public static String getStandardAutoKey(String uid) {
        return getDBStandardReference(uid).push().getKey();
    }

    public static String getRecipeAutoKey(String uid) {
        return getDBRecipeReference(uid).push().getKey();
    }

    public static void addStandardData(String uid, String key, Standard standard) {
        Map<String, Object> standardData = standard.toMap();
        getDBStandardReference(uid).child(key).updateChildren(standardData);
    }

    public static void addRecipeData(String uid, String key, Recipe recipe) {
        Map<String, Object> recipeData = recipe.toMap();
        getDBRecipeReference(uid).child(key).updateChildren(recipeData);
    }

//    public static void addMaterialData(String uid, String key, ) {
//
//    }


    public static void getAllStandard(String uid, final StandardCallback standardCallback) {
        getDBStandardReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Standard> result = null;
                if(dataSnapshot.hasChildren()) {
                    result = new ArrayList<>();
                    for(DataSnapshot target : dataSnapshot.getChildren()) {
                        Standard standard = target.getValue(Standard.class);
                        result.add(standard);
                    }
                }
                standardCallback.getStandardDataCallback(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                standardCallback.cancelledCallback(databaseError);
            }
        });
    }

    public static void getAllRecipe(String uid, final RecipeCallback recipeCallback) {
        getDBRecipeReference(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> result = null;
                if(dataSnapshot.hasChildren()) {
                    result = new ArrayList<>();
                    for (DataSnapshot target : dataSnapshot.getChildren()) {
                        Recipe recipe = target.getValue(Recipe.class);
                        result.add(recipe);
                    }
                }
                recipeCallback.getRecipeDataCallback(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                recipeCallback.cancelledCallback(databaseError);
            }
        });
    }

    public static void getFindRecipe(String uid, final String findString, final RecipeCallback recipeCallback) {
        getDBRecipeReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Recipe> result = null;
                if (dataSnapshot.hasChildren()) {
                    result = new ArrayList<>();
                    for (DataSnapshot target : dataSnapshot.getChildren()) {
                        Recipe recipe = target.getValue(Recipe.class);
                        if(findTargetString(recipe, findString))
                            result.add(recipe);
                    }
                }
                recipeCallback.getRecipeDataCallback(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                recipeCallback.cancelledCallback(databaseError);
            }
        });
    }

    private static boolean findTargetString(Recipe recipe, String targetString) {
        if (recipe.getName().contains(targetString)
                || recipe.getIngredients().contains(targetString))
            return true;
        else
            return false;

    }

    public static void updateRecipeData(final String uid, final Recipe oldRecipe, final Recipe newRecipe) {
        getDBRecipeReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    String targetKey = null;
                    for(DataSnapshot target : dataSnapshot.getChildren()) {
                        Recipe recipe = target.getValue(Recipe.class);
                        if(oldRecipe.equals(recipe)) {
                            targetKey = target.getKey();
                        }
                    }
                    if (targetKey != null) {
                        addRecipeData(uid, targetKey, newRecipe);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void updateStandardData(final String uid, final Standard oldStandard, final Standard newStandard) {
        getDBStandardReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    String targetKey = null;
                    for(DataSnapshot target : dataSnapshot.getChildren()) {
                        Standard standard = target.getValue(Standard.class);
                        if(oldStandard.equals(standard)) {
                            targetKey = target.getKey();
                        }
                    }
                    if (targetKey != null) {
                        addStandardData(uid, targetKey, newStandard);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getTargetBitmap(String fileName, ImageCallback imageCallback) {
        getTargetBitmap(fileName, defaultImageSize, imageCallback);
    }

    public static void getTargetBitmap(String fileName, long size, final ImageCallback imageCallback) {
        Log.i(TAG, "fileName : " + fileName);
        FirebaseStorage.getInstance().getReference().child(fileName).getBytes(size)
                .addOnCompleteListener(new OnCompleteListener<byte[]>() {
                    @Override
                    public void onComplete(@NonNull Task<byte[]> task) {
                        if(task.isSuccessful()) {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize = 4;
                            Bitmap bitmap = BitmapFactory.decodeByteArray(task.getResult(), 0, task.getResult().length, options);
                            imageCallback.getImageDataCallback(true, bitmap);
                        }
                        else {
                            imageCallback.getImageDataCallback(false, null);
                        }
                    }
                });
    }

    public static void removeRecipeData(final String uid, final Recipe removeRecipe) {
        getDBRecipeReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    String targetKey = null;
                    for(DataSnapshot target : dataSnapshot.getChildren()) {
                        Recipe recipe = target.getValue(Recipe.class);
                        if(removeRecipe.equals(recipe)) {
                            targetKey = target.getKey();
                        }
                    }
                    if (targetKey != null) {
                        getDBRecipeReference(uid).child(targetKey).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public static void removeStandardData(final String uid, final Standard removeStandard) {
        getDBStandardReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {
                    String targetKey = null;
                    for(DataSnapshot target : dataSnapshot.getChildren()) {
                        Standard standard = target.getValue(Standard.class);
                        if(removeStandard.equals(standard)) {
                            targetKey = target.getKey();
                        }
                    }
                    if (targetKey != null) {
                        getDBStandardReference(uid).child(targetKey).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}