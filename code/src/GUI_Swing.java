import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class GUI_Swing {

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JMenuItem mntmAddFile;
	private JMenuItem mntmAddDir;
	private JSeparator separator;
	private JMenuItem mntmPathEditor;
	private JMenuItem mntmRunPUML;
	private JMenu mnOptionen;
	private JCheckBoxMenuItem chckbxmntmUseJava;
	private JCheckBoxMenuItem chckbxmntmUseJar;
	private JToolBar toolBar;
	private JButton btnOpenDir;
	private JButton btnOpenFile;
	private JButton btnSave;
	private JButton btnRunPUML;
	private JTabbedPane tabbedPane;
	private JPanel pnlClass;
	private JPanel pnlClassPrev;
	private JButton btnClassPrev;
	private JPanel pnlSequence;
	private JPanel pnlSeqPrev;
	private JButton btnSeqPrev;
	private JFileChooser fDialog;
	private DefaultMutableTreeNode dmtnClasses;
	private JScrollPane scrollPaneClass;
	private JTextArea textClass;
	private JScrollPane scrollPaneSequence;
	private JTextArea textSequence;
	private JLabel lblPlantumlCodeClass;
	private JLabel lblPlantumlCodeSequence;
	private JSplitPane splitPane;
	private JPanel panel;
	private JTree tree_1;

	private ArrayList<String> paths;
	private String srcCode;
	private String pumlCode;
	private boolean useJava;
	private boolean useJar;
	private boolean modified;
	private File tmpImage;
	private JLabel lblMethode;
	private JLabel lblKlasse;
	private JPanel panel_1;


	/**
	 * Launch the application.
	 */
	public static void showGUI() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Swing window = new GUI_Swing();
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
	public GUI_Swing() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		paths = new ArrayList<String>();
		useJava = true;
		useJar = false;
		modified = true;
		tmpImage = new File(System.getProperty("user.dir"), "tmp.png");

		frame = new JFrame();
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PUML");
		frame.setMinimumSize(new Dimension(640, 480));

		fDialog = new JFileChooser(new File("."));

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		mntmAddFile = new JMenuItem("Datei hinzuf\u00FCgen");
		mntmAddFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(true);
			}
		});
		mnDatei.add(mntmAddFile);

		mntmAddDir = new JMenuItem("Ordner hinzuf\u00FCgen");
		mntmAddDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(false);
			}
		});
		mnDatei.add(mntmAddDir);

		separator = new JSeparator();
		mnDatei.add(separator);

		mntmPathEditor = new JMenuItem("Suchliste bearbeiten");
		mntmPathEditor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, new PathEditorPanel(paths), "Pfade bearbeiten",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnDatei.add(mntmPathEditor);

		mntmRunPUML = new JMenuItem("PUML ausf\u00FChren");
		mntmRunPUML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runPUML();
			}
		});
		mnDatei.add(mntmRunPUML);

		mnOptionen = new JMenu("Optionen");
		menuBar.add(mnOptionen);

		chckbxmntmUseJava = new JCheckBoxMenuItem("Java-Dateien verwenden");
		chckbxmntmUseJava.setSelected(useJava);
		chckbxmntmUseJava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmUseJava.isSelected()) {
					useJava = true;
				} else {
					useJava = false;
				}
			}
		});
		mnOptionen.add(chckbxmntmUseJava);

		chckbxmntmUseJar = new JCheckBoxMenuItem("Jar-Dateien verwenden");
		chckbxmntmUseJar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxmntmUseJar.isSelected()) {
					useJar = true;
				} else {
					useJar = false;
				}
			}
		});
		chckbxmntmUseJar.setSelected(useJar);
		mnOptionen.add(chckbxmntmUseJar);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		pnlClass = new JPanel();
		tabbedPane.addTab("Klassendiagramm", null, pnlClass, null);
		pnlClass.setLayout(new BorderLayout(0, 0));

		pnlClassPrev = new JPanel();
		pnlClass.add(pnlClassPrev, BorderLayout.SOUTH);

		btnClassPrev = new JButton("Vorschau");
		btnClassPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showPreview(tmpImage, "Klassendiagramm");
			}
		});
		btnClassPrev.setHorizontalAlignment(SwingConstants.LEFT);
		pnlClassPrev.add(btnClassPrev);

		scrollPaneClass = new JScrollPane();
		pnlClass.add(scrollPaneClass, BorderLayout.CENTER);

		textClass = new JTextArea();
		scrollPaneClass.setViewportView(textClass);

		lblPlantumlCodeClass = new JLabel("PlantUML Code:");
		scrollPaneClass.setColumnHeaderView(lblPlantumlCodeClass);

		pnlSequence = new JPanel();
		tabbedPane.addTab("Sequenzdiagramm", null, pnlSequence, null);
		pnlSequence.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		pnlSequence.add(splitPane, BorderLayout.CENTER);

		panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		scrollPaneSequence = new JScrollPane();
		panel.add(scrollPaneSequence);

		textSequence = new JTextArea();
		scrollPaneSequence.setViewportView(textSequence);

		lblPlantumlCodeSequence = new JLabel("PlantUML Code:");
		scrollPaneSequence.setColumnHeaderView(lblPlantumlCodeSequence);

		pnlSeqPrev = new JPanel();
		panel.add(pnlSeqPrev, BorderLayout.SOUTH);
		pnlSeqPrev.setLayout(new BorderLayout(0, 0));

		btnSeqPrev = new JButton("Vorschau");
		pnlSeqPrev.add(btnSeqPrev, BorderLayout.SOUTH);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder("Entry-Point:"));
		pnlSeqPrev.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
			
		lblMethode = new JLabel("Methode:");
		panel_1.add(lblMethode);
		
		lblKlasse = new JLabel("Klasse:");
		panel_1.add(lblKlasse, BorderLayout.NORTH);

		
		tree_1 = new JTree();
		splitPane.setLeftComponent(tree_1);

		// Selection Listener welches Leaf und welcher Parent ausgewählt wurden (nur
		// Leaf-Ebene)
		tree_1.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree_1.getLastSelectedPathComponent();
				if (node == null) {
					return;
				}

				if (node.isLeaf()) {
					System.out.println(node.getUserObject() + "  ||  " + node.toString() + "  ||  " + node.getParent());
					//TODO: lblKlasse
					
				}
			}
		});

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		btnOpenDir = new JButton("");
		btnOpenDir.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/directory.gif")));
		btnOpenDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(false);
			}
		});
		btnOpenDir.setToolTipText("Ordner hinzuf\u00FCgen");
		toolBar.add(btnOpenDir);

		btnOpenFile = new JButton("");
		btnOpenFile.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/file.gif")));
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(true);
			}
		});
		btnOpenFile.setToolTipText("Datei hinzuf\u00FCgen");
		toolBar.add(btnOpenFile);

		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/floppy.gif")));
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveOutput();
			}
		});
		btnSave.setToolTipText("Speichern");
		toolBar.add(btnSave);

		btnRunPUML = new JButton("");
		btnRunPUML.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runPUML();
			}
		});
		btnRunPUML.setToolTipText("PUML ausf\u00FChren");
		btnRunPUML.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/JavaCup16.png")));
		toolBar.add(btnRunPUML);

		dmtnClasses = new DefaultMutableTreeNode("Klassen");
	}

	private void openFile(boolean useFiles) {
		File[] items = {};
		if (useFiles) {
			fDialog.setDialogTitle("Dateien ausw�hlen");
			fDialog.setFileFilter(new FileNameExtensionFilter("Java Code (.jar, .java)", "java", "jar"));
			fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fDialog.setMultiSelectionEnabled(true);
			fDialog.showOpenDialog(frame);
			items = fDialog.getSelectedFiles();
			for (int i = 0; i < items.length; i++) {
				paths.add(items[i].getPath());
			}

		} else {
			fDialog.setDialogTitle("Ordner ausw�hlen");
			fDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fDialog.setMultiSelectionEnabled(true);
			fDialog.setAcceptAllFileFilterUsed(false);
			if (fDialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
				items = fDialog.getSelectedFiles();
				for (int i = 0; i < items.length; i++) {
					paths.add(items[i].getPath());
				}
			}
		}
		if (items.length > 0) {
			modified = true;
		}

	}

	private void runPUML() {

		try {
			PUMLgenerator.codeCollector.paths = paths;
			PUMLgenerator.codeCollector.setUseJarFiles(useJar);
			PUMLgenerator.codeCollector.setUseJavaFiles(useJava);
			srcCode = PUMLgenerator.codeCollector.getSourceCode();
			//System.out.println(srcCode);

			PUMLgenerator.parser.parse(srcCode);
			Document tempPR = PUMLgenerator.parser.getParsingResult();
			Document tmpClass = PUMLgenerator.classDiagramGenerator.createDiagram(tempPR);
			//pumlCode = PUMLgenerator.outputPUML.getPUML(tempPR);

			/* TREE */
			// Erstellen eines Trees zur Auswahl des Einstiegpunktes im Sequenzdiagramm
			// Füllen des Trees
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Einstiegspunkt");
			for (int nodeCnt = 0; nodeCnt < 4; nodeCnt++) {
				DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode("Knoten " + nodeCnt);
				root.add(dmtn);

				for (int leafCnt = 1; leafCnt < 4; leafCnt++)
					dmtn.add(new DefaultMutableTreeNode("Blatt " + (nodeCnt * 3 + leafCnt)));
			}
			tree_1.expandRow(0);
		

			
			
			textClass.setText(pumlCode);

			PUMLgenerator.outputPUML.createPUMLfromString(tmpImage.getAbsolutePath(), pumlCode);
			modified = false;

		} catch (NullPointerException npe) {
			// von CodeCollector
			JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException iae) {
			// von CodeCollector
			JOptionPane.showMessageDialog(frame, "Kein Suchtyp ausgew�hlt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(frame, "Ein-/Ausgabe-Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
			// e1.printStackTrace();
		}

	}

	private void showPreview(File imageFile, String title) {
		if (modified) {
			if (paths.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "Pfadliste wurde ver�ndert.\nBitte erst PUML ausf�hren!", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			// Vorschau �ffnen
			JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(imageFile.getAbsolutePath())), title,
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	private void saveOutput() {

		File save;
		if (modified) {
			if (paths.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(frame, "Pfadliste wurde ver�ndert.\nBitte erst PUML ausf�hren!", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			fDialog.setFileFilter(new FileNameExtensionFilter("Image File (.png)", "png"));
			if (fDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
				save = fDialog.getSelectedFile();
				try {
					copyFile(tmpImage, save);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frame, "Ein-/Ausgabe-Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void copyFile(File in, File out) throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try {
			inChannel = new FileInputStream(in).getChannel();
			outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException e) {

			}
		}
	}
}
