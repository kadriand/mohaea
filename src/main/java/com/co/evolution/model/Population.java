package com.co.evolution.model;

import com.co.evolution.model.individual.Individual;

import java.util.ArrayList;
import java.util.Collection;

public class Population<E extends Individual> extends ArrayList<E> {

    private E best;

    public E getBest() {
        if (this.best != null)
            return this.best;

        E best = get(0);
        for (int i = 1; i < size(); i++) {
            E actual = get(i);
            if (actual.compareTo(best) < 0)
                best = actual;
        }
        this.best = best;
        return this.best;
    }

    @Override
    public boolean add(E element) {
        this.best = null;
        return super.add(element);
    }

    @Override
    public void add(int index, E element) {
        this.best = null;
        super.add(index, element);
    }

    public boolean add(E... elements) {
        this.best = null;
        boolean result = true;
        for (E element : elements)
            result = result && this.add(element);
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> elements) {
        this.best = null;
        return super.addAll(elements);
    }

    public boolean remove(E element) {
        this.best = null;
        if (!super.contains(element))
            return false;
        return super.remove(element);
    }

    @Override
    public E remove(int index) {
        this.best = null;
        return super.remove(index);
    }

}
