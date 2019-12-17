package com.updesigns.moneycircle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class CreateAccount extends AppCompatActivity {
    CallbackManager callbackManager,callbackManager1;
    ProgressDialog mDialog;
    EditText phone, balance, pass, cpass, uName;
    String Phone, bal, Pass, CPass, UName, chkUname;
    List<Circles> joinCircle;
    List<Transaction> transactionList;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initialize();                                   //Initializing all the variables and views and Managers...

        //The following Code handles the click made on "Continue with Facebook" Button....
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //Get Value of each and every text box
                        Pass = pass.getText().toString();
                        CPass = cpass.getText().toString();
                        bal = balance.getText().toString();
                        Phone = phone.getText().toString();
                        UName = uName.getText().toString();
                        try {
                            chkUname = MainActivity.database.MyDao().getUsername(UName); //check if usuername is taken.. returns null if available
                        }
                        catch (Exception e){
                            Toast.makeText(CreateAccount.this,"Hello",Toast.LENGTH_LONG).show();
                        }

                        if(Pass.equals("") && CPass.equals("") && bal.equals("") && Phone.equals("")){ //Check if any field is left empty
                            LoginManager.getInstance().logOut();
                            Toast.makeText(CreateAccount.this, "ALL FIELDS ARE MANDATORY",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(chkUname==null) {
                                if (Pass.equals(CPass)) {
                                    mDialog = new ProgressDialog(CreateAccount.this);
                                    mDialog.setMessage("Retrieving Data....");
                                    mDialog.show();      //A loading type dialog is shown
                                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            //this code runs if login was successful.
                                            mDialog.dismiss();
//                                            Log.d("response", response.toString());
                                            getData(object);   //call to method where creating account is handled
                                        }
                                    });
                                    Bundle parameters = new Bundle();
                                    parameters.putString("fields", "id,email,birthday,friends,name"); //These values are requested from facebook...
                                    request.setParameters(parameters);
                                    request.executeAsync();
                                } else {
                                    LoginManager.getInstance().logOut();
                                    Toast.makeText(CreateAccount.this, "PASSWORDS DO NOT MATCH", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                LoginManager.getInstance().logOut();
                                Toast.makeText(CreateAccount.this,"USERNAME IN USE", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(CreateAccount.this,"Cancel",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(CreateAccount.this,exception.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getData(JSONObject object) {
        try {
            joinCircle = new ArrayList<Circles>();
            transactionList = new ArrayList<Transaction>();                 //Both lists are initialized

            //All values are set to their respective fields in user.class
            //Fb id and name is taken from facebook
            //***You will need to set user.points = 0 here for gamification since at the account creation points are zero...
            //***Set points = 0 for the test users as well
            //---When you will be linking with a bank account, you will need to get all the information required for linking on this page
            //---or if the info is alot you can create another activity right here in which you get all the bank details from the user...
            user.setFb_user_id(object.getString("id"));
            user.setBank_account("123123");
            user.setName_user(object.getString("name"));
            user.setPassword(Pass);
            user.setUser_name(UName);
            user.setBalance(Long.parseLong(bal));
            user.setJoinedCircles(0);
            user.setJoinCircleList(joinCircle);
            user.setTransactionList(transactionList);

            //Two more Users created (if not previously created) for testing purposes
            User use = new User();
            if(MainActivity.database.MyDao().getUsername("munamomer") == null) {
                use.setFb_user_id("12314241");
                use.setBank_account("123123");
                use.setName_user("Munam Omer");
                use.setPassword("123");
                use.setUser_name("munamomer");
                use.setBalance(5000);
                use.setJoinedCircles(0);
                use.setJoinCircleList(joinCircle);
                use.setTransactionList(transactionList);
                MainActivity.database.MyDao().addUser(use);
            }
            if(MainActivity.database.MyDao().getUsername("omer_munam") == null){
                use.setFb_user_id("14234");
                use.setBank_account("123123");
                use.setName_user("ORSERS");
                use.setPassword("123");
                use.setUser_name("omer_munam");
                use.setBalance(5000);
                use.setJoinedCircles(0);
                use.setJoinCircleList(joinCircle);
                use.setTransactionList(transactionList);
                MainActivity.database.MyDao().addUser(use);
            }
            if(MainActivity.database.MyDao().getFbID(object.getString("id"))== null){       //check if fb id is already linked
                try {
                    MainActivity.database.MyDao().addUser(user);            //Add the user to database
                    Toast.makeText(CreateAccount.this,"ACCOUNT CREATION SUCCESSFUL", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(CreateAccount.this,LoginActivity.class);
                    startActivity(intent);              //get back to login screen
                }
                catch (Exception e){
                    LoginManager.getInstance().logOut();
                    Toast.makeText(this,"Failed", Toast.LENGTH_LONG).show();
                }
            }
            else {
                LoginManager.getInstance().logOut();
                Toast.makeText(this,"This Facebook Account is Already Linked", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialize(){
        callbackManager = CallbackManager.Factory.create();
        callbackManager1 = CallbackManager.Factory.create();
        phone = (EditText) findViewById(R.id.numberPhone);
        pass = (EditText) findViewById(R.id.password);
        cpass = (EditText) findViewById(R.id.cpass);
        balance = (EditText) findViewById(R.id.balance);
        uName = (EditText) findViewById(R.id.uName);
        user = new User();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
