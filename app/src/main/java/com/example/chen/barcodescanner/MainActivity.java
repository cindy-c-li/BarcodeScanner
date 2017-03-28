package com.example.chen.barcodescanner;
//

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView barcodeResult;
    SurfaceView cameraView;
    ArrayAdapter arrayAdapter;


    public static final String address = "http://api.walmartlabs.com/v1/items?apiKey=";
    public static final String walmartApikey = "kgf35974z93mq9knncwprhkc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcodeResult = (ListView) findViewById(R.id.barcode_result);
        arrayAdapter = new ArrayAdapter<String>(barcodeResult.getContext(), android.R.layout.simple_list_item_1);

    }

    public void checkPrice(String barcodeValue) {
        //EditText barcode = (EditText) findViewById(R.id.barcode);
        //String inputBarcode = barcode.getText().toString();
        //"035000521019"

        final String query = String.format("%s%s&upc=%s", address, walmartApikey, barcodeValue);

        //TextView textView = (TextView) findViewById(R.id.textView);

        RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask(query, barcodeResult, arrayAdapter);
        retrieveFeedTask.execute();


    }



    public void scanBarcode(View view) {
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
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




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
