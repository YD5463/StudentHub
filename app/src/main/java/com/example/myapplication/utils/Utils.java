package com.example.myapplication.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;

import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.util.List;

public class Utils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * checks if device is connected to internet(There is a possibility it's
     * connected to a network but not to internet).
     * @return bool isInternetAvailable
     */
    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }
    public static void onValidationFailed(List<ValidationError> errors,Context context){
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(context);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }
    }
    public static ProgressDialog createProgressDialog(Context context){
        ProgressDialog mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.show();
        return mDialog;
    }
    public static Intent createImageChooserIntent(){
        final String TYPE = "image/*";
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType(TYPE);
        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,TYPE);
        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
        return chooserIntent;
    }

    public static Bitmap imageViewToBitmap(ImageView image){
        Drawable drawable = image.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static byte[] before_upload(ImageView image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = imageViewToBitmap(image);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
