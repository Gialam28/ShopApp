package com.example.shopapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shopapp.R;

import java.util.*;

public class AccountSelectionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lưu ý: Đảm bảo layout có ListView id là lvAccounts
        setContentView(R.layout.activity_selection);

        ListView listView = findViewById(R.id.lvAccounts);
        Set<String> accounts = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getStringSet("saved_accounts", new HashSet<>());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, new ArrayList<>(accounts));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}