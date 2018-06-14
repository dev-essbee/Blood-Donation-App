package com.dev.sb.blooddonationapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;

public class ChangeEmailDialogFragment extends DialogFragment {

    private TextInputEditText mPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.layout_del_acc_dialog, null))
                .setTitle(R.string.change_email)
                .setMessage(R.string.change_email_txt)
                .setNeutralButton(getResources().getString(R.string.label_forgot_password_q), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), ResetPasswordActivity.class)
                                .putExtra("activity", "MainActivity"));
                        getActivity().finish();
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPassword = getDialog().findViewById(R.id.input_pass_del);
                        if(!TextUtils.isEmpty(mPassword.getText().toString().trim())){
                        onDialogPositiveClick(mPassword.getText().toString().trim());}
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private void onDialogPositiveClick(String pass) {
        Intent i = new Intent().putExtra("del", true);
        i.putExtra("pass", pass);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        dismiss();

    }
}
