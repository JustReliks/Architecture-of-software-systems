package org.architecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private static final float tAlpha = 1.643f;
    private static final float delta = 0.1f;

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


//        float lambda = random.nextFloat();
//
//        SimulatedSystem simulatedSystem = new SimulatedSystem(5, lambda, 10, 10, 1.0f, 4f);
//        simulatedSystem.startSimulation(10000);
//
//        ReportGenerator reportGenerator = new ReportGenerator("D:\\programming\\Architecture of software systems\\src\\main\\resources\\reports");
//        reportGenerator.generateXLSXReport(simulatedSystem.getStatistic(), "Report" + ".xlsx", 0, 1, 2, 5, 10, 21);


//        int startN = random.nextInt(100, 200);
//
//        int N = startN;
//        SimulatedSystem simulatedSystem = new SimulatedSystem(5, lambda, 10, 10, 1.0f, 8.5f);
//        simulatedSystem.startSimulation(N);
//        float p = simulatedSystem.getStatistic().getRejectProbability();
//        int newN = getNewN(p);
//        int iteration = 0;
//        float newP = p;
//
//        while (p != 0 && (iteration++ == 0 || Math.abs(p - newP) >= 0.1 * p)) {
//            p = newP;
//            N = newN;
//            simulatedSystem.startSimulation(N);
//            newP = simulatedSystem.getStatistic().getRejectProbability();
//            newN = getNewN(newP);
//        }
//        simulatedSystem.getLog().forEach(stringBuilder -> System.out.println(stringBuilder.toString()));
//        System.out.printf("Completed with N %d ON %d iteration, lambda: %f, start N: %d\n", N, iteration, lambda, startN);
//        if (p == 0) {
//            System.out.println("System is perfect! p == 0");
//        }
//        ReportGenerator reportGenerator = new ReportGenerator("D:\\programming\\Architecture of software systems\\src\\main\\resources\\reports");
//        reportGenerator.generateXLSXReport(simulatedSystem.getStatistic(), "Report" + ".xlsx", 0, 1, 2, 5, 10, 21);

    }

    private static int getNewN(float p) {
        return (int) (Math.pow(tAlpha, 2) * (1 - p) / (p * Math.pow(delta, 2)));
    }
}