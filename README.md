# EasyAdapter
 一个通用简易的RecyclerView适配器
 
 ## Method and Attribute

构造方法
```
// 默认支持上拉加载，如果列表无分页的功能，推荐使用2参构造，并传false
BaseListAdapter(Context context)
// 参数2：是否支持上拉加载
BaseListAdapter(Context context, boolean enableLoadMore)
```

```
// 添加数据
addData()
// 移除指定位置数据
remove(int position)
// 清空数据源
clearAll()
// 获取绑定的xxx，如数据源长度，context，RecyclerView等
getAttachXXX()
// 添加头、脚布局
addHeaderView() addFooterView()
// 移除头、脚布局
removeHeader() removeAllHeaders() removeFooter() removeAllFooters()
// 设置加载状态的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
// 自适应图片大小，最大50dp
setLoadingRes(int loadingRes)
// 设置loading状态文字描述
setLoadingText()
// 设置加载到底状态文字描述
setEndText()
// 设置加载出错的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
// 自适应图片大小，最大50dp
setErrorRes(int errorRes)
// 设置error状态文字描述
setErrorText()
// 设置空数据的资源，可以是drawable/mipmap下的图片资源，也可以是raw下的json动画资源
setEmptyRes(int emptyRes)
// 设置空数据状态文字描述
setEmptyText()
```

监听器
```
1. OnItemClickListener() Item的点击事件
2. OnItemLongClickListener() Item的长按点击事件
3. OnLoadMoreListener() 上拉加载的监听事件
4. OnScrollListener() 列表的滚动监听事件
5. OnStateFooterClickListener(LoadState state) 加载状态脚布局点击的监听事件
```


## About


注意：
1. 当RecyclerView与Adapter绑定时，内部会给RecyclerView设置滚动监听addOnScrollListener()，进行上拉监听管理。如需使用滚动监听进行一些自定义操作，请调用EasyAdapter的addOnScrollListener()进行监听【而非RecyclerView的addOnScrollListener()】。
2. setAdapter()前需要先设置LayoutManager，内部会根据LayoutManger进行选择适当的加载状态脚布局【横向、纵向】
