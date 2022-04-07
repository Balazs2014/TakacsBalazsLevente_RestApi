package hu.csepel.takacsbalazslevente_restapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListResultActivity extends AppCompatActivity {
    private Button backButton;
    private ListView listView;

    List<City> cityList = new ArrayList<>();
    private String URL = "http://127.0.0.1:8000/api/cities";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        RequestTask requestTask = new RequestTask();
        requestTask.execute();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void init() {
        backButton = findViewById(R.id.backButton);
        listView = findViewById(R.id.listView);
    }

    private class RequestTask extends AsyncTask<Void,Void,Response> {

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                response = RequestHandler.get(URL);
            }catch (IOException e){
                e.printStackTrace();
            }
            return response;
        }


        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            Gson gson = new Gson();
            if (response == null || response.getResponseCode() >= 400){
                Toast.makeText(ListResultActivity.this, "Hiba történt a kérés feldolgozása során.", Toast.LENGTH_SHORT).show();
            }
            else {
                City[] locations = gson.fromJson(response.getContent(), City[].class);
                cityList.clear();
                cityList.addAll(Arrays.asList(locations));
                ArrayAdapter<City> arrayAdapter = new ArrayAdapter<>(ListResultActivity.this, R.layout.city_list_item, R.id.virsli, cityList);
                listView.setAdapter(arrayAdapter);
            }
        }
    }
}