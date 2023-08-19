package org.lyora.firefly.mvc.services;

import org.lyora.leona.mvc.components.containers.ExecutionView;
import org.lyora.leona.mvc.components.containers.TupleOperators;
import org.lyora.leona.mvc.services.AsyncService;
import org.lyora.leona.mvc.services.ServiceMetadata;
import org.lyora.leona.mvc.services.ServiceMetadataProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class AsyncServiceTest {

    @Test
    @SneakyThrows
    public void isReallyAsync() {
        TestClass testClass = new TestClass();
        log.info("test: {}", testClass.testMethod4());
    }





    @Slf4j
    private static class TestClass implements AsyncService {
        @Override
        public final ServiceMetadata metadata(ServiceMetadataProvider metadataProvider) {
            return metadataProvider.defaults();
        }


        public int testMethod4() {
            return handleAsync(() -> sleeperMethod(1, 1500))
                    .concat(() -> sleeperMethod(2, 1300))
                    .concat(() -> sleeperMethod(3, 1100))
                    .concat(() -> sleeperMethod(4, 900))
                    .concat(() -> sleeperMethod(5, 500))
                    .concat(() -> sleeperMethod(6, 0))
                    .map(t -> TupleOperators.unpack(t).ofType(Integer.class).reduce(Integer::sum)).get().orElse(0);
        }


        public void printResult(ExecutionView<Integer> result) {
            log.info("My result {}", result.result());
        }

        @SneakyThrows
        private int sleeperMethod(int result, long timeout) {
            log.info("Sleeping for {}", timeout);
            Thread.sleep(timeout);
            log.info("Done sleeping after {}.. result={}", timeout, result);
            return result;
        }
    }

}
