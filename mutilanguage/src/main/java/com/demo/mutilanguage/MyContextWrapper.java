package com.demo.mutilanguage;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

public class MyContextWrapper extends ContextWrapper {

    public static String localeStr="en";
    private static Locale locale=null;

    public MyContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wap(Context context) {

        Configuration configuration=context.getResources().getConfiguration();
        String lang=localeStr;
        System.out.println("lang:"+lang);
        String systemLocale=getSystemLocale(configuration).getLanguage();
        System.out.println("sys:"+systemLocale);
        if (!"".equals(lang) && !systemLocale.equals(lang)) {
//            System.out.println("123456");
            locale = new Locale(lang);
//            System.out.println(locale.getLanguage());
            Locale.setDefault(locale);
            setSystemLocale(configuration, locale);
        }
        return new MyContextWrapper(context.createConfigurationContext(configuration));
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
//        System.out.println("setSystemLocale");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
    }

    @SuppressWarnings("deprecation")
    private static Context updateConfiguration(Context context,Configuration config) {
//        System.out.println("updateConfiguration");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context=context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        return context;
    }

}
