# CameraDemo

选择图片的几种方式 

包括从相机，图库读取图片，可以选择执行剪裁操作，也可以选择多选（类似微信朋友圈）操作。

1.对于 multi-image-selector，可以参考 https://github.com/lovetuzitong/MultiImageSelector. 

2.对于 onActivityResult 中的图片读取，请参考 项目 包名/utils/BitmapUtils

3.注意读取图片地址后对path的操作，由于图片很大，所以两种方式去读取：

a.使用开源ImageLoader读取，它会自动帮你转换大小，以达到节省内存的目的，优点是方法简单，你只需要调用一句即可完成图片异步加载到

显示的过程，但是缺点是由于存在大小转换，所以读取时间会略长，对于选择头像等功能，不适用，对于读取大图适用。

b.使用 BitmapUtils的 getScaledBitmap，这是通过自行计算图片的宽高与所设置的最大宽高之间的比例，然后设置 insample属性从path读出图片。
