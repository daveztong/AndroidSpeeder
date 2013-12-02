/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.util
 * Filename: ImageUtil.java
 * Created time: 2013-12-2
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */

package me.waye.androidspeeder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

/**
 * @ClassName: ImageUtil
 * @Description: 图片处理工具类
 * @author tangwei
 * @date 2013-12-2 上午11:26:17
 * 
 */
public class ImageUtil {

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
     * @see {@link #getImageStorageDir(String)}
     */
    @SuppressLint("SimpleDateFormat")
    public File createTmpImageFile(Context context, String prefix, String suffix) throws IOException {

        if (StringUtil.isEmpty(suffix))
            suffix = IMAGE_SUFFIX;

        if (StringUtil.isEmpty(prefix)) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Timestamp(System.currentTimeMillis()));
            String appLabel = (String) context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
            if (!StringUtil.isEmpty(appLabel)) {
                prefix = appLabel + "_" + timestamp;
            } else {
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
    public static void AddPic2Gallery(Context context, File imageFile) {
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
    public static Bitmap getScaledBmpImage(File imageFile, View targetView) {
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
     * @see {@link #calculateInSampleSize(android.graphics.BitmapFactory.Options, int, int)}
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
     * @see {@link #calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)}
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

    /**
     * 通过文件获取Bitmap对象
     * 
     * @param file 文件对象
     * @return 得到的bitmap对象
     */
    public static Bitmap getBitmapFromFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 放大缩小图片
     * 
     * @param bitmap 原始图片
     * @param w 欲缩放的宽度
     * @param h 欲缩放的高度
     * @return 缩放后的图片
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    /**
     * 将Drawable转化为Bitmap
     * 
     * @param drawable
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 获得圆角图片
     * 
     * @param bitmap 原始图片
     * @param roundPx 一般设成14
     * @return 带圆角的图片
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 根据原图片创建倒影图片
     * 
     * @param originalImage 原始图片
     * @return 有倒影的图片
     */
    public static Bitmap createReflectedImage(Bitmap originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        Matrix matrix = new Matrix();
        // 实现图片翻转90度
        matrix.preScale(1, -1);
        // 创建倒影图片（是原始图片的一半大小）
        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);
        // 创建新的图片（原图片 + 倒影图片）
        Bitmap finalReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(finalReflection);
        canvas.drawBitmap(originalImage, 0, 0, null);
        // 把倒影图片画到画布上
        canvas.drawBitmap(reflectionImage, 0, height + 1, null);
        Paint shaderPaint = new Paint();
        // 创建线性渐变LinearGradient对象
        LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, finalReflection.getHeight() + 1, 0x70ffffff, 0x00ffffff,
                TileMode.MIRROR);
        shaderPaint.setShader(shader);
        shaderPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // 画布画出反转图片大小区域，然后把渐变效果加到其中，就出现了图片的倒影效果。
        canvas.drawRect(0, height + 1, width, finalReflection.getHeight(), shaderPaint);
        return finalReflection;
    }

    /**
     * 将bitmap转化为drawable
     * 
     * @param res Resources
     * @param bitmap Bitmap
     * @return drawable
     */
    public static Drawable bitmapToDrawable(Resources res, Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(res, bitmap);
        return drawable;
    }

    /** =======================获取图片类型开始================================ */

    /**
     * 获取图片类型
     * 
     * @param file 图片文件
     * @return 图片的类型
     * @see #getImageType(InputStream)
     */
    public static String getImageType(File file) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            String type = getImageType(in);
            return type;
        } catch (IOException e) {
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取图片的类型
     * 
     * @param in 图片输入流
     * @return 图片的类型
     * @see #getImageType(byte[])
     */
    public static String getImageType(InputStream in) {
        if (in == null) {
            return null;
        }
        try {
            byte[] bytes = new byte[8];
            in.read(bytes);
            return getImageType(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取图片的类型
     * 
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }
    /** =======================获取图片类型结束================================ */
}
