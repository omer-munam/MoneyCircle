package com.updesigns.moneycircle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//This class is called in background at defined intervals to perform transactions of circles.. This will run even when app is closed...

public class Transfer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = new MainActivity();
        activity.performTransac(context);       //the function inside MainActivity which handles the transaction is called
    }

}
