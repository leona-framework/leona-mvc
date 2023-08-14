package com.tealeaf.leona.mvc.services;

import org.slf4j.Logger;

public record ServiceMetadata(LeonaService service, Logger logger, String serviceName, String executionTarget) {
}
