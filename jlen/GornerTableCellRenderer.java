package bsu.rfe.java.group5.Lab3_Barsukevich.var4B.jlen;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer {
    private final JPanel panel = new JPanel();
    private final JLabel label = new JLabel();
    // Ищем ячейки, строковое представление которых совпадает с needle
// (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
// стога сена - таблица
    private String needle = null;
    private String needleStart = null;
    private String needleEnd = null;
    private final DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
// Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
// Не использовать группировку (т.е. не отделять тысячи
// ни запятыми, ни пробелами), т.е. показывать число как "1000",
// а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
// Установить в качестве разделителя дробной части точку, а не
// запятую. По умолчанию, в региональных настройках
// Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// Разместить надпись внутри панели
        panel.add(label);
// Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int col
    ) {
// Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
// Установить текст надписи равным строковому представлению числа

        boolean isItNeedle = needle != null && needle.equals(formattedDouble);
        boolean isNeedleInRange =
                needleStart != null &&
                needleEnd != null &&
                isInRange(formattedDouble);

        if ((col == 1 || col == 2) &&
            (isItNeedle || isNeedleInRange)
        ) {
// Номер столбца = 1 (т.е. второй столбец) + иголка не null
// (значит что-то ищем) +
// значение иголки совпадает со значением ячейки таблицы -
// окрасить задний фон панели в красный цвет
            panel.setBackground(Color.RED);
            label.setForeground(Color.BLACK);
            label.setText(formattedDouble);
        } else {
            if ((row + col) % 2 == 0) {
                panel.setBackground(Color.WHITE);
                label.setForeground(Color.BLACK);
                label.setText(formattedDouble);
            } else {
                panel.setBackground(Color.BLACK);
                label.setForeground(Color.WHITE);
                label.setText(formattedDouble);
            }
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    public void setNeedleRange(String start, String end) {
        this.needleStart = start;
        this.needleEnd = end;
    }

    private boolean isInRange(String valueStr) {
        double value = Double.parseDouble(valueStr);
        double start = Double.parseDouble(needleStart);
        double end = Double.parseDouble(needleEnd);
        return start <= value && end >= value;
    }
}