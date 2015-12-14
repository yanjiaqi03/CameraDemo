package ihealthlabs.camerademo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import java.util.List;
import ihealthlabs.camerademo.R;
import ihealthlabs.camerademo.utils.BitMapUtils;
import ihealthlabs.camerademo.utils.CameraUtils;
import ihealthlabs.camerademo.utils.FileUtils;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by YanJiaqi on 15/12/14
 */
public class TestActivity extends FragmentActivity implements View.OnClickListener{
    private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraDemo/";
    private static final String TAG = "TestActivity";
    private ImageView display;
    private String logoPath = "";
    private Bitmap iconBmp = null;
    private boolean isCut = false; // true 为可以剪裁 false 为跳过剪裁

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        FileUtils.getInstance().createDir(PATH);
        initViews();
    }

    private void initViews(){
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);

        display = (ImageView) findViewById(R.id.imageView);
        display.setImageBitmap(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                isCut = false;
                CameraUtils.getInstance().choosePhotoFromCamera(this,PATH + System.currentTimeMillis() + ".jpg");
                break;
            case R.id.button2:
                isCut = false;
                CameraUtils.getInstance().choosePhotoFromGallery(this);
                break;
            case R.id.button3:
                isCut = true;
                CameraUtils.getInstance().choosePhotoFromCamera(this,PATH + System.currentTimeMillis() + ".jpg");
                break;
            case R.id.button4:
                isCut = true;
                CameraUtils.getInstance().choosePhotoFromGallery(this);
                break;
            case R.id.button5:
                isCut = true;
                CameraUtils.getInstance().choosePhotoFromActivityForOne(this);
                break;
            case R.id.button6:
                isCut = true;
                CameraUtils.getInstance().choosePhotoFromActivityForMuti(this,4);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CameraUtils.REQUEST_IMAGE) {
                // Get the result list of select image paths
                List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // do your logic ....
                if (paths != null && paths.size() > 0) {
                    if(paths.size() == 1){ // 单张
                        logoPath = paths.get(0);
                        if(isCut){
                            CameraUtils.getInstance().cutGalleryPhoto(this, Uri.parse("file://" + logoPath), PATH + System.currentTimeMillis() + ".jpg", 1, 1);
                        }else{
                            refreshDisplay(logoPath);
                        }
                    }else{  // 多张
                        String pathString = "";
                        for (int i = 0;i<paths.size();i++){
                            pathString += (i + " -> " + paths.get(i)+"\n");
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("选择图片的路径:\n"+pathString);
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
            } else if(requestCode == CameraUtils.CHOOSE_PHOTO_FREM_CAMERA){
                logoPath = CameraUtils.getInstance().getPhotoAbsolutePath(this,data);
                if(isCut){
                    CameraUtils.getInstance().cutGalleryPhoto(this, Uri.parse("file://" + logoPath), PATH + System.currentTimeMillis() + ".jpg", 1, 1);
                }else{
                    refreshDisplay(logoPath);
                }
            } else if(requestCode == CameraUtils.CHOOSE_PHOTO_FREM_STORAGE){
                logoPath = CameraUtils.getInstance().getPhotoAbsolutePath(this,data);
                if(isCut){
                    CameraUtils.getInstance().cutGalleryPhoto(this, Uri.parse("file://" + logoPath), PATH + System.currentTimeMillis() + ".jpg", 1, 1);
                }else{
                    refreshDisplay(logoPath);
                }
            } else if (requestCode == CameraUtils.GALLERY_PHOTO_CUT) {
                logoPath = CameraUtils.getInstance().getPhotoAbsolutePath(this, data);
                refreshDisplay(logoPath);
            }
        }
    }

    /**
     * 刷新display
     * Author YanJiaqi
     * Created at 15/12/14 上午11:59
     */

    private void refreshDisplay(String path){
        display.setImageDrawable(null);
        if (iconBmp != null && !iconBmp.isRecycled()) {
            iconBmp.recycle();
        }
        int iconSize = (int) getResources().getDimension(R.dimen.img_size);
        iconBmp = BitMapUtils.getInstance().getScaledBitmap(path, iconSize, iconSize);
        display.setImageBitmap(iconBmp);
    }
}
