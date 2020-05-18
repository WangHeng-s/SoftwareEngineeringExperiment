package GameOfLife;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Control extends JPanel implements Runnable{
	private final int rows;
	private final int columns;
	JLabel  record;
	boolean diy=false;
	boolean clean=false;//判断鼠标点击事件是否响应，用于添加/杀死细胞
	private int speed=5;
	private int lnum;//记录细胞个数
	private int[][] shape =new int [40][50];
	public  int[][] pauseshape=new int [40][50];
	private int[][] currentGeneration;
	private int[][] nextGeneration;
	private boolean isChanging = false;
	public Control(int rows, int columns) {

		this.rows=rows;
		this.columns=columns;
		record = new JLabel();
		add(record);
		currentGeneration=new int[rows][columns];


		nextGeneration=new int[rows][columns];

	}
	public void transfrom(int[][] generation, int[][] pauseshape) {
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				pauseshape[i][j]=generation[i][j];
			}
		}
	}
	public void run() {
		while(true) {
			synchronized(this) {
				while(isChanging) {
					try {
						this.wait();
					}catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				sleep(speed);//线程睡眠，控制速度
				for(int i=0;i<rows;i++) {
					for(int j=0;j<columns;j++) {
						evolve(i,j);//判断下一代
					}
				}
				int[][] temp=null;
				temp=currentGeneration;
				currentGeneration=nextGeneration;
				nextGeneration=temp;
				for(int i=0;i<rows;i++) {
					for(int j=0;j<columns;j++) {
						nextGeneration[i][j]=1;
					}
				}
				//更新临时数组信息
				transfrom(currentGeneration,pauseshape);
				repaint();
				updateNumber();
			}
		}
	}

	//更新活细胞个数
	public void updateNumber() {
		String s = "    存活数量： " + lnum +"    ";
		LifeGame.jLabelCount.setText(s);
	}
	//修改迭代速度
	public void changeSpeedSlow() {
		speed=5;
		//设置速度显示文本内容
		LifeGame.jLabelV.setText("    当前迭代速度：5*50ms");
	}
	public void changeSpeedFast() {
		speed=3;
		LifeGame.jLabelV.setText("    当前迭代速度：3*50 ms");
	}
	public void changeSpeedHyper() {
		speed=1;
		LifeGame.jLabelV.setText("    当前迭代速度：1*50 ms");
	}

	//绘制表格
	@Override
	public void paintComponent(Graphics g) {
		lnum=0;
		super.paintComponent(g);
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				if(currentGeneration[i][j]==1) {
					//填写指定的矩形
					g.fillRect(j*20, i*20, 20, 20);
					lnum++;
				}
				else {
					g.drawRect(j*20, i*20, 20, 20);
				}
			}
		}
	}

	public void setShape() {
		setShape(pauseshape);
	}
	public void setRandom() {
		setPause();//先暂停线程，再生成随机数组，否则线程占用，数组会赋值失败
		Random a=new Random();
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				pauseshape[i][j]=Math.abs(a.nextInt(2));
			}
		}
		setShapetemp(pauseshape);//刷新页面
	}
	public void setStop() {
		setPause();//先暂停线程，再将临时数组的值清零，否则线程占用数组会清零失败
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				pauseshape[i][j]=0;
			}
		}
		setShape(pauseshape);//重启动线程
	}
	
	public void setPause() {
		setShapetemp(pauseshape);
	}
	
	public void setDiy() {
		setShapetemp(pauseshape);
	}
	private void setShapetemp(int[][] shape) {
		isChanging=true;
		synchronized(this) {
			for(int i=0;i<rows;i++) {
				for(int j=0;j<columns;j++) {
					currentGeneration[i][j]=0;
				}
			}
			for(int i=0;i<rows;i++) {
				for(int j=0;j<columns;j++) {
					if(shape[i][j]==1) {
						currentGeneration[i][j]=1;
					}
				}
			}
			repaint();//重回绘制图案
			updateNumber();//更新活细胞个数
			this.notifyAll();
		}
	}
	private void setShape(int[][] shape) {
		isChanging=true;
		synchronized(this) {
			for(int i=0;i<rows;i++) {
				for(int j=0;j<columns;j++){
					currentGeneration[i][j]=0;
				}
			}
			for(int i=0;i<rows;i++) {
				for(int j=0;j<columns;j++) {
					if(shape[i][j]==1) {
						currentGeneration[i][j]=1;
					}
				}
			}
			isChanging=false;
			this.notifyAll();
		}
	}
	//计算下一代生死
	public void evolve(int x,int y)
	{
		int activeSurroundingCell=0;

		if(isVaildCell(x-1,y-1)&&(currentGeneration[x-1][y-1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x,y-1)&&(currentGeneration[x][y-1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y-1)&&(currentGeneration[x+1][y-1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y)&&(currentGeneration[x+1][y]==1))
			activeSurroundingCell++;
		if(isVaildCell(x+1,y+1)&&(currentGeneration[x+1][y+1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x,y+1)&&(currentGeneration[x][y+1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x-1,y+1)&&(currentGeneration[x-1][y+1]==1))
			activeSurroundingCell++;
		if(isVaildCell(x-1,y)&&(currentGeneration[x-1][y]==1))
			activeSurroundingCell++;

		if(activeSurroundingCell==3)
		{
			nextGeneration[x][y]=1;
		}
		else if(activeSurroundingCell==2)
		{
			nextGeneration[x][y]=currentGeneration[x][y];
		}
		else
		{
			nextGeneration[x][y]=0;
		}
	}
	//判断细胞是否越出边界
	private boolean isVaildCell(int x,int y)
	{
		if((x>=0)&&(x<rows)&&(y>=0)&&(y<columns))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//线程睡眠，控制迭代速度
	private void sleep(int x) {
		try {
			Thread.sleep(80*x);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
}

