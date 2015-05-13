package com.mawsom.mawsom.mawsomnobl.service;

import com.mawsom.mawsom.mawsomnobl.data.Channel;

/**
 * Created by Waqas on 5/9/2015.
 */
public interface WeatherServiceCallBack {

    void serviceSucess(Channel channel);

    void serviceFailure(Exception exception);

}
