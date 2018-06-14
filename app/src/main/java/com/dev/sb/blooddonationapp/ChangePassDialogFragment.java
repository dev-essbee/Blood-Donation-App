package com.dev.sb.blooddonationapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassDialogFragment extends DialogFragment {
    private TextInputEditText mNewPassword;
    private TextInputEditText mOldPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.layout_change_pass_dialog, null))
                .setTitle(getResources().getString(R.string.change_password))
                .setNeutralButton(getResources().getString(R.string.label_forgot_password_q), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), ResetPasswordActivity.class).putExtra("activity", "MainActivity"));
                        getActivity().finish();
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNewPassword = getDialog().findViewById(R.id.input_new_pass);
                        mOldPassword = getDialog().findViewById(R.id.input_old_pass);
                        if (!TextUtils.isEmpty(mNewPassword.getText().toString().trim()) && !TextUtils
                                .isEmpty
                                (mOldPassword.getText().toString().trim())){
                            final String newPass = mNewPassword.getText().toString().trim();
                            final String oldPass = mOldPassword.getText().toString().trim();


                            Intent i = new Intent().putExtra("pass", newPass);
                            i.putExtra("oldPass", oldPass);

                            getTargetFragment().onActivityResult(getTargetRequestCode(),
                                    Activity.RESULT_OK, i);
                            dismiss();

                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }



}
