package com.co.evolution.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class ObjectiveFunction<T> {

    @Getter
    @Setter
    private boolean minimize;

    public abstract double apply(T individual);

}
