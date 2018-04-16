package com.co.evolution.selection;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.SelectionMethod;
import com.co.evolution.util.RandomUtils;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
public class TournamentSelection implements SelectionMethod<RealIndividual> {

    private int numberRivals;

    @Override
    public void init(List<RealIndividual> individuals) {

    }

    @Override
    public List<RealIndividual> select(List<RealIndividual> individuals, int individualNumber, boolean minimize) {

        int max = individuals.size();
        List<RealIndividual> selected = new ArrayList<>();

        for (int i = 0; i < individualNumber; i++) {

            HashSet<Integer> hs = RandomUtils.getDifferentRandomIntegers(max, numberRivals);
            List<Integer>  indexes = new ArrayList<>(hs);
            RealIndividual winner = individuals.get(indexes.get(0));

            for (int j = 1; j < numberRivals; j++) {
                if (minimize && individuals.get(indexes.get(j)).compareTo(winner) < 0)
                    winner = individuals.get(indexes.get(j));
                else if (!minimize && individuals.get(indexes.get(j)).compareTo(winner) > 0)
                    winner = individuals.get(indexes.get(j));
            }
            selected.add(winner);
        }
        return selected;
    }
}
