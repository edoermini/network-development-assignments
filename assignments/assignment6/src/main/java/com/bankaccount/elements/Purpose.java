package com.bankaccount.elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Purpose {
    BONIFICO,
    ACCREDITO,
    BOLLETTINO,
    F24,
    PAGOBANCOMAT;

    private static final List<Purpose> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Purpose randomPurpose()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
