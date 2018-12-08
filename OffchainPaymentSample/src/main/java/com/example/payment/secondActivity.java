package com.example.payment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class secondActivity extends AppCompatActivity {
    private static boolean canPass = false;
    TextView myAddress;
    String myAddressString;
    TextView recipientAddress;
    View.OnClickListener buttonListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.pay:
                    if(canPass){
                        Intent intent = new Intent(secondActivity.this, thirdActivity.class);
                        intent.putExtra("recipientAddress", recipientAddress.getText().toString());
                        startActivity(intent);
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),"Please Follow Step by Step",Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    break;
                case R.id.copyButton:
                    Log.d("sundaTest", "one");
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    Log.d("sundaTest", "two");
                    ClipData clip = ClipData.newPlainText("myAddress", myAddressString);
                    clipboard.setPrimaryClip(clip);
                    Toast toast = Toast.makeText(getApplicationContext(), "Copying \" "+myAddressString+"\"",Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("sundaTest", "three");
                    break;
                case R.id.checkAvailableButton:
                    if(recipientAddress.getText().toString().equals("")){
                        Toast toast2 = Toast.makeText(getApplicationContext(),"Please enter Recipient Address",Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                    else{
                        Log.d("celerRecip", CelerClientAPIHelper.verifyReceiver(recipientAddress.getText().toString()));
                        if(CelerClientAPIHelper.verifyReceiver(recipientAddress.getText().toString()).equals("0")){
                            Toast toast2 = Toast.makeText(getApplicationContext(),"wait recipient to be online",Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                        else{
                            Toast toast2 = Toast.makeText(getApplicationContext(),"Recipient online",Toast.LENGTH_SHORT);
                            toast2.show();
                            canPass = true;
                        }
                    }
                    break;
            }



        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        String message = intent.getStringExtra("myAddress");
        myAddress = findViewById(R.id.myAddress);
        myAddress.setText(message);
        myAddressString = message;
        recipientAddress = findViewById(R.id.recipientAddress);
        Button buttonSendPayment = findViewById(R.id.pay);
        Button copyButton = findViewById(R.id.copyButton);
        Button checkAvailableButton = findViewById(R.id.checkAvailableButton);

        buttonSendPayment.setOnClickListener(buttonListener);
        copyButton.setOnClickListener(buttonListener);
        checkAvailableButton.setOnClickListener(buttonListener);
    }
}
