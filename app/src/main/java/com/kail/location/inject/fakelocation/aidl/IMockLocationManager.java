package com.kail.location.inject.fakelocation.aidl;

import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import java.util.List;
import com.kail.location.inject.fakelocation.model.CellTowerInfo;

public interface IMockLocationManager extends IInterface {

    String DESCRIPTOR = "com.kail.location.aidl.IMockLocationManager";

    abstract class Stub extends Binder implements IMockLocationManager {

        private static class Proxy implements IMockLocationManager {
            public static IMockLocationManager defaultImpl;

            private final IBinder remoteBinder;

            Proxy(IBinder remoteBinder) {
                this.remoteBinder = remoteBinder;
            }

            @Override
            public IBinder asBinder() {
                return this.remoteBinder;
            }

            @Override
            public List<String> getSafeApps() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(33, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getSafeApps();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<String> getAllowMockPackages() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(10, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getAllowMockPackages();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isMocking() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(4, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isMocking();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isMockGpsStatus() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(22, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isMockGpsStatus();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public Location getMockLocation() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(6, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getMockLocation();
                    }
                    reply.readException();
                    return reply.readInt() != 0 ? Location.CREATOR.createFromParcel(reply) : null;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isStepSensorMocking() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(13, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isStepSensorMocking();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<CellTowerInfo> getMockCells() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(21, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getMockCells();
                    }
                    reply.readException();
                    return reply.createTypedArrayList(CellTowerInfo.CREATOR);
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<SubscriptionInfo> getMockSubscriptionInfo() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(29, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getMockSubscriptionInfo();
                    }
                    reply.readException();
                    return reply.createTypedArrayList(SubscriptionInfo.CREATOR);
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isMockSubscriptionInfoEnabled() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(31, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isMockSubscriptionInfoEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isMockSourceTagEnabled() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(25, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isMockSourceTagEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isSensorFeatureEnabled() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(26, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().isSensorFeatureEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public long getIntervalTimeout() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(8, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getIntervalTimeout();
                    }
                    reply.readException();
                    return reply.readLong();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public long getMockStepCount() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(17, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getMockStepCount();
                    }
                    reply.readException();
                    return reply.readLong();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public float getStepSpeed() throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (!remoteBinder.transact(15, data, reply, 0) && getDefaultImpl() != null) {
                        return getDefaultImpl().getStepSpeed();
                    }
                    reply.readException();
                    return reply.readFloat();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            // ---- Writes ----

            @Override
            public void updateLicenseState(String licenseToken, String deviceId) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeString(licenseToken);
                    data.writeString(deviceId);
                    if (!remoteBinder.transact(1, data, reply, 0) && getDefaultImpl() != null) {
                        getDefaultImpl().updateLicenseState(licenseToken, deviceId);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void startMockLocation() throws RemoteException {
                writeNoArg(2);
            }

            @Override
            public void stopMockLocation() throws RemoteException {
                writeNoArg(3);
            }

            @Override
            public void setMockLocation(Location location) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    if (location != null) {
                        data.writeInt(1);
                        location.writeToParcel(data, 0);
                    } else {
                        data.writeInt(0);
                    }
                    if (!remoteBinder.transact(5, data, reply, 0) && getDefaultImpl() != null) {
                        getDefaultImpl().setMockLocation(location);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setIntervalTimeout(long intervalMillis) throws RemoteException {
                writeLong(7, intervalMillis);
            }

            @Override
            public void setAllowMockPackages(List<String> packages) throws RemoteException {
                writeStringList(9, packages);
            }

            @Override
            public void startStepSensorMock() throws RemoteException {
                writeNoArg(11);
            }

            @Override
            public void stopStepSensorMock() throws RemoteException {
                writeNoArg(12);
            }

            @Override
            public void setStepSpeed(float speed) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeFloat(speed);
                    if (!remoteBinder.transact(14, data, reply, 0) && getDefaultImpl() != null) {
                        getDefaultImpl().setStepSpeed(speed);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setStepCountOffset(long stepCount) throws RemoteException {
                writeLong(16, stepCount);
            }

            @Override
            public void addOnMockListener(IBinder listenerBinder) throws RemoteException {
                writeBinder(18, listenerBinder);
            }

            @Override
            public void removeOnMockListener(IBinder listenerBinder) throws RemoteException {
                writeBinder(19, listenerBinder);
            }

            @Override
            public void setMockCells(List<CellTowerInfo> cells) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeTypedList(cells);
                    if (!remoteBinder.transact(20, data, reply, 0) && getDefaultImpl() != null) {
                        getDefaultImpl().setMockCells(cells);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setMockGpsStatus(boolean enabled) throws RemoteException {
                writeBool(23, enabled);
            }

            @Override
            public void setMockSourceTagEnabled(boolean enabled) throws RemoteException {
                writeBool(24, enabled);
            }

            @Override
            public void setSensorFeatureEnabled(boolean enabled) throws RemoteException {
                writeBool(27, enabled);
            }

            @Override
            public void setMockSubscriptionInfo(List<SubscriptionInfo> subscriptionInfo) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeTypedList(subscriptionInfo);
                    if (!remoteBinder.transact(28, data, reply, 0) && getDefaultImpl() != null) {
                        getDefaultImpl().setMockSubscriptionInfo(subscriptionInfo);
                    } else {
                        reply.readException();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setMockSubscriptionInfoEnabled(boolean enabled) throws RemoteException {
                writeBool(30, enabled);
            }

            @Override
            public void setSafeApps(List<String> safeApps) throws RemoteException {
                writeStringList(32, safeApps);
            }

            @Override
            public void setBaseStepCount(long stepCount) throws RemoteException {
                writeLong(34, stepCount);
            }

            // ---- helpers ----

            private void writeNoArg(int code) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeBool(int code, boolean v) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeInt(v ? 1 : 0);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeLong(int code, long v) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeLong(v);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeStringList(int code, List<String> list) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeStringList(list);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeBinder(int code, IBinder b) throws RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken(DESCRIPTOR);
                    data.writeStrongBinder(b);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMockLocationManager asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            IInterface localInterface = binder.queryLocalInterface(DESCRIPTOR);
            if (localInterface != null && localInterface instanceof IMockLocationManager) {
                return (IMockLocationManager) localInterface;
            }
            return new Proxy(binder);
        }

        public static IMockLocationManager getDefaultImpl() {
            return Proxy.defaultImpl;
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    updateLicenseState(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    startMockLocation();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    stopMockLocation();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mocking = isMocking();
                    reply.writeNoException();
                    reply.writeInt(mocking ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    setMockLocation(data.readInt() != 0 ? Location.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    Location mockLocation = getMockLocation();
                    reply.writeNoException();
                    if (mockLocation != null) {
                        reply.writeInt(1);
                        mockLocation.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    setIntervalTimeout(data.readLong());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    long intervalTimeout = getIntervalTimeout();
                    reply.writeNoException();
                    reply.writeLong(intervalTimeout);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setAllowMockPackages(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> allowMockPackages = getAllowMockPackages();
                    reply.writeNoException();
                    reply.writeStringList(allowMockPackages);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    startStepSensorMock();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    stopStepSensorMock();
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean stepSensorMocking = isStepSensorMocking();
                    reply.writeNoException();
                    reply.writeInt(stepSensorMocking ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    setStepSpeed(data.readFloat());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    float stepSpeed = getStepSpeed();
                    reply.writeNoException();
                    reply.writeFloat(stepSpeed);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    setStepCountOffset(data.readLong());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    long mockStepCount = getMockStepCount();
                    reply.writeNoException();
                    reply.writeLong(mockStepCount);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    addOnMockListener(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    removeOnMockListener(data.readStrongBinder());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    setMockCells(data.createTypedArrayList(CellTowerInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    List<CellTowerInfo> mockCells = getMockCells();
                    reply.writeNoException();
                    reply.writeTypedList(mockCells);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mockGpsStatus = isMockGpsStatus();
                    reply.writeNoException();
                    reply.writeInt(mockGpsStatus ? 1 : 0);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    setMockGpsStatus(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    setMockSourceTagEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mockSourceTagEnabled = isMockSourceTagEnabled();
                    reply.writeNoException();
                    reply.writeInt(mockSourceTagEnabled ? 1 : 0);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    boolean sensorFeatureEnabled = isSensorFeatureEnabled();
                    reply.writeNoException();
                    reply.writeInt(sensorFeatureEnabled ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    setSensorFeatureEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    setMockSubscriptionInfo(data.createTypedArrayList(SubscriptionInfo.CREATOR));
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    List<SubscriptionInfo> mockSubscriptionInfo = getMockSubscriptionInfo();
                    reply.writeNoException();
                    reply.writeTypedList(mockSubscriptionInfo);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    setMockSubscriptionInfoEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    boolean mockSubscriptionInfoEnabled = isMockSubscriptionInfoEnabled();
                    reply.writeNoException();
                    reply.writeInt(mockSubscriptionInfoEnabled ? 1 : 0);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    setSafeApps(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> safeApps = getSafeApps();
                    reply.writeNoException();
                    reply.writeStringList(safeApps);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    setBaseStepCount(data.readLong());
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    List<String> getSafeApps() throws RemoteException;

    void updateLicenseState(String licenseToken, String deviceId) throws RemoteException;

    void setSafeApps(List<String> safeApps) throws RemoteException;

    void setMockSubscriptionInfoEnabled(boolean enabled) throws RemoteException;

    List<String> getAllowMockPackages() throws RemoteException;

    void setAllowMockPackages(List<String> packages) throws RemoteException;

    boolean isMocking() throws RemoteException;

    boolean isMockGpsStatus() throws RemoteException;

    Location getMockLocation() throws RemoteException;

    boolean isStepSensorMocking() throws RemoteException;

    void setMockCells(List<CellTowerInfo> cells) throws RemoteException;

    void removeOnMockListener(IBinder listenerBinder) throws RemoteException;

    void setSensorFeatureEnabled(boolean enabled) throws RemoteException;

    void setIntervalTimeout(long intervalMillis) throws RemoteException;

    void setMockLocation(Location location) throws RemoteException;

    void setMockSourceTagEnabled(boolean enabled) throws RemoteException;

    boolean isMockSubscriptionInfoEnabled() throws RemoteException;

    List<CellTowerInfo> getMockCells() throws RemoteException;

    void setStepCountOffset(long stepCount) throws RemoteException;

    void stopMockLocation() throws RemoteException;

    long getMockStepCount() throws RemoteException;

    void setMockSubscriptionInfo(List<SubscriptionInfo> subscriptionInfo) throws RemoteException;

    void setMockGpsStatus(boolean enabled) throws RemoteException;

    void addOnMockListener(IBinder listenerBinder) throws RemoteException;

    void startStepSensorMock() throws RemoteException;

    long getIntervalTimeout() throws RemoteException;

    boolean isSensorFeatureEnabled() throws RemoteException;

    void setBaseStepCount(long stepCount) throws RemoteException;

    void setStepSpeed(float speed) throws RemoteException;

    boolean isMockSourceTagEnabled() throws RemoteException;

    void stopStepSensorMock() throws RemoteException;

    List<SubscriptionInfo> getMockSubscriptionInfo() throws RemoteException;

    float getStepSpeed() throws RemoteException;

    void startMockLocation() throws RemoteException;
}
