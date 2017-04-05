package com.example.chen.barcodescanner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.barcodescanner.model.Model;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Adi on 4/4/2017.
 */

public class ItemListAdapter extends ArrayAdapter<Model> {

    private List<Model> objectsModelList;
    private int resource;
    private LayoutInflater inflater;

    public ItemListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Model> objects) {
        super(context, resource, objects);

        objectsModelList = objects;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }


    public ItemListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }

        ImageView Pimage = (ImageView) convertView.findViewById(R.id.Pimage);
        TextView Pname = (TextView) convertView.findViewById(R.id.Pname);
        TextView Pid = (TextView) convertView.findViewById(R.id.Pid);
        TextView Pprice = (TextView) convertView.findViewById(R.id.Pprice);
        Button addToCart = (Button) convertView.findViewById(R.id.Paddtocart);

        if (objectsModelList != null && !objectsModelList.isEmpty()) {
            Pname.setText(objectsModelList.get(position).getName());
            Pid.setText("" + objectsModelList.get(position).getItemId());
            Pprice.setText(objectsModelList.get(position).getSalePrice());
            ImageLoader.getInstance().displayImage(objectsModelList.get(position).getThumbnailImage(), Pimage);
            addToCart.setTag(objectsModelList.get(position).getItemPurchaseURL());
            addToCart.setVisibility(View.VISIBLE);
            addToCart.setEnabled(true);
        }

        addToCart.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {
                                             v.setEnabled(false);
                                             String url = (String) v.getTag();
                                             Log.d("place order", url);
                                             Intent i = new Intent(Intent.ACTION_VIEW);
                                             i.setData(Uri.parse(url));
                                             v.getContext().startActivity(i);
                                         }
                                     }

        );

        return convertView;


    }
}