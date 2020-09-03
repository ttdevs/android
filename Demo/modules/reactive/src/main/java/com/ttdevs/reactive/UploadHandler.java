/*
 * Created by ttdevs at 16-4-14 下午3:52.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.reactive;

import java.util.List;

public abstract class UploadHandler {
    private boolean isCancled;

    public void cancel() {
        isCancled = true;
    }

    public boolean isCancled() {
        return isCancled;
    }

    public abstract void onProgress(String filePath, double percent);

    public abstract void onSuccess(List<QiniuModel> infos);

    public abstract void onError(String msg);
}
