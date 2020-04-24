package org.camunda.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseImpl {

    private ThreadLocal<Logger> logger = new ThreadLocal<>();

    protected String getLoggerName() {
        return "commonLogger";//this.getClass().getName();
    }

    protected Logger getLogger() {
        if (logger.get() == null)
            logger.set(LoggerFactory.getLogger(getLoggerName()));
        return logger.get();
    }

    protected String logPrefix() {
        return String.format("%s ", this.getClass().getSimpleName());
    }

    protected void I(String msg) {
        getLogger().info(msg);
    }

    protected void I(String msg, Object... args) {
        getLogger().info(logPrefix() + String.format(msg, args));
    }

    protected void D(String msg) {
        getLogger().debug(msg);
    }

    protected void D(String msg, Object... args) {
        getLogger().debug(logPrefix() + String.format(msg, args));
    }

    protected void E(String msg) {
        getLogger().error(msg);
    }

    protected void E(String msg, Object ...args) {
        getLogger().error(logPrefix() + String.format(msg, args));
    }

    protected void E(Throwable e, String msg, Object ...args) { getLogger().error(logPrefix() + String.format(msg, args), e);
    }
}
