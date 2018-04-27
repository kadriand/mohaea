package com.co.evolution.selection;

import com.co.evolution.model.SelectionMethod;
import com.co.evolution.model.individual.Individual;
import com.co.evolution.util.RandomUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
public class TournamentSelection<T extends Individual> implements SelectionMethod<T> {

    private int numberRivals;

    @Override
    public void init(List<T> individuals) {

    }

    @Override
    public List<T> select(List<T> individuals, int individualNumber) {
        int max = individuals.size();
        List<T> selected = new ArrayList<>();

        for (int i = 0; i < individualNumber; i++) {
            HashSet<Integer> hs = RandomUtils.getDifferentRandomIntegers(max, numberRivals);
            List<Integer>  indexes = new ArrayList<>(hs);
            T winner = individuals.get(indexes.get(0));

            for (int j = 1; j < numberRivals; j++)
                if (individuals.get(indexes.get(j)).compareTo(winner) < 0)
                    winner = individuals.get(indexes.get(j));
            selected.add(winner);
        }
        return selected;
    }
}
