package com.example.chen.barcodescanner;
//

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;

import com.example.webservice.RetrieveFeedTask;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MainActivity extends AppCompatActivity {

    public static final String address = "http://api.walmartlabs.com/v1/items?apiKey=";
    public static final String walmartApikey = "kgf35974z93mq9knncwprhkc";
    ListView barcodeResult;
    SurfaceView cameraView;
    //DatabaseHandler mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        barcodeResult = (ListView) findViewById(R.id.barcode_result);
        DisplayImageOptions defaultoptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        // mydb = new DatabaseHandler(this);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(defaultoptions).build();
        ImageLoader.getInstance().init(config);
    }

    public void checkPrice(String barcodeValue) {
        //EditText barcode = (EditText) findViewById(R.id.barcode);
        //String inputBarcode = barcode.getText().toString();
        //"035000521019"

        //final String query = String.format("%s%s&upc=%s", address, walmartApikey, barcodeValue);

        //TextView textView = (TextView) findViewById(R.id.textView);

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(getApplicationContext(), barcodeValue, barcodeResult);
        retrieveFeedTask.execute();


    }
    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    public void AmazonBag(View view) {
        Intent intent = new Intent(this, AmazonBag.class);
        startActivity(intent);
    }

    public void WalmartBag(View view) {
        Intent intent = new Intent(this, WalmartBag.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    //barcodeResult.setText("Barcode value: " + barcode.displayValue);
                    checkPrice(barcode.displayValue);

                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
/*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
     //   android.app.FragmentManager fragmentManager = new android.app.FragmentManager() {

        return false;
    }
    */
}
