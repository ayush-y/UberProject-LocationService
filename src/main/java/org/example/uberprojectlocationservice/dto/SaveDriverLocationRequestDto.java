package org.example.uberprojectlocationservice.dto;


import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveDriverLocationRequestDto {
    private String driverId;
    private Double latitude;
    private Double longitude;
}
