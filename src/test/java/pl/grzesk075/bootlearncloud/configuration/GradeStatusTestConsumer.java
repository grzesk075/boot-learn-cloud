package pl.grzesk075.bootlearncloud.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.jms.annotation.JmsListener;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;

import java.util.ArrayList;
import java.util.List;

import static pl.grzesk075.bootlearncloud.queue.Destination.GRADE_STATUS_QUEUE_OUT;

@TestConfiguration
public class GradeStatusTestConsumer {

    List<GradeStatus> gradeStatusList = new ArrayList<>();

    @JmsListener(destination = GRADE_STATUS_QUEUE_OUT,
            subscription = "testSubscription",
            containerFactory = "jmsListenerContainerFactoryPublishSubscribe")
    public void onGradeStatus(GradeStatus gradeStatus) {
        gradeStatusList.add(gradeStatus);
    }

    public List<GradeStatus> getGradeStatusList() {
        return gradeStatusList;
    }
}
