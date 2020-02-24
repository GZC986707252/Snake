package com.gzc;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainFrame() {
		JLabel lblGameName = new JLabel("贪吃蛇", SwingConstants.CENTER);
		lblGameName.setBounds(20, 0, 482, 100);
		lblGameName.setFont(new Font("隶书", Font.BOLD, 50));
//		lblGameName.setBorder(BorderFactory.createEtchedBorder());  //设置边框
		lblGameName.setForeground(new Color(30, 144, 255)); // 设置前景颜色，即字体颜色
		getContentPane().add(lblGameName);
		setSize(535, 557);
		setLocationRelativeTo(null);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); // 设置不能拉缩大小
	}

	public static void main(String[] args) {

		MainFrame mf = new MainFrame();
		Snake snake = new Snake();
		snake.autoMove();
		mf.getContentPane().add(snake);
		mf.setVisible(true);
	}
}
