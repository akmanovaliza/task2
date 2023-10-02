import java.util.Arrays;

public class ChannelProcessor {

    public static void main(String[] args) {
        double[] inputChannel = {1.3, 2.0, 2.1, 2.6, 2.6, 3.2, 3.6, 1.0};
        double threshold = 0.5;

        processChannels(inputChannel, threshold);
    }

    private static void processChannels(double[] inputChannel, double threshold) {
        Arrays.stream(inputChannel)
                .reduce((prev, current) -> processValue(prev, current, threshold));
    }

    private static Double processValue(Double prev, double current, double threshold) {
        if (Math.abs(current - prev) > threshold) {
            printAndReturn(current);
            return current;
        } else {
            return current;
        }
    }

    private static void printAndReturn(double value) {
        System.out.println("Writing to output channel: " + value);
    }
}
