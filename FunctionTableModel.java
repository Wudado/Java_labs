package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;

import javax.swing.table.AbstractTableModel;

public class FunctionTableModel extends AbstractTableModel{

    private Double from, to, step, parameter;

    public FunctionTableModel(Double from, Double to, Double step, Double parameter) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.parameter = parameter;
    }

    public Double getParameter() { return parameter; }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    @Override
    // Вычислить количество точек между началом и концом отрезка
    // исходя из шага табулирования
    public int getRowCount() {
        return (int)(Math.ceil((to - from) / step)) + 1;
    }

    @Override
    // В данной модели 3 столбца
    public int getColumnCount() {
        return 3;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        Double x = from + step * rowIndex;

        Double y=parameter+x*2+x*x*x; //хуй
        Boolean z = gcd(x.intValue(), y.intValue());
        switch (columnIndex){
            case 0: return x;
            case 1: return y;
            case 2: return z;
        }
        return null;
    }

    private Boolean gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a == 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 2) return Boolean.class;
        return Double.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            // Название 1-го столбца
            case 0: return "Значение Х";
            // Название 2-го столбца
            case 1: return "Значение многочлена";
            // Название 3-го столбца
            case 2: return "Взаимно простые?";
        }
        return "";
    }
}