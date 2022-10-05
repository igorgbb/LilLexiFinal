/*
 *  Contributors: Igor Bernardon and Julian Garcia. (Igorb1 , cjuliang)
 *  Class: CSC 352
 *  GUI: Built by Igor.
 *  DESCRIPTION: Text Editor implemented using java awt, and java swing.
 *  The user is able to insert text,images and rectangles. The user is able
 *  to edit the text, change font, change color and change font size. The user
 *  is able to scroll through the document. The user is able to undo and redo
 *  actions. The user is able to set the text to bold italic or both. 
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import java.awt.Panel;
import java.awt.BorderLayout;
import java.io.File;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.*;
import javax.swing.undo.UndoManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.JScrollPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.AbstractAction;

public class LilLexi {

	private JFrame frame;
	JMenuBar menuBar = new JMenuBar();
	JButton btnImage = new JButton("Insert Image");
	Panel panel = new Panel();
	private UndoManager undoManager = new UndoManager();
	JFileChooser fileC = new JFileChooser();
	JTextPane textPane = new JTextPane();
	JScrollPane sp = new JScrollPane(textPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private final JComboBox comboBoxColor = new JComboBox();
	Color text_color = Color.BLACK;
	private final JComboBox comboBoxFont = new JComboBox();
	private int font_size = 24;
	private String font_name = "Arial";
	private int font_mode = Font.PLAIN;
	private final JSpinner spinner = new JSpinner();
	private final JLabel lblTextSize = new JLabel("Text Size");
	private final JToggleButton tglbtnItalic = new JToggleButton("Italic");
	private final JToggleButton tglbtnBold = new JToggleButton("Bold");
	private final JComboBox comboBoxFile = new JComboBox();
	private final JButton btnRect = new JButton("Insert Rectangle");
	private final JButton btnUndo = new JButton("Undo");
	private final JButton btnRedo = new JButton("Redo");
	private Boolean can_undo = false;
	private Boolean can_redo = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LilLexi window = new LilLexi();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LilLexi() {
		initialize();
		imageShow();
		colorSelect();
		fontChange();
		sizeChange();
		boldItalic();
		exitFile();
		insertRectangle();
		undo();
		redo();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setJMenuBar(menuBar);
		comboBoxFile.setModel(new DefaultComboBoxModel(new String[] { "File...", "Save ", "Exit" }));

		menuBar.add(comboBoxFile);

		menuBar.add(btnImage);

		menuBar.add(btnRect);
		comboBoxColor
				.setModel(new DefaultComboBoxModel(new String[] { "Text Color", "Black", "Red", "Green", "Blue" }));
		menuBar.add(comboBoxColor);
		comboBoxFont.setModel(new DefaultComboBoxModel(new String[] { "Font", "Serif", "Courier", "Arial", "Roboto" }));

		menuBar.add(comboBoxFont);

		menuBar.add(lblTextSize);

		menuBar.add(spinner);

		textPane.getDocument().addUndoableEditListener(new UndoListener());

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		// Scroll Panel added.
		panel.add(sp, BorderLayout.CENTER);

		textPane.setForeground(text_color);
		textPane.setFont(new Font(font_name, font_mode, font_size));

		spinner.setValue(24);

		menuBar.add(tglbtnItalic);

		menuBar.add(tglbtnBold);

		menuBar.add(btnUndo);

		menuBar.add(btnRedo);

	}

	/*
	 * Function inserts a rectangle into the file. Implemented by: Igor.
	 */
	public void insertRectangle() {
		textPane.setEditable(true);
		btnRect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Icon icon = new ImageIcon("rect.jpg");
				textPane.insertIcon(icon);

			}
		});
	}

	/*
	 * Function exits the file when you press "Exit". Implemented by: Julian.
	 */
	public void exitFile() {
		comboBoxFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String c = (String) comboBoxFile.getSelectedItem();
				if (c.equals("Exit")) {
					frame.dispose();
					System.exit(0);
				}
			}
		});
	}

	/*
	 * Function sets the text to either bold, italic or both, when the user toggles
	 * their respective buttons. Implemented by: Igor.
	 */
	public void boldItalic() {
		tglbtnBold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnBold.isSelected() && tglbtnItalic.isSelected()) {
					font_mode = Font.BOLD | Font.ITALIC;
				} else if (tglbtnBold.isSelected() == false && tglbtnItalic.isSelected() == false) {
					font_mode = Font.PLAIN;
				} else if (tglbtnBold.isSelected() && tglbtnItalic.isSelected() == false) {
					font_mode = Font.BOLD;
				} else {
					font_mode = Font.ITALIC;
				}
				textPane.setFont(new Font(font_name, font_mode, font_size));
			}
		});
		tglbtnItalic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnBold.isSelected() && tglbtnItalic.isSelected()) {
					font_mode = Font.BOLD | Font.ITALIC;
				} else if (tglbtnBold.isSelected() == false && tglbtnItalic.isSelected() == false) {
					font_mode = Font.PLAIN;
				} else if (tglbtnBold.isSelected() && tglbtnItalic.isSelected() == false) {
					font_mode = Font.BOLD;
				} else {
					font_mode = Font.ITALIC;
				}
				textPane.setFont(new Font(font_name, font_mode, font_size));
			}
		});
	}

	/*
	 * Function changes the color of text when, the user selects a color from the
	 * drop down menu. Implemented by: Julian.
	 */
	public void colorSelect() {
		comboBoxColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String c = (String) comboBoxColor.getSelectedItem();
				if (c.equals("Black")) {
					text_color = Color.black;
					textPane.setForeground(Color.black);
				} else if (c.equals("Red")) {
					text_color = Color.red;
					textPane.setForeground(Color.red);
				} else if (c.equals("Green")) {
					text_color = Color.green;
					textPane.setForeground(Color.green);
				} else if (c.equals("Blue")) {
					text_color = Color.blue;
					textPane.setForeground(Color.blue);
				}
			}
		});

	}

	/*
	 * Function changes the size of the font, when the user changes the values on a
	 * spinner. Implemented by: Igor.
	 */
	public void sizeChange() {

		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				font_size = (Integer) spinner.getValue();
				textPane.setFont(new Font(font_name, font_mode, font_size));
			}
		});
	}

	/*
	 * Function changes the font of the text, when user selects a font from a drop
	 * down menu. Implemented by: Julian
	 */
	public void fontChange() {
		comboBoxFont.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String c = (String) comboBoxFont.getSelectedItem();
				if (c.equals("Serif")) {
					font_name = "Serif";
					textPane.setFont(new Font("Serif", font_mode, font_size));
				} else if (c.equals("Courier")) {
					font_name = "Courier";
					textPane.setFont(new Font("Courier", font_mode, font_size));
				} else if (c.equals("Arial")) {
					font_name = "Arial";
					textPane.setFont(new Font("Arial", font_mode, font_size));
				} else if (c.equals("Roboto")) {
					font_name = "Roboto";
					textPane.setFont(new Font("Roboto", font_mode, font_size));
				}
			}
		});
	}

	/*
	 * Function inserts an image into the file. When the user selects to insert
	 * image, a menu opens where the user may select an image from their computer.
	 * The image can be deleted, and interacts with the text line. Implemented by:
	 * Igor
	 */
	public void imageShow() {
		textPane.setEditable(true);
		btnImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileC.setFileFilter(new FileNameExtensionFilter("open Image", "jpg", "jpeg", "gif"));
				int returnval = fileC.showOpenDialog(frame);
				if (returnval == JFileChooser.APPROVE_OPTION) {
					File file = fileC.getSelectedFile();
					System.out.println(file.getAbsolutePath());
					Icon icon = new ImageIcon(file.getAbsolutePath());

					textPane.insertIcon(icon);

				}

			}
		});
	}

	/*
	 * Class handles when events done in the text panel can be undone or redone. It
	 * also adds the edits made on the file to the undo manager. Implemented by:
	 * Igor
	 */
	class UndoListener implements UndoableEditListener {
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
			undoManager.getUndoPresentationName();
			can_undo = undoManager.canUndo();
			undoManager.getRedoPresentationName();
			can_redo = undoManager.canRedo();
		}
	}

	/*
	 * Function undos an action that ocurred to the text panel. Implemented by: Igor
	 */
	public void undo() {
		undoManager.getUndoPresentationName();
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (can_undo == true) {
					undoManager.undo();
					undoManager.getUndoPresentationName();
					can_undo = undoManager.canUndo();
					undoManager.getRedoPresentationName();
					can_redo = undoManager.canRedo();
				}
			}
		});
	}

	/*
	 * Function redos an action that ocurred to the text panel. Implemented by: Igor
	 */
	public void redo() {
		undoManager.getRedoPresentationName();
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (can_redo == true) {
					undoManager.redo();
					undoManager.getRedoPresentationName();
					can_redo = undoManager.canRedo();
					can_undo = undoManager.canUndo();
				}
			}
		});
	}
}
