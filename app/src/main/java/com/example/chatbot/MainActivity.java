package com.example.chatbot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textViewResult;
    EditText editText;
    ImageButton imageButton;
  //Key-1  //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
    //Key -2 //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
    //Key -3 //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
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewResult= findViewById(R.id.resultTv);
        editText= findViewById(R.id.editTextQuery);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            performAction(editText.getText().toString());
            editText.setText("");

            }
        });


    }

    public void performAction(String inputText) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.openai.com/v1/completions";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("prompt", inputText);
            jsonObject.put("max_tokens", 500);
            jsonObject.put("model", "text-davinci-003");
           // jsonObject.put("temperature",0.9);

// Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String answer = null;
                        try {
                            answer = response.getJSONArray("choices").getJSONObject(0).getString("text");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        // Display the first 500 characters of the response string.
                       // textViewResult.setText("Response is: " + response.toString());
                        textViewResult.setText(answer);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textViewResult.setText("That didn't work!");
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map= new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("Authorization", "Bearer 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");
                return map;
            }
        };

        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 15 ;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

        }catch (JSONException e) {
        throw new RuntimeException(e);
    }
    }
}