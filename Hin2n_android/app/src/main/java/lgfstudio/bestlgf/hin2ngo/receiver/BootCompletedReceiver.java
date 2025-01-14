package lgfstudio.bestlgf.hin2ngo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import lgfstudio.bestlgf.hin2ngo.HiN2NGOApplication;
import lgfstudio.bestlgf.hin2ngo.model.Config;
import lgfstudio.bestlgf.hin2ngo.service.N2NService;
import lgfstudio.bestlgf.hin2ngo.storage.db.base.model.N2NSettingModel;
import lgfstudio.bestlgf.hin2ngo.tool.ThreadUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        ThreadUtils.mainThreadExecutor(new Runnable() {
            @Override
            public void run() {
                SharedPreferences n2nSp = context.getSharedPreferences("Hin2n", Context.MODE_PRIVATE);
                // 如果没有配置开机启动,就直接退出
                if (!n2nSp.getBoolean("start_at_boot", false))
                    return;

                // 获取当前设置,没有的话直接退出
                long currentSettingId = n2nSp.getLong("current_setting_id", -1);
                if (currentSettingId < 0)
                    return;

                Intent vpnPrepareIntent = VpnService.prepare(context);
                if (vpnPrepareIntent != null)
                    // 说明用户之前还没有同意过vpn服务,不适合开机运行,直接退出即可
                    return;

                N2NSettingModel mCurrentSettingInfo = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao().load((long) currentSettingId);
                if (mCurrentSettingInfo == null)
                    return;

                // 启动服务
                Intent i = new Intent(context, N2NService.class);
                Bundle bundle = new Bundle();
                Config config = new Config(mCurrentSettingInfo);
                bundle.putParcelable("n2nSettingInfo", config);
                i.putExtra("Setting", bundle);

                ContextCompat.startForegroundService(context,i);
            }
        });
    }
}