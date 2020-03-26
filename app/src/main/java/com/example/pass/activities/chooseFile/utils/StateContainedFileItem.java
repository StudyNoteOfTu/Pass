package com.example.pass.activities.chooseFile.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.net.URI;

public class StateContainedFileItem {

    private boolean isNeedMore = false;

    private File file;

    public StateContainedFileItem(boolean isNeedMore, File file) {
        this.isNeedMore = isNeedMore;
        this.file = file;
    }

    public boolean isNeedMore() {
        return isNeedMore;
    }

    public void setNeedMore(boolean needMore) {
        isNeedMore = needMore;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
