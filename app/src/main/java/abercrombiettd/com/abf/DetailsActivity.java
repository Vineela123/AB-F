package abercrombiettd.com.abf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_details);
        Intent i = getIntent();

        byte[] byteArray = i.getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        final Promotions promos = (Promotions)i.getSerializableExtra("Object");
        ImageView image= (ImageView) findViewById(R.id.thumbline);
        TextView title= (TextView) findViewById(R.id.title);
        TextView desc= (TextView) findViewById(R.id.desc);
        TextView footer= (TextView) findViewById(R.id.footer);
        Button button= (Button) findViewById(R.id.buttons);
        image.setImageBitmap(bmp);
        title.setText(promos.getButtons().getTitle());
        desc.setText(promos.getDescription());
        footer.setText(promos.getFooter());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url =promos.buttons.getTarget();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

}
