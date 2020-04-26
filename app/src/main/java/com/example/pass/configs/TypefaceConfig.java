package com.example.pass.configs;

import android.os.Environment;

import java.io.File;

public class TypefaceConfig {

    public static final String ttfFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "PassTypeface";

}
