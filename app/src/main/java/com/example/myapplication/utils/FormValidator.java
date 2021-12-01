package com.example.myapplication.utils;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;


public abstract class FormValidator extends AppCompatActivity implements Validator.ValidationListener {
    // https://learningprogramming.net/mobile/android/form-validation-in-android/

    protected Validator validator;

    protected FormValidator(){
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    protected void onSubmit(View v){
        validator.validate();
    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
