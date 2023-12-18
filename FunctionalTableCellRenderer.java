package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;

//ВИЗУАЛИЗАТОР ЯЧЕЕК
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

class FunctionTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
    // (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
    // стога сена - таблица
    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public FunctionTableCellRenderer() {
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
        // Не использовать группировку (т.е. не отделять тысячи
        // ни запятыми, ни пробелами)
        formatter.setGroupingUsed(false);
        // Установить в качестве разделителя дробной части точку
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        // Разместить надпись внутри панели
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean  isSelected, boolean hasFocus, int row, int col) {
        // Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
        // Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if (col == 1 && needle != null && needle.equals(formattedDouble)) {
            // Номер столбца = 1 (т.е. второй столбец) + иголка не null // (значит что-то ищем) +
            // значение иголки совпадает со значением ячейки таблицы -
            // окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
        } else {
            if (col != 2 && formattedDouble != null && (formattedDouble.substring(formattedDouble.indexOf('.') + 1, formattedDouble.length()).length() < 3)) {
                // Номер столбца = 1 (т.е. второй столбец) + иголка не null // (значит что-то ищем) +
                // значение иголки совпадает со значением ячейки таблицы -
                // окрасить задний фон панели в красный цвет
                panel.setBackground(Color.CYAN);
            }
            // Иначе - в обычный белый
            else {
                panel.setBackground(Color.WHITE);
            }
        }
        return panel;
    }
}