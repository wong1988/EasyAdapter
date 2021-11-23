# EasyAdapter
 一个通用简易的RecyclerView适配器
 
 ## Method and Attribute


监听器
```
1. OnItemClickListener() Item的点击事件
2. OnItemLongClickListener() Item的长按点击事件
3. OnLoadMoreListener() 上拉加载的监听事件
4. OnScrollListener() 列表的滚动监听事件
5. OnStateFooterClickListener(LoadState state) 加载状态脚布局点击的监听
```


## About


注意：
1. 当RecyclerView与Adapter绑定时，内部会给RecyclerView设置滚动监听addOnScrollListener()，进行上拉监听管理。如需使用滚动监听进行一些自定义操作，请调用EasyAdapter的addOnScrollListener()进行监听【而非RecyclerView的addOnScrollListener()】。
