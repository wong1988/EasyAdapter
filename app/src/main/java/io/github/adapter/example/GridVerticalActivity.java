package io.github.adapter.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.adapter.example.adapter.LinearVerticalAdapter;
import io.github.wong1988.adapter.attr.LoadState;

public class GridVerticalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_vertical);

        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);

        LinearVerticalAdapter adapter = new LinearVerticalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOAD_END);
    }
}