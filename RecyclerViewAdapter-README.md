# BaseListAdapter

一个通用简易的RecyclerView适配器，单布局可以使用SimpleListAdapter

## Method and Attribute

构造方法

```
// 默认支持上拉加载，如果列表无分页的功能，推荐使用2参构造，并传false
BaseListAdapter(Context context)
// 参数2：是否支持上拉加载
BaseListAdapter(Context context, boolean enableLoadMore)
```

实现方法

```
// position只是在数据源中的位置，非实际位置（包含头布局时）
onBindViewHolders(RecyclerViewHolder holder, int viewType, T t, int position);
```

常用方法

```
// 添加数据
addData()
// 移除指定数据源中的位置数据
remove(int position)
// 更新指定数据源中的位置数据
update(int position, T t)
// 清空数据源
clearAll()
// 获取绑定的xxx，如数据源长度，context，RecyclerView等
getAttachXXX()
// 添加头、脚布局
addHeaderView() addFooterView()
// 移除头、脚布局
removeHeader() removeAllHeaders() removeFooter() removeAllFooters()
// 设置头、脚布局的宽高 ，流式布局部分情况下 宽高某一个属性可能会不生效
setHeaderLayoutParams() setFooterLayoutParams()
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

```
// 设置加载状态
// 提供了 加载完成, 加载中, 加载到底, 加载出错, 无数据 几种状态
// 并设置了默认的样式，通过提供的方法可进行简单的样式更改
setLoadState()
```

监听器

```
1. OnItemClickListener() item的点击事件
2. OnItemLongClickListener() item的长按点击事件
3. OnLoadMoreListener() 上拉加载的监听事件
4. OnScrollListener() 列表的滚动监听事件
5. OnStateFooterClickListener(LoadState state) 加载状态脚布局点击的监听事件
```

## About

注意：

1. 当RecyclerView与Adapter绑定时，内部会给RecyclerView设置滚动监听addOnScrollListener()
   ，进行上拉监听管理。如需使用滚动监听进行一些自定义操作，请调用EasyAdapter的addOnScrollListener()
   进行监听【而非RecyclerView的addOnScrollListener()】。
2. setAdapter()前需要先设置LayoutManager，内部会根据LayoutManger进行选择适当的加载状态脚布局【横向、纵向】
3. 设置LayoutManager时千万不要设置setSpanSizeLookup()，请重写继承的适配器方法getSpanSize()进行设置
4. 重写onViewAttachedToWindow2()方法，可对瀑布流布局实现类似setSpanSizeLookup()效果
5. 内部对getItemCount()进行了处理，所以适配器onBindViewHolders()
   参数返回的position并不一定是真实的position，如果使用notifyItemRangeChanged()，notifyItemInserted()
   等方法可能达不到预期的效果（notifyDataSetChanged方法不影响）。如果一定要使用，可通过getRealPosition()
   返回真正的position，再去调用notifyXXX()方法。推荐使用addData()，remove()，update()等方法，方法内部会触发自动刷新适配器。
6. 添加头布局（默认为垂直方向），头布局宽默认为 match_parent，反之水平方向高度默认为 match_parent(注：默认值在整个适配器生命周期中仅第一次生效)
   ，可调用方法进行更改宽高，脚布局同理
7. 头脚布局以最后一次设置的方向为最终效果
