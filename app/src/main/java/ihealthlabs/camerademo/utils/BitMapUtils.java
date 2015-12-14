package ihealthlabs.camerademo.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.View;
import android.view.View.MeasureSpec;
import android.graphics.Bitmap.Config;


public class BitMapUtils {
	private final String TAG = "BitMapUtils";
	private static BitMapUtils mInstance = null;

	public static BitMapUtils getInstance() {
		if (mInstance == null) {
			mInstance = new BitMapUtils();
		}

		return mInstance;
	}

	public void saveToSdCard(String path, Bitmap bitmap) throws IOException {
		saveToSdCard(path, bitmap, false, 100);
	}

	public void saveToSdCard(String path, Bitmap bitmap, boolean isReplace) throws IOException {
		saveToSdCard(path, bitmap, isReplace, 100);
	}

	public void saveToSdCard(String path, Bitmap bitmap, int compressQuality) throws IOException {
		saveToSdCard(path, bitmap, false, compressQuality);
	}

	/**
	 * 将bitmap存入本地。
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @param bitmap
	 * @param isReplace
	 *            是否替换原有文件
	 * @param compressQuality
	 *            压缩质量 100为不压缩 (只对JPEG有效)
	 */
	public void saveToSdCard(String path, Bitmap bitmap, boolean isReplace, int compressQuality) throws IOException {
		if (null != bitmap && null != path && !path.equalsIgnoreCase("")) {
			File file = new File(path);
			if (file.exists() && !isReplace) {
				return;
			}
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bos);
			bos.flush();
			bos.close();
			if (bitmap != null && !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	/**
	 * 获取本地图片信息。
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return
	 */
	public BitMapData getBitmapInfo(String path) {
		BitMapData info = new BitMapData();
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		info.setWidth(opt.outWidth);
		info.setHeight(opt.outHeight);

		return info;
	}

	/**
	 * 读取资源图片信息。
	 * 
	 * @author YanJiaqi
	 * @param context
	 * @param resid
	 * @return BitMapData
	 */
	public BitMapData getBitmapInfo(Context context, int resid) {
		BitMapData info = new BitMapData();
		if (context == null) {
			return info;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resid, opt);
		info.setWidth(opt.outWidth);
		info.setHeight(opt.outHeight);

		return info;
	}

	/**
	 * 获取RGB565可回收Bitmap
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return
	 */
	public Bitmap getRGB565Bmp(String path) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inInputShareable = true;
		opt.inPurgeable = true;
		opt.inPreferredConfig = Config.RGB_565;

		return getBitmap(path, opt);
	}

	/**
	 * 获取RGB565可回收Bitmap
	 * 
	 * @author YanJiaqi
	 * @param context
	 * @param resid
	 * @return
	 */
	public Bitmap getRGB565Bmp(Context context, int resid) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inInputShareable = true;
		opt.inPurgeable = true;
		opt.inPreferredConfig = Config.RGB_565;
		return getBitmap(context, resid, opt);
	}

	/**
	 * 获取Bitmap
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @param opt
	 * @return
	 */
	public Bitmap getBitmap(String path, BitmapFactory.Options opt) {
		if (opt == null) {
			opt = new BitmapFactory.Options();
			opt.inInputShareable = true;
			opt.inPurgeable = true;
			opt.inPreferredConfig = Config.ARGB_8888;
		}
		Bitmap bmp = BitmapFactory.decodeFile(path, opt);
		return bmp;
	}

	/**
	 * 获取bitmap
	 * 
	 * @author YanJiaqi
	 * @param context
	 * @param resid
	 * @param opt
	 * @return
	 */
	public Bitmap getBitmap(Context context, int resid, BitmapFactory.Options opt) {
		if (context == null) {
			return null;
		}
		if (opt == null) {
			opt = new BitmapFactory.Options();
			opt.inInputShareable = true;
			opt.inPurgeable = true;
			opt.inPreferredConfig = Config.ARGB_8888;
		}
		return BitmapFactory.decodeResource(context.getResources(), resid, opt);
	}
	
	/**
	 * 设置最大宽高获取bitmap
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public Bitmap getScaledBitmap(String path,int maxWidth,int maxHeight){
		float ratioWidth = 1f;
		float ratioHeight = 1f;
		BitMapData data = getBitmapInfo(path);
		ratioWidth = data.getWidth() * 1f / maxWidth;
		ratioHeight = data.getHeight() * 1f / maxHeight;
		if(ratioWidth<1f){
			ratioWidth = 1f;
		}
		if(ratioHeight<1f){
			ratioHeight = 1f;
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inInputShareable = true;
		opt.inPurgeable = true;
		opt.inPreferredConfig = Config.RGB_565;
		opt.inSampleSize = (int)Math.max(ratioWidth,ratioHeight);
		return getBitmap(path,opt);
	}

	/**
	 * 截取原图的一部分(原图不回收)
	 * 
	 * @author LanBaoshi
	 * @param bmpSrc
	 * @param width
	 * @param height
	 * @param config
	 * @return
	 */
	public Bitmap duplicateBitmap(Bitmap bmpSrc, int width, int height, Config config) {
		return duplicateBitmap(bmpSrc, width, height, false, config);
	}

	/**
	 * 截取原图的一部分
	 * 
	 * @author LanBaoshi
	 * @param bmpSrc
	 * @param width
	 * @param height
	 * @param needRecycle
	 * @param config
	 * @return
	 */
	public Bitmap duplicateBitmap(Bitmap bmpSrc, int width, int height, boolean needRecycle, Config config) {
		if (null == bmpSrc) {
			return null;
		}
		int bmpSrcWidth = bmpSrc.getWidth();
		int bmpSrcHeight = bmpSrc.getHeight();

		Bitmap bmpDest = Bitmap.createBitmap(width, height, config);
		if (null != bmpDest) {
			Canvas canvas = new Canvas(bmpDest);
			Rect viewRect = new Rect();
			final Rect rect = new Rect(0, 0, bmpSrcWidth, bmpSrcHeight);
			if (bmpSrcWidth <= width && bmpSrcHeight <= height) {
				viewRect.set(rect);
			} else if (bmpSrcHeight > height && bmpSrcWidth <= width) {
				viewRect.set(0, 0, bmpSrcWidth, height);
			} else if (bmpSrcHeight <= height && bmpSrcWidth > width) {
				viewRect.set(0, 0, width, bmpSrcWidth);
			} else if (bmpSrcHeight > height && bmpSrcWidth > width) {
				viewRect.set(0, 0, width, height);
			}
			canvas.drawBitmap(bmpSrc, rect, viewRect, null);
		}

		if (needRecycle && !bmpSrc.isRecycled()) {
			bmpSrc.recycle();
		}
		return bmpDest;
	}

	/**
	 * bitmap 转 byte[] (bitmap不回收)
	 * 
	 * @author LanBaoshi
	 * @param bitmap
	 * @return
	 */
	public byte[] bitmapToByteArray(Bitmap bitmap) throws IOException {
		return bitmapToByteArray(bitmap, false);
	}

	/**
	 * bitmap 转 byte[]
	 * 
	 * @author LanBaoshi
	 * @param bitmap
	 * @param needRecycle
	 * @return
	 */
	public byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) throws IOException {
		byte[] array = null;
		if (null != bitmap) {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
			array = os.toByteArray();
			os.close();

			if (needRecycle && !bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
		return array;
	}

	/**
	 * Bitmap 转base64字符串
	 * @author lanbaoshi
	 * created at 15/11/28 下午8:32
	 */
	public String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * byte[] 转 bitmap
	 * 
	 * @author LanBaoshi
	 * @param array
	 * @return
	 */
	public Bitmap byteArrayToBitmap(byte[] array) {
		if (null == array) {
			return null;
		}

		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	/**
	 * bitmap转drawable
	 * 
	 * @author YanJiaqi
	 * @param context
	 * @param bitmap
	 * @return
	 */
	public BitmapDrawable bitmapToDrawable(Context context, Bitmap bitmap) {
		if (context == null) {
			return null;
		}
		return new BitmapDrawable(context.getResources(), bitmap);
	}

	/**
	 * view转bitmap
	 * 
	 * @author YanJiaqi
	 * @param view
	 * @param config
	 * @param isMutable
	 * @return
	 */
	public Bitmap viewToBitmap(View view, Config config, boolean isMutable) {
		view.setDrawingCacheEnabled(true);
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap_temp = view.getDrawingCache();
		Bitmap bitmap = bitmap_temp.copy(config, isMutable);
		view.setDrawingCacheEnabled(false);

		return bitmap;
	}
}
