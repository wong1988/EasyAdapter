package io.github.adapter.example;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.adapter.example.adapter.LinearHorizontalAdapter;
import io.github.wong1988.adapter.attr.LoadState;

public class GridHorizontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_horizontal);

        RecyclerView rv = findViewById(R.id.rv);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);


        LinearHorizontalAdapter adapter = new LinearHorizontalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOADING);
    }
}