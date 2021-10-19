package com.example.libassignment;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.AbilityPackage;
import ohos.app.Context;
import ohos.bundle.BundleInfo;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.NetHandle;
import ohos.net.NetManager;
import ohos.telephony.SimInfoManager;


import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MyApplication extends AbilityPackage {
    @Override
    public void onInitialize() {
        /*Context con = null;
        File file = con.getFilesDir();
        HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,"File"), ""+file);*/
        super.onInitialize();
    }
}
