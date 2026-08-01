package com.kail.location.inject.utils;

import android.os.RemoteException;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IMockAntiDetectionManager;

public class AntiDetectionServiceManager {
    private IMockAntiDetectionManager antiDetectionService;

    private static final class Holder {
        static AntiDetectionServiceManager instance = new AntiDetectionServiceManager();
    }

    public static AntiDetectionServiceManager getInstance() {
        return Holder.instance;
    }

    public List<String> getHookTargetPackages() {
        if (getAntiDetectionService() == null) {
            return null;
        }
        try {
            return this.antiDetectionService.getTargetPackages();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getHiddenFileNames() {
        if (getAntiDetectionService() == null) {
            return null;
        }
        try {
            return this.antiDetectionService.getDetectedPackages();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getHookMethodRules() {
        if (getAntiDetectionService() == null) {
            return null;
        }
        try {
            return this.antiDetectionService.getScopedPackageRules();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IMockAntiDetectionManager getAntiDetectionService() {
        if (this.antiDetectionService == null) {
            try {
                this.antiDetectionService = IMockAntiDetectionManager.Stub.asInterface(ServiceManagerBridge.getService(ClassLoader.getSystemClassLoader(), "oem_security"));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return this.antiDetectionService;
    }

    public boolean isAntiDetectionEnabled() {
        if (getAntiDetectionService() == null) {
            return false;
        }
        try {
            return this.antiDetectionService.isPackageManagerHookEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isFileNameHidingEnabled() {
        if (getAntiDetectionService() == null) {
            return false;
        }
        try {
            return this.antiDetectionService.isPackageVisibilityFilteringEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHookRulesEnabled() {
        if (getAntiDetectionService() == null) {
            return false;
        }
        try {
            return this.antiDetectionService.isPackageFilterEnabled();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
