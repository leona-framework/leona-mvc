package com.tealeaf.leona.mvc.client.retry;

import org.slf4j.Logger;

public interface RetryLogger {
    void log(RetryView retryView);
    void setLogger(Logger logger);
}
