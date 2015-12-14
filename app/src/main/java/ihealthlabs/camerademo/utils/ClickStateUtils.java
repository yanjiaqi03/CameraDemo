package ihealthlabs.camerademo.utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by YanJiaqi on 15/11/29
 */
public class ClickStateUtils {
    private boolean touchFlag = true;

    /**
     * 点击改变背景
     * Author YanJiaqi
     * Created at 15/11/29 上午1:45
     */

    public void setBgClick(View view,final Drawable oriDrawable,final Drawable clickDrawable){
        view.setClickable(true);
        view.setFocusable(false);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!view.isClickable()){
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setBackground(clickDrawable);
                        touchFlag = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setBackground(oriDrawable);
                        touchFlag = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setBackground(oriDrawable);
                        touchFlag = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!(motionEvent.getX() > 0 && motionEvent.getX() < view.getWidth()
                                && motionEvent.getY() > 0 && motionEvent.getY() < view
                                .getHeight())&&touchFlag) {
                            view.setBackground(oriDrawable);
                            touchFlag = false;
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 点击改变透明度
     * Author YanJiaqi
     * Created at 15/11/29 上午2:03
     */

    public void setAlphaClick(View view,final float alpha){
        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!view.isClickable()){
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setAlpha(alpha * 7 / 10);
                        touchFlag = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        view.setAlpha(alpha);
                        touchFlag = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        view.setAlpha(alpha);
                        touchFlag = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!(motionEvent.getX() > 0 && motionEvent.getX() < view.getWidth()
                                && motionEvent.getY() > 0 && motionEvent.getY() < view
                                .getHeight())&&touchFlag) {
                            view.setAlpha(alpha);
                            touchFlag = false;
                        }
                        break;
                }
                return false;
            }
        });
    }
}
