package com.kail.location.inject.fakelocation.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.List;

public interface IHideRootManager extends IInterface {

    abstract class Stub extends Binder implements IHideRootManager {

        private static class Proxy implements IHideRootManager {
            public static IHideRootManager defaultImpl;

            private IBinder remoteBinder;

            Proxy(IBinder remoteBinder) {
                this.remoteBinder = remoteBinder;
            }

            @Override
            public IBinder asBinder() {
                return this.remoteBinder;
            }

            @Override
            public List<String> getHiddenProcesses() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (!this.remoteBinder.transact(10, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHiddenProcesses();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isHideAppListEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (!this.remoteBinder.transact(6, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHideAppListEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public List<String> getHiddenPackages() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (!this.remoteBinder.transact(8, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHiddenPackages();
                    }
                    reply.readException();
                    return reply.createStringArrayList();
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void disableHideRoot() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (this.remoteBinder.transact(3, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().disableHideRoot();
                    }
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public boolean isHideRootEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (!this.remoteBinder.transact(4, data, reply, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHideRootEnabled();
                    }
                    reply.readException();
                    return reply.readInt() != 0;
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void updateLicenseState(String licenseToken, String deviceId) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    data.writeString(licenseToken);
                    data.writeString(deviceId);
                    if (this.remoteBinder.transact(1, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().updateLicenseState(licenseToken, deviceId);
                    }
                } catch (android.os.RemoteException ignored) {
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void refreshHideRootEnabled() throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    if (this.remoteBinder.transact(2, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().refreshHideRootEnabled();
                    }
                } catch (android.os.RemoteException ignored) {
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setHideAppListEnabled(boolean enabled) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    data.writeInt(enabled ? 1 : 0);
                    if (this.remoteBinder.transact(5, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().setHideAppListEnabled(enabled);
                    }
                } catch (android.os.RemoteException ignored) {
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setHiddenPackages(List<String> packages) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    data.writeStringList(packages);
                    if (this.remoteBinder.transact(7, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().setHiddenPackages(packages);
                    }
                } catch (android.os.RemoteException ignored) {
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }

            @Override
            public void setHiddenProcesses(List<String> processes) throws android.os.RemoteException {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeInterfaceToken("com.kail.location.aidl.IHideRootManager");
                    data.writeStringList(processes);
                    if (this.remoteBinder.transact(9, data, reply, 0) || Stub.getDefaultImpl() == null) {
                        reply.readException();
                    } else {
                        Stub.getDefaultImpl().setHiddenProcesses(processes);
                    }
                } catch (android.os.RemoteException ignored) {
                } finally {
                    reply.recycle();
                    data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, "com.kail.location.aidl.IHideRootManager");
        }

        public static IHideRootManager asInterface(IBinder binder) {
            if (binder == null) {
                return null;
            }
            IInterface localInterface = binder.queryLocalInterface("com.kail.location.aidl.IHideRootManager");
            return (localInterface == null || !(localInterface instanceof IHideRootManager)) ? new Proxy(binder) : (IHideRootManager) localInterface;
        }

        public static IHideRootManager getDefaultImpl() {
            return Proxy.defaultImpl;
        }

        @Override
        public IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws android.os.RemoteException {
            if (code == 1598968902) {
                reply.writeString("com.kail.location.aidl.IHideRootManager");
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    updateLicenseState(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    refreshHideRootEnabled();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    disableHideRoot();
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    boolean hideRootEnabled = isHideRootEnabled();
                    reply.writeNoException();
                    reply.writeInt(hideRootEnabled ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    setHideAppListEnabled(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    boolean hideAppListEnabled = isHideAppListEnabled();
                    reply.writeNoException();
                    reply.writeInt(hideAppListEnabled ? 1 : 0);
                    return true;
                case 7:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    setHiddenPackages(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    List<String> hiddenPackages = getHiddenPackages();
                    reply.writeNoException();
                    reply.writeStringList(hiddenPackages);
                    return true;
                case 9:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    setHiddenProcesses(data.createStringArrayList());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface("com.kail.location.aidl.IHideRootManager");
                    List<String> hiddenProcesses = getHiddenProcesses();
                    reply.writeNoException();
                    reply.writeStringList(hiddenProcesses);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    List<String> getHiddenProcesses() throws android.os.RemoteException;

    void updateLicenseState(String licenseToken, String deviceId) throws android.os.RemoteException;

    void setHiddenProcesses(List<String> processes) throws android.os.RemoteException;

    void setHideAppListEnabled(boolean enabled) throws android.os.RemoteException;

    boolean isHideAppListEnabled() throws android.os.RemoteException;

    void setHiddenPackages(List<String> packages) throws android.os.RemoteException;

    List<String> getHiddenPackages() throws android.os.RemoteException;

    void disableHideRoot() throws android.os.RemoteException;

    void refreshHideRootEnabled() throws android.os.RemoteException;

    boolean isHideRootEnabled() throws android.os.RemoteException;
}
