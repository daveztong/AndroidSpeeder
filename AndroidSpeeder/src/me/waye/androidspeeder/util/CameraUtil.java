/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.util
 * Filename: CameraUtil.java
 * Created time: Nov 27, 2013
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */

package me.waye.androidspeeder.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

/**
 * @ClassName: CameraUtil
 * @Description: 拍照处理工具类
 * @author tangwei
 * @date Nov 27, 2013 1:20:43 PM
 * 
 */
public class CameraUtil {

    public static final String IMAGE_SUFFIX = ".png";

    /**
     * 取得拍摄照片的存储路径，存储位置是"Environment.DIRECTORY_PICTURES/相册名称".
     * 
     * @param albumName 相册名称，如果为null或空,则图片存储在Environment.DIRECTORY_PICTURES下.
     * @return 照片存储目录
     */
    public static File getImageStorageDir(String albumName) {
        File storageDir = null;
        if (StringUtil.isEmpty(albumName))
            storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        else
            storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);

        return storageDir;
    }

    /**
     * 创建一个空的临时文件来存储图片.
     * 
     * @param context 上下文
     * @param prefix 图片名称前缀，为null时前缀默认为: App名称_yyyyMMdd_HHmmss
     * @param suffix 图片扩展名,后缀, 为null时默认为".png"
     * @return 空的临时文件
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    public File createTmpImageFile(Context context, String prefix, String suffix) throws IOException {

        if (StringUtil.isEmpty(suffix))
            suffix = IMAGE_SUFFIX;

        if (StringUtil.isEmpty(prefix)) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String appLabel = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
            if (!StringUtil.isEmpty(appLabel)) {
                prefix = appLabel + "_" + timestamp;
            }else{
                prefix = timestamp;
            }
        }
        return File.createTempFile(prefix, suffix, getImageStorageDir(null));
    }

    /**
     * 将照片添加到gallery中，以供其他App访问.
     * 
     * @param context 上下文
     * @param imageFile 要添加到gallery中的图片文件
     */
    public static void AddPic2gallery(Context context, File imageFile) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 在显示全尺寸的图片时，很容易出现内存不足的情况，解决方法就是将图片针对要显示的View进行缩放. 该方法就是针对指定的View获取缩放后的图片.
     * 
     * @param imageFile 要缩放的图片
     * @param targetView 要显示图片的View
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap getScaledImage(File imageFile, View targetView) {
        // Get the dimensions of the View
        int targetW = targetView.getWidth();
        int targetH = targetView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
        return bitmap;
    }
}
