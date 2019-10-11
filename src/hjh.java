import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class hjh extends JFrame {

    String inputStr = "";
    JPanel panel;
    JTextField text;
    String ss[] = { "7", "8", "9", "/", "4", "5", "6", "*", "1", "2", "3", "-", ".", "0", "=", "+", "c", "<-", "(",
            ")" };

    public hjh() {
        this.setBounds(300, 200, 400, 300);
        this.setTitle("计算器");
        this.setLayout(new BorderLayout());


        text = new JTextField(15);
        text.setHorizontalAlignment(JTextField.RIGHT);
        text.setEditable(false);
        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4));

        for (int i = 0; i < ss.length; i++) {
            JButton button = new JButton(ss[i]);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO Auto-generated method stub
                    String s = e.getActionCommand();
                    if(inputStr.isEmpty())
                    {
                        inputStr += s;
                        text.setText(inputStr);
                        return;
                    }
                    char lastChar = inputStr.charAt(inputStr.length()-1);
                    if (s.equals(".") || s.equals("/") || s.equals("*") || s.equals("+") || s.equals("-")
                            || s.equals("(") || s.equals(")")) {

                        // inputStr+=s;
                        if (lastChar == '+'||lastChar == '-'||lastChar == '*'||lastChar == '/')
                        {

                            inputStr = inputStr.substring(0, inputStr.length()-1) + s;
                        }
                        else
                        {
                            inputStr += s;
                        }
                        text.setText(inputStr);

                    } else if (s.equals("c")) {
                        inputStr = "";
                        text.setText(inputStr);
                    } else if (s.equals("<-")) {

                        if (!inputStr.isEmpty()) {
                            inputStr = inputStr.substring(0, inputStr.length() - 1);
                            text.setText(inputStr);
                        } else {
                            inputStr = "";
                            text.setText(inputStr);
                        }


                    } else if (s.equals("=")) {

                        inputStr = "" + c(inputStr);
                        text.setText(inputStr);

                    } else {
                        inputStr += s;
                        text.setText(inputStr);
                    }
                }
            });
            panel.add(button);
        }

        text.setText(inputStr);
        this.add(text, BorderLayout.NORTH);
        this.add(panel);

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public double c(String str) {
        // str = "3+(2*5-2)/4";

        Stack<String> strStack = new Stack<String>();
        Stack<Double> numStack = new Stack<Double>();
        StringBuilder strBud = new StringBuilder(16);
        char ch = str.charAt(0);
        if (ch == '-') {
            numStack.push((double) 0);
            strStack.push("-");
            for (int i = 1; i < str.length(); i++) {

                char c = str.charAt(i);

                // char d= str[i];
                if ((c >= '0' && c <= '9') || c == '.') {
                    strBud.append(c);
                } else {
                    if (strBud.length() > 0) {
                        numStack.push(Double.parseDouble(strBud.toString()));
                        strBud.delete(0, strBud.length());
                    }

                    String s = String.valueOf(c);
                    if (strStack.empty()) {
                        strStack.push(s);
                    } else {
                        if (s.equals("(")) {
                            strStack.push(s);
                        } else if (s.equals(")")) {
                            directCalc(strStack, numStack, true);
                        } else {
                            compareAndCalc(strStack, numStack, s);
                        }
                    }
                }
            }
        } else {

            for (int i = 0; i < str.length(); i++) {

                char c = str.charAt(i);

                // char d= str[i];
                if ((c >= '0' && c <= '9') || c == '.') {
                    strBud.append(c);
                } else {
                    if (strBud.length() > 0) {
                        numStack.push(Double.parseDouble(strBud.toString()));
                        strBud.delete(0, strBud.length());
                    }

                    String s = String.valueOf(c);
                    if (strStack.empty()) {
                        strStack.push(s);
                    } else {
                        if (s.equals("(")) {
                            strStack.push(s);
                        } else if (s.equals(")")) {
                            directCalc(strStack, numStack, true);
                        } else {
                            compareAndCalc(strStack, numStack, s);
                        }
                    }
                }
            }
        }
        if (strBud.length() > 0) {
            numStack.push(Double.parseDouble(strBud.toString()));
        }
        directCalc(strStack, numStack, false);

        return numStack.pop();

    }
    /*
     *
     */
    private void compareAndCalc(Stack<String> strStack, Stack<Double> numStack, String s) {
        // TODO Auto-generated method stub
        String peekOpt = strStack.peek();
        int priority = priority(peekOpt, s);
        if (priority == -1 || priority == 0) {
            String opt = strStack.pop();
            double num2 = numStack.pop();
            double num1 = numStack.pop();
            double d = cul(opt, num1, num2);

            numStack.push(d);

            if (strStack.empty()) {
                strStack.push(s);
            } else {
                compareAndCalc(strStack, numStack, s);
            }
        } else {
            strStack.push(s);
        }

    }
    /*
     * 计算值
     */
    private double cul(String opt, double num1, double num2) {
        // TODO Auto-generated method stub
        double d = 0;
        switch (opt) {
            case "+":
                d = num1 + num2;
                break;
            case "-":
                d = num1 - num2;
                break;
            case "*":
                d = num1 * num2;
                break;
            case "/":
                d = num1 / num2;
                break;
            default:
                break;
        }
        return d;
    }
    /*
     * 运算符优先级
     */
    private int priority(String peekStr, String s) {
        // TODO Auto-generated method stub
        final Map<String, Integer> OPTMAP = new HashMap<String, Integer>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                put("(", 1);
                put(")", 1);
                put("+", 2);
                put("-", 2);
                put("*", 3);
                put("/", 3);
                put("=", 5);
            }
        };

        int i = OPTMAP.get(s) - OPTMAP.get(peekStr);
        return i;

    }
    /*
     * 判断括号
     */
    private void directCalc(Stack<String> strStack, Stack<Double> numStack, boolean b) {
        // TODO Auto-generated method stub
        String s = strStack.pop();
        double num2 = numStack.pop();
        double num1 = numStack.pop();
        double d = cul(s, num1, num2);
        numStack.push(d);

        if (b) {
            if ("(".equals(strStack.peek())) {
                strStack.pop();
            } else {
                directCalc(strStack, numStack, b);
            }
        } else {
            if (!strStack.empty()) {
                directCalc(strStack, numStack, b);
            }

        }

    }

    public double d(String str) {

        str = " ( ( - 32 - 22 ) - 6 + 15 ) * 4 / 2";
        // str="8 - 2 / 5";
        // str = "8 + 2 / 5 - 3 * 2";

        String s[] = str.split(" ");
        Stack<Double> stack = new Stack<Double>();

        if (s[1].equals("(")) {

            int in = 0;

            if (s[2].equals("-")) {
                stack.push(-Double.parseDouble(s[3]));
                for (int i = 0; i < s.length; i++) {
                    if (s[i].equals(")")) {
                        in = i;
                    }
                }
                for (int i = 4; i < in; i++) {

                    if (s[i].equals("-")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a - Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("+")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a + Double.parseDouble(s[i + 1]));

                    }
                }
                for (int i = in; i < s.length; i++) {

                    if (s[i].equals("-")) {

                        stack.push(-Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("+")) {

                        stack.push(Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("/")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a / Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("*")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a * Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("(")) {

                    }
                }
            } else {
                stack.push(Double.parseDouble(s[2]));
                for (int i = 0; i < s.length; i++) {
                    if (s[i].equals(")")) {
                        in = i;
                    }
                }
                for (int i = 3; i < in; i++) {

                    if (s[i].equals("-")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a - Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("+")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a + Double.parseDouble(s[i + 1]));

                    }
                }
                for (int i = in; i < s.length; i++) {

                    if (s[i].equals("-")) {

                        stack.push(-Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("+")) {

                        stack.push(Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("/")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a / Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("*")) {

                        double a = stack.peek();
                        stack.pop();
                        stack.push(a * Double.parseDouble(s[i + 1]));

                    }
                }
            }

        } else {
            if (s[1].equals("-")) {
                stack.push(-Double.parseDouble(s[2]));
                for (int i = 2; i < s.length; i++) {

                    if (s[i].equals("-")) {
                        stack.push(-Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("*")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a * Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("/")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a / Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("+")) {
                        stack.push(Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("(")) {

                    }
                }
            } else {
                stack.push(Double.parseDouble(s[0]));
                for (int i = 1; i < s.length; i++) {

                    if (s[i].equals("-")) {
                        stack.push(-Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("*")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a * Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("/")) {
                        double a = stack.peek();
                        stack.pop();

                        stack.push(a / Double.parseDouble(s[i + 1]));
                    } else if (s[i].equals("+")) {
                        stack.push(Double.parseDouble(s[i + 1]));

                    } else if (s[i].equals("(")) {

                    }
                }

            }

        }

        double a = 0;
        while (!stack.isEmpty()) {
            a += stack.peek();
            stack.pop();
        }

        return a;

    }

    public static void main(String[] args) {
        new hjh();
    }
}
