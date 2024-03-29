package io.github.adapter.example;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.adapter.example.adapter.LinearHorizontalAdapter;
import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.divider.GridLayoutManagerDivider;

public class GridHorizontalActivity extends AppCompatActivity {

    private LinearHorizontalAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_horizontal);

        rv = findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rv.setLayoutManager(gridLayoutManager);
        rv.addItemDecoration(GridLayoutManagerDivider.getHorizontalDivider());

        adapter = new LinearHorizontalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOADING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 0, 1, "添加头布局[水平方向]");
        menu.add(1, 1, 2, "添加脚布局[垂直方向]");
        menu.add(1, 2, 3, "清空头布局");
        menu.add(1, 3, 4, "删除头布局的第一个view");
        menu.add(1, 4, 5, "清空脚布局");
        menu.add(1, 5, 6, "删除脚布局的第一个view");
        menu.add(1, 6, 7, "20高分割线");
        menu.add(1, 7, 8, "设置头布局的宽高[MATCH_P..，30]");
        menu.add(1, 8, 9, "设置脚布局的宽高[WRAP_CONTENT]");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                adapter.addHeader();
                rv.scrollToPosition(0);
                break;
            case 1:
                rv.scrollToPosition(adapter.getItemCount() - 1);
                adapter.addFooter();
                break;
            case 2:
                rv.scrollToPosition(0);
                adapter.removeAllHeaders();
                break;
            case 3:
                rv.scrollToPosition(0);
                adapter.removeHeader(0);
                break;
            case 4:
                rv.scrollToPosition(adapter.getItemCount() - 1);
                adapter.removeAllFooters();
                break;
            case 5:
                rv.scrollToPosition(adapter.getItemCount() - 1);
                adapter.removeFooter(0);
                break;
            case 6:
                adapter.removeAllItemDecoration();
                rv.addItemDecoration(GridLayoutManagerDivider.getHorizontalDivider(Color.parseColor("#000000"), 20));
                break;
            case 7:
                adapter.setHeaderLayoutParams(MATCH_PARENT, 30);
                break;
            case 8:
                adapter.setFooterLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}