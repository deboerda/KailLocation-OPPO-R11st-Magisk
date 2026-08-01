package com.kail.location.inject.utils;

import android.location.Location;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import java.util.HashMap;
import java.util.List;
import com.kail.location.inject.fakelocation.aidl.IMockLocationManager;
import com.kail.location.inject.fakelocation.model.CellTowerInfo;

public final class MockLocationServiceManager {
    private IMockLocationManager mockLocationService;
    private HashMap<?, IBinder> listenerBinders = new HashMap<>();

    private static final class Holder {
        static MockLocationServiceManager instance = new MockLocationServiceManager();
    }

    public static MockLocationServiceManager getInstance() {
        return Holder.instance;
    }

    public List<String> getAllowMockPackages() {
        if (getMockLocationService() == null) {
            return null;
        }
        try {
            return this.mockLocationService.getAllowMockPackages();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<CellTowerInfo> getMockCells() {
        if (getMockLocationService() == null) {
            return null;
        }
        try {
            return this.mockLocationService.getMockCells();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Location getMockLocation() {
        if (getMockLocationService() == null) {
            return null;
        }
        try {
            return this.mockLocationService.getMockLocation();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<SubscriptionInfo> getMockSubscriptionInfo() {
        if (getMockLocationService() == null) {
            return null;
        }
        try {
            return this.mockLocationService.getMockSubscriptionInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getSafeApps() {
        if (getMockLocationService() == null) {
            return null;
        }
        try {
            return this.mockLocationService.getSafeApps();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public IMockLocationManager getMockLocationService() {
        if (this.mockLocationService == null) {
            try {
                this.mockLocationService = IMockLocationManager.Stub.asInterface(ServiceManagerBridge.getService(ClassLoader.getSystemClassLoader(), "oem_location"));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return this.mockLocationService;
    }

    public boolean isMocking() {
        if (getMockLocationService() == null) {
            return false;
        }
        try {
            return this.mockLocationService.isMocking();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isMockGpsStatusEnabled() {
        if (getMockLocationService() == null) {
            return false;
        }
        try {
            return this.mockLocationService.isMockGpsStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
