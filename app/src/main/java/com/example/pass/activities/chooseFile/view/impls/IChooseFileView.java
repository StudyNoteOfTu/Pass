package com.example.pass.activities.chooseFile.view.impls;

import java.io.File;
import java.util.List;

public interface IChooseFileView {

    void onLoadingFiles();
    void onShowFiles(List<File> fileList);
}
