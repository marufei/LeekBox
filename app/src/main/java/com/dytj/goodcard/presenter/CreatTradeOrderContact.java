package com.dytj.goodcard.presenter;

import com.dytj.goodcard.model.CreateTradeOrderEntity;
import com.dytj.goodcard.model.JsonResponse;
import com.dytj.goodcard.mvpBase.BasePresenter;
import com.dytj.goodcard.mvpBase.BaseView;

import java.util.HashMap;

/*
 * 创建时间:  2019/4/7 on 11:13
 * 用于设置Presenter里设置数据和获取数据的方法
 */
public interface CreatTradeOrderContact {

    interface view extends BaseView {
        /**
         * 设置交易数据源
         * 用于网络请求成功失败后，回调给view的方法
         * @param createTradeOrderEntity
         */
        void createOrder(JsonResponse<CreateTradeOrderEntity> createTradeOrderEntity, String tag);


        void ErrorData(Throwable e);
    }

    interface presenter extends BasePresenter {
        /**
         * 获取数据
         * 用于view层发起网络请求，获取数据
         */
        void getData(HashMap<Object, Object> map, String tag);
    }
}
