package com.github.tntim96.awsinaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.Toast;
import com.amazonaws.mobileconnectors.lambdainvoker.*;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
      }
    });


    executeLambda();
  }


  private void executeLambda() {
    // Create an instance of CognitoCachingCredentialsProvider
    CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
        this.getApplicationContext(), "ap-southeast-2:18ecdecb-0a8f-4362-8416-072ae129589a", Regions.AP_SOUTHEAST_2);

// Create LambdaInvokerFactory, to be used to instantiate the Lambda proxy.
    LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
        Regions.AP_SOUTHEAST_2, cognitoProvider);

// Create the Lambda proxy object with a default Json data binder.
// You can provide your own data binder by implementing
// LambdaDataBinder.
    final AWSLambda myInterface = factory.build(AWSLambda.class);

    RequestClass request = new RequestClass("Rusty");
// The Lambda function invocation results in a network call.
// Make sure it is not called from the main thread.
    new AsyncTask<RequestClass, Void, String>() {
      @Override
      protected String doInBackground(RequestClass... params) {
        // invoke "echo" method. In case it fails, it will throw a
        // LambdaFunctionException.
        try {
          return myInterface.greetingOnDemand(params[0]);
        } catch (LambdaFunctionException lfe) {
          //Log.e("Tag", "Failed to invoke echo", lfe);
          return null;
        }
      }

      @Override
      protected void onPostExecute(String result) {
        if (result == null) {
          return;
        }

        // Do a toast
        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
      }
    }.execute(request);
  }

}
