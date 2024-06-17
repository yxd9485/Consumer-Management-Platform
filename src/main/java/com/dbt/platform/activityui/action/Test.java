package com.dbt.platform.activityui.action;

import com.alibaba.fastjson.JSON;
import com.dbt.platform.activityui.bean.VpsTemplateUi;

public class Test {

    public static void main(String[] args) {
        VpsTemplateUi ui  = new VpsTemplateUi();
        VpsTemplateUi.PicInfo homeBackgroundPic = new VpsTemplateUi.PicInfo();
        homeBackgroundPic.setPicUrl("https://img.vjifen.com/images/vma/test/20200528/piclib/vjf20200528154726882.png");
        homeBackgroundPic.setPicHeight("11");
        homeBackgroundPic.setPicWidth("11");
        homeBackgroundPic.setPicX("0");
        homeBackgroundPic.setPicY("0");
        VpsTemplateUi.PicInfo homeSloganPic = new VpsTemplateUi.PicInfo();
        homeSloganPic.setPicUrl("https://img.vjifen.com/images/vma/test/20200528/piclib/vjf20200528155456833.png");
        homeSloganPic.setPicHeight("11");
        homeSloganPic.setPicWidth("11");
        homeSloganPic.setPicX("0");
        homeSloganPic.setPicY("0");
        VpsTemplateUi.PicInfo homeOpenRedpacketPic = new VpsTemplateUi.PicInfo();
        homeOpenRedpacketPic.setPicUrl("https://img.vjifen.com/images/vma/test/20200528/piclib/vjf20200528155556142.png");
        homeOpenRedpacketPic.setPicHeight("11");
        homeOpenRedpacketPic.setPicWidth("11");
        homeOpenRedpacketPic.setPicX("0");
        homeOpenRedpacketPic.setPicY("0");
        VpsTemplateUi.PicInfo homeLogoPic = new VpsTemplateUi.PicInfo();
        homeLogoPic.setPicUrl("https://img.vjifen.com/images/vma/test/20200528/piclib/vjf20200528155138771.png");
        homeLogoPic.setPicHeight("11");
        homeLogoPic.setPicWidth("11");
        homeLogoPic.setPicX("0");
        homeLogoPic.setPicY("0");
        ui.setHomeBackgroundPic(homeBackgroundPic);
        ui.setHomeSloganPic(homeSloganPic);
        ui.setHomeOpenRedpacketPic(homeOpenRedpacketPic);
        ui.setHomeLogoPic(homeLogoPic);
        String a = JSON.toJSONString(ui);
        System.out.println(a);
        VpsTemplateUi v = JSON.parseObject(a,VpsTemplateUi.class);
        System.out.println(a);
    }

    }

