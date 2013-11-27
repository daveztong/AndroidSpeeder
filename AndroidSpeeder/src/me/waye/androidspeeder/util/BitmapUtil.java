/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.util
 * Filename: BitmapUtil.java
 * Created time: Nov 27, 2013
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */

package me.waye.androidspeeder.util;

import java.io.File;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @ClassName: BitmapUtil
 * @Description: Bitmap处理工具类
 * @author tangwei
 * @date Nov 27, 2013 3:21:38 PM
 * 
 */
public class BitmapUtil {

    /**
     * 计算解码图片的inSampleSize
     * 
     * @param options BitmapFactory.Options
     * @param reqWidth 目标宽度
     * @param reqHeight 目标高度
     * @return inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 根据目标宽度和高度来解码图片
     * 
     * @param res Resources对象
     * @param resId 资源ID
     * @param reqWidth 目标宽度(单位为:pixel)
     * @param reqHeight 目标高度
     * @return 解码后的样本图片
     */
    public static Bitmap decodeSampledBitmap(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 根据目标宽度和高度来解码图片
     * 
     * @param file 待解码的图片文件
     * @param reqWidth 目标宽度(单位为:pixel)
     * @param reqHeight 目标高度
     * @return 解码后的样本图片
     */
    public static Bitmap decodeSampledBitmap(File file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

}
