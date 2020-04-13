package com.yc.async;

import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 默认runnable wrapper service
 *
 * @author yangchuan
 * @version 1.0 create at 2020/4/13
 */
@AllArgsConstructor
public class DefaultRunnableWrapperService implements RunnableWrapperService {

    private final List<RunnableWrapper> runnableWrappers;

    @Override
    public Runnable wrapper(Runnable runnable) {

        if (CollectionUtils.isEmpty(runnableWrappers)) {
            return runnable;
        }

        for (RunnableWrapper wrapper : runnableWrappers) {
            runnable = wrapper.wrapper(runnable);
        }
        return runnable;
    }
}

