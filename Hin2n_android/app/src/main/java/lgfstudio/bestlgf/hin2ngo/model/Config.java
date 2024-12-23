package lgfstudio.bestlgf.hin2ngo.model;

import android.os.Parcel;
import android.os.Parcelable;

import lgfstudio.bestlgf.hin2ngo.storage.db.base.model.N2NSettingModel;

/**
 * Created by janiszhang on 2018/5/11.
 */

public class Config implements Parcelable {

    Long id;
    String name;
    int framework;
    int ipMode;
    String ip;
    String netmask;
    String community;
    /**
     * 后续需要加密存储
     */
    String password;
    String devDesc;
    String superNode;
    boolean moreSettings;
    String superNodeBackup;
    String macAddr;
    int mtu;
    String localIP;
    int holePunchInterval;
    boolean resoveSupernodeIP;
    int localPort;
    boolean allowRouting;
    boolean dropMuticast;
    int traceLevel;
    boolean useHttpTunnel;
    String gatewayIp;
    String dnsServer;
    String encryptionMode;
    boolean headerEnc;

    public Config(N2NSettingModel configModel) {
        this.id = configModel.getId();
        this.name = configModel.getName();
        this.framework = configModel.getVersion();
        this.ipMode = configModel.getIpMode();
        this.ip = configModel.getIp();
        this.netmask = configModel.getNetmask();
        this.community = configModel.getCommunity();
        this.password = configModel.getPassword();
        this.devDesc = configModel.getDevDesc();
        this.superNode = configModel.getSuperNode();
        this.moreSettings = configModel.getMoreSettings();
        this.superNodeBackup = configModel.getSuperNodeBackup();
        this.macAddr = configModel.getMacAddr();
        this.mtu = configModel.getMtu();
        this.localIP = configModel.getLocalIP();
        this.holePunchInterval = configModel.getHolePunchInterval();
        this.resoveSupernodeIP = configModel.getResoveSupernodeIP();
        this.localPort = configModel.getLocalPort();
        this.allowRouting = configModel.getAllowRouting();
        this.dropMuticast = configModel.getDropMuticast();
        this.traceLevel = configModel.getTraceLevel();
        this.useHttpTunnel = configModel.isUseHttpTunnel();
        this.gatewayIp = configModel.getGatewayIp();
        this.dnsServer = configModel.getDnsServer();
        this.encryptionMode = configModel.getEncryptionMode();
        this.headerEnc = configModel.getHeaderEnc();
    }

    protected Config(Parcel in) {
        id = in.readLong();
        name = in.readString();
        framework = in.readInt();
        ipMode = in.readInt();
        ip = in.readString();
        netmask = in.readString();
        community = in.readString();
        password = in.readString();
        devDesc = in.readString();
        superNode = in.readString();
        moreSettings = in.readByte() != 0;
        superNodeBackup = in.readString();
        macAddr = in.readString();
        mtu = in.readInt();
        localIP = in.readString();
        holePunchInterval = in.readInt();
        resoveSupernodeIP = in.readByte() != 0;
        localPort = in.readInt();
        allowRouting = in.readByte() != 0;
        dropMuticast = in.readByte() != 0;
        traceLevel = in.readInt();
        useHttpTunnel = in.readByte() != 0;
        gatewayIp = in.readString();
        dnsServer = in.readString();
        encryptionMode = in.readString();
        headerEnc = in.readByte() != 0;
    }

    public static final Creator<Config> CREATOR = new Creator<Config>() {
        @Override
        public Config createFromParcel(Parcel in) {
            return new Config(in);
        }

        @Override
        public Config[] newArray(int size) {
            return new Config[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIpMode() {return ipMode;}

    public void setIpMode(int ipMode) { this.ipMode = ipMode; }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSuperNode() {
        return superNode;
    }

    public void setSuperNode(String superNode) {
        this.superNode = superNode;
    }

    public String getDevDesc() { return devDesc; }

    public void setDevDesc(String devDesc) { this.devDesc = devDesc; }

    public boolean isMoreSettings() {
        return moreSettings;
    }

    public void setMoreSettings(boolean moreSettings) {
        this.moreSettings = moreSettings;
    }

    public String getSuperNodeBackup() {
        return superNodeBackup;
    }

    public void setSuperNodeBackup(String superNodeBackup) {
        this.superNodeBackup = superNodeBackup;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public int getMtu() {
        return mtu;
    }

    public void setMtu(int mtu) {
        this.mtu = mtu;
    }

    public String getLocalIP() {
        return localIP;
    }

    public void setLocalIP(String localIP) {
        this.localIP = localIP;
    }

    public int getHolePunchInterval() {
        return holePunchInterval;
    }

    public void setHolePunchInterval(int holePunchInterval) {
        this.holePunchInterval = holePunchInterval;
    }

    public boolean isResoveSupernodeIP() {
        return resoveSupernodeIP;
    }

    public void setResoveSupernodeIP(boolean resoveSupernodeIP) {
        this.resoveSupernodeIP = resoveSupernodeIP;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public boolean isAllowRouting() {
        return allowRouting;
    }

    public void setAllowRouting(boolean allowRouting) {
        this.allowRouting = allowRouting;
    }

    public boolean isDropMuticast() {
        return dropMuticast;
    }

    public void setDropMuticast(boolean dropMuticast) {
        this.dropMuticast = dropMuticast;
    }

    public int getTraceLevel() {
        return traceLevel;
    }

    public void setTraceLevel(int traceLevel) {
        this.traceLevel = traceLevel;
    }

    public int getFramework() {
        return framework;
    }

    public void setFramework(int framework) {
        this.framework = framework;
    }

    public boolean isUseHttpTunnel() {
        return useHttpTunnel;
    }

    public void setUseHttpTunnel(boolean useHttpTunnel) {
        this.useHttpTunnel = useHttpTunnel;
    }

    public String getGatewayIp() {
        return gatewayIp;
    }

    public String getDnsServer() {
        return dnsServer;
    }

    public String getEncryptionMode() { return encryptionMode; }

    public boolean isHeaderEnc() {
        return headerEnc;
    }

    public void setHeaderEnc(boolean headerEnc) {
        this.headerEnc = headerEnc;
    }

    @Override
    public String toString() {
        return "N2NSettingInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", version=" + framework +
                ", ipMode=" + ipMode +
                ", ip='" + ip + '\'' +
                ", netmask='" + netmask + '\'' +
                ", community='" + community + '\'' +
                ", password='" + password + '\'' +
                ", devDesc='" + devDesc + '\'' +
                ", superNode='" + superNode + '\'' +
                ", moreSettings=" + moreSettings +
                ", superNodeBackup='" + superNodeBackup + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", mtu=" + mtu +
                ", localIP='" + localIP + '\'' +
                ", holePunchInterval=" + holePunchInterval +
                ", resoveSupernodeIP=" + resoveSupernodeIP +
                ", localPort=" + localPort +
                ", allowRouting=" + allowRouting +
                ", dropMuticast=" + dropMuticast +
                ", traceLevel=" + traceLevel +
                ", useHttpTunnel=" + useHttpTunnel +
                ", gatewayIp=" + gatewayIp +
                ", dnsServer=" + dnsServer +
                ", encryptionMode=" + encryptionMode +
                ", headerEnc=" + headerEnc +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (id == null) {
            parcel.writeLong(new Long(-1));
        } else {
            parcel.writeLong(id);
        }
        parcel.writeString(name);
        parcel.writeInt(framework);
        parcel.writeInt(ipMode);
        parcel.writeString(ip);
        parcel.writeString(netmask);
        parcel.writeString(community);
        parcel.writeString(password);
        parcel.writeString(devDesc);
        parcel.writeString(superNode);
        parcel.writeByte((byte) (moreSettings ? 1 : 0));
        parcel.writeString(superNodeBackup);
        parcel.writeString(macAddr);
        parcel.writeInt(mtu);
        parcel.writeString(localIP);
        parcel.writeInt(holePunchInterval);
        parcel.writeByte((byte) (resoveSupernodeIP ? 1 : 0));
        parcel.writeInt(localPort);
        parcel.writeByte((byte) (allowRouting ? 1 : 0));
        parcel.writeByte((byte) (dropMuticast ? 1 : 0));
        parcel.writeInt(traceLevel);
        parcel.writeByte((byte) (useHttpTunnel ? 1 : 0));
        parcel.writeString(gatewayIp);
        parcel.writeString(dnsServer);
        parcel.writeString(encryptionMode);
        parcel.writeByte((byte) (headerEnc ? 2 : 0));
    }
}
