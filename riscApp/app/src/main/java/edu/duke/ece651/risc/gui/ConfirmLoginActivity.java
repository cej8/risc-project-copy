package edu.duke.ece651.risc.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.OrderInterface;

public class ConfirmLoginActivity extends AppCompatActivity {
    ExecuteClient executeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_login);
       // ParentActivity parentActivity = new ParentActivity();
       // parentActivity.generateOrders();
        executeClient = new ExecuteClient(this);
        executeClient.createGame();
    }

    public void login(View view){
//        executeClient.registrationAlert(true);
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
   }
    public void register(View view){
      //  executeClient.registrationAlert(false);
        Intent regIntent = new Intent(this,RegisterActivity.class);
        startActivity(regIntent);
    }
}
