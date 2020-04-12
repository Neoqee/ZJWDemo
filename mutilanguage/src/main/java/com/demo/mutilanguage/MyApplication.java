package com.demo.mutilanguage;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class MyApplication extends Application {

    public static String localeStr="en";
    private Locale locale=null;
    private Context context=null;
    private Configuration configuration ;

    @Override
    public void onCreate() {
        super.onCreate();

        configuration = getBaseContext().getResources().getConfiguration();
        String lang=localeStr;
        String systemLocale=getSystemLocale(configuration).getLanguage();
        System.out.println(systemLocale);
        if (!"".equals(lang) && !systemLocale.equals(lang)) {
            System.out.println("123456");
            locale = new Locale(lang);
            System.out.println(locale.getLanguage());
            Locale.setDefault(locale);
            setSystemLocale(configuration, locale);
            updateConfiguration(configuration);
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("change");
        if (locale != null) {
            setSystemLocale(newConfig, locale);
            Locale.setDefault(locale);
            updateConfiguration(newConfig);
        }
    }


    @SuppressWarnings("deprecation")
    private static Locale getSystemLocale(Configuration config) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return config.getLocales().get(0);
        } else {
            return config.locale;
        }
    }

    @SuppressWarnings("deprecation")
    private static void setSystemLocale(Configuration config, Locale locale) {
        System.out.println("setSystemLocale");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
    }

    @SuppressWarnings("deprecation")
    private void updateConfiguration(Configuration config) {
        System.out.println("updateConfiguration");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getBaseContext().createConfigurationContext(config);
        } else {
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
    }

}
