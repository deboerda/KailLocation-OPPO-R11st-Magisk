package com.kail.location.inject.fakelocation.service;

import com.kail.location.lib.lhooker.LHooker;
import com.kail.location.inject.fakelocation.aidl.INativeCatchManager;

public class NativeCatchManagerService extends INativeCatchManager.Stub {

    @Override
    public int getNativeCatchInitStatus() {
        return !LHooker.initialized ? -1 : 0;
    }

    @Override
    public boolean isNativeCatchEnabled() {
        boolean initialized = LHooker.initialized;
        return false;
    }

    @Override
    public int getNativeCatchHookStatus() {
        return !LHooker.initialized ? -1 : 0;
    }
}
