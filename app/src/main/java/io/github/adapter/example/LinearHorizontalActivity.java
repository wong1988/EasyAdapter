package io.github.adapter.example;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.adapter.example.adapter.LinearHorizontalAdapter;
import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.divider.LinearLayoutManagerDivider;

public class LinearHorizontalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_horizontal);

        RecyclerView rv = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);

//        rv.addItemDecoration(LinearLayoutManagerDivider.getHorizontalDivider());
//        rv.addItemDecoration(LinearLayoutManagerDivider.getHorizontalDivider(Color.parseColor("#000000"), 20));
        rv.addItemDecoration(LinearLayoutManagerDivider.getHorizontalDivider(Color.parseColor("#000000"), 20, 10, 20));

        LinearHorizontalAdapter adapter = new LinearHorizontalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOADING);
    }
}