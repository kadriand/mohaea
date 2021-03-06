package com.co.evolution.demo.functions;

import com.co.evolution.individual.RealIndividual;
import com.co.evolution.model.ObjectiveFunction;

public class ZitzlerDebThiele {

    public static class ZDT3_F1 extends ObjectiveFunction<RealIndividual> {
        public ZDT3_F1(boolean minimize) {
            super(minimize);
        }

        @Override
        public double compute(RealIndividual individual) {
            return individual.get()[0];
        }
    }

    public static class ZDT3_F2 extends ObjectiveFunction<RealIndividual> {

        public ZDT3_F2(boolean minimize) {
            super(minimize);
        }

        @Override
        public double compute(RealIndividual individual) {
            double g = 1;
            for (int i = 1; i < 30;i++)
                g += 9 / 29 * individual.get()[i];
            double h = 1 - Math.pow(individual.get()[0] / g, 0.5) - individual.get()[0] / g * Math.sin(10 * Math.PI * individual.get()[0]);
            return g*h;
        }
    }

    public static class ZDT2_F1 extends ObjectiveFunction<RealIndividual> {
        public ZDT2_F1(boolean minimize) {
            super(minimize);
        }

        @Override
        public double compute(RealIndividual individual) {
            return individual.get()[0];
        }
    }

    public static class ZDT2_F2 extends ObjectiveFunction<RealIndividual> {

        public ZDT2_F2(boolean minimize) {
            super(minimize);
        }

        @Override
        public double compute(RealIndividual individual) {
            double g = 1;
            for (int i = 1; i < 30;i++)
                g += 9 / 29 * individual.get()[i];
            double h = 1 - Math.pow(individual.get()[0] / g, 2);
            return g*h;
        }
    }
}
