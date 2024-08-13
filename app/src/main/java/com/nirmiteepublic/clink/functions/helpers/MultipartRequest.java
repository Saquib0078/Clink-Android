package com.nirmiteepublic.clink.functions.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<JSONObject> {

    private final Response.Listener<JSONObject> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> mParams;
    private final Map<String, DataPart> mData;
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "your_boundary_string";
    private static final String LINE_END = "\r\n";

    public MultipartRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mErrorListener = errorListener;
        mParams = new HashMap<>();
        mData = new HashMap<>();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // Set any headers if needed
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            // Add string parameters
            for (Map.Entry<String, String> entry : mParams.entrySet()) {
                buildTextPart(dos, entry.getKey(), entry.getValue());
            }

            // Add binary parameters
            for (Map.Entry<String, DataPart> entry : mData.entrySet()) {
                buildDataPart(dos, entry.getKey(), entry.getValue());
            }

            // Write boundary end
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

            return bos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String paramName, String value) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"" + LINE_END);
        dataOutputStream.writeBytes(LINE_END);
        dataOutputStream.writeBytes(value + LINE_END);
    }

    private void buildDataPart(DataOutputStream dataOutputStream, String paramName, DataPart dataPart) throws IOException {
        dataOutputStream.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + paramName + "\"; filename=\"" + dataPart.getFileName() + "\"" + LINE_END);
        dataOutputStream.writeBytes("Content-Type: " + dataPart.getType() + LINE_END);
        dataOutputStream.writeBytes(LINE_END);

        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dataPart.getData());
        int bytesAvailable = fileInputStream.available();
        int maxBufferSize = 1024 * 1024;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];
        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        }

        dataOutputStream.writeBytes(LINE_END);
    }

    static class DataPart {
        private final String fileName;
        private final byte[] data;
        private final String type;

        DataPart(String fileName, byte[] data, String type) {
            this.fileName = fileName;
            this.data = data;
            this.type = type;
        }

        String getFileName() {
            return fileName;
        }

        byte[] getData() {
            return data;
        }

        String getType() {
            return type;
        }
    }
}
