package me.waye.androidspeeder.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @ClassName: FileUtil
 * @Description: 文件操作工具类
 * @author tangwei
 * @date 2013-12-2 下午5:25:11
 * 
 */
public class FileUtil {

    /**
     * 以文本文件的形式保存文件至 /data/data/PACKAGE_NAME/files下.
     * 
     * @param context 上下文
     * @param fileName 文件名
     * @param content 欲保存的内容
     */
    public static void write(Context context, String fileName, String content) {
        FileOutputStream fos = null;
        if (content == null)
            content = "";

        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取/data/data/PACKAGE_NAME/files下的文本文件. 出错时返回空.
     * 
     * @param context 上下文
     * @param fileName 文件名
     * @return 读取的内容,者为空,亦或者null
     * @see {@link #readFromInputStream(FileInputStream)}
     */
    public static String read(Context context, String fileName) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(fileName);
            return readFromInputStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    /**
     * 从输入流中读取文本内容
     * 
     * @param inStream 输入流
     * @return 读取的文本字符串, 或者null
     */
    private static String readFromInputStream(FileInputStream inStream) {
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }

            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据指定目录和文件名创建文件
     * 
     * @param folderPath 目录路径
     * @param fileName 文件名
     * @return 创建的文件
     */
    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }

    /**
     * 根据文件绝对路径获取文件名.
     * 
     * @param filePath 文件路径
     * @return 文件名或空
     */
    public static String getFileName(String filePath) {
        if (StringUtil.isEmpty(filePath))
            return "";
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
    }

    /**
     * 转换文件大小格式
     * 
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 获取目录文件大小
     * 
     * @param dir 目标目录
     * @return 目录大小，如果为null或不是目录的情况将返回0
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 获取目录文件个数
     * 
     * @param 目标目录
     * @return 目录文件个数
     */
    public long getFileList(File dir) {
        long count = 0;
        File[] files = dir.listFiles();
        count = files.length;
        for (File file : files) {
            if (file.isDirectory()) {
                count = count + getFileList(file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     * 将输入流转换成字节数组
     * 
     * @param in 输入流
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] InputStream2Bytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while ((ch = in.read()) != -1) {
            out.write(ch);
        }
        byte buffer[] = out.toByteArray();
        out.close();
        return buffer;
    }

    /**
     * 检查是否安装SD卡
     * 
     * @return true/false
     */
    public static boolean isSDCardAvaliable() {
        String sdCardStatus = Environment.getExternalStorageState();
        boolean status = false;
        if (sdCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }
}