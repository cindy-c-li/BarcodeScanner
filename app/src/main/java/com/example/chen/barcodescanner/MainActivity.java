package com.example.chen.barcodescanner;
//

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ListView barcodeResult;
    SurfaceView cameraView;
    ArrayAdapter arrayAdapter;
    Button buyButton;


    public static final String address = "http://api.walmartlabs.com/v1/items?apiKey=";
    public static final String walmartApikey = "kgf35974z93mq9knncwprhkc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        barcodeResult = (ListView) findViewById(R.id.barcode_result);
        arrayAdapter = new ArrayAdapter<String>(barcodeResult.getContext(), android.R.layout.simple_list_item_1);
        buyButton = (Button)findViewById(R.id.walmart_buy);
    }

    public void checkPrice(String barcodeValue) {
        //EditText barcode = (EditText) findViewById(R.id.barcode);
        //String inputBarcode = barcode.getText().toString();
        //"035000521019"

        final String query = String.format("%s%s&upc=%s", address, walmartApikey, barcodeValue);

        //TextView textView = (TextView) findViewById(R.id.textView);

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(query, barcodeResult, arrayAdapter, buyButton);
        retrieveFeedTask.execute();


    }



    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    public void placeOrder(View view){
        buyButton.setEnabled(false);
        String url = (String)barcodeResult.getTag();
        Log.d("place order", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    //barcodeResult.setText("Barcode value: " + barcode.displayValue);
                    checkPrice(barcode.displayValue);

                }else {
                    ArrayList<String> list = new ArrayList<String>();
                    list.add("No barcode found");
                    arrayAdapter.clear();
                    arrayAdapter.addAll(list);
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
