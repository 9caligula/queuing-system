import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CompleteMain {

    static double momentOfTheModelTime = 0.0; // модельный момент времени
    static double lambda = 3;
    static double nu = 6;
    static Queue<Requirement> queue = new LinkedList<>(); // очередь на обслуживание
    static List<Requirement> queueServiced = new ArrayList<>(); // очередь обслуженных требований
    static Double beginEvent = (double) 0; // указатель на следующее послупление требования
    static Double startEvent = Double.MAX_VALUE; // указатель на следующее начало обслуживания требования
    static Double endEvent = Double.MAX_VALUE; // указатель на следующий уход требования
    static boolean isFreeDevice = true; // свободен ли прибор
    static int k = 5; // чисто требований, которые находились в СМО
    static int countIntervals = 0; // количество интервалов времени, в течении которых в СМО находятся k требований
    static double sumIntervalsTime = 0; // сумма интервалов времени, в течении которых в СМО находятся k требований

    public static void main(String[] args) {

        while (queueServiced.size() != 1000) {
            momentOfTheModelTime = Math.min(Math.min(beginEvent, endEvent), startEvent);
            if (momentOfTheModelTime == beginEvent) {
                beginServiceReq();
            }
            if (momentOfTheModelTime == startEvent || (endEvent == Double.MAX_VALUE)) {
                startServiceReq();
            }
            if (momentOfTheModelTime == endEvent) {
                endServiceReq();
            }
        }

        for (Requirement req : queueServiced) {
            System.out.println(req);
        }

        System.out.printf("\nМатематическое ожидание длительности пребывания требований равна: %f\n",
                getExpectedValueOfDurationOfStayInReq());
        System.out.printf("Количество интервалов времени, в течении которых в СМО находятся %d требований = %d\n", k, countIntervals);
        System.out.printf("Сумма интервалов времени, в течении которых в СМО находятся %d требований = %f\n", k, sumIntervalsTime);
        System.out.printf("Оценка математического ожидания числа требований в СМО: %f",
                getExpectedValueOfNumberRequirements());
    }

    public static void beginServiceReq() { // процедура-сегмент процесса на послупление требования
        queue.offer(new Requirement(momentOfTheModelTime));
        beginEvent = momentOfTheModelTime + exp(lambda);
    }

    public static void startServiceReq() { // процедура-сегмент процесса на начало обслуживания требования
        if (!queue.isEmpty()) {
            isFreeDevice = false;
            queue.peek().setMomentOfStart(momentOfTheModelTime);
            endEvent = momentOfTheModelTime + exp(nu);
        }
        startEvent = Double.MAX_VALUE;
    }

    public static void endServiceReq() { // процедура-сегмент процесса, связанный с уходом требования
        if (!queue.isEmpty()) {
            Requirement req = queue.poll();
            if (queue.size() == k - 1) {
                if (((LinkedList<Requirement>) queue).peekLast() != null) {
                    sumIntervalsTime += (momentOfTheModelTime - ((LinkedList<Requirement>) queue).peekLast().getMomentOfReceipt());
                } else {
                    sumIntervalsTime += (momentOfTheModelTime - req.getMomentOfReceipt());
                }
                countIntervals++;
            }
            req.setMomentOfEnd(momentOfTheModelTime);
            queueServiced.add(req);
            isFreeDevice = true;
            startEvent = momentOfTheModelTime;
        } else {
            endEvent = Double.MAX_VALUE;
        }
    }

    public static Double exp(double lam) {
        return (-1 / lam) * Math.log(Math.random());
    }

    // мат. ожидание длительности предывания требования в смо
    public static Double getExpectedValueOfDurationOfStayInReq() {
        double result = 0;
        for (Requirement requirement : queueServiced) {
            result += (requirement.getMomentOfEnd() - requirement.getMomentOfReceipt());
        }
        return result / queueServiced.size();
    }

    // оценка мат. ожидания числа требований в смо
    public static Double getExpectedValueOfNumberRequirements() {
        double p = sumIntervalsTime / momentOfTheModelTime;
        System.out.printf("Оценка вероятности того, что в системе обслуживания находится ровно %d требований: %f\n", k, p);
        double n = 0;
        for (int i = 1; i <= k; i++) {
            n += i * p;
        }
        return n;
    }
}
