package tech.gujin.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

    private static final String TAG = "ToastUtil";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static boolean initialized;
    private static Mode sDefaultMode;
    private static int sDefaultGravity;
    private static ToastUtilHandler sHandler;
    private static Toast sToast;
    private static int sDuration;
    private static int sGravity;

    /**
     * Initialize util.
     *
     * @param context The context to use.
     */
    public static void initialize(Context context) {
        initialize(context, Mode.NORMAL);
    }

    /**
     * Initialize util with the mode from user.
     *
     * @param context The context to use.
     * @param mode    The default display mode tu use. Either{@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void initialize(Context context, Mode mode) {
        if (initialized) {
            Log.w(TAG, "Invalid initialize, ToastUtil is already initialized");
            return;
        }

        if (context == null) {
            throw new NullPointerException("context can not be null");
        }

        sContext = context.getApplicationContext();

        // nullable
        sDefaultMode = mode;
        sDefaultGravity=Gravity.BOTTOM;
        sHandler = new ToastUtilHandler(Looper.getMainLooper());
        initialized = true;
    }

    /**
     * Display mode
     */
    public enum Mode {

        /**
         * Show as a normal toast. This mode could be user-definable.  This is the default.
         */
        NORMAL,

        /**
         * When the toast is shown to the user , the text will be replaced if call the show() method again.  This mode could be user-definable.
         */
        REPLACEABLE
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId The resource id of the string resource to use.
     */
    public static void show(int resId) {
        show(sContext.getText(resId), false,sDefaultGravity, sDefaultMode);
    }

    public static void show(int resId,int gravity) {
        show(sContext.getText(resId), false,gravity, sDefaultMode);
    }
    public static void showCenter(int resId) {
        show(sContext.getText(resId), false,Gravity.CENTER, sDefaultMode);
    }

    /**
     * Show a toast.
     *
     * @param text The text to show.
     */
    public static void show(CharSequence text) {
        show(text, false, sDefaultMode);
    }

    public static void show(CharSequence text, int gravity) {
        show(text, false,gravity, sDefaultMode);
    }

    public static void showCenter(CharSequence text) {
        show(text, false,Gravity.CENTER, sDefaultMode);
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId        The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     */
    public static void show(int resId, boolean durationLong) {
        show(sContext.getText(resId), durationLong, sDefaultMode);
    }

    public static void show(int resId, boolean durationLong,int gravity) {
        show(sContext.getText(resId), durationLong,gravity, sDefaultMode);
    }

    public static void showCenter(int resId, boolean durationLong) {
        show(sContext.getText(resId), durationLong,Gravity.CENTER, sDefaultMode);
    }

    /**
     * Show a toast.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     */
    public static void show(CharSequence text, boolean durationLong) {
        show(text, durationLong, sDefaultMode);
    }

    public static void show(CharSequence text,int gravity, boolean durationLong) {
        show(text, durationLong,gravity, sDefaultMode);
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId The resource id of the string resource to use.
     * @param mode  The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void show(int resId, Mode mode) {
        show(sContext.getText(resId), false, mode);
    }

    public static void show(int resId,int gravity, Mode mode) {
        show(sContext.getText(resId), false,gravity, mode);
    }

    /**
     * Show a toast.
     *
     * @param text The text to show.
     * @param mode The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void show(CharSequence text, Mode mode) {
        show(text, false, mode);
    }

    public static void show(CharSequence text,int gravity, Mode mode) {
        show(text, false, gravity,mode);
    }

    /**
     * Show a toast with the text form a resource.
     *
     * @param resId        resId The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void show(int resId, boolean durationLong, Mode mode) {
        show(sContext.getText(resId), durationLong,sDefaultGravity, mode);
    }

    public static void show(int resId, boolean durationLong, int gravity, Mode mode) {
        show(sContext.getText(resId), durationLong,gravity, mode);
    }

    /**
     * Show a toast.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void show(CharSequence text, boolean durationLong, Mode mode) {
        show( text,  durationLong,  sDefaultGravity,  mode) ;
    }

    /**
     * Show a toast.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @param gravity      location in screen
     * @param mode         The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     */
    public static void show(CharSequence text, boolean durationLong, int gravity, Mode mode) {
        final int duration = durationLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;

        int yOffset=0;
        if(gravity==Gravity.BOTTOM) yOffset=  dip2px(sContext,64);

        if (mode != Mode.REPLACEABLE) {
           Toast toast= Toast.makeText(sContext, text, duration);
           toast.setGravity(gravity,0,yOffset);
           toast.show();
            return;
        }

        if (sToast == null || sDuration != duration || sGravity != gravity) {
            sDuration = duration;
            sGravity=gravity;
            sToast = Toast.makeText(sContext, text, duration);
            sToast.setGravity(gravity,0,yOffset);
        } else {
            try {
                sToast.setText(text);
            } catch (RuntimeException e) {
                sToast = Toast.makeText(sContext, text, duration);
                sToast.setGravity(gravity,0,yOffset);
                e.printStackTrace();
            }
        }
        sToast.show();
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId The resource id of the string resource to use.
     * @see #show(int)
     */
    public static void postShow(int resId) {
        final ToastInfo info = new ToastInfo();
        info.resId = resId;
        info.durationLong = false;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_RES_ID, info).sendToTarget();
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text The text to show.
     * @see #show(CharSequence)
     */
    public static void postShow(CharSequence text) {
        final ToastInfo info = new ToastInfo();
        info.text = text;
        info.durationLong = false;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_CHAR_SEQUENCE, info).sendToTarget();
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId        The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @see #show(int, boolean)
     */
    public static void postShow(int resId, boolean durationLong) {
        final ToastInfo info = new ToastInfo();
        info.resId = resId;
        info.durationLong = durationLong;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_RES_ID, info).sendToTarget();
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @see #show(CharSequence, boolean)
     */
    public static void postShow(CharSequence text, boolean durationLong) {
        final ToastInfo info = new ToastInfo();
        info.text = text;
        info.durationLong = durationLong;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_CHAR_SEQUENCE, info).sendToTarget();
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId The resource id of the string resource to use.
     * @param mode  The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     * @see #show(int, Mode)
     */
    public static void postShow(int resId, Mode mode) {
        final ToastInfo info = new ToastInfo();
        info.resId = resId;
        info.mode = mode;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_RES_ID, info).sendToTarget();
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text The text to show.
     * @param mode The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     * @see #show(CharSequence, Mode)
     */
    public static void postShow(CharSequence text, Mode mode) {
        final ToastInfo info = new ToastInfo();
        info.text = text;
        info.mode = mode;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_CHAR_SEQUENCE, info).sendToTarget();
    }

    /**
     * Show a toast with the text form a resource.
     * This method can be invoked from non-UI thread.
     *
     * @param resId        resId The resource id of the string resource to use.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     * @see #show(int, boolean, Mode)
     */
    public static void postShow(int resId, boolean durationLong, Mode mode) {
        final ToastInfo info = new ToastInfo();
        info.resId = resId;
        info.durationLong = durationLong;
        info.mode = mode;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_RES_ID, info).sendToTarget();
    }

    /**
     * Show a toast.
     * This method can be invoked from non-UI thread.
     *
     * @param text         The text to show.
     * @param durationLong Whether the toast show for a long period of time?
     * @param mode         The display mode to use.  Either {@link Mode#NORMAL} or {@link Mode#REPLACEABLE}
     * @see #show(CharSequence, boolean, Mode)
     */
    public static void postShow(CharSequence text, boolean durationLong, Mode mode) {
        final ToastInfo info = new ToastInfo();
        info.text = text;
        info.durationLong = durationLong;
        info.mode = mode;
        sHandler.obtainMessage(ToastUtilHandler.MSG_POST_CHAR_SEQUENCE, info).sendToTarget();
    }

    private ToastUtil() {
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}