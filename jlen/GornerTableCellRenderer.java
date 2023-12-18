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
    // ���� ������, ��������� ������������� ������� ��������� � needle
// (�������). ����������� �������� ������ ������ � ����� ����, � ����
// ����� ���� - �������
    private String needle = null;
    private String needleStart = null;
    private String needleEnd = null;
    private final DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
// ���������� ������ 5 ������ ����� �������
        formatter.setMaximumFractionDigits(5);
// �� ������������ ����������� (�.�. �� �������� ������
// �� ��������, �� ���������), �.�. ���������� ����� ��� "1000",
// � �� "1 000" ��� "1,000"
        formatter.setGroupingUsed(false);
// ���������� � �������� ����������� ������� ����� �����, � ��
// �������. �� ���������, � ������������ ����������
// ������/�������� ������� ����� ���������� �������
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
// ���������� ������� ������ ������
        panel.add(label);
// ���������� ������������ ������� �� ������ ���� ������
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
// ������������� double � ������ � ������� ��������������
        String formattedDouble = formatter.format(value);
// ���������� ����� ������� ������ ���������� ������������� �����

        boolean isItNeedle = needle != null && needle.equals(formattedDouble);
        boolean isNeedleInRange =
                needleStart != null &&
                needleEnd != null &&
                isInRange(formattedDouble);

        if ((col == 1 || col == 2) &&
            (isItNeedle || isNeedleInRange)
        ) {
// ����� ������� = 1 (�.�. ������ �������) + ������ �� null
// (������ ���-�� ����) +
// �������� ������ ��������� �� ��������� ������ ������� -
// �������� ������ ��� ������ � ������� ����
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