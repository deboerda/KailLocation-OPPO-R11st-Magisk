package com.kail.location.inject.fakelocation.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;
import com.kail.location.inject.fakelocation.model.MockWifiNetwork;

public interface IMockWifiManager extends IInterface {

    abstract class Stub extends Binder implements IMockWifiManager {
        public Stub() {
            attachInterface(this, "com.kail.location.aidl.IMockWifiManager");
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws android.os.RemoteException {
            if (code == 1598968902) {
                reply.writeString("com.kail.location.aidl.IMockWifiManager");
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    updateLicenseState(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    startMockWifi();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    stopMockWifi();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    boolean mockWifiEnabled = isMockWifiEnabled();
                    reply.writeNoException();
                    reply.writeInt(mockWifiEnabled ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    setPrimaryMockWifiNetwork(data.readInt() != 0 ? MockWifiNetwork.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    MockWifiNetwork primaryMockWifiNetwork = getPrimaryMockWifiNetwork();
                    reply.writeNoException();
                    if (primaryMockWifiNetwork != null) {
                        reply.writeInt(1);
                        primaryMockWifiNetwork.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    setAllowMockPackages(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    List<String> allowMockPackages = getAllowMockPackages();
                    reply.writeNoException();
                    reply.writeStringList(allowMockPackages);
                    return true;
                case 9:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    setMockWifiNetworks(data.createTypedArrayList(MockWifiNetwork.CREATOR));
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    List<MockWifiNetwork> mockWifiNetworks = getMockWifiNetworks();
                    reply.writeNoException();
                    reply.writeTypedList(mockWifiNetworks);
                    return true;
                case 11:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    setScopedAllowMockRules(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface("com.kail.location.aidl.IMockWifiManager");
                    List<String> scopedAllowMockRules = getScopedAllowMockRules();
                    reply.writeNoException();
                    reply.writeStringList(scopedAllowMockRules);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    List<String> getScopedAllowMockRules();

    void updateLicenseState(String licenseToken, String deviceId);

    void setScopedAllowMockRules(List<String> scopedAllowMockRules);

    List<String> getAllowMockPackages();

    void setAllowMockPackages(List<String> packages);

    boolean isMockWifiEnabled();

    List<MockWifiNetwork> getMockWifiNetworks();

    MockWifiNetwork getPrimaryMockWifiNetwork();

    void setPrimaryMockWifiNetwork(MockWifiNetwork network);

    void startMockWifi();

    void setMockWifiNetworks(List<MockWifiNetwork> networks);

    void stopMockWifi();
}
