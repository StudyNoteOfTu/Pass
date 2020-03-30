package com.example.pass.activities.analyseOfficeActivity.presenter;

import com.example.pass.activities.analyseOfficeActivity.model.OfficeModelImpl;
import com.example.pass.activities.analyseOfficeActivity.model.impls.IOfficeModel;
import com.example.pass.activities.analyseOfficeActivity.view.impls.ITitleSelectView;
import com.example.pass.base.BasePresenter;

public class AnalyseOfficePresenter<T extends ITitleSelectView> extends BasePresenter<T> {

    private static final String TAG = "AnalyseOfficePresenter";
    private OfficeModelImpl mOfficeModel;

    public AnalyseOfficePresenter() {
        mOfficeModel = new OfficeModelImpl();
    }

    /**
     * 获取xml按行List
     *
     * @param path xml文件路径
     * @param dir  新文件父目录
     * @param name 新文件名称 不带后缀
     */
    public void readFileAndGetLineList(String path, String dir, String name) {
        if (isViewAttached() && mOfficeModel != null) {
            mOfficeModel.readOffice(path, dir, name, new IOfficeModel.OnLoadProgressListener<String>() {
                @Override
                public void onStart() {
                    getView().beginLoadXml();
                }

                @Override
                public void onFinish(String result) {
                    //转换成功后加载文字
                    String content = mOfficeModel.xmlToString(result);
                    getView().loadLineList(mOfficeModel.getXmlLines(content));
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    /**
     * 更新行信息（ p的状态）
     * @param key key
     * @param value value
     * @param position 集合中该行所在下标
     * @return 是否更新成功
     */
    public boolean updateLine(String key, String value, int position) {
        if (isViewAttached() && mOfficeModel != null) {
            return mOfficeModel.changeLineState(key, value, position);
        }
        return false;
    }

    /**
     * 将model中最新的XMLLines转为最新文件
     * @param dir 新文件父目录
     * @param name 新文件名字，不带后缀
     * @param listener 进度监听器
     * @return 是否正常进行
     */
    public boolean compileLinesToXml(String dir, String name, IOfficeModel.OnLoadProgressListener listener){
        if (isViewAttached() && mOfficeModel != null){
            return mOfficeModel.recompileListToXml(dir, name, listener);
        }
        return false;
    }

}
