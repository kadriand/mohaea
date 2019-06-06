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

    private int rivalsSize;

    @Override
    public void init(List<T> individuals) {
    }

    @Override
    public List<T> select(List<T> population, int tournamentRounds) {
        int max = population.size();
        List<T> selected = new ArrayList<>();

        for (int i = 0; i < tournamentRounds; i++) {
            HashSet<Integer> hs = RandomUtils.getDifferentRandomIntegers(max, rivalsSize);
            List<Integer> indexes = new ArrayList<>(hs);
            T winner = null;
            for (Integer index : indexes)
                if (population.get(index).isBetter(winner))
                    winner = population.get(index);
            selected.add(winner);
        }
        return selected;
    }
}
