package com.example.dansdistractor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.dansdistractor.databaseSchema.MessageSchema;
import com.example.dansdistractor.message.Locator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ExampleDialog extends AppCompatDialogFragment {

    private EditText message_input;

    // Access to Google Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection("Message");

    private MyApplication user;
    List<Location> myLocations;
    Location myLocation;

    public ExampleDialog(MyApplication user){
        super();
        this.user = user;
        myLocations = user.getMyLocations();
        myLocation = myLocations.get(myLocations.size()-1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        message_input =  view.findViewById(R.id.message_input);

        builder.setView(view)
                .setTitle("Leave a message")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        createMessage();

                        Toast.makeText(getActivity(), "Message has been successfully set in the location " + myLocation.getLatitude()+ ", " + myLocation.getLongitude(), Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }

    private void createMessage() {

        String[] authorNameData = {"Traveler", "Adventurer", "Sightseer", "Voyager", "Wanderer", "Explorer", "Commuter", "Peddler", "Journeyer", "Backpacker", "Straggler"};

        //equal to true when the leave message form contains error(s)
        boolean incompleteForm = false;

        String contentInput = message_input.getText().toString().trim();

        if (contentInput.isEmpty()) {
            message_input.setError("Message is required!");
            message_input.requestFocus();
            incompleteForm = true;
        }

        //terminate the register process when the form is incomplete
        if(incompleteForm) return;

        double lat = myLocation.getLatitude();
        double lon = myLocation.getLongitude();
        String  address;


        Geocoder geocoder = new Geocoder(getActivity());

        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

            address = addresses.get(0).getAddressLine(0);

        }catch (Exception e){
            address = "Unavailable";
        }


        MessageSchema message = new MessageSchema("author", lat, lon, contentInput, address);

        db.collection("Message")
                .add(message)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(@NonNull DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}

