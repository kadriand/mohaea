package com.co.evolution.model;

import java.util.List;

public interface SelectionMethod<T> {

    void init(List<T> individuals);

    List<T> select(List<T> individuals, int individualNumber);

}
