package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class OptionPane extends JDialog implements ActionListener {

	JPanel panel = new JPanel();

	public OptionPane(JFrame parent, String title, String message, Component Component) {

		super(parent, title, true);

		if (parent != null) {
			Dimension parentSize = parent.getSize();
			Point p = parent.getLocation();
			setLocation(p.x + parentSize.width, p.y + parentSize.height);
		}


		panel.add(Component);
		getContentPane().add(panel);

		JPanel jpanel = new JPanel();
		JButton exitBtn = new JButton("Exit");

		jpanel.add(exitBtn);

		exitBtn.addActionListener(this);
		getContentPane().add(jpanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		this.setSize(200, 200);
		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - this.getWidth()) / 2;
		final int y = (screenSize.height - this.getHeight()) / 2;
		this.setLocation(x, y);

		pack();
		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		setVisible(false);
		dispose();
	}
}