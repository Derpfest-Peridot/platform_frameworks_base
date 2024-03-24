/*
 * Copyright (C) 2023 GenesisOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.derpfest;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @hide
 */
public final class GamesPropsUtils {

    private static final String TAG = GamesPropsUtils.class.getSimpleName();
    private static final boolean DEBUG = false;
    
    private static final Map<String, Map<String, Object>> propsToChange = new HashMap<>();
    private static final Map<String, String[]> packagesToChange = new HashMap<>();

    static {
        propsToChange.put("ROG9P", createROG9PProps());
        packagesToChange.put(
            "ROG9P",
            new String[] {
                "com.mobilelegends.mi",
                "com.supercell.brawlstars",
                "com.blizzard.diablo.immortal",
                "com.netease.newspike",
                "com.activision.callofduty.warzone",
                "com.pubg.newstate",
                "com.gamedevltd.destinywarfare",
                "com.pikpok.dr2.play",
                "com.CarXTech.highWay",
                "com.nekki.shadowfight3",
                "com.nekki.shadowfightarena",
                "com.gameloft.android.ANMP.GloftA8HM",
                "com.nekki.shadowfight",
                "com.ea.game.nfs14_row",
                "com.ea.games.r3_row",
                "com.supercell.squad",
                "com.blitzteam.battleprime",
                "com.proximabeta.mf.uamo",
                "com.ea.gp.apexlegendsmobilefps",
                "com.levelinfinite.hotta.gp",
                "com.supercell.clashofclans",
                "com.vng.mlbbvn",
                "com.levelinfinite.sgameGlobal",
                "com.tencent.tmgp.sgame",
                "com.netease.lztgglobal",
                "com.riotgames.league.wildrift",
                "com.riotgames.league.wildrifttw",
                "com.riotgames.league.wildriftvn",
                "com.kiloo.subwaysurf",
                "com.ea.gp.fifamobile",
                "com.gameloft.android.ANMP.GloftA9HM",
                "com.madfingergames.legends",
                "com.pearlabyss.blackdesertm",
                "com.pearlabyss.blackdesertm.gl"
            }
        );

        propsToChange.put("S25U", createS25UProps());
        packagesToChange.put(
            "S25U",
            new String[] {
                "com.mobile.legends",
                "com.pubg.imobile",
                "com.pubg.krmobile",
                "com.rekoo.pubgm",
                "com.tencent.ig",
                "com.tencent.tmgp.pubgmhd",
                "com.vng.pubgmobile",
                "com.carxtech.sr",
                "com.proxima.dfm",
                "com.epicgames.fortnite"
            }
        );

        propsToChange.put("RMX14", createRMX14Props());
        packagesToChange.put(
            "RMX14",
            new String[] {
                "com.dts.freefireth",
                "com.dts.freefiremax"
            }
        );

        propsToChange.put("LY700", createLY700Props());
        packagesToChange.put(
            "LY700",
            new String[] {
                "com.netease.ko",
                "com.garena.game.codm",
                "com.epicgames.portal",
                "com.tencent.lolm",
                "com.activision.callofduty.shooter",
                "com.tencent.tmgp.kr.codm",
                "com.vng.codmvn"
            }
        );
    }

    private static Map<String, Object> createROG9PProps() {
        Map<String, Object> props = new HashMap<>();
        props.put("BRAND", "Asus");
        props.put("DEVICE", "ROG Phone 9 PRO");
        props.put("MANUFACTURER", "Asus");
        props.put("MODEL", "ASUS_AI2501");
        return props;
    }

    private static Map<String, Object> createS25UProps() {
        Map<String, Object> props = new HashMap<>();
        props.put("BRAND", "Samsung");
        props.put("DEVICE", "Samsung S25 Ultra");
        props.put("MANUFACTURER", "samsung");
        props.put("MODEL", "SM-S938B");
        return props;
    }

    private static Map<String, Object> createRMX14Props() {
        Map<String, Object> props = new HashMap<>();
        props.put("BRAND", "Realme");
        props.put("DEVICE", "Realme 14");
        props.put("MANUFACTURER", "Realme");
        props.put("MODEL", "RMX5070");
        return props;
    }

    private static Map<String, Object> createLY700Props() {
        Map<String, Object> props = new HashMap<>();
        props.put("BRAND", "Lenovo");
        props.put("DEVICE", "Lenovo Y700");
        props.put("MANUFACTURER", "Lenovo");
        props.put("MODEL", "Lenovo TB-9707F");
        return props;
    }

    public static void setProps(Context context) {

        final String packageName = context.getPackageName();

        if (TextUtils.isEmpty(packageName)) {
            return;
        }

        for (String device : packagesToChange.keySet()) {
            String[] packages = packagesToChange.get(device);
            if (Arrays.asList(packages).contains(packageName)) {
                dlog("Defining props for: " + packageName);
                Map<String, Object> props = propsToChange.get(device);
                for (Map.Entry<String, Object> prop : props.entrySet()) {
                    String key = prop.getKey();
                    Object value = prop.getValue();
                    setPropValue(key, value);
                }
                break;
            }
        }
    }

    private static void setPropValue(String key, Object value) {
        try {
            dlog("Defining prop " + key + " to " + value.toString());
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(null, value);
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to set prop " + key, e);
        }
    }

    public static void dlog(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
