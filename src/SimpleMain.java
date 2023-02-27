import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SimpleMain {

    static double momentOfTheModelTime = 0.0; // модельный момент времени
    static double lambda = 3;
    static double nu = 6;
    static Queue<Requirement> queue = new LinkedList<>(); // очередь на обслуживание
    static List<Requirement> queueServiced = new ArrayList<>(); // очередь обслуженных требований
    static Double beginEvent = (double) 0; // указатель на следующее послупление требования
    static Double startEvent = Double.MAX_VALUE; // указатель на следующее начало обслуживания требования
    static Double endEvent = Double.MAX_VALUE; // указатель на следующий уход требования
    static boolean isFreeDevice = true; // свободен ли прибор

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

        for (Requirement req: queueServiced) {
            System.out.println(req);
        }
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
}
