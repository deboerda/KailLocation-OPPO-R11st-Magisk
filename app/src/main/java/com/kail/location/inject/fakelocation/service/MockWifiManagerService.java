package com.kail.location.inject.fakelocation.service;

import com.kail.location.inject.fakelocation.InjectDex;
import com.kail.location.inject.utils.LicenseStateManager;
import com.kail.location.inject.utils.MockWifiConfigManager;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IMockWifiManager;
import com.kail.location.inject.fakelocation.model.MockWifiNetwork;
import com.kail.location.inject.fakelocation.hook.system.WifiServiceHook;

public class MockWifiManagerService extends IMockWifiManager.Stub {

    @Override
    public List<String> getScopedAllowMockRules() {
        return MockWifiConfigManager.getScopedAllowMockRules();
    }

    @Override
    public void updateLicenseState(String licenseToken, String deviceId) {
        LicenseStateManager.updateLicenseState(licenseToken, deviceId);
    }

    @Override
    public void setScopedAllowMockRules(List<String> scopedAllowMockRules) {
        MockWifiConfigManager.setScopedAllowMockRules(scopedAllowMockRules);
    }

    @Override
    public List<String> getAllowMockPackages() {
        return MockWifiConfigManager.getAllowMockPackages();
    }

    @Override
    public void setAllowMockPackages(List<String> packages) {
        MockWifiConfigManager.setAllowMockPackages(packages);
    }

    @Override
    public boolean isMockWifiEnabled() {
        return MockWifiConfigManager.isMockWifiEnabled();
    }

    @Override
    public List<MockWifiNetwork> getMockWifiNetworks() {
        return MockWifiConfigManager.getMockWifiNetworks();
    }

    @Override
    public MockWifiNetwork getPrimaryMockWifiNetwork() {
        return MockWifiConfigManager.getPrimaryMockWifiNetwork();
    }

    @Override
    public void setPrimaryMockWifiNetwork(MockWifiNetwork network) {
        MockWifiConfigManager.setPrimaryMockWifiNetwork(network);
    }

    @Override
    public void startMockWifi() {
        if (LicenseStateManager.isLicenseUsable()) {
            if (!WifiServiceHook.scanResultsHooked) {
                WifiServiceHook.hook(InjectDex.getApplicationContext().getClassLoader());
            }
            if (!WifiServiceHook.connectionInfoHooked) {
                WifiServiceHook.hookGetConnectionInfo(InjectDex.getApplicationContext().getClassLoader());
            }
            MockWifiConfigManager.setMockWifiEnabled(true);
        }
    }

    @Override
    public void setMockWifiNetworks(List<MockWifiNetwork> networks) {
        MockWifiConfigManager.setMockWifiNetworks(networks);
    }

    @Override
    public void stopMockWifi() {
        MockWifiConfigManager.setMockWifiEnabled(false);
    }
}
