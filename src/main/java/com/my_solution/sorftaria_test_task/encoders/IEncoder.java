package com.my_solution.sorftaria_test_task.encoders;

public interface IEncoder<T> {
    byte[] encode(T source);
}
