package com.bravvura.nestledtime.network.volley;

public interface RequestCallback {
    void error(NetworkError volleyError);

    void success(Object obj);
}
