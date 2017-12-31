package uom.gr.imagecfs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;


public class ImageFullScreen extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = FetchResponseTask.bitmap;

        TouchImageView img = new TouchImageView(this);
        img.setImageBitmap(bitmap);
        img.setMaxZoom(4f);
        setContentView(img);

    }

}