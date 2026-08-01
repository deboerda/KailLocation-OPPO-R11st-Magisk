package com.kail.location.inject.fakelocation.service;

import com.kail.location.inject.utils.LicenseStateManager;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IHideRootManager;

public class HideRootManagerService extends IHideRootManager.Stub {

    private static boolean hideRootEnabled;

    private static List<String> hiddenPackages;

    private static List<String> hiddenProcesses;

    private Object configLock = new Object();

    private boolean hideAppListEnabled;

    @Override
    public List<String> getHiddenProcesses() {
        List<String> processes;
        synchronized (this.configLock) {
            processes = hiddenProcesses;
        }
        return processes;
    }

    @Override
    public void updateLicenseState(String licenseToken, String deviceId) {
        LicenseStateManager.updateLicenseState(licenseToken, deviceId);
    }

    @Override
    public void setHiddenProcesses(List<String> processes) {
        synchronized (this.configLock) {
            hiddenProcesses = processes;
        }
    }

    @Override
    public void setHideAppListEnabled(boolean enabled) {
        this.hideAppListEnabled = enabled;
    }

    @Override
    public boolean isHideAppListEnabled() {
        return this.hideAppListEnabled;
    }

    @Override
    public void setHiddenPackages(List<String> packages) {
        synchronized (this.configLock) {
            hiddenPackages = packages;
        }
    }

    @Override
    public List<String> getHiddenPackages() {
        List<String> packages;
        synchronized (this.configLock) {
            packages = hiddenPackages;
        }
        return packages;
    }

    @Override
    public void disableHideRoot() {
        hideRootEnabled = false;
    }

    @Override
    public void refreshHideRootEnabled() {
        hideRootEnabled = LicenseStateManager.isLicenseUsable();
    }

    @Override
    public boolean isHideRootEnabled() {
        return hideRootEnabled;
    }
}
