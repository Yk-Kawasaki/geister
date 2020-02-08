package geister;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*-------------------------------------------------------------------------------------
 * drawPanel��static�ɂł���H
 *------------------------------------------------------------------------------------*/
public class Geister {

	private final JPanel mainPanel;			//���C���̃p�l��
	private int phase = 0;					//�Q�[���̐i�s�t�F�[�Y
	private final boolean[][] isGoodGhost;	//�ǂ����������ǂ������i�[����z��
	private String message;					//�\�����Ă��郁�b�Z�[�W
	private boolean wasWindowIconified = false;	//�E�B���h�E���ŏ�������Ă�����

	public static final int MARGIN = 10;	//�ΐ�ՂƂ��̎��ӂƂ̌���
	public static final int SIDELENGTH = 50;	//�Ղ̖ڂ̈�Ђ̒���
	public static final int MESSAGEMARGIN = 10;	//���b�Z�[�W�̂��߂Ɏ��}�[�W��
	public static final int PIECEMARGIN = 5;	//�Ղ̖ڂƋ�̊Ԃ̃}�[�W��

	public static final int PANELWIDTH;		//�p�l���̉���
	public static final int PANELHEIGHT;	//�p�l���̏c��
	public static final int BOARDLX;		//�ΐ�Ղ̍��[��x���W
	public static final int BOARDRX;		//�ΐ�Ղ̉E�[��x���W
	public static final int BOARDHY;		//�ΐ�Ղ̏�[��y���W
	public static final int BOARDFY;		//�ΐ�Ղ̉��[��y���W

	static {
		PANELWIDTH = SIDELENGTH * 14 + MARGIN * 4;
		PANELHEIGHT = SIDELENGTH * 6 + MARGIN * 3 + MESSAGEMARGIN;
		BOARDLX = MARGIN * 2 + SIDELENGTH * 4;
		BOARDRX = MARGIN * 2 + SIDELENGTH * 10;
		BOARDHY = MARGIN;
		BOARDFY = MARGIN + SIDELENGTH * 6;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Geister geister = new Geister();
				geister.exec();
			}
		});
	}

	public Geister() {
		mainPanel = new MainPanel();
		isGoodGhost = new boolean[][]{
			{true, true, true, true, false, false, false, false},
			{true, true, true, true, true, true, true, true},};
		message = "��̎�ނ�I�����A���肷��ꍇ�ՊO���N���b�N";
	}

	public void exec() {
		final JFrame frame = new JFrame("Geister");
		frame.addWindowListener(new MyWindowListener());

		mainPanel.addMouseListener(new PanelMouseListener());
		frame.getContentPane().add(mainPanel);
		frame.pack();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
	}

	class MainPanel extends JPanel {

		public MainPanel() {
			setPreferredSize(new Dimension(PANELWIDTH, PANELHEIGHT));
		}

		@Override
		public void paint(Graphics g) {
			if (wasWindowIconified) {
				drawPanel(g);
				wasWindowIconified = false;
			}
			if (phase == 0) {
				drawPanel(g);
				phase++;
			} else {
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 4; k++) {
						g.setColor(isGoodGhost[1][j * 4 + k] ? Color.BLUE : Color.RED);
						g.fillOval(BOARDLX + SIDELENGTH + SIDELENGTH * k + PIECEMARGIN, BOARDHY + SIDELENGTH * 4 + SIDELENGTH * j + PIECEMARGIN,
								SIDELENGTH - PIECEMARGIN * 2, SIDELENGTH - PIECEMARGIN * 2);
						g.setColor(Color.BLACK);
						g.drawOval(BOARDLX + SIDELENGTH + SIDELENGTH * k + PIECEMARGIN, BOARDHY + SIDELENGTH * 4 + SIDELENGTH * j + PIECEMARGIN,
								SIDELENGTH - PIECEMARGIN * 2, SIDELENGTH - PIECEMARGIN * 2);
					}
				}
			}
		}

		private void drawPanel(Graphics g) {
			g.setColor(Color.GREEN);
			g.fillRect(BOARDLX, BOARDHY, SIDELENGTH * 6, SIDELENGTH * 6);
			g.setColor(Color.BLACK);
			for (int i = 0; i < 2; i++) {		//�������\����
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 4; k++) {
						g.drawOval(BOARDRX * i + MARGIN + SIDELENGTH * k + PIECEMARGIN, i * SIDELENGTH * 4 + MARGIN + SIDELENGTH * j + PIECEMARGIN,
								SIDELENGTH - PIECEMARGIN * 2, SIDELENGTH - PIECEMARGIN * 2);
					}
				}
			}
			for (int i = 0; i < 6; i++) {		//�ΐ��
				for (int j = 0; j < 6; j++) {
					g.drawRect(BOARDLX + SIDELENGTH * j, BOARDHY + SIDELENGTH * i, SIDELENGTH, SIDELENGTH);
				}
			}
			for (int i = 0; i < 2; i++) {		//�l���̖��
				for (int j = 0; j < 2; j++) {
					g.drawLine(BOARDLX + j * SIDELENGTH * 5 + PIECEMARGIN, BOARDHY + i * SIDELENGTH * 5 + SIDELENGTH / 2,
							BOARDLX + SIDELENGTH + j * SIDELENGTH * 5 - PIECEMARGIN, BOARDHY + i * SIDELENGTH * 5 + SIDELENGTH / 2);
					for (int k = 0; k < 2; k++) {
						g.drawLine(BOARDLX + j * SIDELENGTH * 6 + (1 - j * 2) * PIECEMARGIN, BOARDHY + i * SIDELENGTH * 5 + SIDELENGTH / 2,
								BOARDLX + j * SIDELENGTH * 5 + SIDELENGTH / 2, BOARDHY + i * SIDELENGTH * 5 + k * SIDELENGTH + (1 - k * 2) * PIECEMARGIN);
					}
				}
			}
			for (int i = 0; i < 2; i++) {		//��̕\��
				for (int j = 0; j < 2; j++) {
					for (int k = 0; k < 4; k++) {
						g.setColor(i == 0 ? Color.WHITE : Color.BLUE);
						g.fillOval(BOARDLX + SIDELENGTH + SIDELENGTH * k + PIECEMARGIN, BOARDHY + i * SIDELENGTH * 4 + SIDELENGTH * j + PIECEMARGIN,
								SIDELENGTH - PIECEMARGIN * 2, SIDELENGTH - PIECEMARGIN * 2);
						g.setColor(Color.BLACK);
						g.drawOval(BOARDLX + SIDELENGTH + SIDELENGTH * k + PIECEMARGIN, BOARDHY + i * SIDELENGTH * 4 + SIDELENGTH * j + PIECEMARGIN,
								SIDELENGTH - PIECEMARGIN * 2, SIDELENGTH - PIECEMARGIN * 2);
					}
				}
			}
			g.drawString(message, MARGIN, BOARDFY + MARGIN + MESSAGEMARGIN);
		}
	}

	class PanelMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			/*System.out.println(x + "," + y + "," + (BOARDLX + SIDELENGTH) + "," + (BOARDRX - SIDELENGTH)
					+ "," + (BOARDHY + SIDELENGTH * 4) + "," + BOARDFY);*/
			if (x > BOARDLX + SIDELENGTH && x < BOARDRX - SIDELENGTH && y > BOARDHY + SIDELENGTH * 4 && y < BOARDFY) {
				int xbox = (x - (BOARDLX + SIDELENGTH)) / SIDELENGTH;
				int ybox = (y - (BOARDHY + SIDELENGTH * 4)) / SIDELENGTH;
				//System.out.println(xbox + "," + ybox);
				isGoodGhost[1][ybox * 4 + xbox] = !isGoodGhost[1][ybox * 4 + xbox];
				mainPanel.updateUI();
			}
		}
	}

	class MyWindowListener extends WindowAdapter {

		@Override
		public void windowDeiconified(WindowEvent e) {
			wasWindowIconified = true;
		}
	}
}
