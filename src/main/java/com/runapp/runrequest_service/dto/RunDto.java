package com.runapp.runrequest_service.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunDto {
    private long durationMillis;
    private Integer distanceMeters;
    private long epochMilliis;
    private double lat;
    private double lon;
    private double avgSpeedKmh;
    private double maxSpeedKmh;
    private Integer totalElevationMeters;
    private String id;
    private String dateTimeUtc;
    private String mapPictureUrl;
}
