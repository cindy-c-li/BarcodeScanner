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
import android.widget.Toast;

import com.example.helper.DBConstants;
import com.example.helper.DatabaseHandler;
import com.example.helper.FromActivityEnum;
import com.example.webservice.AmazonPurchaseUtility;
import com.example.webservice.Model;
import com.example.webservice.ServiceType;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Adi on 4/4/2017.
 */

public class ItemListAdapter extends ArrayAdapter<Model> {

    private FromActivityEnum fromView;
    private int resource;
    private LayoutInflater inflater;

    public ItemListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Model> objects) {
        super(context, resource, objects);
        this.resource = resource;
        fromView = FromActivityEnum.MAIN_ACTIVITY;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        final DatabaseHandler mydb = new DatabaseHandler(this.getContext());

        ImageView Pimage = (ImageView) convertView.findViewById(R.id.Pimage);
        TextView Ptype = (TextView) convertView.findViewById(R.id.Ptype);
        TextView Pname = (TextView) convertView.findViewById(R.id.Pname);
        TextView Pid = (TextView) convertView.findViewById(R.id.Pid);
        TextView Pprice = (TextView) convertView.findViewById(R.id.Pprice);
        Button addToCart = (Button) convertView.findViewById(R.id.Paddtocart);
        Button saveOrDeleteButton = (Button) convertView.findViewById(R.id.Psave);
        if (fromView != FromActivityEnum.MAIN_ACTIVITY) {
            saveOrDeleteButton.setText(DBConstants.DELETE_ITEM_BUTTON);
        }

        Ptype.setText("Type: " + getItem(position).getType());
        Pname.setText("Name: " + getItem(position).getName());
        Pid.setText("ID:        " + getItem(position).getItemUPC());
        Pprice.setText("Price:  " + getItem(position).getSalePrice());
        ImageLoader.getInstance().displayImage(getItem(position).getThumbnailImage(), Pimage);
        addToCart.setTag(getItem(position));
        addToCart.setVisibility(View.VISIBLE);
        addToCart.setEnabled(true);

        addToCart.setOnClickListener(new View.OnClickListener() {

                                         @Override
                                         public void onClick(View v) {
                                             v.setEnabled(false);
                                             Model item = (Model) v.getTag();
                                             if (item.getType() == ServiceType.AMAZON) {
                                                 AmazonPurchaseUtility amazonService = new AmazonPurchaseUtility();
                                                 amazonService.setEnvironement(item, v);
                                                 amazonService.execute();
                                             } else {
                                                 String url = item.getItemPurchaseURL();
                                                 Log.d("place order", url);
                                                 Intent i = new Intent(Intent.ACTION_VIEW);
                                                 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                 i.setData(Uri.parse(url));
                                                 v.getContext().startActivity(i);
                                             }
                                         }
                                     }
        );
        saveOrDeleteButton.setOnClickListener(new View.OnClickListener() {

                                                  @Override
                                                  public void onClick(View v) {

                                                      if (fromView != FromActivityEnum.MAIN_ACTIVITY) {
                                                          Model product = getItem(position);
                                                          refresh(product);
                                                          mydb.deleteProduct(product);
                                                          Toast.makeText(getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
                                                      } else {
                                                          try {
                                                              v.setEnabled(false);
                                                              mydb.addItem(getItem(position));
                                                              Toast.makeText(getContext(), "Data saving....", Toast.LENGTH_SHORT).show();
                                                          } catch (Exception e) {
                                                              Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                          }
                                                      }

                                                  }
                                              }
        );


        return convertView;
    }

    public void setFromView(FromActivityEnum fromView) {
        this.fromView = fromView;
    }

    public void refresh(Model product) {
        this.remove(product);
    }
}
