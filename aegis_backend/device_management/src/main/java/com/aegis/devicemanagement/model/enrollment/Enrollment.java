package com.aegis.devicemanagement.model.enrollment;

import com.aegis.devicemanagement.pojos.enrollmentConfig.CardReadWrite;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollSpCard;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document("Enrollment")
public class Enrollment {
    @Id
    private String id;
    private String deviceId;
    private EnrollUser enrollUser;
    private EnrollSpCard enrollSpCard;
    private CardReadWrite cardReadWrite;
    private boolean isDeleted=false;
}
