# Questions
## 1. (24 marks) Matrix multiplication is a common operation in linear algebra. Suppose you have multiple processors, so you can speed up a given matrix multiplication.
1.1. Modify the following method in the sample code to Implement a matrix multiplication
sequentially:
public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b)
Explain how you validate the method produces the correct results.

1.2. Modify the following method in the sample code to Implement a matrix multiplication in
parallel:
public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b)
Explain the parallel tasks defined in your code and how their results are combined to
produce the correct results. Explain how you validate the method works correctly.

1.3. Add a method to the source code that measures the execution time for both sequential
and parallel matrix multiplication.

1.4. Vary the number of threads being used by the parallel method for matrix sizes of 2000 by
2000 and plot the execution time as a function of the number of threads.

1.5. Vary the size of the matrices being multiplied as (100 by 100, 200 by 200, 500 by 500,
1000 by 1000, 2000 by 2000, 3000 by 3000, 4000 by 4000) and plot the execution time
as a function of matrix size for both parallel and sequential methods in one figure. Use
the number of threads for which the parallel execution time in 1.4 is minimum.

1.6. For the generated graphs in 1.4 and 1.5 comment on their shape and possible reasons
for the observed behavior.

## 2. (8 marks) Write a program that demonstrates deadlock.
2.1. Explain under what conditions deadlock could occur and comment on its possible
consequences.

2.2. Discuss possible design solutions to avoid deadlock.

## 3. (16 marks) The original dining philosophers problem was invented by E. W. Dijkstra, a concurrency pioneer, to clarify the notions of deadlock- and starvation-freedom. Imagine five philosophers who spend their days just thinking and feasting on rice. They sit around a circular table with five chairs. The table has a big plate of rice in the center; however there are only five chopsticks available, as shown in Fig. 1. Each philosopher thinks. When they get hungry, they pick up the two chopsticks closest to them. If a philosopher can pick up both chopsticks, they can eat for a while. When philosopher finishes eating, they put down the chopsticks and start to think again
