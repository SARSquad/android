package com.sarsquad.sarassist;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.parse.Parse;

/**
 * Created by CHRIS on 7/18/2015.
 */
public class SARAssist extends Application {

    private static Context sContext ;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "SAlhWqn4ERwfsVT7MHjky4PLiURrI7tY1K3xF6Sa", "ZXPXc9OLzOBfQF5zIq78E5ozbJtxnsjd0wkwlQgj");

    }
    public static void makeToastLong( String toast )
    {
        Toast.makeText(sContext, toast, Toast.LENGTH_LONG).show();
    }
    public static void makeToastShort( String toast )
    {
        Toast.makeText( sContext, toast, Toast.LENGTH_LONG ).show();
    }

}
