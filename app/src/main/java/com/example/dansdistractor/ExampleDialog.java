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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.dansdistractor.databaseSchema.MessageSchema;
import com.example.dansdistractor.message.Locator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ExampleDialog extends AppCompatDialogFragment {

    private EditText message_input;
    private TextView textView_location;
    private TextView textView_address;
    private TextView textView_author;

    private String author, address;
    private Double lat, lon;


    // Access to Google Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MyApplication user;
    private List<Location> myLocations;
    private Location myLocation;

    public ExampleDialog(MyApplication user){
        super();
        this.user = user;
        myLocations = user.getMyLocations();
        myLocation = myLocations.get(myLocations.size()-1);
        this.author =  getRandomAuthorName();
        this.lat = myLocation.getLatitude();
        this.lon = myLocation.getLongitude();
    }

    private String getCurrentAddress(Double lat, Double lon) {
        Geocoder geocoder = new Geocoder(getActivity());

        String result = "Unavailable";

        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

            result = addresses.get(0).getAddressLine(0);

        }catch (Exception e){
            result = "Unavailable";
        }

        return result;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        message_input =  view.findViewById(R.id.message_input);
        textView_location =  view.findViewById(R.id.textView_location);
        textView_address =  view.findViewById(R.id.textView_address);
        textView_author =  view.findViewById(R.id.textView_author);

        address = getCurrentAddress(lat, lon);


        textView_location.setText(String.valueOf(lat+ ", " + lon));
        textView_address.setText(String.valueOf(address));
        textView_author.setText(String.valueOf(author));

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

                        if(!createMessage()){
                            Toast.makeText(getActivity(), "Fail to create message with empty content", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Message has been successfully set in the location " + lat+ ", " + lon, Toast.LENGTH_LONG).show();
                        }

                    }
                });
        return builder.create();
    }

    private Boolean createMessage() {

        //equal to true when the leave message form contains error(s)
        boolean incompleteForm = false;

        String contentInput = message_input.getText().toString().trim();

        if (contentInput.isEmpty()) {
            message_input.setError("Message is required!");
            message_input.requestFocus();
            incompleteForm = true;
        }

        //terminate the register process when the form is incomplete
        if(incompleteForm) return false;


        MessageSchema message = new MessageSchema(author, lat, lon, contentInput, address);

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

        return true;
    }

    private String getRandomAuthorName(){

        String[] authorNameData = {"Traveler", "Adventurer", "Sightseer", "Voyager", "Wanderer", "Explorer", "Commuter", "Peddler", "Journeyer", "Backpacker", "Straggler"};
        String[] authorAdjData = {"Adventurous", "Affectionate", "Ambitious", "Amiable", "Compassionate", "Courageous", "Courteous", "Diligent", "Generous", "Gregarious", "Impartial", "Passionate", "Witty"};

        int rnd = new Random().nextInt(authorNameData.length);
        int rnd2 = new Random().nextInt(authorNameData.length);

        return authorAdjData[rnd2] + " " + authorNameData[rnd];
    }

}

