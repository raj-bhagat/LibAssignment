package com.example.libassignment;

import com.example.libassignment.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import com.org.appcrashtracker.ACT;

public class MainAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        //HiLog.info(new HiLogLabel(HiLog.LOG_APP, 0x00201 ,"Path"), ""+intent.getBundle());
    }
}
