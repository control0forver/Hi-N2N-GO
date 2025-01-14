package lgfstudio.bestlgf.hin2ngo.entity;

/**
 * Created by janiszhang on 2018/5/5.
 */

public class ConfigItemEntity {
    private String mSettingName;
    private Long mSaveId;
    private boolean isSelected;

    public ConfigItemEntity(String settingName, Long saveId, boolean isSelected) {
        mSettingName = settingName;
        mSaveId = saveId;
        this.isSelected = isSelected;
    }

    public String getSettingName() {
        return mSettingName;
    }

    public void setSettingName(String settingName) {
        mSettingName = settingName;
    }

    public Long getSaveId() {
        return mSaveId;
    }

    public void setSaveId(Long saveId) {
        mSaveId = saveId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public OnLongClickListener mOnLongClickListener;
    public OnSelectBtnClickListener mOnSelectBtnClickListener;
    public OnMoreBtnClickListener mOnMoreBtnClickListener;

    public void setOnLongClickListener(OnLongClickListener OnLongClickListener) {
        mOnLongClickListener = OnLongClickListener;
    }

    public void setOnSelectBtnClickListener(OnSelectBtnClickListener OnSelectBtnClickListener) {
        mOnSelectBtnClickListener = OnSelectBtnClickListener;
    }

    public void setOnMoreBtnClickListener(OnMoreBtnClickListener onMoreBtnClickListener) {
        mOnMoreBtnClickListener = onMoreBtnClickListener;
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    public interface OnMoreBtnClickListener {
        void onClick(int position);
    }

    public interface OnSelectBtnClickListener {
        void onClick(int position);
    }
}
