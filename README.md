# EasyAdapter [![](https://jitpack.io/v/wong1988/EasyAdapter.svg)](https://jitpack.io/#wong1988/EasyAdapter)

 通用适配器

 [![](https://github.com/wong1988/EasyAdapter/blob/main/img/preview.gif)

 Step 1. Add it in your root build.gradle at the end of repositories:
 ```
 allprojects {
     repositories {
         ...
         maven { url 'https://jitpack.io' }
     }
 }
 ```
 Step 2. Add the dependency
 ```
 dependencies {
     implementation 'com.github.wong1988:EasyAdapter:1.2.0'
     // 动画插件包
     implementation 'com.airbnb.android:lottie:4.2.1'
 }
 ```


 ### BaseAdapter(RecyclerView适配器) | [查看使用方式](https://github.com/wong1988/EasyAdapter/blob/main/RecyclerViewAdapter-README.md)
 ### LinearLayoutManagerDivider(RecyclerView垂直布局管理器分割线)/GridLayoutManagerDivider(RecyclerView网格布局管理器分割线)/StaggeredGridLayoutManagerDivider(RecyclerView流式布局管理器分割线) | [查看使用方式](https://github.com/wong1988/EasyAdapter/blob/main/XxxLayoutManagerDivider-README.md)


 ## Change Log
 
  1.2.0:

 * 新增删除所有分割线的方法
 * 修复添加头、脚布局部分情况闪退的问题
 * 头、脚布局支持自定义宽高
 * 新增StaggeredGridLayoutManager -> vertical 分割线
 * 新增StaggeredGridLayoutManager -> Horizontal 分割线

  1.1.0:

 * 新增ViewPager + Fragment懒加载的适配器

  1.0.9:

 * 优化spanSize方法，并修复返回的position错误的bug

  1.0.7:

 * 新增GridLayoutManager -> vertical 分割线
 * 新增GridLayoutManager -> Horizontal 分割线

  1.0.4:

 * 新增LinearLayoutManager -> vertical 分割线
 * 新增LinearLayoutManager -> Horizontal 分割线
 * 优化头脚布局摆放方向以最后设置的方向为最终效果

  0.0.9:

 * 优化传入的图片资源找不到时对crash拦截
 * 修复移除脚布局crash的bug
 * 优化空布局时的显示效果

  0.0.5:

 * 去除默认error动画的背景色

 0.0.4:

 * 首个版本发布
