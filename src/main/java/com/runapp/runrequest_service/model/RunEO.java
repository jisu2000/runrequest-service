package com.runapp.runrequest_service.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RunEO {
    @Id
    private String id;
    private long durationMillis;
    private Integer distanceMeters;
    private long epochMilliis;
    private double lat;
    private double lon;
    private double avgSpeedKmh;
    private double maxSpeedKmh;
    private Integer totalElevationMeters;
    @CreationTimestamp
    private LocalDateTime dateTimeUtc;
    private String mapPictureUrl;
    private Integer userId;
}
