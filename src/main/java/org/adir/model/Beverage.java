package org.adir.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class Beverage {

    private String name;
    private Map<String,Integer> ingredientByQuantity;
}
