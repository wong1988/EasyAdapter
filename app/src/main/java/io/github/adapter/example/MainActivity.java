package io.github.adapter.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void linearManager_vertical(View view) {
        startActivity(new Intent(this, LinearVerticalActivity.class));
    }

    public void linearManager_horizontal(View view) {
        startActivity(new Intent(this, LinearHorizontalActivity.class));
    }

    public void gridManager_vertical(View view) {
        startActivity(new Intent(this, GridVerticalActivity.class));
    }

    public void gridManager_horizontal(View view) {
        startActivity(new Intent(this, GridHorizontalActivity.class));
    }

    public void staggeredGridManager_vertical(View view) {
        startActivity(new Intent(this, StaggeredGridVerticalActivity.class));
    }

    public void staggeredGridManager_horizontal(View view) {
        startActivity(new Intent(this, StaggeredGridHorizontalActivity.class));
    }
}