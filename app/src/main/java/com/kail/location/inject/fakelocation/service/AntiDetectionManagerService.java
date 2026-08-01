package com.kail.location.inject.fakelocation.service;

import com.kail.location.inject.utils.LicenseStateManager;
import com.kail.location.inject.utils.PackageAntiDetectionConfig;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IMockAntiDetectionManager;

public class AntiDetectionManagerService extends IMockAntiDetectionManager.Stub {

    @Override
    public List<String> getScopedPackageRules() {
        return PackageAntiDetectionConfig.getScopedPackageRules();
    }

    @Override
    public void updateLicenseState(String licenseToken, String deviceId) {
        LicenseStateManager.updateLicenseState(licenseToken, deviceId);
    }

    @Override
    public void setScopedPackageRules(List<String> scopedPackageRules) {
        PackageAntiDetectionConfig.setScopedPackageRules(scopedPackageRules);
    }

    @Override
    public void setPackageFilterEnabled(boolean enabled) {
        PackageAntiDetectionConfig.setPackageFilterEnabled(enabled);
    }

    @Override
    public List<String> getTargetPackages() {
        return PackageAntiDetectionConfig.getTargetPackages();
    }

    @Override
    public void setTargetPackages(List<String> targetPackages) {
        PackageAntiDetectionConfig.setTargetPackages(targetPackages);
    }

    @Override
    public boolean isPackageFilterEnabled() {
        return PackageAntiDetectionConfig.isPackageFilterEnabled();
    }

    @Override
    public boolean isPackageVisibilityFilteringEnabled() {
        return PackageAntiDetectionConfig.isPackageVisibilityFilteringEnabled();
    }

    @Override
    public void disablePackageManagerHook() {
        PackageAntiDetectionConfig.setPackageManagerHookEnabled(false);
    }

    @Override
    public boolean isPackageManagerHookEnabled() {
        return PackageAntiDetectionConfig.isPackageManagerHookEnabled();
    }

    @Override
    public List<String> getDetectedPackages() {
        return PackageAntiDetectionConfig.getDetectedPackages();
    }

    @Override
    public void refreshPackageManagerHookEnabled() {
        PackageAntiDetectionConfig.setPackageManagerHookEnabled(LicenseStateManager.isLicenseUsable());
    }

    @Override
    public void setPackageVisibilityFilterEnabled(boolean enabled) {
        PackageAntiDetectionConfig.setPackageVisibilityFilterEnabled(enabled);
    }

    @Override
    public void setDetectedPackages(List<String> detectedPackages) {
        PackageAntiDetectionConfig.setDetectedPackages(detectedPackages);
    }
}
