package bsu.rfe.java.group5.Lab3_Barsukevich.var4B;


import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main extends JFrame {
    // Константы с исходным размером окна приложения
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    // Объект диалогового окна для выбора файлов
    // Компонент не создаются изначально, т.к. может и не понадобиться
    // пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;
    // Элементы меню вынесены в поля данных класса, так как ими необходимо
    // манипулировать из разных мест
    private final JMenuItem saveToTextMenuItem;
    private JMenuItem searchValueMenuItem;
    private final JMenuItem infoMenuItem;

    private JCheckBoxMenuItem showColumnMenuItem;

    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;

    // Визуализатор ячеек таблицы
    private FunctionTableCellRenderer renderer = new FunctionTableCellRenderer();

    // Модель данных с результатами вычислений
    private FunctionTableModel data;

    private JTable table;
    private TableColumn bool_column;
    private Double param = -1.0;

    public Main(){
        // Обязательный вызов конструктора предка
        super("Табулирование функции на отрезке");
        // Установить размеры окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        // Создать меню
        JMenuBar menuBar = new JMenuBar();
        // Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
        // Добавить в меню пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        // Добавить его в главное меню
        menuBar.add(fileMenu);
        // Создать пункт меню "Таблица"
        JMenu tableMenu = new JMenu("Таблица");
        // Добавить его в главное меню
        menuBar.add(tableMenu);
        // Создать пункт меню "Справка"
        JMenu infoMenu = new JMenu("Справка");
        menuBar.add(infoMenu);
        // Создать новое "действие" по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction( "Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    // Если экземпляр диалогового окна "Открыть файл" ещ? не создан,
                    // то создать его
                    fileChooser = new JFileChooser();
                    // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
                // Показать диалоговое окно
                if (fileChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION){
                    // Если результат его показа успешный,
                    // сохранить данные в текстовый файл
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        // Добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem = fileMenu.add(saveToTextAction);

        fileMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                // По умолчанию пункт меню является недоступным (данных еще нет)
                if (data == null) saveToTextMenuItem.setEnabled(false);
                else saveToTextMenuItem.setEnabled(true);
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        });
        // Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение функции") {
            public void actionPerformed(ActionEvent event) {
                // Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(Main.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                // Установить введенное значение в качестве иголки
                renderer.setNeedle(value);
                // Обновить таблицу
                getContentPane().repaint();
            }
        };
        // Добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
        // По умолчанию пункт меню является недоступным (данных ещ? нет)
        tableMenu.add(new JSeparator());
        showColumnMenuItem = new JCheckBoxMenuItem("Показать третий столбец", true);
        tableMenu.add(showColumnMenuItem);
        showColumnMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 2) {
                    bool_column = table.getColumnModel().getColumn(2);
                    table.removeColumn(bool_column);
                }if(e.getStateChange() == 1){
                    table.addColumn(bool_column);
                }
            }
        });

        Action aboutProgrammAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
                Info info1 = new Info();
                JLabel info = new JLabel(info1.toString());
                info.setHorizontalTextPosition(JLabel.CENTER);
                info.setVerticalTextPosition(JLabel.BOTTOM);
                info.setIconTextGap(10);
                JOptionPane.showMessageDialog(Main.this, info, "О программе", JOptionPane.PLAIN_MESSAGE);
            }
        };
        infoMenuItem = infoMenu.add(aboutProgrammAction);
        // Создать текстовое поле для ввода значения длиной в 10 символов
        // со значением по умолчанию 0.0
        textFieldFrom = new JTextField("0.0", 10);
        // предотвратить увеличение размера поля ввода
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        // Создать контейнер 1 типа "коробка с горизонтальной укладкой"
        Box hboxXRange = Box.createHorizontalBox();
        // Задать для контейнера тип рамки "объ?мная"
        hboxXRange.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Настройки:"));
        hboxXRange.add(Box.createHorizontalGlue());
        hboxXRange.add(new JLabel("X изменяется на интервале от:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldFrom);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("до:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldTo);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("с шагом:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldStep);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(Box.createHorizontalGlue());
        // Установить предпочтительный размер области равным удвоенному
        // минимальному, чтобы при компоновке область совсем не сдавили
        hboxXRange.setPreferredSize(new Dimension((int)(hboxXRange.getMaximumSize().getWidth()),
                (int)(hboxXRange.getMinimumSize().getHeight()*1.5)));
        // Установить область в верхнюю (северную) часть компоновки
        getContentPane().add(hboxXRange, BorderLayout.NORTH);
        // Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
        // Задать действие на нажатие "Вычислить" и привязать к кнопке
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    // Считать значения начала и конца отрезка, шага
                    showColumnMenuItem.setState(true);
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    // На основе считанных данных создать новый экземпляр модели таблицы
                    data = new FunctionTableModel(-10d, 10d, 0.000001, param);
                    // Создать новый экземпляр таблицы
                    table = new JTable(data);
                    // Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
                    table.setDefaultRenderer(Double.class, renderer);
                    // Установить размер строки таблицы в 30 пикселов
                    table.setRowHeight(30);
                    // Удалить все вложенные элементы из контейнера hBoxResult
                    hBoxResult.removeAll();
                    // Добавить в hBoxResult таблицу, "обёрнутую" в панель с полосами прокрутки
                    hBoxResult.add(new JScrollPane(table));
                    // Обновить область содержания главного окна
                    hBoxResult.revalidate();

                }
                // В случае ошибки преобразования чисел показать сообщение об ошибке
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Main.this,
                            "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        // Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
        // Задать действие на нажатие "Очистить поля" и привязать к кнопке
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // Установить в полях ввода значения по умолчанию
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                // Удалить все вложенные элементы контейнера hBoxResult
                hBoxResult.removeAll();
                // Добавить в контейнер пустую панель
                hBoxResult.repaint();
                data = null;
            }
        });
        // Поместить созданные кнопки в контейнер
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createEtchedBorder());
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

        // Установить предпочтительный размер области равным удвоенному минимальному, чтобы при
        // компоновке окна область совсем не сдавили
        hboxButtons.setPreferredSize(new Dimension((int)(hboxButtons.getMaximumSize().getWidth()),
                (int)(hboxButtons.getMinimumSize().getHeight() * 2)));
        // Разместить контейнер с кнопками в нижней (южной) области граничной компоновки
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        // Область для вывода результата пока что пустая
        hBoxResult = Box.createHorizontalBox();
        // Установить контейнер hBoxResult в главной (центральной) области граничной компоновки
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToTextFile(File selectedFile) {// Создать  символьный поток вывода, направленный в указанный файл
        try{

            PrintStream out = new PrintStream(selectedFile); // Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования функции:");
            out.println("Интервал от " + data.getFrom() + " до " + data.getTo()+ " с шагом " +
                    data.getStep() + " и параметром " + data.getParameter());

            for (int i = 0; i < data.getRowCount(); i++)// Записать в поток вывода значения в точках
            {
                out.println("Значение в точке " + data.getValueAt(i,0)  + " равно " + data.getValueAt(i,1));
            }
            out.close();// Закрыть поток вывода
        } catch (FileNotFoundException e){
            // Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
            // так как мы файл создаём, а не открываем для чтения
        }
    }

    public static void main(String[] args){ // Создать экземпляр главного окна, передав ему коэффициенты
        Main frame = new Main();
        // Задать действие, выполняемое при закрытии окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}