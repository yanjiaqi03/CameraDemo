package ihealthlabs.camerademo.utils;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import ihealthlabs.camerademo.activity.CameraActivity;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class CameraUtils {
	public final static int CHOOSE_PHOTO_FREM_STORAGE = 100;
	public final static int CHOOSE_PHOTO_FREM_CAMERA = 101;
	public final static int GALLERY_PHOTO_CUT = 102;
	public final static int REQUEST_IMAGE = 103;

	private static CameraUtils mInstance = null;
	private String imgPath = "";

	public static CameraUtils getInstance() {
		if (mInstance == null) {
			mInstance = new CameraUtils();
		}

		return mInstance;
	}

	/**
	 * 从相册中选取图片
	 * 
	 * @author YanJiaqi
	 * @param activity
	 */
	public void choosePhotoFromGallery(Activity activity) {
		if (activity == null) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		activity.startActivityForResult(intent, CHOOSE_PHOTO_FREM_STORAGE);
		this.imgPath = "";
	}

	/**
	 * 从app自定义相册打开一张图片
	 * Author YanJiaqi
	 * Created at 15/12/1 上午2:27
	 */

	public void choosePhotoFromActivityForOne(Activity activity) {
		Intent intent = new Intent(activity, CameraActivity.class);
		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
		activity.startActivityForResult(intent, REQUEST_IMAGE);
	}
	/**
	 * 从app自定义相册打开多张图片
	 * Author YanJiaqi
	 * Created at 15/12/1 上午2:27
	 */
	public void choosePhotoFromActivityForMuti(Activity activity,int num) {
		Intent intent = new Intent(activity, CameraActivity.class);
		// whether show camera
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// max select image amount
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,num);
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

		activity.startActivityForResult(intent, REQUEST_IMAGE);
	}
	/**
	 * 相册选取图片裁剪
	 * 
	 * @author YanJiaqi
	 * @param activity
	 * @param imagePath
	 * @param aspectX
	 * @param aspectY
	 */
	public void cutGalleryPhoto(Activity activity, Uri oriUri, String imagePath, int aspectX, int aspectY) {
		if (activity == null) {
			return;
		}
		if (oriUri == null) {
			return;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(oriUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", aspectX);
		intent.putExtra("aspectY", aspectY);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		activity.startActivityForResult(intent, GALLERY_PHOTO_CUT);
		this.imgPath = imagePath;
	}

	/**
	 * 相机拍照选取图片
	 * 
	 * @author YanJiaqi
	 * @param activity
	 */
	public void choosePhotoFromCamera(Activity activity, String imagePath) {
		if (activity == null) {
			return;
		}
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		File photo = new File(imagePath);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		activity.startActivityForResult(intent, CHOOSE_PHOTO_FREM_CAMERA);
		this.imgPath = imagePath;
	}

	/**
	 * 获取照片存储地址
	 * 
	 * @author YanJiaqi
	 * @param activity
	 * @param data
	 * @return
	 */
	public String getPhotoAbsolutePath(Activity activity, Intent data) {
		if (data == null) {
			return imgPath;
		}
		Uri uri = data.getData();
		if (uri == null) {
			return imgPath;
		}
		
		if (uri.toString().contains("content://")) {
			String[] proj = { MediaStore.Images.Media.DATA};
			Cursor cursor = activity.getContentResolver().query(uri, proj, null, null, null);
			if (cursor != null) {
				if (cursor.getCount() > 0 && cursor.moveToFirst()) {
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					imgPath = cursor.getString(column_index);
				}
				cursor.close();
			}
		} else {
			imgPath = uri.getPath();
		}

		return imgPath;
	}
}
