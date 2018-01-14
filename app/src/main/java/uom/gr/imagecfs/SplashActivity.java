package uom.gr.imagecfs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uom.gr.imagecfs.StartActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }
}
