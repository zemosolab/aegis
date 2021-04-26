package com.aegis.devicemanagement.pojos.request;

import com.aegis.devicemanagement.pojos.enrollmentConfig.CardReadWrite;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollSpCard;
import com.aegis.devicemanagement.pojos.enrollmentConfig.EnrollUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollmentRequest {
    private String deviceId;
    private CardReadWrite cardReadWrite;
    private EnrollSpCard enrollSpCard;
    private EnrollUser enrollUser;
}
