package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The inventory is used to store the players items
 * @author Adam Wareing
 *
 */
public class Inventory extends JDialog implements ActionListener {
    private static Inventory dialog;
    private static String value = "";
    private JList list;

    /**
     * Set up and show the dialog.  The first Component argument
     * determines which frame the dialog depends on; it should be
     * a component in the dialog's controlling frame. The second
     * Component argument should be null if you want the dialog
     * to come up with its left corner in the center of the screen;
     * otherwise, it should be the component on top of which the
     * dialog should appear.
     */
    public static String showDialog(Component frameComp,
                                    Component locationComp,
                                    String labelText,
                                    String title,
                                    String[] possibleValues,
                                    String initialValue,
                                    String longValue) {
        Frame frame = JOptionPane.getFrameForComponent(frameComp);
        dialog = new Inventory(frame,
                locationComp,
                labelText,
                title,
                possibleValues,
                initialValue,
                longValue);
        dialog.setVisible(true);
        return value;
    }

    private void setValue(String newValue) {
        value = newValue;
        list.setSelectedValue(value, true);
    }

    /**
     * Create a new inventory window
     * @param frame
     * @param locationComp
     * @param labelText
     * @param title
     * @param data
     * @param initialValue
     * @param longValue
     */
    @SuppressWarnings("unchecked")
	private Inventory(Frame frame,
                      Component locationComp,
                      String labelText,
                      String title,
                      Object[] data,
                      String initialValue,
                      String longValue) {
        super(frame, title, true);

        // Create and initialize the buttons.

        // Cancel
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        // Interaction
        final JButton setButton = new JButton("Interact");
        setButton.setActionCommand("Interact");
        setButton.addActionListener(this);
        getRootPane().setDefaultButton(setButton);

        // Main part of the dialog
        list = new JList(data) {
            /**
			 *
			 */
			private static final long serialVersionUID = 1L;

			public int getScrollableUnitIncrement(Rectangle visibleRect,
                                                  int orientation,
                                                  int direction) {
                int row;
                if (orientation == SwingConstants.VERTICAL &&
                        direction < 0 && (row = getFirstVisibleIndex()) != -1) {
                    Rectangle r = getCellBounds(row, row);
                    if ((r.y == visibleRect.y) && (row != 0)) {
                        Point loc = r.getLocation();
                        loc.y--;
                        int prevIndex = locationToIndex(loc);
                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);

                        if (prevR == null || prevR.y >= r.y) {
                            return 0;
                        }
                        return prevR.height;
                    }
                }
                return super.getScrollableUnitIncrement(
                        visibleRect, orientation, direction);
            }
        };

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        if (longValue != null) {
            list.setPrototypeCellValue(longValue); // Get extra space
        }
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setButton.doClick(); //emulate button click
                }
            }
        });

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Create a container so that we can add a title around the scroll pane.
        // Lay out the label and scroll pane from top to bottom.
        JPanel listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(labelText);
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lay out the buttons from left to right.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(setButton);

        // Put everything together, using the content pane's BorderLayout.
        Container contentPane = getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        // Initialize values.
        setValue(initialValue);
        pack();
        setLocationRelativeTo(locationComp);
    }

    /**
     * Triggered when the player wants to interact with an item.
     * @param e
     */
    public void actionPerformed(ActionEvent e) {
        if ("Interact".equals(e.getActionCommand())) {
            Inventory.value = (String) (list.getSelectedValue());
        }
        Inventory.dialog.setVisible(false);
    }
}