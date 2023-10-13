import org.architecture.report.ReportGenerator;
import org.architecture.system.SimulatedSystem;
import org.junit.Test;

public class TestSystem {

    @Test
    public void test() {
        int N = 100000;
        int buffer = 10;
        float avg = 1;
        int source = 10;
        int device = 10;
        float alpha = 1;
        float betta = 5;
        SimulatedSystem simulatedSystem = new SimulatedSystem(buffer, avg, source, device, alpha, betta);
        simulatedSystem.startSimulation(N);
        simulatedSystem.getLog().forEach(stringBuilder -> System.out.println(stringBuilder.toString()));
        ReportGenerator reportGenerator = new ReportGenerator("D:\\programming\\Architecture of software systems\\src\\main\\resources\\reports");
        reportGenerator.generateXLSXReport(simulatedSystem.getStatistic(), "Report" + ".xlsx");


    }
}
