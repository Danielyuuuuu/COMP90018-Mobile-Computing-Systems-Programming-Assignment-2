package com.example.dansdistractor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;

public class ShowMessageDialog extends AppCompatDialogFragment {

    private TextView txt_author;
    private TextView txt_message;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_show_message_dialog, null);

        txt_author = view.findViewById(R.id.txt_author);
        txt_message = view.findViewById(R.id.txt_message);

        txt_author.setText(getArguments().getString("messageAuthor"));
        txt_message.setText(getArguments().getString("messageContent"));


        builder.setView(view)
            .setTitle("Message Board")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        return builder.create();
    }

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    static ShowMessageDialog newInstance(String author, String message) {
        ShowMessageDialog f = new ShowMessageDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("messageAuthor", author);
        args.putString("messageContent", message);
        f.setArguments(args);

        return f;
    }
}
