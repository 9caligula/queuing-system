public class Requirement {

    private double momentOfReceipt = 0;
    private double momentOfStart = 0;
    private double momentOfEnd = 0;

    public Requirement(double momentOfReceipt) {
        this.momentOfReceipt = momentOfReceipt;
    }

    public double getMomentOfReceipt() {
        return momentOfReceipt;
    }

    public double getMomentOfStart() {
        return momentOfStart;
    }

    public double getMomentOfEnd() {
        return momentOfEnd;
    }

    public void setMomentOfStart(double momentOfStart) {
        this.momentOfStart = momentOfStart;
    }

    public void setMomentOfEnd(double momentOfEnd) {
        this.momentOfEnd = momentOfEnd;
    }

    @Override
    public String toString() {
        return "{Момент поступления требования=" + momentOfReceipt +
                ", Момент начала обслуживания требования=" + momentOfStart +
                ", Момент завершения обслуживания требования=" + momentOfEnd +
                '}';
    }
}
