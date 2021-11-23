# EasyAdapter
 一个通用简易的RecyclerView适配器


## About


注意：
1. 当RecyclerView与Adapter绑定时，内部会给RecyclerView设置滚动监听addOnScrollListener()，进行上拉监听管理。如需使用滚动监听进行一些自定义操作，请调用EasyAdapter的addOnScrollListener()进行监听【而非RecyclerView的addOnScrollListener()】。
