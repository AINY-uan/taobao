package org.ainy;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 主面板
 *
 * @ClassName MainFrame
 * @Author    AINY-uan
 * @Date      2019-02-27 10:30
 * @Version   1.2
 */
class MainFrame extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JPanel mainPanel = new JPanel(); // 主面板

    private JLabel jLabelNameOfAccount = new JLabel("账户名");  // 账户标签
    private JTextField jTextFieldOfAccount = new JTextField();       // 账户输入框

    private JLabel sizePanelName = new JLabel();
    private DefaultTableModel sizeTableModel = null;
    private JTable sizeTable = null;
    private JButton sizeTableBtnOfAdd = new JButton("添加"); // 添加平方米数据
    private JButton sizeTableBtnOfDel = new JButton("删除"); // 删除平方米数据

    private JLabel materialPanelName = new JLabel();
    private DefaultTableModel materialTableModel = null;
    private JTable materialTable = null;
    private JButton materialTableBtnOfAdd = new JButton("添加"); // 添加材料数据
    private JButton materialTableBtnOfDel = new JButton("删除"); // 删除材料数据

    private JLabel partsPanelName = new JLabel();
    private DefaultTableModel partsTableModel = null;
    private JTable partsTable = null;
    private JButton partsTableBtnOfAdd = new JButton("添加"); // 添加配件数据
    private JButton partsTableBtnOfDel = new JButton("删除"); // 删除配件数据

    private JLabel expressPanelName = new JLabel();
    private DefaultTableModel expressTableModel = null;
    private JTable expressTable = null;
    private JButton expressTableBtnOfAdd = new JButton("添加"); // 添加快递数据
    private JButton expressTableBtnOfDel = new JButton("删除"); // 删除快递数据

    private JTextArea inputArea = new JTextArea();

    private JButton outBtn = new JButton("按平方米输出"); // 输出按钮
    private JButton outBtn2 = new JButton("按米输出");   // 输出按钮
    private JTextArea outputArea = new JTextArea();

    private JButton copyBtn = new JButton("复制到剪贴板"); // 复制按钮

    private Connection conn;
    private Statement stmt;

    MainFrame(String username, String[][] sizeDatas, String[][] materialDatas, String[][] partsDatas, String[][] expressDatas) {

        this.setTitle("淘宝");
        this.mainPanel.setLayout(null);
        this.setBounds(10, 10, 800, 600);
        this.setResizable(true);
        this.setBackground(Color.WHITE);

        initAccountPanel(username);
        initSizePanel(sizeDatas);
        initMaterialPanel(materialDatas);
        initPartsPanel(partsDatas);
        initExpressPanel(expressDatas);
        initOutPanel();
        initCopyPanel();

        this.add(mainPanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                String url = "jdbc:Access:///db/Taobao.accdb"; // 连接名为Taobao的数据库
                String driver = "com.hxtt.sql.access.AccessDriver";
                try {
                    Class.forName(driver); // Microsoft Access数据库指定与JDBC-ODBC桥驱动
                    try {
                        conn = DriverManager.getConnection(url); // 创建与指定数据库的连接对象
                        stmt = conn.createStatement();
                        deleteAllDataByUsername();
                        InsertAllDataByUsername();
                    } catch (SQLException sqle) {
                        JOptionPane.showMessageDialog(null, "无法连接数据库！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "无法加载驱动程序！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * 初始化账户面板
     *
     * @param username 登录用户名
     */
    private void initAccountPanel(String username) {

        this.jLabelNameOfAccount.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.jLabelNameOfAccount.setBounds(10, 5, 100, 25); // 账户标签
        this.jTextFieldOfAccount.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.jTextFieldOfAccount.setBounds(110, 5, 140, 25); // 账户输入框
        this.jTextFieldOfAccount.setBackground(Color.PINK);
        this.jTextFieldOfAccount.setEditable(false);
        this.jTextFieldOfAccount.setText(username);
        this.mainPanel.add(jLabelNameOfAccount);
        this.mainPanel.add(jTextFieldOfAccount);
    }

    /**
     * 平方米管理面板初始化
     *
     * @param sizeDatas 平方米数据
     */
    private void initSizePanel(String[][] sizeDatas) {

        this.sizePanelName.setText("平方米管理");
        this.sizePanelName.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.sizePanelName.setBounds(10, 40, 130, 25);
        this.mainPanel.add(sizePanelName);
        this.sizeTableModel = generateSizeTableModel(sizeDatas);
        this.sizeTable = new JTable(sizeTableModel);
        DefaultTableCellRenderer sizeTableStyle = new DefaultTableCellRenderer();
        sizeTableStyle.setHorizontalAlignment(JLabel.CENTER);
        this.sizeTable.setDefaultRenderer(Object.class, sizeTableStyle);
        this.sizeTable.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        JTableHeader sizeTableHead = sizeTable.getTableHeader();
        sizeTableHead.setPreferredSize(new Dimension(sizeTableHead.getWidth(), 30));// 设置表头大小
        sizeTableHead.setFont(new Font("方正正中黑", Font.PLAIN, 20));// 设置表格字体
        this.sizeTable.setRowHeight(30);
        this.sizeTableBtnOfAdd.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.sizeTableBtnOfAdd.setBounds(150, 40, 80, 25);
        this.sizeTableBtnOfAdd.addActionListener(e -> sizeTableModel.addRow(new String[]{}));
        this.sizeTableBtnOfDel.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.sizeTableBtnOfDel.setBounds(240, 40, 80, 25);
        sizeTableBtnOfDel.addActionListener(e -> {
            int[] count = sizeTable.getSelectedRows();
            for (int i = count.length; i > 0; i--) {
                sizeTableModel.removeRow(sizeTable.getSelectedRow());
            }
        });
        JScrollPane sizeJScrollPane = new JScrollPane(sizeTable);
        sizeJScrollPane.setViewportView(sizeTable);
        sizeJScrollPane.setBounds(10, 80, 400, 400);
        this.mainPanel.add(sizeTableBtnOfAdd);
        this.mainPanel.add(sizeTableBtnOfDel);
        this.mainPanel.add(sizeJScrollPane);
    }

    /**
     * 生成尺寸表格
     *
     * @param sizeDatas 平方米数据
     * @return DefaultTableModel
     */
    private DefaultTableModel generateSizeTableModel(String[][] sizeDatas) {

        String[] columnNames = {"宽度", "长度", "数量"};
        return new DefaultTableModel(sizeDatas, columnNames);
    }

    /**
     * 初始化材料面板
     *
     * @param materialDatas 材料数据
     */
    private void initMaterialPanel(String[][] materialDatas) {

        this.materialPanelName.setText("材料管理");
        this.materialPanelName.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.materialPanelName.setBounds(430, 40, 130, 25);
        this.mainPanel.add(materialPanelName);
        this.materialTableModel = generateMaterialTableModel(materialDatas);
        this.materialTable = new JTable(materialTableModel);
        DefaultTableCellRenderer materialTableStyle = new DefaultTableCellRenderer();
        materialTableStyle.setHorizontalAlignment(JLabel.CENTER);
        this.materialTable.setDefaultRenderer(Object.class, materialTableStyle);
        this.materialTable.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        JTableHeader materialTableHead = materialTable.getTableHeader();
        materialTableHead.setPreferredSize(new Dimension(materialTableHead.getWidth(), 30));// 设置表头大小
        materialTableHead.setFont(new Font("方正正中黑", Font.PLAIN, 20));// 设置表格字体
        this.materialTable.setRowHeight(30);
        this.materialTableBtnOfAdd.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.materialTableBtnOfAdd.setBounds(540, 40, 80, 25);
        this.materialTableBtnOfAdd.addActionListener(e -> materialTableModel.addRow(new String[]{}));
        this.materialTableBtnOfDel.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.materialTableBtnOfDel.setBounds(630, 40, 80, 25);
        materialTableBtnOfDel.addActionListener(e -> {
            int[] count = materialTable.getSelectedRows();
            for (int i = count.length; i > 0; i--) {
                materialTableModel.removeRow(materialTable.getSelectedRow());
            }
        });
        JScrollPane materialJScrollPane = new JScrollPane(materialTable);
        materialJScrollPane.setViewportView(materialTable);
        materialJScrollPane.setBounds(430, 80, 290, 400);
        this.mainPanel.add(materialTableBtnOfAdd);
        this.mainPanel.add(materialTableBtnOfDel);
        this.mainPanel.add(materialJScrollPane);
    }

    /**
     * 生成材料表格
     *
     * @param materialDatas 材料数据
     * @return DefaultTableModel
     */
    private DefaultTableModel generateMaterialTableModel(String[][] materialDatas) {

        String[] columnNames = {"材料名称"};
        return new DefaultTableModel(materialDatas, columnNames);
    }

    /**
     * 初始化配件面板
     *
     * @param partsDatas 配件数据
     */
    private void initPartsPanel(String[][] partsDatas) {

        this.partsPanelName.setText("配件管理");
        this.partsPanelName.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.partsPanelName.setBounds(740, 40, 130, 25);
        this.mainPanel.add(partsPanelName);
        this.partsTableModel = generatePartsTableModel(partsDatas);
        this.partsTable = new JTable(partsTableModel);
        DefaultTableCellRenderer partsTableStyle = new DefaultTableCellRenderer();
        partsTableStyle.setHorizontalAlignment(JLabel.CENTER);
        this.partsTable.setDefaultRenderer(Object.class, partsTableStyle);
        this.partsTable.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        JTableHeader partsTableHead = partsTable.getTableHeader();
        partsTableHead.setPreferredSize(new Dimension(partsTableHead.getWidth(), 30));// 设置表头大小
        partsTableHead.setFont(new Font("方正正中黑", Font.PLAIN, 20));// 设置表格字体
        this.partsTable.setRowHeight(30);
        this.partsTableBtnOfAdd.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.partsTableBtnOfAdd.setBounds(850, 40, 80, 25);
        this.partsTableBtnOfAdd.addActionListener(e -> partsTableModel.addRow(new String[]{}));
        this.partsTableBtnOfDel.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.partsTableBtnOfDel.setBounds(940, 40, 80, 25);
        partsTableBtnOfDel.addActionListener(e -> {
            int[] count = partsTable.getSelectedRows();
            for (int i = count.length; i > 0; i--) {
                partsTableModel.removeRow(partsTable.getSelectedRow());
            }
        });
        JScrollPane partsJScrollPane = new JScrollPane(partsTable);
        partsJScrollPane.setViewportView(partsTable);
        partsJScrollPane.setBounds(740, 80, 280, 400);
        this.mainPanel.add(partsTableBtnOfAdd);
        this.mainPanel.add(partsTableBtnOfDel);
        this.mainPanel.add(partsJScrollPane);
    }

    /**
     * 生成配件表格
     *
     * @param partsDatas 配件数据
     * @return DefaultTableModel
     */
    private DefaultTableModel generatePartsTableModel(String[][] partsDatas) {

        String[] columnNames = {"配件名称"};
        return new DefaultTableModel(partsDatas, columnNames);
    }

    /**
     * 初始化快递面板
     *
     * @param expressDatas 快递数据
     */
    private void initExpressPanel(String[][] expressDatas) {

        this.expressPanelName.setText("快递管理");
        this.expressPanelName.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.expressPanelName.setBounds(1050, 40, 130, 25);
        this.mainPanel.add(expressPanelName);
        this.expressTableModel = generateExpressTableModel(expressDatas);
        this.expressTable = new JTable(expressTableModel);
        DefaultTableCellRenderer expressTableStyle = new DefaultTableCellRenderer();
        expressTableStyle.setHorizontalAlignment(JLabel.CENTER);
        this.expressTable.setDefaultRenderer(Object.class, expressTableStyle);
        this.expressTable.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        JTableHeader expressTableHead = expressTable.getTableHeader();
        expressTableHead.setPreferredSize(new Dimension(expressTableHead.getWidth(), 30));// 设置表头大小
        expressTableHead.setFont(new Font("方正正中黑", Font.PLAIN, 20));// 设置表格字体
        this.expressTable.setRowHeight(30);
        this.expressTableBtnOfAdd.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.expressTableBtnOfAdd.setBounds(1150, 40, 80, 25);
        this.expressTableBtnOfAdd.addActionListener(e -> expressTableModel.addRow(new String[]{}));
        this.expressTableBtnOfDel.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.expressTableBtnOfDel.setBounds(1240, 40, 80, 25);
        expressTableBtnOfDel.addActionListener(e -> {
            int[] count = expressTable.getSelectedRows();
            for (int i = count.length; i > 0; i--) {
                expressTableModel.removeRow(expressTable.getSelectedRow());
            }
        });
        JScrollPane expressJScrollPane = new JScrollPane(expressTable);
        expressJScrollPane.setViewportView(expressTable);
        expressJScrollPane.setBounds(1050, 80, 270, 400);
        this.mainPanel.add(expressTableBtnOfAdd);
        this.mainPanel.add(expressTableBtnOfDel);
        this.mainPanel.add(expressJScrollPane);
    }

    /**
     * 生成快递表格
     *
     * @param expressDatas 快递数据
     * @return DefaultTableModel
     */
    private DefaultTableModel generateExpressTableModel(String[][] expressDatas) {

        String[] columnNames = {"快递名称"};
        return new DefaultTableModel(expressDatas, columnNames);
    }

    /**
     * 初始化输出面板
     */
    private void initOutPanel() {

        this.inputArea.setEditable(true); // 文本区可编辑
        this.inputArea.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.mainPanel.add(getInputArea(), null);
        this.outBtn.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.outBtn.setBounds(10, 650, 195, 25);
        this.outBtn.addActionListener(e -> outputButton_ActionEvent());
        this.outBtn2.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.outBtn2.setBounds(215, 650, 195, 25);
        this.outBtn2.addActionListener(e -> outputButton_ActionEvent2());
        this.mainPanel.add(outBtn);
        this.mainPanel.add(outBtn2);
    }

    /**
     * 初始化复制面板
     */
    private void initCopyPanel() {

        this.outputArea.setEditable(false); // 文本区不可编辑
        this.outputArea.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.mainPanel.add(getOutputArea(), null);
        this.copyBtn.setFont(new Font("方正正中黑", Font.PLAIN, 20));
        this.copyBtn.setBounds(430, 650, 400, 25);
        this.copyBtn.addActionListener(e -> {
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable tText = new StringSelection(outputArea.getText());
            clip.setContents(tText, null);
        });
        this.mainPanel.add(copyBtn);
    }

    /**
     * 输入文本框
     */
    private JScrollPane getInputArea() { // 显示框滚动条
        if (inputArea == null) {
            inputArea = new JTextArea();
        }
        inputArea.setLineWrap(true);
        JScrollPane inSP = new JScrollPane(inputArea);
        inSP.setBounds(10, 500, 400, 140);
        return inSP;
    }

    /**
     * 输出文本框
     */
    private JScrollPane getOutputArea() { // 显示框滚动条
        if (outputArea == null) {
            outputArea = new JTextArea();
        }
        outputArea.setLineWrap(true);
        outputArea.setBackground(Color.PINK);
        JScrollPane outSP = new JScrollPane(outputArea);
        outSP.setBounds(430, 500, 400, 140);
        return outSP;
    }

    /**
     * 输出按钮响应
     */
    private void outputButton_ActionEvent() {

        String name = jTextFieldOfAccount.getText().trim();
        String input = inputArea.getText().trim();
        if ("".equals(name)) {
            jTextFieldOfAccount.setText("");
            JOptionPane.showMessageDialog(null, "请输入账户名！", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            int[] materialTableModelCounts = materialTable.getSelectedRows(); // 选中的材料行数
            int[] sizeTableModelCounts = sizeTable.getSelectedRows();         // 选中的平方米行数
            int[] partsTableModelCounts = partsTable.getSelectedRows();       // 选中的配件行数
            int[] expressTableModelCounts = expressTable.getSelectedRows();   // 选中的快递行数
            if (materialTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择材料，只能选择一种，多选默认取第一条！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (sizeTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择平方米！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (partsTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择配件！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (expressTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择快递，只能选择一种，多选默认取第一条！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    sb.append(name);
                    sb.append("/");
                    sb.append(materialTableModel.getValueAt(materialTable.getSelectedRow(), 0));
                    sb.append("/");
                    BigDecimal area = new BigDecimal("0");
                    int counts = 0;
                    for (int sizeTableModelCount : sizeTableModelCounts) {
                        sb1.append(sizeTableModel.getValueAt(sizeTableModelCount, 0));
                        sb1.append("米宽");
                        sb1.append(sizeTableModel.getValueAt(sizeTableModelCount, 1));
                        sb1.append("米长");
                        sb1.append(sizeTableModel.getValueAt(sizeTableModelCount, 2));
                        sb1.append("条");
                        sb1.append("+");
                        BigDecimal width = new BigDecimal(sizeTableModel.getValueAt(sizeTableModelCount, 0).toString().trim());
                        BigDecimal length = new BigDecimal(sizeTableModel.getValueAt(sizeTableModelCount, 1).toString().trim());
                        BigDecimal count = new BigDecimal(sizeTableModel.getValueAt(sizeTableModelCount, 2).toString().trim());
                        area = area.add(length.multiply(width).multiply(count));
                        counts += count.intValue();
                    }
                    sb.append(sb1.toString(), 0, sb1.toString().length() - 1);
                    sb.append("/");
                    sb.append("总共");
                    sb.append(counts);
                    sb.append("条");
                    sb.append("/");
                    for (int partsTableModelCount : partsTableModelCounts) {
                        sb2.append(partsTableModel.getValueAt(partsTableModelCount, 0));
                        sb2.append("+");
                    }
                    if ("".equals(input)) {
                        sb2.append("无");
                    } else {
                        sb2.append(input);
                    }
                    sb.append(sb2.toString());
                    sb.append("/");
                    sb.append(area);
                    sb.append("/");
                    sb.append(expressTableModel.getValueAt(expressTable.getSelectedRow(), 0));
                    outputArea.setText(sb.toString());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * 按米输出按钮响应
     */
    private void outputButton_ActionEvent2() {

        String name = jTextFieldOfAccount.getText().trim();
        String input = inputArea.getText().trim();
        if ("".equals(name)) {
            jTextFieldOfAccount.setText("");
            JOptionPane.showMessageDialog(null, "请输入账户名！", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            int[] materialTableModelCounts = materialTable.getSelectedRows(); // 选中的材料行数
            int[] sizeTableModelCounts = sizeTable.getSelectedRows(); // 选中的平方米行数
            int[] partsTableModelCounts = partsTable.getSelectedRows(); // 选中的配件行数
            int[] expressTableModelCounts = expressTable.getSelectedRows(); // 选中的快递行数
            if (materialTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择材料，只能选择一种，多选默认取第一条！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (sizeTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择平方米！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (partsTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择配件！", "错误", JOptionPane.ERROR_MESSAGE);
            } else if (expressTableModelCounts.length <= 0) {
                JOptionPane.showMessageDialog(null, "请选择快递，只能选择一种，多选默认取第一条！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    StringBuilder sb = new StringBuilder();
                    StringBuilder sb1 = new StringBuilder();
                    StringBuilder sb2 = new StringBuilder();
                    sb.append(name);
                    sb.append("/");
                    sb.append(materialTableModel.getValueAt(materialTable.getSelectedRow(), 0));
                    sb.append("/");
                    sb.append("总共");
                    BigDecimal counts = new BigDecimal("0");
                    for (int sizeTableModelCount : sizeTableModelCounts) {
                        counts = counts.add(new BigDecimal(sizeTableModel.getValueAt(sizeTableModelCount, 2).toString().trim()));
                    }
                    sb.append(counts);
                    sb.append("条");
                    sb.append("/");
                    for (int partsTableModelCount : partsTableModelCounts) {
                        sb1.append(partsTableModel.getValueAt(partsTableModelCount, 0));
                        sb1.append("+");
                    }
                    if ("".equals(input)) {
                        sb1.append("无");
                    } else {
                        sb1.append(input);
                    }
                    sb.append(sb1.toString());
                    sb.append("/");
                    for (int sizeTableModelCount : sizeTableModelCounts) {
                        sb2.append(sizeTableModel.getValueAt(sizeTableModelCount, 0));
                        sb2.append("=");
                        BigDecimal length = new BigDecimal(sizeTableModel.getValueAt(sizeTableModelCount, 1).toString().trim());
                        sb2.append(length);
                        sb2.append("米");
                        sb2.append("+");
                    }
                    sb.append(sb2.toString(), 0, sb2.toString().length() - 1);
                    sb.append("/");
                    sb.append(expressTableModel.getValueAt(expressTable.getSelectedRow(), 0));
                    outputArea.setText(sb.toString());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * 根据用户名删除所有数据
     *
     * @throws SQLException SQLException
     */
    private void deleteAllDataByUsername() throws SQLException {

        String username = jTextFieldOfAccount.getText();
        stmt.executeUpdate("DELETE FROM Size WHERE USERNAME = " + "'" + username + "'");
        stmt.executeUpdate("DELETE FROM Material WHERE USERNAME = " + "'" + username + "'");
        stmt.executeUpdate("DELETE FROM Parts WHERE USERNAME = " + "'" + username + "'");
        stmt.executeUpdate("DELETE FROM Express WHERE USERNAME = " + "'" + username + "'");
    }

    /**
     * 将面板上的所有数据写入数据库
     *
     * @throws SQLException SQLException
     */
    private void InsertAllDataByUsername() throws SQLException {

        String username = jTextFieldOfAccount.getText();
        for (int a = 0; a < sizeTable.getRowCount(); a++) {
            stmt.executeUpdate("INSERT INTO Size VALUES('" + username + "','" + sizeTableModel.getValueAt(a, 1) + "','"
                    + sizeTableModel.getValueAt(a, 0) + "','" + sizeTableModel.getValueAt(a, 2) + "')");
        }
        for (int a = 0; a < materialTable.getRowCount(); a++) {
            stmt.executeUpdate("INSERT INTO Material VALUES('" + username + "','" + materialTableModel.getValueAt(a, 0) + "')");
        }
        for (int a = 0; a < partsTable.getRowCount(); a++) {
            stmt.executeUpdate("INSERT INTO Parts VALUES('" + username + "','" + partsTableModel.getValueAt(a, 0) + "')");
        }
        for (int a = 0; a < expressTable.getRowCount(); a++) {
            stmt.executeUpdate("INSERT INTO Express VALUES('" + username + "','" + expressTableModel.getValueAt(a, 0) + "')");
        }
    }
}