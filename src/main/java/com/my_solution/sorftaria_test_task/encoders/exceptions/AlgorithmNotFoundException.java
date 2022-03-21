package com.my_solution.sorftaria_test_task.encoders.exceptions;

import com.my_solution.sorftaria_test_task.encoders.exceptions.AbstractEncodingException;

public class AlgorithmNotFoundException extends AbstractEncodingException {
    public AlgorithmNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
