package com.example.pass.configs;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        RequestCodeConfig.PICTURE_GALLERY,
        RequestCodeConfig.PICTURE_TAKEPHOTO
})
public @interface RequestCodeConfig {

    int PICTURE_GALLERY = 200;

    int PICTURE_TAKEPHOTO = 201;

}
