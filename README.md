# 水滴落水波进度
## 效果如下:视频录制比较卡顿，实际很流畅
<img src="/preview/demo.gif">

## 使用方法:
## Step 1. Add the JitPack repository to your build file
## Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


## Step 2. Add the dependency

```
 	        dependencies {
        	        implementation 'com.github.yhongm:WaveProgressView:master'
        	}
```
### 1，布局文件添加以下属性
```xml
<com.yhongm.wave_progress_view.WaveProgressView
        android:id="@+id/wave_progress_view"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        app:circleColor="#e38854"
        app:progress="0"
        app:waterColor="#5488e3" />

```
***
### 属性说明
#### app:circleColor 圆环颜色
#### app:progress 初始进度
#### app:waterColor 水滴和水波的颜色

### 2，setProgress() 方法设置当前的进度 
