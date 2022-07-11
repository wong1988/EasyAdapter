# XxxLayoutManagerDivider
 一个通用简易的RecyclerView分割线
 
 ## Method and Attribute

常用方法
```
// LayoutManager - > 水平方向
LinearLayoutManagerDivider.getHorizontalDivider()
GridLayoutManagerDivider.getHorizontalDivider()
// LayoutManager - > 垂直方向
LinearLayoutManagerDivider.getVerticalDivider()
GridLayoutManagerDivider.getVerticalDivider()
```

## About


注意：
1. 使用项目提供的适配器时，头、脚、状态布局不会加分割线
2. 最后一个item（即 脚布局上面的item）不会加分割线
3. 分割线以RecyclerView实际可用大小计算 比如：屏幕宽度 1080px RecyclerView match_parent, marginLeft:20px paddingRight: 20px 分割线按照 1080 - 20 - 20 = 1040px 进行计算
而Item设置的 margin padding 自动忽略
4. Grid类型的分割线 垂直方向时 item的宽会变小（屏幕宽度固定，以1080px屏幕为例，spanCount：3 每个 item 为 360px ,如果要加入分割线，分割线以 20px 宽计算，每个item的宽度为346.7px）
4. Grid类型的分割线 水平方向时 item的高可能会变小（根据设置的RecyclerView高度，固定高度肯定会变小，wrap_content 未超出屏幕高度item不会变小）
