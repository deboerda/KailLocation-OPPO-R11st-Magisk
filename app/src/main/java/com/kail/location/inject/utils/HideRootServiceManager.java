package com.kail.location.inject.utils;

import android.os.RemoteException;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IHideRootManager;

public class HideRootServiceManager {
    private IHideRootManager hideRootService;

    private static final class Holder {
        static HideRootServiceManager instance = new HideRootServiceManager();
    }

    public static HideRootServiceManager getInstance() {
        return Holder.instance;
    }

    public List<String> getHiddenPackages() {
        if (getHideRootService() == null) {
            return null;
        }
        try {
            return this.hideRootService.getHiddenPackages();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getHiddenProcesses() {
        if (getHideRootService() == null) {
            return null;
        }
        try {
            return this.hideRootService.getHiddenProcesses();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IHideRootManager getHideRootService() {
        if (this.hideRootService == null) {
            try {
                this.hideRootService = IHideRootManager.Stub.asInterface(ServiceManagerBridge.getService(ClassLoader.getSystemClassLoader(), "oem_integrity"));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return this.hideRootService;
    }

    public boolean isHideRootEnabled() {
        if (getHideRootService() == null) {
            return false;
        }
        try {
            return this.hideRootService.isHideRootEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHideAppListEnabled() {
        if (getHideRootService() == null) {
            return false;
        }
        try {
            return this.hideRootService.isHideAppListEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disableHideRoot() {
        if (getHideRootService() == null) {
            return;
        }
        try {
            this.hideRootService.disableHideRoot();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
