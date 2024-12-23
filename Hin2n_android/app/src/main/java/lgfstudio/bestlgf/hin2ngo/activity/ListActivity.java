package lgfstudio.bestlgf.hin2ngo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lgfstudio.bestlgf.hin2ngo.HiN2NGOApplication;
import lgfstudio.bestlgf.hin2ngo.R;
import lgfstudio.bestlgf.hin2ngo.adapter.SettingItemAdapter;
import lgfstudio.bestlgf.hin2ngo.entity.ConfigItemEntity;
import lgfstudio.bestlgf.hin2ngo.event.ErrorEvent;
import lgfstudio.bestlgf.hin2ngo.event.StartEvent;
import lgfstudio.bestlgf.hin2ngo.event.StopEvent;
import lgfstudio.bestlgf.hin2ngo.model.EdgeStatus;
import lgfstudio.bestlgf.hin2ngo.model.Config;
import lgfstudio.bestlgf.hin2ngo.service.N2NService;
import lgfstudio.bestlgf.hin2ngo.storage.db.base.N2NSettingModelDao;
import lgfstudio.bestlgf.hin2ngo.storage.db.base.model.N2NSettingModel;
import lgfstudio.bestlgf.hin2ngo.template.BaseTemplate;
import lgfstudio.bestlgf.hin2ngo.template.CommonTitleTemplate;
import lgfstudio.bestlgf.hin2ngo.tool.ThreadUtils;


/**
 * Created by janiszhang on 2018/5/4.
 */

public class ListActivity extends BaseActivity {

    private static final int REQUECT_CODE_VPN = 2;

    private ListView mSettingsListView;
    private SettingItemAdapter mSettingItemAdapter;
    private ArrayList<ConfigItemEntity> mSettingItemEntities;

    private SharedPreferences mHin2nSp;
    private SharedPreferences.Editor mHin2nEdit;
    private N2NSettingModel mConfigModel;
    private int mTargetSettingPosition;

    @Override
    protected BaseTemplate createTemplate() {
        CommonTitleTemplate titleTemplate = new CommonTitleTemplate(mContext, getString(R.string.title_setting_list));
        titleTemplate.mRightAction.setVisibility(View.VISIBLE);
        titleTemplate.mRightAction.setImageResource(R.mipmap.ic_add);
        titleTemplate.mRightAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, SettingDetailsActivity.class);
                intent.putExtra("type", SettingDetailsActivity.TYPE_SETTING_ADD);
                startActivity(intent);
            }
        });

        titleTemplate.mLeftAction.setVisibility(View.VISIBLE);
        titleTemplate.mLeftAction.setImageResource(R.drawable.titlebar_icon_return_selector);
        titleTemplate.mLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        return titleTemplate;
    }

    @Override
    protected void doOnCreate(Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mHin2nSp = getSharedPreferences("Hin2n", MODE_PRIVATE);
        mHin2nEdit = mHin2nSp.edit();

        mSettingsListView = (ListView) findViewById(R.id.lv_setting_item);

        mSettingItemEntities = new ArrayList<>();

        mSettingItemAdapter = new SettingItemAdapter(this, mSettingItemEntities);

        mSettingsListView.setAdapter(mSettingItemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ThreadUtils.cachedThreadExecutor(new Runnable() {
            @Override
            public void run() {
                N2NSettingModelDao configModelDao = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
                List<N2NSettingModel> configModels = configModelDao.loadAll();

                N2NSettingModel configModel;
                mSettingItemEntities.clear();
                for (int i = 0; i < configModels.size(); i++) {
                    configModel = configModels.get(i);
                    final ConfigItemEntity configItemEntity = new ConfigItemEntity(configModel.getName(), configModel.getId(), configModel.getIsSelcected());

                    configItemEntity.setOnLongClickListener(new ConfigItemEntity.OnLongClickListener() {
                        @Override
                        public void onLongClick(int position) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                            builder.setTitle(getString(R.string.whats_next)).setItems(new String[]{getString(R.string.use), getString(R.string.clone), getString(R.string.delete)}, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int what) {
                                    switch (what) {
                                        // Use
                                        case 0: {
                                            List<N2NSettingModel> settings = configModelDao.loadAll();
                                            for (int iSetting = 0; iSetting < settings.size(); iSetting++) {
                                                N2NSettingModel setting = settings.get(iSetting);
                                                setting.setIsSelcected(iSetting == position);
                                                configModelDao.update(setting);
                                            }
                                            onResume();
                                            onNewConfigSelected(position);
                                            break;
                                        }
                                        // Clone
                                        case 1: {
                                            final ConfigItemEntity configItemEntity = mSettingItemEntities.get(position);
                                            N2NSettingModelDao configModelDao1 = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
                                            N2NSettingModel configModelCopy = configModelDao1.load(configItemEntity.getSaveId());

                                            //1.db update
                                            String copyName = String.format("%s-%s",configModelCopy.getName(), getString(R.string.n_copy));
                                            String copyNameTmp = copyName;

                                            int i = 0;
                                            while (configModelDao1.queryBuilder().where(N2NSettingModelDao.Properties.Name.eq(copyName)).unique() != null) {
                                                i++;
                                                copyName = copyNameTmp + "(" + i + ")";

                                            }

                                            N2NSettingModel configModel = new N2NSettingModel(null, configModelCopy.getVersion(), copyName, configModelCopy.getIpMode(), configModelCopy.getIp(), configModelCopy.getNetmask(), configModelCopy.getCommunity(), configModelCopy.getPassword(), configModelCopy.getDevDesc(), configModelCopy.getSuperNode(), configModelCopy.getMoreSettings(), configModelCopy.getSuperNodeBackup(), configModelCopy.getMacAddr(), configModelCopy.getMtu(), configModelCopy.getLocalIP(), configModelCopy.getHolePunchInterval(), configModelCopy.getResoveSupernodeIP(), configModelCopy.getLocalPort(), configModelCopy.getAllowRouting(), configModelCopy.getDropMuticast(), configModelCopy.isUseHttpTunnel(), configModelCopy.getTraceLevel(), false, configModelCopy.getGatewayIp(), configModelCopy.getDnsServer(), configModelCopy.getEncryptionMode(), configModelCopy.getHeaderEnc());
                                            configModelDao1.insert(configModel);

                                            //2.ui update
                                            final ConfigItemEntity configItemEntity2 = new ConfigItemEntity(configModel.getName(), configModel.getId(), configModel.getIsSelcected());

                                            configItemEntity2.setOnMoreBtnClickListener(new ConfigItemEntity.OnMoreBtnClickListener() {
                                                @Override
                                                public void onClick(int position) {
                                                    Intent intent = new Intent(ListActivity.this, SettingDetailsActivity.class);
                                                    intent.putExtra("type", SettingDetailsActivity.TYPE_SETTING_MODIFY);
                                                    intent.putExtra("saveId", configItemEntity2.getSaveId());

                                                    startActivity(intent);
                                                }
                                            });
                                            mSettingItemEntities.add(configItemEntity2);
                                            mSettingItemAdapter.notifyDataSetChanged();

                                            break;
                                        }
                                        // Delete
                                        case 2: {
                                            final ConfigItemEntity configItemEntity = mSettingItemEntities.get(position);
                                            final ConfigItemEntity finalConfigItemEntity = configItemEntity;
                                            final Long currentSettingId = mHin2nSp.getLong("current_setting_id", -1);
                                            new SweetAlertDialog(ListActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText(getString(R.string.dialog_delete)).setCancelText(getString(R.string.dialog_no)).setConfirmText(getString(R.string.dialog_yes)).showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.cancel();
                                                }
                                            }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    N2NSettingModelDao configModelDao = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
                                                    configModelDao.deleteByKey(finalConfigItemEntity.getSaveId());

                                                    mSettingItemEntities.remove(finalConfigItemEntity);
                                                    mSettingItemAdapter.notifyDataSetChanged();

                                                    if (N2NService.INSTANCE != null && currentSettingId == finalConfigItemEntity.getSaveId()) {
                                                        N2NService.INSTANCE.stop(null);
                                                    }

                                                    sweetAlertDialog.cancel();
                                                }
                                            }).show();
                                        }
                                        break;
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                    configItemEntity.setOnSelectBtnClickListener(new ConfigItemEntity.OnSelectBtnClickListener() {
                        @Override
                        public void onClick(int position) {
                            List<N2NSettingModel> settings = configModelDao.loadAll();
                            for (int iSetting = 0; iSetting < settings.size(); iSetting++) {
                                N2NSettingModel setting = settings.get(iSetting);
                                setting.setIsSelcected(iSetting == position);
                                configModelDao.update(setting);
                            }
                            onResume();
                            onNewConfigSelected(position);
                        }
                    });
                    configItemEntity.setOnMoreBtnClickListener(new ConfigItemEntity.OnMoreBtnClickListener() {
                        @Override
                        public void onClick(int position) {
                            Intent intent = new Intent(ListActivity.this, SettingDetailsActivity.class);
                            intent.putExtra("type", SettingDetailsActivity.TYPE_SETTING_MODIFY);
                            intent.putExtra("saveId", configItemEntity.getSaveId());

                            startActivity(intent);
                        }
                    });
                    mSettingItemEntities.add(configItemEntity);

                    if (configModel.getIsSelcected()) {
                        mHin2nEdit.putLong("current_setting_id", configModel.getId());
                        mHin2nEdit.commit();
                    }
                }
                ThreadUtils.mainThreadExecutor(new Runnable() {
                    @Override
                    public void run() {
                        mSettingItemAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void onNewConfigSelected(int position){
        final Long currentSettingId = mHin2nSp.getLong("current_setting_id", -1);

        ConfigItemEntity configItemEntity = mSettingItemEntities.get(position);
        Long saveId = configItemEntity.getSaveId();
        if (currentSettingId.equals(saveId)) {
            return;
        }

        if (N2NService.INSTANCE != null && N2NService.INSTANCE.getCurrentStatus() != EdgeStatus.RunningStatus.DISCONNECT && N2NService.INSTANCE.getCurrentStatus() != EdgeStatus.RunningStatus.FAILED) {
            new SweetAlertDialog(ListActivity.this, SweetAlertDialog.WARNING_TYPE).setTitleText(getString(R.string.dialog_reconnect)).setCancelText(getString(R.string.dialog_no)).setConfirmText(getString(R.string.dialog_yes)).showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel();
                }
            }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    N2NService.INSTANCE.stop(new Runnable() {
                        @Override
                        public void run() {
                            Intent vpnPrepareIntent = VpnService.prepare(ListActivity.this);
                            if (vpnPrepareIntent != null) {
                                startActivityForResult(vpnPrepareIntent, REQUECT_CODE_VPN);
                            } else {
                                onActivityResult(REQUECT_CODE_VPN, RESULT_OK, null);
                            }
                        }
                    });

                    mTargetSettingPosition = position;

                    if (currentSettingId != -1) {
                        ThreadUtils.cachedThreadExecutor(new Runnable() {
                            @Override
                            public void run() {
                                N2NSettingModel currentSettingItem = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao().load((long) currentSettingId);
                                if (currentSettingItem != null) {
                                    currentSettingItem.setIsSelcected(false);
                                    HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao().update(currentSettingItem);
                                }
                            }
                        });
                    }

                    for (int i = 0; i < mSettingItemEntities.size(); i++) {
                        mSettingItemEntities.get(i).setSelected(false);
                    }
                    ThreadUtils.cachedThreadExecutor(new Runnable() {
                        @Override
                        public void run() {
                            N2NSettingModelDao configModelDao = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
                            mConfigModel = configModelDao.load(mSettingItemEntities.get(position).getSaveId());
                            mConfigModel.setIsSelcected(true);
                            configModelDao.update(mConfigModel);
                            mHin2nEdit.putLong("current_setting_id", mConfigModel.getId());
                            mHin2nEdit.commit();
                            mSettingItemEntities.get(position).setSelected(true);
                            ThreadUtils.mainThreadExecutor(new Runnable() {
                                @Override
                                public void run() {
                                    mSettingItemAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                    sweetAlertDialog.cancel();
                }
            }).show();
        } else {
            if (currentSettingId != -1) {
                ThreadUtils.cachedThreadExecutor(new Runnable() {
                    @Override
                    public void run() {
                        N2NSettingModel currentSettingItem = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao().load((long) currentSettingId);
                        if (currentSettingItem != null) {
                            currentSettingItem.setIsSelcected(false);
                            HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao().update(currentSettingItem);
                        }
                    }
                });
            }

            for (int i = 0; i < mSettingItemEntities.size(); i++) {
                mSettingItemEntities.get(i).setSelected(false);
            }
            ThreadUtils.cachedThreadExecutor(new Runnable() {
                @Override
                public void run() {
                    N2NSettingModelDao configModelDao = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
                    mConfigModel = configModelDao.load(mSettingItemEntities.get(position).getSaveId());
                    mConfigModel.setIsSelcected(true);

                    configModelDao.update(mConfigModel);

                    mHin2nEdit.putLong("current_setting_id", mConfigModel.getId());
                    mHin2nEdit.commit();
                    mSettingItemEntities.get(position).setSelected(true);
                    ThreadUtils.mainThreadExecutor(new Runnable() {
                        @Override
                        public void run() {
                            mSettingItemAdapter.notifyDataSetChanged();
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUECT_CODE_VPN && resultCode == RESULT_OK) {
            ConfigItemEntity configItemEntity = mSettingItemEntities.get(mTargetSettingPosition);

            N2NSettingModelDao configModelDao1 = HiN2NGOApplication.getInstance().getDaoSession().getN2NSettingModelDao();
            N2NSettingModel configModel = configModelDao1.load(configItemEntity.getSaveId());

            Intent intent = new Intent(ListActivity.this, N2NService.class);
            Bundle bundle = new Bundle();

            Config config = new Config(configModel);
            bundle.putParcelable("n2nSettingInfo", config);
            intent.putExtra("Setting", bundle);

            startService(intent);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting_list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStartEvent(StartEvent event) {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStopEvent(StopEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        Toast.makeText(mContext, "~_~Error~_~", Toast.LENGTH_SHORT).show();
    }
}
