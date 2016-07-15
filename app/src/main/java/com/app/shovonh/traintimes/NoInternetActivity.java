package com.app.shovonh.traintimes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        Button retry = (Button) findViewById(R.id.retry_button);
        if (retry != null) {
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Utilities.hasConnection(view.getContext())) {
                        Intent intent = new Intent(view.getContext(), TopTrainsActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Snackbar.make(view, "Still no connection.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
