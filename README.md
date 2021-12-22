### 效果图：
![](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/551f8a614bb94c7cade1b3550785b404~tplv-k3u1fbpfcp-watermark.image)

---

# 使用文档
## 1.仓库引入
```java
//root/build
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

//app/build
implementation 'com.github.IAmWilling:MediaPlayerServer:1.0.3'
```
## 2.初始化播放服务
```kotlin
//Application下初始化
//参数1 Application 参数2 Notification实例（DefaultNotification内部封装需要传入此实例可高度自定义API参考下面）
MediaPlayerService.init(this,DefaultNotification.Builder().build())
```
## 3.音频监听器
```kotlin
//音频播放状态
interface MediaPlayStateListener
//音频进度
interface MediaProgressListener
//音频切换声音
interface MediaSwitchTrackChange
//音频错误监听
interface MediaErrorListener
```
## 3.使用
**MediaManager** `音频管理类含有播放以及暂停等功能`<br>
```kotlin
/**
* @param itemList 需要实现PlaylistItem接口
* @param index 选择播放 默认为0
*/
MediaManager.playList(item:PlaylistItem,playIndex:Int = 0)
//上一首
MediaManager.playLast()
//下一首
MediaManager.playNext()
//设置播放模式
MediaManager.switchPlayMode(mode:Int)
//exoPlayer实例 若是有些功能没有实现，则可以拿到实例进行相应功能调用
MediaManager.getSimpleExoPlayer()
//等等....具体查看该类SDK API
```

## 4.API详解
> **MediaManager** API
```kotlin
    /**
     * 添加进度监听
     */
    fun addProgressListener(mediaProgressListener: MediaProgressListener)

    /**
     * 添加音频播放状态监听
     */
    fun addMediaPlayerStateListener(mediaPlayStateListaner: MediaPlayStateListaner)

    /**
     * 添加音频错误监听
     */
    fun addMediaErrorListener(mediaErrorListener: MediaErrorListener) 

    /**
     * 移除音频进度监听
     */
    fun removeProgressListener(mediaProgressListener: MediaProgressListener) 

    /**
     * 移除音频错误监听
     */
    fun removeErrorListener(mediaErrorListener: MediaErrorListener) 

    /**
     * 移除音频改变监听
     */
    fun removeMediaSwitchChange(mediaSwitchTrackChange: MediaSwitchTrackChange) 

    /**
     * 添加音频改变监听
     */
    fun addMediaSwitchChange(mediaSwitchTrackChange: MediaSwitchTrackChange) 

    /**
     * 是否正在播放
     */
    fun isPlaying()

    /**
     * 当前音频缓存的封面bitmap
     */
    fun getCacheBitmap(): Bitmap

    /**
     * 设置当前id的bitmap
     */
    fun setCacheBitmap(bitmap: Bitmap)
     /**
     * 播放下一首
     */
    fun playNext(): Boolean 

    /**
     * 播放上一首
     */
    fun playLast(): Boolean

    /**
     * 播放切换
     */
    fun playOrPause()

     /**
     * 播放列表（按顺序播放）
     *
     * @param mutableList 播放列表
     */
    fun playlist(mutableList: MutableList<PlaylistItem>, playIndex: Int = 0) 

    /**
     * {@see #MediaPlayerExoPlayMode}
     *
     * 切换音频播放模式
     */
    fun switchPlayMode(@MediaPlayerExoPlayMode mode: Int) 

    /**
     * 获取当前播放id
     */
    fun currentId() = simpleExoPlayer.currentMediaItem?.mediaId

    /**
     * 获取播放实例
     */
    fun getSimpleExoPlayer(): SimpleExoPlayer 

    /**
     * 获取播放总进度
     */
    fun getCurrentDuration()

    /**
     * 获取当前音频标题
     */
    fun getCurrentMediaTitle(): String 

    /**
     * 获取当前音频详情
     */
    fun getCurrentMediaDesc(): String 

    /**
     * 获取当前音频封面地址
     */
    fun getCurrentMediaCover(): String 

    /**
     * 获取当前播放模式
     *
     * @link {#MediaPlayerExoPlayMode}
     */
    fun getCurrentPlayMode(): Int
    
    
     /**
     * 加入媒体列表放置播放器内
     */
    fun addMediaItem(item: PlaylistItem, playIndex: Int)

    /**
     * 加入媒体列表
     */
    fun addMediaItem(item: PlaylistItem)
    /**
     *
     * 改变媒体url
     */
    fun changeMediaItemAtUrl(playIndex: Int, url: String)

    /**
     * 获取当前媒体列表
     */
    fun getMediaList(): MutableList<PlaylistItem>
```
> **Notification.Builder** API

```kotlin
     /**
         * 设置通知栏应用图标
         */
        fun setSmallIcon(resid: Int): Builder 
        /**
         * 设置通知栏自定义布局
         * 不设置 则 默认是sdk自带布局
         */
        fun setRemoteViews(layoutId: Int): Builder 

        /**
         * 设置图标
         */
        fun setLargeIcon(icon: Bitmap?): Builder

        /**
         * 设置播放状态
         * 非必要初始化不需要设置
         */
        fun setPlayState(state: Boolean): Builder 

        /**
         * 设置通知栏音频标题
         * 初始化可设置初始标题
         */
        fun setTitleText(title: String): Builder 

        /**
         * 设置通知栏布局详情
         * 初始化可设置初始详情
         */
        fun setIntroText(intro: String): Builder

        /**
         * 设置封面
         */
        fun setMediaCover(bitmap: Bitmap): Builder 

        /**
         * 设置播放状态时的图片
         */
        fun setPlayImage(resid: Int): Builder

        /**
         * 设置暂停状态时的图片
         */
        fun setPauseImage(resid: Int): Builder 

        /**
         * 设置上一曲图片
         */
        fun setLastImage(resid: Int): Builder 
        /**
         * 设置下一曲图片
         */
        fun setNextImage(resid: Int): Builder

        /**
         * 设置通知栏适配小型布局
         * 有些手机需要设置比如小米，总是初始为小布局 
         * 锁屏界面需要的话 可以设置
         */
        fun setSmallRemoteViews(layoutId: Int): 

        /**
         * 设置通知栏整体点击之后需要跳转的activity组
         * 一般最后一个activity是你需要最终跳转到activity
         * 第一个一般都是MainActivity 看需求
         */
        fun setStartActivityClassArray(array: Array<Class<*>>): Builder

        /**
         * 跳转之后需要携带的一些数据可以自己设置 
         * 但这些数据最好是全局都可以拿到的，比如在MediaManager可以拿到的任何数据等
         */
        fun setStartActivityBundle(extraName: String, block: () -> Bundle): Builder 

        /**
         * 打包
         */
        fun build(): Notification
```
## 5.自定义通知栏布局注意事项（**重要**）
`标题和详情需要为TextView 其余的需要设置的id组件都是ImageView  或者说也只能是ImageView`
* 若要展示封面则ImageView需要设置id为：”default_song_cover_img“
* 若要展示通知栏的关闭按钮则ImageView需要设置id为：
“default_notific_close”
* 上一首："default_last"
* 下一首："default_next"
* 播放与暂停： "default_play"
* 歌曲标题：“default_song_name”
* 歌曲详情："default_song_intro"

## 6.看Demo具体的操作设置


