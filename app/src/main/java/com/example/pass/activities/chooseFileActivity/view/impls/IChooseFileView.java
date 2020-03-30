package com.example.pass.activities.chooseFileActivity.view.impls;

import java.io.File;
import java.util.List;

public interface IChooseFileView {

    void onLoadingFiles();
    void onShowFiles(List<File> fileList);
}
