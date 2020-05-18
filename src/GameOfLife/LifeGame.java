package GameOfLife;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class LifeGame extends JFrame implements MouseMotionListener{
	private final Control world;
	private static Button Start;
	private static Button Random;
	private static Button Stop;
	private static Button Pause;
	private static Button Add;
	private static Button Kill;

	public static JLabel jLabelCount;//显示个数
	public static JLabel jLabelV;//显示速度

	static JMenu location=new JMenu();
	public LifeGame(int rows,int columns)
	{
		world=new Control(rows, columns);
		world.setBackground(Color.LIGHT_GRAY);
		new Thread(world).start();
		add(world);
		Start=new Button("Start");
		Random=new Button("Random");
		Stop=new Button("Stop");
		Pause=new Button("Pause");
		Add=new Button("Add New Cell");
		Kill=new Button("Kill Cell");
	}
	public static void main(String[]args)
	{
		LifeGame frame=new LifeGame(40, 50);
		
		frame.addMouseMotionListener(frame);
		JMenuBar menu=new JMenuBar();
		frame.setJMenuBar(menu);

		menu.add(Start);
		menu.add(Random);
		menu.add(Stop);
		menu.add(Pause);
		menu.add(Add);
		menu.add(Kill);
		JMenu changeSpeed=new JMenu("ChangeSpeed");
		menu.add(changeSpeed);
		jLabelCount=new JLabel();
		jLabelV=new JLabel();
		menu.add(jLabelCount);
		jLabelV.setText("    当前迭代速度：5*50ms");
		menu.add(jLabelV);

		Start.addActionListener(frame.new StartActionListener());
		Random.addActionListener(frame.new RandomActionListener());
		Stop.addActionListener(frame.new StopActionListener());
		Pause.addActionListener(frame.new PauseActionListener());
		Add.addActionListener(frame.new AddactionListener());
		Kill.addActionListener(frame.new CleanActionListener());
		
		JMenuItem slow=changeSpeed.add("Slow");
		slow.addActionListener(frame.new SlowActionListener());
		JMenuItem fast=changeSpeed.add("Fast");
		fast.addActionListener(frame.new FastActionListener());
		JMenuItem hyper=changeSpeed.add("Hyper");
		hyper.addActionListener(frame.new HyperActionListener());
		

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1008,859);

		frame.setTitle("生命游戏");
		frame.setVisible(true);
		frame.setResizable(false);
	}
	class RandomActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			world.diy=false;
			world.clean=false;
			world.setBackground(Color.LIGHT_GRAY);
			world.setRandom();
		}
	}
	class StartActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//重设背景色，避免变色
			world.setBackground(Color.LIGHT_GRAY);
			world.diy=false;
			world.clean=false;
			world.setShape();
		}
	}
	class StopActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//设置背景色为green，标志暂停状态
			world.setBackground(Color.green);
			world.diy=false;
			world.clean=false;
			world.setStop();
		}
	}
	class PauseActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.diy=false;
			world.clean=false;
			world.setPause();
		}
	}
	class SlowActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedSlow();
		}
	}
	class FastActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedFast();
		}
	}
	class HyperActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.changeSpeedHyper();
		}
	}

	//杀死细胞
	class CleanActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			world.setPause();
			world.clean=true;
			world.diy=false;//改变状态，使得鼠标点击事件能够得到对应响应
			world.setBackground(Color.orange);
		}
	}
	//添加细胞
	class AddactionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			world.setPause();
			world.diy=true;
			world.clean=false;//改变状态，使得鼠标点击事件能够得到对应响应
			world.setBackground(Color.cyan);
		}
	}

	//实现两个接口，实现添加/杀死细胞
	//鼠标点击响应事件，用于选中格点
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//响应添加细胞
		if(world.diy){
		int x=e.getX();
		int y=e.getY();
		world.pauseshape[(y-50)/20][x/20]=1;
		world.setDiy();
		}
		//响应杀死细胞
		if(world.clean){
			int x=e.getX();
			int y=e.getY();
			world.pauseshape[(y-50)/20][x/20]=0;
			world.setDiy();
		}
	}
	//鼠标移动事件不方便使用，不进行实现
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}