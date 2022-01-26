# EasyAdapter [![](https://jitpack.io/v/wong1988/EasyAdapter.svg)](https://jitpack.io/#wong1988/EasyAdapter)

 通用适配器

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
     implementation 'com.github.wong1988:EasyAdapter:1.0.1'
     // 动画插件包
     implementation 'com.airbnb.android:lottie:4.2.1'
 }
 ```


 ### BaseAdapter(RecyclerView适配器) | [查看使用方式](https://github.com/wong1988/EasyAdapter/blob/main/RecyclerViewAdapter-README.md)


 ## Change Log
 
  1.0.1:

 * 新增LinearLayoutManager -> vertical 分割线

  0.0.9:

 * 优化传入的图片资源找不到时进行crash拦截
 * 修复移除脚布局crash的bug
 * 优化空布局时的显示效果

  0.0.5:

 * 去除默认error动画的背景色

 0.0.4:

 * 首个版本发布
