package com.dev.sb.blooddonationapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.io.IOException;
import java.net.URLEncoder;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    private LinearLayout mCall, mMsg, mMail, mShare;

    public BottomSheetFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.bottom_sheet_actions, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mCall = getView().findViewById(R.id.button_call);
        mMsg = getView().findViewById(R.id.button_msg);
        mMail = getView().findViewById(R.id.button_email);
        mShare = getView().findViewById(R.id.button_share);
//TODO: Display bottom list using recyclerview
        mCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent();
                callIntent.setAction(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "123456789"));
                if (callIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(callIntent);
                }
            }
        });
        mMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "", msg = "";
                try {
                    msg = "Hi jfkdj";
                    url = "https://api.whatsapp.com/send?phone=" + "917728891621" + "&text=" +
                            URLEncoder.encode(msg, "UTF-8");
                } catch (Exception e) {
                    Log.e("Message", e.toString());
                }
                Intent msgIntent = new Intent(Intent.ACTION_VIEW);
                msgIntent.setPackage("com.whatsapp");
                msgIntent.setData(Uri.parse(url));
                if (msgIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(msgIntent);
                }
            }
        });
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "text to be shared");
                shareIntent.setType("text/plain");
                String title = getResources().getString(R.string.share_chooser_title);
                Intent chooser = Intent.createChooser(shareIntent, title);
                if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        });
        mMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] emailId = new String[]{"dev.sb18@gmaill.com"};
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO);
                mailIntent.setData(Uri.parse("mailto:"));
                mailIntent.putExtra(Intent.EXTRA_EMAIL, emailId);
                mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi");
                mailIntent.putExtra(Intent.EXTRA_TEXT, "this is the body of the email");
                if (mailIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mailIntent);
                }
            }
        });

    }
}
