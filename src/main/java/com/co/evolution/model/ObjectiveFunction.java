package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class ObjectiveFunction<T extends Individual> {

    @Getter
    @Setter
    private boolean minimize;

    public abstract double apply(T individual);

    /**
     * Consider whether the objective must be minimized or maximizec
     *
     * @param individual
     * @return
     */
    public double compute(T individual) {
        return (minimize ? 1.0 : -1.0) * apply(individual);
    }

}
