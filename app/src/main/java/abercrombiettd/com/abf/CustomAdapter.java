package abercrombiettd.com.abf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vineela on 5/12/2016.
 */
public class CustomAdapter extends BaseAdapter {

    Context context;
    Bitmap bitmap;
   ArrayList<Bitmap> bmp=new ArrayList<Bitmap>();
    List<Promotions> promotionlist;
    private static LayoutInflater inflater=null;
    public CustomAdapter(PromotionActivity mainActivity,  List<Promotions> promotionList) {
        // TODO Auto-generated constructor stub
promotionlist=promotionList;
        context=mainActivity;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return promotionlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        RelativeLayout second;
        TextView title;
        TextView desc;
        TextView footer;
        Button button;
        ImageView imageview;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.program_list, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.second= (RelativeLayout) rowView.findViewById(R.id.secondView);
        holder.title= (TextView) rowView.findViewById(R.id.title);
        holder.desc= (TextView) rowView.findViewById(R.id.desc);
        holder.footer= (TextView) rowView.findViewById(R.id.footer);
        holder.imageview= (ImageView) rowView.findViewById(R.id.thumbline);
        holder.button= (Button) rowView.findViewById(R.id.buttons);
        holder.tv.setText(promotionlist.get(position).getButtons().getTitle());
        if (holder.img != null) {
        new ImageDownloaderTask(holder.img).execute(promotionlist.get(position).getImage());}
        //holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Position",""+position);
               // Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
if(holder.second.getVisibility()==View.GONE){
    holder.second.setVisibility(View.VISIBLE);
    if(bmp.get(position)!=null) {
        holder.imageview.setImageBitmap(bmp.get(position));
    }
    holder.title.setText(promotionlist.get(position).getButtons().getTitle());
    holder.desc.setText(promotionlist.get(position).getDescription());
    holder.footer.setText(promotionlist.get(position).getFooter());
    holder.button.setText(promotionlist.get(position).getButtons().getTitle());
    holder.button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String url =promotionlist.get(position).getButtons().getTarget();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    });

}
                else {
    holder.second.setVisibility(View.GONE);
                }
            }
        });
        return rowView;
    }
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.fab_background);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }
    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {

                bitmap = BitmapFactory.decodeStream(inputStream);
bmp.add(bitmap);
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}
