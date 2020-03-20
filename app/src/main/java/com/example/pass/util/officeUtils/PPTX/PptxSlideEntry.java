package com.example.pass.util.officeUtils.PPTX;

import java.util.zip.ZipEntry;


/**
 * 一张PPT的文件组合
 */
public class PptxSlideEntry {

    //一个PPTSlideEntry中包含 一个 slide的 slide、images,所有压缩文件内部文件均为ZipEntry

    ZipEntry slide;
    ZipEntry rels;

    public PptxSlideEntry(ZipEntry slide, ZipEntry rels) {
        this.slide = slide;
        this.rels = rels;
    }

    public ZipEntry getSlide() {
        return slide;
    }

    public void setSlide(ZipEntry slide) {
        this.slide = slide;
    }

    public ZipEntry getRels() {
        return rels;
    }

    public void setRels(ZipEntry rels) {
        this.rels = rels;
    }
}
