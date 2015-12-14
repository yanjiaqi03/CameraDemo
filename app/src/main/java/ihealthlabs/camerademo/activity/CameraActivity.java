package ihealthlabs.camerademo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ihealthlabs.camerademo.R;
import ihealthlabs.camerademo.utils.ClickStateUtils;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.MultiImageSelectorFragment;

/**
 * Created by YanJiaqi on 15/11/30
 */
public class CameraActivity extends FragmentActivity implements MultiImageSelectorFragment.Callback{
    private final String TAG = "CameraActivity";

    private int mDefaultCount;
    private ArrayList<String> resultList = new ArrayList<String>();

    /** XML Resource **/
    private FrameLayout back;
    private TextView finish;

    @Override
    protected void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_camera);

        initViews();
    }

    private void initViews(){
        back = (FrameLayout) findViewById(R.id.camera_return);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        finish = (TextView) findViewById(R.id.camera_finish);
        finish.setClickable(false);
        finish.setAlpha(0.5f);
        ClickStateUtils clickUtils = new ClickStateUtils();
        clickUtils.setAlphaClick(finish,1f);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resultList != null && resultList.size() >0){
                    // 返回已选择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 4);
        int mode = intent.getIntExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        if(mode == MultiImageSelectorActivity.MODE_MULTI){
            finish.setVisibility(View.VISIBLE);
        }else if(mode == MultiImageSelectorActivity.MODE_SINGLE){
            finish.setVisibility(View.GONE);
        }
        boolean isShow = intent.getBooleanExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        if(mode == MultiImageSelectorActivity.MODE_MULTI && intent.hasExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST);
        }
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        // Add fragment to your Activity
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if(!resultList.contains(path)) {
            resultList.add(path);
        }
        adjustEnableFinish();
    }

    @Override
    public void onImageUnselected(String path) {
        if(resultList.contains(path)){
            resultList.remove(path);
        }
        adjustEnableFinish();
    }

    @Override
    public void onCameraShot(File imageFile) {
        if(imageFile != null) {
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    private void adjustEnableFinish(){
        boolean clickable = false;
        if(resultList == null || resultList.size() == 0){
            clickable = false;
        }else{
            clickable = true;
        }

        if(clickable){
            finish.setClickable(true);
            finish.setAlpha(1f);
            finish.setText("("+resultList.size()+"/" + mDefaultCount + ")" + getResources().getString(R.string.camera_finish));
        }else{
            finish.setClickable(false);
            finish.setAlpha(0.5f);
            finish.setText(getResources().getString(R.string.camera_finish));
        }
    }
}
