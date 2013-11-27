/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.util
 * Filename: IntentUtil.java
 * Created time: Nov 27, 2013
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */

package me.waye.androidspeeder.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * @ClassName: IntentUtil
 * @Description: Intent util
 * @author tangwei
 * @date Nov 27, 2013 11:13:24 AM
 * 
 */
public class IntentUtil {

    /**
     * 检查是否有合适的Activities能够接收这个Intent
     * 
     * @param context The context.
     * @param action The intent action.
     * @return true/false
     */
    public static boolean isActivitiesAvaliableForIntent(Context context, String action) {
        final Intent intent = new Intent(action);
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 检查是否有合适的Services能够接收这个Intent
     * 
     * @param context The context.
     * @param action The intent action.
     * @return true/false
     */
    public static boolean isServicesAvailableForIntent(Context context, String action) {

        final Intent intent = new Intent(action);
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentServices(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
