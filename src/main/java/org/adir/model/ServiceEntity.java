package org.adir.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceEntity {
    String msg;
    Beverage beverage;
    Boolean isSuccessful;
}
