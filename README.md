# EasyAdapter
 一个通用简易的RecyclerView适配器
 
 ## Method and Attribute

```
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
2. setAdapter()前需要先设置LayoutManager，内部会根据LayoutManger进行选择适当的加载状态脚布局【横向、纵向】，且横向纵向一经
