package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;

public class Info {
    private final String surname = "Барсукевич";
    private final int group = 5;

    @Override
    public String toString() {
        return "Группа: " + group + " " +
                ", фамилия: " + surname;
    }
}
