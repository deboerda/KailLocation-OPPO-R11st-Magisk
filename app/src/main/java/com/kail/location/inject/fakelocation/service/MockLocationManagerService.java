package com.kail.location.inject.fakelocation.service;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SubscriptionInfo;
import com.kail.location.inject.fakelocation.InjectDex;
import com.kail.location.inject.utils.LicenseStateManager;
import com.kail.location.inject.utils.MockLocationHookManager;
import com.kail.location.inject.utils.MockStepSensorManager;
import java.util.Arrays;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IMockLocationManager;
import com.kail.location.inject.fakelocation.listener.IOnMockLocationListener;
import com.kail.location.inject.fakelocation.model.CellTowerInfo;
import com.kail.location.inject.fakelocation.hook.system.TelephonyRegistryHook;
import com.kail.location.inject.fakelocation.hook.system.WifiServiceHook;

public class MockLocationManagerService extends IMockLocationManager.Stub {

    boolean routeMockingEnabled = false;

    List<SubscriptionInfo> mockSubscriptionInfo = null;

    boolean mockSubscriptionInfoEnabled = false;

    List<String> supportedMockModes = Arrays.asList("1", "3", "5", "7");

    @Override
    public List<String> getSafeApps() {
        return MockLocationHookManager.getSafeApps();
    }

    @Override
    public void updateLicenseState(String licenseToken, String deviceId) {
        LicenseStateManager.updateLicenseState(licenseToken, deviceId);
    }

    @Override
    public void setSafeApps(List<String> safeApps) {
        MockLocationHookManager.setSafeApps(safeApps);
    }

    @Override
    public void setMockSubscriptionInfoEnabled(boolean enabled) {
        if (LicenseStateManager.isLicenseUsable()) {
            this.mockSubscriptionInfoEnabled = enabled;
        } else {
            this.mockSubscriptionInfoEnabled = false;
        }
    }

    @Override
    public List<String> getAllowMockPackages() {
        return MockLocationHookManager.getAllowMockPackages();
    }

    @Override
    public void setAllowMockPackages(List<String> packages) {
        if (LicenseStateManager.isLicenseUsable()) {
            MockLocationHookManager.setAllowMockPackages(packages);
        }
    }

    @Override
    public boolean isMocking() {
        return MockLocationHookManager.isMocking();
    }

    @Override
    public boolean isMockGpsStatus() {
        return MockLocationHookManager.isMockGpsStatus();
    }

    @Override
    public Location getMockLocation() {
        return MockLocationHookManager.getMockLocation();
    }

    @Override
    public boolean isStepSensorMocking() {
        return MockStepSensorManager.isStepSensorMocking();
    }

    @Override
    public void setMockCells(List<CellTowerInfo> cells) {
        if (LicenseStateManager.isLicenseUsable()) {
            MockLocationHookManager.setMockCells(cells);
        } else {
            MockLocationHookManager.setMockCells(null);
        }
    }

    @Override
    public void removeOnMockListener(IBinder listenerBinder) {
        IOnMockLocationListener listener = IOnMockLocationListener.Stub.asInterface(listenerBinder);
        if (listener == null) {
            return;
        }
        MockLocationHookManager.removeOnMockListener(listener);
    }

    @Override
    public void setSensorFeatureEnabled(boolean enabled) {
        MockStepSensorManager.setSensorFeatureEnabled(enabled);
    }

    @Override
    public void setIntervalTimeout(long intervalMillis) {
        MockLocationHookManager.setIntervalTimeout(intervalMillis);
    }

    @Override
    public void setMockLocation(Location location) {
        Bundle extras;
        if (location != null && (extras = location.getExtras()) != null) {
            String locationSource = extras.getString("from", "loc");
            if (MockLocationHookManager.isMocking()) {
                if ("route".equals(locationSource) && !this.routeMockingEnabled) {
                    return;
                }
                if ("rocker".equals(locationSource) && LicenseStateManager.hasRemoteDenial()) {
                    return;
                }
            }
        }
        MockLocationHookManager.setMockLocation(location);
    }

    @Override
    public void setMockSourceTagEnabled(boolean enabled) {
    }

    @Override
    public boolean isMockSubscriptionInfoEnabled() {
        return this.mockSubscriptionInfoEnabled;
    }

    @Override
    public List<CellTowerInfo> getMockCells() {
        return MockLocationHookManager.getMockCells();
    }

    @Override
    public void setStepCountOffset(long stepCount) {
        MockStepSensorManager.setStepCountOffset(stepCount);
    }

    @Override
    public void stopMockLocation() {
        MockLocationHookManager.stopMockLocation();
    }

    @Override
    public long getMockStepCount() {
        return MockStepSensorManager.getMockStepCount();
    }

    @Override
    public void setMockSubscriptionInfo(List<SubscriptionInfo> subscriptionInfo) {
        if (LicenseStateManager.isLicenseUsable()) {
            this.mockSubscriptionInfo = subscriptionInfo;
        } else {
            this.mockSubscriptionInfo = null;
        }
    }

    @Override
    public void setMockGpsStatus(boolean enabled) {
        if (!enabled || LicenseStateManager.isLicenseUsable()) {
            MockLocationHookManager.setMockGpsStatus(enabled);
        } else {
            MockLocationHookManager.setMockGpsStatus(false);
        }
    }

    @Override
    public void addOnMockListener(IBinder listenerBinder) {
        IOnMockLocationListener listener = IOnMockLocationListener.Stub.asInterface(listenerBinder);
        if (listener == null) {
            return;
        }
        MockLocationHookManager.addOnMockListener(listener);
    }

    @Override
    public void startStepSensorMock() {
        if (LicenseStateManager.isLicenseUsable()) {
            MockStepSensorManager.startStepSensorMock();
        }
    }

    @Override
    public long getIntervalTimeout() {
        return MockLocationHookManager.getIntervalTimeout();
    }

    @Override
    public boolean isSensorFeatureEnabled() {
        return MockStepSensorManager.isSensorFeatureEnabled();
    }

    @Override
    public void setBaseStepCount(long stepCount) {
        MockStepSensorManager.setBaseStepCount(stepCount);
    }

    @Override
    public void setStepSpeed(float speed) {
        MockStepSensorManager.setStepSpeed(speed);
    }

    @Override
    public boolean isMockSourceTagEnabled() {
        return false;
    }

    @Override
    public void stopStepSensorMock() {
        MockStepSensorManager.stopStepSensorMock();
    }

    @Override
    public List<SubscriptionInfo> getMockSubscriptionInfo() {
        return this.mockSubscriptionInfo;
    }

    @Override
    public float getStepSpeed() {
        return MockStepSensorManager.getStepSpeed();
    }

    @Override
    public void startMockLocation() {
        this.routeMockingEnabled = LicenseStateManager.isLicenseUsable();
        if (!MockLocationHookManager.initialized) {
            Context context = InjectDex.getApplicationContext();
            MockLocationHookManager.init(context);
            WifiServiceHook.hook(context.getClassLoader());
            TelephonyRegistryHook.hook(context.getClassLoader());
        }
        MockLocationHookManager.startMockLocation();
    }
}
