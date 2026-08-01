package com.kail.location.inject.fakelocation.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;

public interface IMockAntiDetectionManager extends IInterface {

    abstract class Stub extends Binder implements IMockAntiDetectionManager {

        private static class Proxy implements IMockAntiDetectionManager {
            public static IMockAntiDetectionManager defaultImpl;

            private IBinder remoteBinder;

            Proxy(IBinder remoteBinder) {
                this.remoteBinder = remoteBinder;
            }

            @Override
            public IBinder asBinder() {
                return this.remoteBinder;
            }

            @Override
            public List<String> getScopedPackageRules() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(14, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getScopedPackageRules();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<String> getTargetPackages() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(12, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTargetPackages();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isPackageFilterEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(6, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageFilterEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isPackageVisibilityFilteringEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(8, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageVisibilityFilteringEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isPackageManagerHookEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(4, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPackageManagerHookEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<String> getDetectedPackages() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    if (!this.remoteBinder.transact(10, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDetectedPackages();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void updateLicenseState(String licenseToken, String deviceId) throws android.os.RemoteException {
                writeStringPair(1, licenseToken, deviceId);
            }

            @Override
            public void refreshPackageManagerHookEnabled() throws android.os.RemoteException {
                writeNoArg(2);
            }

            @Override
            public void disablePackageManagerHook() throws android.os.RemoteException {
                writeNoArg(3);
            }

            @Override
            public void setPackageFilterEnabled(boolean enabled) throws android.os.RemoteException {
                writeBool(5, enabled);
            }

            @Override
            public void setPackageVisibilityFilterEnabled(boolean enabled) throws android.os.RemoteException {
                writeBool(7, enabled);
            }

            @Override
            public void setDetectedPackages(List<String> detectedPackages) throws android.os.RemoteException {
                writeStringList(9, detectedPackages);
            }

            @Override
            public void setTargetPackages(List<String> targetPackages) throws android.os.RemoteException {
                writeStringList(11, targetPackages);
            }

            @Override
            public void setScopedPackageRules(List<String> scopedPackageRules) throws android.os.RemoteException {
                writeStringList(13, scopedPackageRules);
            }

            private void writeNoArg(int code) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeBool(int code, boolean v) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    data.writeInt(v ? 1 : 0);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeStringList(int code, List<String> list) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    data.writeStringList(list);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            private void writeStringPair(int code, String a, String b) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IMockAntiDetectionManager");
                    data.writeString(a);
                    data.writeString(b);
                    remoteBinder.transact(code, data, reply, 0);
                    reply.readException();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.kail.location.aidl.IMockAntiDetectionManager");
        }

        public static IMockAntiDetectionManager asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            IInterface localInterface = binder.queryLocalInterface("com.kail.location.aidl.IMockAntiDetectionManager");
            return (localInterface == null || !(localInterface instanceof IMockAntiDetectionManager)) ? new Proxy(binder) : (IMockAntiDetectionManager) localInterface;
        }

        public static IMockAntiDetectionManager getDefaultImpl() {
            return Proxy.defaultImpl;
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws android.os.RemoteException {
            if (code == 1598968902) {
                reply.writeString("com.kail.location.aidl.IMockAntiDetectionManager");
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    updateLicenseState(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    refreshPackageManagerHookEnabled();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    disablePackageManagerHook();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    boolean packageManagerHookEnabled = isPackageManagerHookEnabled();
                    reply.writeNoException();
                    reply.writeInt(packageManagerHookEnabled ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    setPackageFilterEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    boolean packageFilterEnabled = isPackageFilterEnabled();
                    reply.writeNoException();
                    reply.writeInt(packageFilterEnabled ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    setPackageVisibilityFilterEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    boolean packageVisibilityFilteringEnabled = isPackageVisibilityFilteringEnabled();
                    reply.writeNoException();
                    reply.writeInt(packageVisibilityFilteringEnabled ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    setDetectedPackages(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    List<String> detectedPackages = getDetectedPackages();
                    reply.writeNoException();
                    reply.writeStringList(detectedPackages);
                    return true;
                case 11:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    setTargetPackages(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    List<String> targetPackages = getTargetPackages();
                    reply.writeNoException();
                    reply.writeStringList(targetPackages);
                    return true;
                case 13:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    setScopedPackageRules(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface("com.kail.location.aidl.IMockAntiDetectionManager");
                    List<String> scopedPackageRules = getScopedPackageRules();
                    reply.writeNoException();
                    reply.writeStringList(scopedPackageRules);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    List<String> getScopedPackageRules() throws android.os.RemoteException;

    void updateLicenseState(String licenseToken, String deviceId) throws android.os.RemoteException;

    void setScopedPackageRules(List<String> scopedPackageRules) throws android.os.RemoteException;

    void setPackageFilterEnabled(boolean enabled) throws android.os.RemoteException;

    List<String> getTargetPackages() throws android.os.RemoteException;

    void setTargetPackages(List<String> targetPackages) throws android.os.RemoteException;

    boolean isPackageFilterEnabled() throws android.os.RemoteException;

    boolean isPackageVisibilityFilteringEnabled() throws android.os.RemoteException;

    void disablePackageManagerHook() throws android.os.RemoteException;

    boolean isPackageManagerHookEnabled() throws android.os.RemoteException;

    List<String> getDetectedPackages() throws android.os.RemoteException;

    void refreshPackageManagerHookEnabled() throws android.os.RemoteException;

    void setPackageVisibilityFilterEnabled(boolean enabled) throws android.os.RemoteException;

    void setDetectedPackages(List<String> detectedPackages) throws android.os.RemoteException;
}
