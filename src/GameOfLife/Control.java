package GameOfLife;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Control extends JPanel implements Runnable{
	private final int rows;
	private final int columns;
	JLabel  record;
	boolean diy=false;
	boolean clean=false;//�ж�������¼��Ƿ���Ӧ���������/ɱ��ϸ��
	private int speed=5;
	private int lnum;//��¼ϸ������
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
				sleep(speed);//�߳�˯�ߣ������ٶ�
				for(int i=0;i<rows;i++) {
					for(int j=0;j<columns;j++) {
						evolve(i,j);//�ж���һ��
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
				//������ʱ������Ϣ
				transfrom(currentGeneration,pauseshape);
				repaint();
				updateNumber();
			}
		}
	}

	//���»�ϸ������
	public void updateNumber() {
		String s = "    ��������� " + lnum +"    ";
		LifeGame.jLabelCount.setText(s);
	}
	//�޸ĵ����ٶ�
	public void changeSpeedSlow() {
		speed=5;
		//�����ٶ���ʾ�ı�����
		LifeGame.jLabelV.setText("    ��ǰ�����ٶȣ�5*50ms");
	}
	public void changeSpeedFast() {
		speed=3;
		LifeGame.jLabelV.setText("    ��ǰ�����ٶȣ�3*50 ms");
	}
	public void changeSpeedHyper() {
		speed=1;
		LifeGame.jLabelV.setText("    ��ǰ�����ٶȣ�1*50 ms");
	}

	//���Ʊ��
	@Override
	public void paintComponent(Graphics g) {
		lnum=0;
		super.paintComponent(g);
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				if(currentGeneration[i][j]==1) {
					//��дָ���ľ���
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
		setPause();//����ͣ�̣߳�������������飬�����߳�ռ�ã�����ḳֵʧ��
		Random a=new Random();
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				pauseshape[i][j]=Math.abs(a.nextInt(2));
			}
		}
		setShapetemp(pauseshape);//ˢ��ҳ��
	}
	public void setStop() {
		setPause();//����ͣ�̣߳��ٽ���ʱ�����ֵ���㣬�����߳�ռ�����������ʧ��
		for(int i=0;i<rows;i++) {
			for(int j=0;j<columns;j++) {
				pauseshape[i][j]=0;
			}
		}
		setShape(pauseshape);//�������߳�
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
			repaint();//�ػػ���ͼ��
			updateNumber();//���»�ϸ������
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
	//������һ������
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
	//�ж�ϸ���Ƿ�Խ���߽�
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

	//�߳�˯�ߣ����Ƶ����ٶ�
	private void sleep(int x) {
		try {
			Thread.sleep(80*x);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}
	}
}

