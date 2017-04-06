package com.example.helper;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignedRequestsHelper {

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String REQUEST_URI = "/onca/xml";
    private static final String REQUEST_METHOD = "GET";

    private String endpoint = "webservices.amazon.com"; // must be lowercase
    private String awsAccessKeyId = "AKIAI73FTWLBDXMU5MNQ";
    private String awsSecretKey = "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";

    private SecretKeySpec secretKeySpec = null;
    private Mac mac = null;

    public SignedRequestsHelper() {
        byte[] secretyKeyBytes = null;
        try {
            secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        secretKeySpec =
                new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
        try {
            mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            mac.init(secretKeySpec);
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static JSONObject sendRequest(Map<String, String> params) {
        // TODO Auto-generated method stub

        SignedRequestsHelper helper = new SignedRequestsHelper();
        URL url = null;
        try {
            url = new URL(helper.sign(params));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(stringBuilder);

        JSONObject object = null;
        try {
            object = XML.toJSONObject(stringBuilder.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String sign(Map<String, String> params) {
        params.put("AWSAccessKeyId", awsAccessKeyId);
        params.put("Timestamp", timestamp());

        SortedMap<String, String> sortedParamMap =
                new TreeMap<String, String>(params);
        String canonicalQS = canonicalize(sortedParamMap);
        String toSign =
                REQUEST_METHOD + "\n"
                        + endpoint + "\n"
                        + REQUEST_URI + "\n"
                        + canonicalQS;

        String hmac = hmac(toSign);
        String sig = percentEncodeRfc3986(hmac);
        String url = "http://" + endpoint + REQUEST_URI + "?" +
                canonicalQS + "&Signature=" + sig;

        return url;
    }

    private String hmac(String stringToSign) {
        String signature = null;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(UTF8_CHARSET);
            rawHmac = mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
        }
        return signature;
    }

    private String timestamp() {
        String timestamp = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }

    private String canonicalize(SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter =
                sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        String canonical = buffer.toString();
        return canonical;
    }

    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8_CHARSET)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

}
