package pl.grzesk075.bootlearncloud.queue.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.grzesk075.bootlearncloud.model.Grade;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeMessage {

    private UUID uuid;

    private Grade grade;

    private Long studentId;
}
