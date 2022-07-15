package io.github.adapter.example;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import io.github.adapter.example.adapter.StaggeredVerticalAdapter;
import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.divider.StaggeredGridLayoutManagerDivider;
import io.github.wong1988.adapter.listener.OnLoadMoreListener;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class StaggeredGridVerticalActivity extends AppCompatActivity {

    private StaggeredVerticalAdapter adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid_vertical);

        rv = findViewById(R.id.rv);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);
        rv.addItemDecoration(StaggeredGridLayoutManagerDivider.getVerticalDivider(20));

        adapter = new StaggeredVerticalAdapter(this);
        rv.setAdapter(adapter);
        adapter.setLoadState(LoadState.LOAD_COMPLETE);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.appendData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 0, 1, "添加头布局[垂直方向]");
        menu.add(1, 1, 2, "添加脚布局[垂直方向]");
        menu.add(1, 2, 3, "清空头布局");
        menu.add(1, 3, 4, "删除头布局中的第一个view");
        menu.add(1, 4, 5, "清空脚布局");
        menu.add(1, 5, 6, "删除脚布局中的第一个view");
        menu.add(1, 6, 7, "2高分割线");
        menu.add(1, 7, 8, "设置头布局的宽高[MATCH_P..，50]");
        menu.add(1, 8, 9, "设置脚布局的宽高[MATCH_P..，50]");
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
                adapter.addFooter();
                break;
            case 2:
                adapter.removeAllHeaders();
                break;
            case 3:
                adapter.removeHeader(0);
                break;
            case 4:
                adapter.removeAllFooters();
                break;
            case 5:
                adapter.removeFooter(0);
                break;
            case 6:
                adapter.removeAllItemDecoration();
                rv.addItemDecoration(StaggeredGridLayoutManagerDivider.getVerticalDivider(2));
                break;
            case 7:
                adapter.setHeaderLayoutParams(MATCH_PARENT, 50);
                break;
            case 8:
                adapter.setFooterLayoutParams(MATCH_PARENT, 50);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}