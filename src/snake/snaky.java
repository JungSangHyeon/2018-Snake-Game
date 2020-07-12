package snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class snaky { 
	//����� ����
		static Clip clip;
		
	@SuppressWarnings("unused")
	public static void main(String[] args) throws FileNotFoundException { 
		snakysnaky start = new snakysnaky();
		Sound("src/snake/���׸�.wav",true);
	}
	public static void Sound(String file, boolean Loop) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
			clip = AudioSystem.getClip();
			clip.open(ais);
			if (Loop) {clip.loop(-1);}
			// Loop ����true�� ������������ѹݺ���ŵ�ϴ�. false�� �ѹ��������ŵ�ϴ�.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

@SuppressWarnings("serial")
class snakysnaky extends JFrame implements MouseListener,  MouseMotionListener, KeyListener, MouseWheelListener, Runnable {   
	
/////////////   vital   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//ȭ��ũ��
	int f_width = 800;  //��üȭ�� = 1920, 1080
	int f_height = 600; 
	
	//����� ����
	static Clip clip;
	
	//run�� ����
	Thread th;
		
	//�׷����� ����
	Image buffImage; 
	Graphics buffg; 
	
	//�̰� ������ �̵��� �ʿ��Ѱ� �ΰ���
	Point mainFrameLocation = new Point(0, 0);
	Point mouseClickedLocation = new Point(0, 0);

////////////   etc   //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	int Line_interval = 40;
	int direction = 1;
	
	int feed_time =0;
	int feed_interval =20; // Snaky_wait_time*feed_interval��ŭ �� ����
	List<Integer> food_x = new ArrayList<Integer>();
	List<Integer> food_y = new ArrayList<Integer>();
	
	boolean start = false;
	
/////////   SNAKY   /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	List<Integer> snaky_x = new ArrayList<Integer>();
	List<Integer> snaky_y = new ArrayList<Integer>();
	
	int Snaky_interval = 8;
	int Snaky_long = 4;
	int Snaky_wait_time = 100; //�������� ��� �ð�
	Color snaky_color = Color.CYAN;
	Color snaky_color_2 = Color.BLUE;
	
	boolean snaky_alive = true;
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	snakysnaky(){Jframe_make();}

	private void Jframe_make() {

		Snaky_ready();

		th = new Thread(this);
		th.start();

		addMouseMotionListener(new FrameMove_mouseMotionAdapter3(this));
		addMouseListener(new FrameMove_mouseAdapter3(this));

		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);

		setSize(f_width, f_height);
		setTitle("Snaky!");

//		//���� Ŀ��
//		Toolkit tk = Toolkit.getDefaultToolkit();
//		Cursor invisCursor = tk.createCustomCursor(tk.createImage(""),new Point(), null);
//		setCursor(invisCursor);
		
		setLocationRelativeTo(null);
		setUndecorated(true);// Ÿ��Ʋ�پ�����
		setBackground(new Color(0, 0, 0, 254));
		setResizable(true); // âũ�� ���� ���� ����
		setVisible(true);
	}

	private void Snaky_ready() {
		for (int i = 0; i < Snaky_long; i++) {
			snaky_x.add(f_width / 2 / Line_interval);
			snaky_y.add(f_height / 2 / Line_interval+i);
		}
	}

	public void run() {
		try {
				while (snaky_alive && Snaky_long < 300) {
					snaky_go();
					feed();
					repaint();
					eat_test();
					sanky_crash_test();
					Thread.sleep(Snaky_wait_time);
					// System.out.println("x = "+ snaky_x);
					// System.out.println("y = "+snaky_y);
				}
				repaint();
		} catch (Exception e) {}
	}
	
	private void sanky_crash_test() {
		for (int i = 0; i < Snaky_long; i++)
			if (snaky_x.get(0) == snaky_x.get(i + 1) && snaky_y.get(0) == snaky_y.get(i + 1)) {
				DIE_Sound();
				snaky_alive = false;
			}
	}

	private void eat_test() {
		for (int i = 0; i < food_x.size(); i++) {
			if (snaky_x.get(0) == food_x.get(i) && snaky_y.get(0) == food_y.get(i)) {
				food_x.remove(i);
				food_y.remove(i);
				Ppororong_Sound();
				Snaky_long++;
			}
		}
	}

	private void feed() {
		feed_time++;
		if(feed_time==feed_interval) {
			food_x.add((int)(Math.random()*f_width/Line_interval)+1);
			food_y.add((int)(Math.random()*f_height/Line_interval)+1);
			feed_time=0;
			snaky_color = new Color((int)(Math.random()*255)+1,(int)(Math.random()*255)+1,(int)(Math.random()*255)+1);
			snaky_color_2 = new Color((int)(Math.random()*255)+1,(int)(Math.random()*255)+1,(int)(Math.random()*255)+1);
		}
	}

	private void snaky_go() {//direction

		if(direction==1) {
			int temp_x = snaky_x.get(0);
			int temp_y = snaky_y.get(0);
			
			snaky_x.add(0, temp_x);
			snaky_y.add(0, temp_y-1);
		}
		if(direction==2) {
			int temp_x = snaky_x.get(0);
			int temp_y = snaky_y.get(0);
			
			snaky_x.add(0, temp_x+1);
			snaky_y.add(0, temp_y);
		}
		if(direction==3) {
			int temp_x = snaky_x.get(0);
			int temp_y = snaky_y.get(0);
			
			snaky_x.add(0, temp_x);
			snaky_y.add(0, temp_y+1);
		}
		if(direction==4) {
			int temp_x = snaky_x.get(0);
			int temp_y = snaky_y.get(0);
			
			snaky_x.add(0, temp_x-1);
			snaky_y.add(0, temp_y);
		}
		
		//������ ���������� ���̰ų�.
		if(snaky_x.get(0)<1) {snaky_x.set(0, 1);}
		if(snaky_x.get(0)>f_width/Line_interval) {snaky_x.set(0, f_width/Line_interval);}
		if(snaky_y.get(0)<1) {snaky_y.set(0, 1);}
		if(snaky_y.get(0)>f_height/Line_interval) {snaky_y.set(0, f_height/Line_interval);}
	}
	
	public void paint(Graphics g) {update(g);}

	public void update(Graphics g) {
	
		g.clearRect(0, 0, f_width, f_height);
		
//		g.setColor(Color.WHITE);
//		//������
//		for(int i=0; i<f_width; i+=Line_interval) {g.drawLine(i, 0, i, f_height);}
//		g.drawLine(f_width - 1, 0, f_width - 1, f_height);
//		//������
//		for(int i=0; i<f_height; i+=Line_interval) {g.drawLine(0, i, f_width, i);}
//		g.drawLine(0, f_height - 1, f_width, f_height - 1);
		
//		//ä���(Rect_on_snaky) �׽�Ʈ
//		for(int i=1; i<21;i++) {for(int v=1; v<16; v++) {Rect_on_snaky(g,i,v);}}
		
		//��, ��  �����α׷���, ���ڱ� ������ ���� �� ����������. �׸��� �����ų� �� ��Ʈ�ѵ� �ٴ밡�����ؼ�. 
		
		g.setFont(new Font("����", Font.BOLD, 200));
		if(Snaky_long<10) {//���ڸ���
			g.drawString(Snaky_long+"", f_width/2-122/2,f_height/2+154/2);
		}else if(Snaky_long<100) {//���ڸ���
			g.drawString(Snaky_long+"", f_width/2-122,f_height/2+154/2);
		}else {//���ڸ���. ���ڸ����� ����.
			g.drawString(Snaky_long+"", f_width/2-122*3/2,f_height/2+154/2);
		}
		
		g.setFont(new Font("����", Font.BOLD, 20));
		g.drawString("Restart = R",10,f_height-90);
		g.drawString("�� ������ ���",10,f_height-50);
		g.drawString("�ڱ� ���� �ھƵ� ���",10,f_height-10);
		
		draw_food(g);
	
		//���� �Ʒ�
		connect_snaky(g);
		draw_snaky(g);
		
//		//���� ��
//		draw_snaky(g);
//		connect_snaky(g);
		
//		//���
//		connect_snaky(g);
//		head_snaky(g);
		
//		//�����
//		connect_snaky(g);
		if(!snaky_alive) {
			g.setColor(Color.RED);
			g.setFont(new Font("����", Font.BOLD, 170));
			g.drawString("YOU DIE",20,f_height-240);
		}
		if(Snaky_long==300) {
			g.clearRect(0, 0, f_width, f_height);
			g.setColor(Color.WHITE);
			g.setFont(new Font("����", Font.BOLD, 170));
			g.drawString("YOU WIN",15,f_height-240);
		}
	}

	private void head_snaky(Graphics g) {
		for(int i=0; i<Snaky_long; i++) {
			int real_x1 = (snaky_x.get(i)-1)*Line_interval+Snaky_interval;
			int real_y1 = (snaky_y.get(i)-1)*Line_interval+Snaky_interval;
			
			int real_w = Line_interval-Snaky_interval*2;
			int real_h = Line_interval-Snaky_interval*2;
			
		//���� // ������ = real_h/2
		g.fillOval(real_x1, real_y1, real_w, real_h);	
		}
	}

	private void connect_snaky(Graphics g) {
		for(int i=0; i<Snaky_long-1; i++) {
			int x1 = snaky_x.get(i);
			int y1 = snaky_y.get(i);
			int x2 = snaky_x.get(i+1);
			int y2 = snaky_y.get(i+1);
			
			g.setColor(snaky_color_2);
			
			//g.fillRect((x-1)*Line_interval+Snaky_interval, (y-1)*Line_interval+Snaky_interval, Line_interval-Snaky_interval*2, Line_interval-Snaky_interval*2);
			int real_x1 = (snaky_x.get(i)-1)*Line_interval+Snaky_interval;
			int real_y1 = (snaky_y.get(i)-1)*Line_interval+Snaky_interval;
			
			int real_w = Line_interval-Snaky_interval*2;
			int real_h = Line_interval-Snaky_interval*2;
			
			//������ ����
			if(x2+1 == x1) {g.fillRect(real_x1 - Line_interval/2, real_y1+(real_h-Line_interval/2)/2, Line_interval, Line_interval/2);} 
			
			//���� ����
			if(x2-1 == x1) {g.fillRect(real_x1 + Line_interval/2, real_y1+(real_h-Line_interval/2)/2, Line_interval, Line_interval/2);} 
			
			//�Ʒ��� ����
			if(y2+1 ==y1) {g.fillRect(real_x1+(real_w-Line_interval/2)/2 , real_y1-Line_interval/2, Line_interval/2, Line_interval);} 
			
			//���� ����
			if(y2-1 ==y1) {g.fillRect(real_x1+(real_w-Line_interval/2)/2 , real_y1+Line_interval/2, Line_interval/2, Line_interval);}
			
		}
	}

	private void draw_food(Graphics g) {
		g.setColor(Color.RED);
		for(int i=0; i<food_x.size(); i++) {
			g.fillOval((food_x.get(i)-1)*Line_interval+Snaky_interval, (food_y.get(i)-1)*Line_interval+Snaky_interval, Line_interval-Snaky_interval*2, Line_interval-Snaky_interval*2);
		}
	}

	private void draw_snaky(Graphics g) {
		g.setColor(snaky_color);
		for(int i=0; i<Snaky_long; i++) {
			if(i==0) {g.setColor(snaky_color);}//�밡�� �ߺ��̰� ����
			else {g.setColor(snaky_color);}
			g.fillRect((snaky_x.get(i)-1)*Line_interval+Snaky_interval, (snaky_y.get(i)-1)*Line_interval+Snaky_interval, Line_interval-Snaky_interval*2, Line_interval-Snaky_interval*2);
		}
	}

	public void keyPressed(KeyEvent e) {// ESC�� �ܹڿ� ����
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {System.exit(0);}
		if (e.getKeyCode() == KeyEvent.VK_UP) {direction = 1;}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {direction = 2;}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {direction = 3;}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {direction = 4;}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			this.setLocation(2000, 0);
			snakysnaky start = new snakysnaky();
			}
		
	}
	
	private void Ppororong_Sound() { // ȿ����
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/snake/�Ϸη�.wav"));// ȿ����
			Clip clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
		} catch (Exception ex) {
		}
	}
	
	private void DIE_Sound() { // ȿ����
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File("src/snake/Qfail.wav"));// ȿ����
			Clip clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
		} catch (Exception ex) {
		}
	}
	
	public void mousePressed(MouseEvent arg0) {
		
//				Sound("Electric Swing Circus - Golden Hour.wav", true);// ���� ���
//				clip.stop();
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) {}
	public void mouseReleased(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseClicked(MouseEvent arg0) {
		System.out.println(arg0.getX()+" / "+arg0.getY());
		if (arg0.getButton() == MouseEvent.BUTTON2) {
		}
	}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent e) {}
	public void MouseProcess() {}
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class FrameMove_mouseAdapter3 extends MouseAdapter {
	
    private snakysnaky frame;
 
    FrameMove_mouseAdapter3(snakysnaky mainframe) {this.frame = mainframe;}
 
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        frame.mouseClickedLocation.x = e.getX();
        frame.mouseClickedLocation.y = e.getY();
    }}

class FrameMove_mouseMotionAdapter3 extends MouseMotionAdapter {
	
    private snakysnaky frame;
     
    FrameMove_mouseMotionAdapter3(snakysnaky mainframe) {this.frame = mainframe;}
    public void mouseMove(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {//����Ʈ�� ���� 20��ŭ���� ������
    	if(0 <= frame.mouseClickedLocation.x && frame.mouseClickedLocation.x <=frame.f_width && 0 <= frame.mouseClickedLocation.y && frame.mouseClickedLocation.y <= 20 ) {
        frame.setLocation(e.getLocationOnScreen().x - frame.mouseClickedLocation.x, e.getLocationOnScreen().y - frame.mouseClickedLocation.y);
    	}}
}


