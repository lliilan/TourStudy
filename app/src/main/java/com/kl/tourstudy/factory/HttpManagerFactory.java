package com.kl.tourstudy.factory;

import com.kl.tourstudy.thread.IHttpManager;

/**
 * Created by KL on 2017/4/11 0011.
 */

public interface HttpManagerFactory {
    public IHttpManager getHttpManagerInstance();
}
