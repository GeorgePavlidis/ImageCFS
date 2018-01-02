package uom.gr.imagecfs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;


public class ImageFullScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bitmap bitmap = FetchResponseTask.bitmap;
        TouchImageView img = new TouchImageView(this);

        if(Uri.parse((String) getIntent().getSerializableExtra("image"))==null){
            img.setImageBitmap(bitmap);
        }else{
            img.setImageURI(Uri.parse((String) getIntent().getSerializableExtra("image")));
        }
        img.setMaxZoom(4f);
        setContentView(img);

    }

}