package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;

//������������ �����
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

class FunctionTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    // ���� ������, ��������� ������������� ������� ��������� � needle
    // (�������). ����������� �������� ������ ������ � ����� ����, � ����
    // ����� ���� - �������
    private String needle = null;
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    public FunctionTableCellRenderer() {
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        // ���������� ������ 5 ������ ����� �������
        formatter.setMaximumFractionDigits(5);
        // �� ������������ ����������� (�.�. �� �������� ������
        // �� ��������, �� ���������)
        formatter.setGroupingUsed(false);
        // ���������� � �������� ����������� ������� ����� �����
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        // ���������� ������� ������ ������
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean  isSelected, boolean hasFocus, int row, int col) {
        // ������������� double � ������ � ������� ��������������
        String formattedDouble = formatter.format(value);
        // ���������� ����� ������� ������ ���������� ������������� �����
        label.setText(formattedDouble);
        if (col == 1 && needle != null && needle.equals(formattedDouble)) {
            // ����� ������� = 1 (�.�. ������ �������) + ������ �� null // (������ ���-�� ����) +
            // �������� ������ ��������� �� ��������� ������ ������� -
            // �������� ������ ��� ������ � ������� ����
            panel.setBackground(Color.RED);
        } else {
            if (col != 2 && formattedDouble != null && (formattedDouble.substring(formattedDouble.indexOf('.') + 1, formattedDouble.length()).length() < 3)) {
                // ����� ������� = 1 (�.�. ������ �������) + ������ �� null // (������ ���-�� ����) +
                // �������� ������ ��������� �� ��������� ������ ������� -
                // �������� ������ ��� ������ � ������� ����
                panel.setBackground(Color.CYAN);
            }
            // ����� - � ������� �����
            else {
                panel.setBackground(Color.WHITE);
            }
        }
        return panel;
    }
}