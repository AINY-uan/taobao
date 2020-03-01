package org.ainy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录面板
 *
 * @author AINY
 * @date 2019-02-27 10:28
 */
public class User extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JPanel userPanel;
    /**
     * 主名称
     */
    private JLabel jLabelName;
    /**
     * 账号标签
     */
    private JLabel jLabelUserName;
    /**
     * 账号显示框
     */
    private JTextField jTextFieldUserName;
    /**
     * 密码标签
     */
    private JLabel jLabelPassWord;
    /**
     * 密码显示框
     */
    private JPasswordField jPasswordFieldPassWord;
    /**
     * 注册按钮
     */
    private JButton btnOfReg;
    /**
     * 登陆按钮
     */
    private JButton btnOfLogin;

    private Connection conn;
    private Statement stmt;

    public static void main(String[] args) {
        new User();
    }

    private User() {

        // 连接名为Taobao的数据库
        String url = "jdbc:Access:///db/Taobao.accdb";
        String driver = "com.hxtt.sql.access.AccessDriver";
        try {
            // Microsoft Access数据库指定与JDBC-ODBC桥驱动
            Class.forName(driver);
            try {
                // 创建与指定数据库的连接对象
                conn = DriverManager.getConnection(url);
                stmt = conn.createStatement();
                userPanel = new JPanel();
                jLabelName = new JLabel();
                jLabelUserName = new JLabel();
                jTextFieldUserName = new JTextField();
                jLabelPassWord = new JLabel();
                jPasswordFieldPassWord = new JPasswordField();
                btnOfReg = new JButton();
                btnOfLogin = new JButton();
                account();
            } catch (SQLException sqle) {
                JOptionPane.showMessageDialog(null, "无法连接数据库！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "无法加载驱动程序！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void account() {

        this.setTitle("用户登录");
        this.userPanel.setLayout(null);
        this.setBounds(500, 180, 370, 300);
        this.setResizable(false);
        this.setBackground(Color.lightGray);
        this.jLabelName.setText("淘宝");
        this.jLabelName.setFont(new Font("华文楷体", Font.PLAIN, 40));
        this.jLabelUserName.setText("账号");
        this.jLabelPassWord.setText("密码");
        this.btnOfReg.setText("注册");
        this.btnOfLogin.setText("登录");
        // 主名称
        this.jLabelName.setBounds(140, 30, 200, 50);
        // 账号标签
        this.jLabelUserName.setBounds(85, 100, 60, 25);
        // 密码标签
        this.jLabelPassWord.setBounds(85, 140, 60, 25);
        // 账号显示框
        this.jTextFieldUserName.setBounds(140, 100, 140, 25);
        // 密码显示框
        this.jPasswordFieldPassWord.setBounds(140, 140, 140, 25);

        // 注册
        this.btnOfReg.setBounds(110, 190, 60, 25);
        this.btnOfReg.addActionListener(e -> btnOfRegActionEvent());

        // 登录
        this.btnOfLogin.setBounds(200, 190, 60, 25);
        this.btnOfLogin.addActionListener(e -> btnOfLoginActionEvent());

        this.userPanel.add(jLabelName);
        this.userPanel.add(jLabelUserName);
        this.userPanel.add(jLabelPassWord);
        this.userPanel.add(jTextFieldUserName);
        this.userPanel.add(jPasswordFieldPassWord);
        this.userPanel.add(btnOfReg);
        this.userPanel.add(btnOfLogin);
        this.add(userPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException exp) {
                    exp.printStackTrace();
                }
            }
        });
    }

    /**
     * 注册按钮响应
     */
    private void btnOfRegActionEvent() {
        String username = jTextFieldUserName.getText().trim();
        String password = String.valueOf(jPasswordFieldPassWord.getPassword()).trim();
        boolean flag = true;
        try {
            ResultSet rsetOfUser = stmt.executeQuery("SELECT * FROM User WHERE USERNAME = " + "'" + username + "'");
            if ("".equals(username) || "".equals(password)) {
                JOptionPane.showMessageDialog(null, "账号或密码为空，请输入！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                while (rsetOfUser.next()) {
                    if (username.equals(rsetOfUser.getString("USERNAME"))) {
                        flag = false;
                        JOptionPane.showMessageDialog(null, "该账号已存在，请重新输入！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
                if (flag) {
                    stmt.executeUpdate("INSERT INTO User VALUES('" + username + "','" + password + "')");
                    JOptionPane.showMessageDialog(null, "恭喜，注册成功！", "系统消息", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * 登录按钮响应
     */
    private void btnOfLoginActionEvent() {

        String username = jTextFieldUserName.getText().trim();
        String password = String.valueOf(jPasswordFieldPassWord.getPassword()).trim();
        boolean flag = false;
        try {
            if ("".equals(username) || "".equals(password)) {
                JOptionPane.showMessageDialog(null, "账号或密码为空，请输入！", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                ResultSet rsetOfUser = stmt.executeQuery("SELECT * FROM User WHERE USERNAME = " + "'" + username + "'");
                while (rsetOfUser.next()) {
                    if (username.equals(rsetOfUser.getString("USERNAME")) && password.equals(rsetOfUser.getString("PASSWORD"))) {
                        flag = true;
                        this.setVisible(false);
                        new MainFrame(username, getSizeDatas(username), getMaterialDatas(username), getPartsDatas(username), getExpressDatas(username));
                        break;
                    }
                }
                if (!flag) {
                    JOptionPane.showMessageDialog(null, "账号或密码错误，请重新输入！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
        }
    }

    /**
     * 获取用户下的平方米数据
     *
     * @param username 登录用户名
     * @return 字符串数组
     * @throws SQLException SQLException
     */
    private String[][] getSizeDatas(String username) throws SQLException {

        ResultSet rsetOfSize = stmt.executeQuery("SELECT * FROM Size WHERE USERNAME = " + "'" + username + "'");
        List<List<String>> allList = new ArrayList<>();
        while (rsetOfSize.next()) {
            List<String> rowList = new ArrayList<>();
            rowList.add(rsetOfSize.getString("WIDTH"));
            rowList.add(rsetOfSize.getString("LENGTH"));
            rowList.add(rsetOfSize.getString("COUNTS"));
            allList.add(rowList);
        }
        String[][] sizeDatas = new String[allList.size()][3];
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
                sizeDatas[i][j] = allList.get(i).get(j);
            }
        }
        return sizeDatas;
    }

    /**
     * 获取用户下的材料数据
     *
     * @param username 登录用户名
     * @return 字符串数组
     * @throws SQLException SQLException
     */
    private String[][] getMaterialDatas(String username) throws SQLException {

        ResultSet rsetOfMaterial = stmt.executeQuery("SELECT * FROM Material WHERE USERNAME = " + "'" + username + "'");
        List<List<String>> allList = new ArrayList<>();
        while (rsetOfMaterial.next()) {
            List<String> rowList = new ArrayList<>();
            rowList.add(rsetOfMaterial.getString("MATERIALNAME"));
            allList.add(rowList);
        }
        String[][] materialDatas = new String[allList.size()][1];
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
                materialDatas[i][j] = allList.get(i).get(j);
            }
        }
        return materialDatas;
    }

    /**
     * 获取用户下的配件数据
     *
     * @param username 登录用户名
     * @return 字符串数组
     * @throws SQLException SQLException
     */
    private String[][] getPartsDatas(String username) throws SQLException {

        ResultSet rsetOfParts = stmt.executeQuery("SELECT * FROM Parts WHERE USERNAME = " + "'" + username + "'");
        List<List<String>> allList = new ArrayList<>();
        while (rsetOfParts.next()) {
            List<String> rowList = new ArrayList<>();
            rowList.add(rsetOfParts.getString("PARTSNAME"));
            allList.add(rowList);
        }
        String[][] partsDatas = new String[allList.size()][1];
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
                partsDatas[i][j] = allList.get(i).get(j);
            }
        }
        return partsDatas;
    }

    /**
     * 获取用户下的快递数据
     *
     * @param username 登录用户名
     * @return 字符串数组
     * @throws SQLException SQLException
     */
    private String[][] getExpressDatas(String username) throws SQLException {

        ResultSet rsetOfExpress = stmt.executeQuery("SELECT * FROM Express WHERE USERNAME = " + "'" + username + "'");
        List<List<String>> allList = new ArrayList<>();
        while (rsetOfExpress.next()) {
            List<String> rowList = new ArrayList<>();
            rowList.add(rsetOfExpress.getString("EXPRESSNAME"));
            allList.add(rowList);
        }
        String[][] expressDatas = new String[allList.size()][1];
        for (int i = 0; i < allList.size(); i++) {
            for (int j = 0; j < allList.get(i).size(); j++) {
                expressDatas[i][j] = allList.get(i).get(j);
            }
        }
        return expressDatas;
    }
}