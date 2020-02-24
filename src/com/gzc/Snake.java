package com.gzc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

enum Direction {
	R, L, U, D
}

public class Snake extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 游戏面板宽高
	private final int PANEL_WIDTH = 482;
	private final int PANEL_HEIGHT = 402;
	// 蛇节点的大小
	private final int NODE_SIZE = 20;
	// 设置颜色
	private final Color BODY_COLOR = new Color(30, 144, 255);
	private final Color FOOD_COLOR = new Color(50, 205, 50);
	// 最高等级
	private final int MAX_LEVEL = 5;

	private Point head = new Point(40, 200); // 定义蛇头部
	private ArrayList<Point> body = new ArrayList<Point>(); // 定义蛇身体
	private Point food; // 定义食物

	private int score = 0; // 定义分数记录
	private int level = 1; // 定义等级,每吃5个食物加1一个等级,等级越高速度越快
	private int count = 0; // 定义一个计数器
	// 设置自动刷新毫秒数
	private int mills = 500;

//	private boolean isEaten;
	private Direction direcion = Direction.R; // 用户触发改变的运动方向,默认向右
	private Direction current_Direcion = Direction.R; // 当前运动方向,默认向右
	private boolean isGameOver;
	private boolean isPause; // 游戏是否暂停

	private Thread run;

	public Snake() {
		InitGame();
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		setBackground(new Color(220, 220, 220));
		setLocation(20, 100);
		this.setFocusable(true);
		this.addKeyListener(this);
	}

	// 游戏初始化
	public void InitGame() {
		// 初始化部分变量
//		isEaten = false;
		isGameOver = false;
		isPause = false;
		current_Direcion = Direction.R;
		direcion = Direction.R;
		score = 0;
		level = 1;
		count = 0;
		mills = 500;
		// 初始化蛇的头部和身体
		head.x = 40;
		head.y = 200;
		body.clear();
		body.add(new Point(20, 200));
		// 初始化食物
		food = new Point();
//		System.out.println(food.toString());
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// 画边界
		g2d.setColor(Color.black);
		g2d.drawLine(0, 0, 0, PANEL_HEIGHT - 1);
		g2d.drawLine(0, PANEL_HEIGHT - 1, PANEL_WIDTH - 1, PANEL_HEIGHT - 1);
		g2d.drawLine(PANEL_WIDTH - 1, PANEL_HEIGHT - 1, PANEL_WIDTH - 1, 0);
		g2d.drawLine(PANEL_WIDTH - 1, 0, 0, 0);

		// 画蛇头
		g2d.setColor(Color.red);
//		g2d.setColor(new Color(30, 144, 255));
		g2d.fillOval(head.x, head.y, NODE_SIZE, NODE_SIZE);

		// 画蛇身
		g2d.setColor(BODY_COLOR);
		for (Point point : body) {
			g2d.fillOval(point.x, point.y, NODE_SIZE, NODE_SIZE);
		}

		// 画食物
		g2d.setColor(FOOD_COLOR);
		g2d.fillOval(food.x, food.y, NODE_SIZE, NODE_SIZE);

		// 画长度信息
		g2d.setColor(Color.black);
		g2d.drawString("当前长度：" + (body.size() + 1), 0, 10);
		// 画分数信息
		g2d.drawString("当前得分：" + score, 200, 10);
		// 画等级信息
		g2d.drawString("当前等级：" + level, 380, 10);

		// 画其他信息
		g2d.drawString("按键盘的↑、↓、←、→键或者w、s、a、d键改变方向，按空格键”暂停“与”继续“ ！", 0, 400);
	}

	// 移动
	public void move(Direction d) {
		// 创建中间变量
		Point temp = new Point(0, 0);
		Point temp1 = new Point(0, 0);
		temp.x = head.x;
		temp.y = head.y;

		// 头部移动
		if (d == Direction.L) {
			head.x -= 20;
		}
		if (d == Direction.R) {
			head.x += 20;
		}
		if (d == Direction.U) {
			head.y -= 20;
		}
		if (d == Direction.D) {
			head.y += 20;
		}
		current_Direcion = d;
		// 身体移动
		for (int i = 0; i < body.size(); i++) {
			temp1.x = body.get(i).x;
			temp1.y = body.get(i).y;
			body.get(i).x = temp.x;
			body.get(i).y = temp.y;
			temp.x = temp1.x;
			temp.y = temp1.y;
		}
		if (eatFood()) {
			body.add(temp);
			// 记录分数，每吃1个加5分
			score += 5;
			count++;
			// 重新生成食物坐标
			food.setRandomPoint();
//			isEaten = false;
		}
		repaint();

		hitWall();
		hitSelf();
		changeLevel();
		gameOver();
	}

	// 判断是否吃了食物
	public boolean eatFood() {
		if (head.x == food.x && head.y == food.y) {
//			isEaten = true;
			return true;
		} else {
			return false;
		}
	}

	// 判断是否撞墙
	public void hitWall() {
		if (!((head.x >= 0 && head.x <= 460) && (head.y >= 0 && head.y <= 380))) {
			isGameOver = true;
		}
	}

	// 判断是否撞自己身体
	public void hitSelf() {
		for (Point point : body) {
			if (head.x == point.x && head.y == point.y) {
				isGameOver = true;
				break;
			}
		}
	}

	// 改变等级
	public void changeLevel() {
		if (count == 5) {
			if (level != MAX_LEVEL) {
				level++;
				mills -= 100;
				count = 0;
			}
		}
	}

	// 游戏结束
	public void gameOver() {
		if (isGameOver) {
			int n = JOptionPane.showConfirmDialog(null, "游戏结束！是否重新开始？", "Game over!", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			if (n == 1) {
//				String name = JOptionPane.showInputDialog(null, "英雄，请留下你的大名！", "请输入名称",
//						JOptionPane.INFORMATION_MESSAGE);
//				DBUtil.insert(name, score);
				System.exit(0);
			} else {
				InitGame();
			}
		}
	}

	// 自动移动
	public void autoMove() {
		run = new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(mills);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (!isPause) {
						move(direcion);
					}
				}
			}
		};
		run.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) {
			if (current_Direcion != Direction.D) {
				direcion = Direction.U;
//				move(direcion);
			}
		}
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			if (current_Direcion != Direction.U) {
				direcion = Direction.D;
//				move(direcion);
			}
		}
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
			if (current_Direcion != Direction.R) {
				direcion = Direction.L;
//				move(direcion);
			}
		}
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) {
			if (current_Direcion != Direction.L) {
				direcion = Direction.R;
//				move(direcion);
			}
		}
		// 按下空格键暂停与运行
		if (keyCode == KeyEvent.VK_SPACE) {
			if (isPause) {
				isPause = false;
			} else {
				isPause = true;
			}
		}

//		repaint();
//		System.out.println(head.toString());
//		for (Point point : body) {
//			System.out.println(point.toString());
//		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
