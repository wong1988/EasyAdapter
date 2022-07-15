package io.github.adapter.example;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.adapter.example.adapter.LinearVerticalAdapter;
import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.divider.LinearLayoutManagerDivider;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LinearVerticalActivity extends AppCompatActivity {

    private LinearVerticalAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear_vertical);

        rv = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(LinearLayoutManagerDivider.getVerticalDivider());

        adapter = new LinearVerticalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOAD_END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 0, 1, "添加头布局[垂直方向]");
        menu.add(1, 1, 2, "添加脚布局[垂直方向]");
        menu.add(1, 2, 3, "清空头布局");
        menu.add(1, 3, 4, "删除头布局中的第一个view");
        menu.add(1, 4, 5, "清空脚布局");
        menu.add(1, 5, 6, "删除脚布局中的第一个view");
        menu.add(1, 6, 7, "20高分割线");
        menu.add(1, 7, 8, "20高 距左10 距右20 分割线");
        menu.add(1, 8, 9, "设置头布局的宽高[WRAP_CONTENT]");
        menu.add(1, 9, 10, "设置脚布局的宽高[WRAP_CONTENT]");
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
                rv.addItemDecoration(LinearLayoutManagerDivider.getVerticalDivider(Color.parseColor("#000000"), 20));
                break;
            case 7:
                adapter.removeAllItemDecoration();
                rv.addItemDecoration(LinearLayoutManagerDivider.getVerticalDivider(Color.parseColor("#589654"), 20, 10, 20));
                break;
            case 8:
                adapter.setHeaderLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                break;
            case 9:
                adapter.setFooterLayoutParams(WRAP_CONTENT, WRAP_CONTENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}