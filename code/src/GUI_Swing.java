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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class GUI_Swing
{

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
	private JSplitPane splitPane;
	private JTree tree;
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

	private ArrayList<String> paths;
	private String srcCode;
	private String pumlCode;
	private boolean useJava;
	private boolean useJar;
	private boolean modified;
	private File tmpImage;
	private JLabel lblPlantumlCodeClass;
	private JLabel lblPlantumlCodeSequence;

	/**
	 * Launch the application.
	 */
	public static void showGUI()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					GUI_Swing window = new GUI_Swing();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_Swing()
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
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
		mntmAddFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(true);
			}
		});
		mnDatei.add(mntmAddFile);

		mntmAddDir = new JMenuItem("Ordner hinzuf\u00FCgen");
		mntmAddDir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(false);
			}
		});
		mnDatei.add(mntmAddDir);

		separator = new JSeparator();
		mnDatei.add(separator);

		mntmPathEditor = new JMenuItem("Suchliste bearbeiten");
		mntmPathEditor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(frame, new PathEditorPanel(paths), "Pfade bearbeiten",
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		mnDatei.add(mntmPathEditor);

		mntmRunPUML = new JMenuItem("PUML ausf\u00FChren");
		mntmRunPUML.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				runPUML();
			}
		});
		mnDatei.add(mntmRunPUML);

		mnOptionen = new JMenu("Optionen");
		menuBar.add(mnOptionen);

		chckbxmntmUseJava = new JCheckBoxMenuItem("Java-Dateien verwenden");
		chckbxmntmUseJava.setSelected(useJava);
		chckbxmntmUseJava.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxmntmUseJava.isSelected())
				{
					useJava = true;
				}
				else
				{
					useJava = false;
				}
			}
		});
		mnOptionen.add(chckbxmntmUseJava);

		chckbxmntmUseJar = new JCheckBoxMenuItem("Jar-Dateien verwenden");
		chckbxmntmUseJar.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (chckbxmntmUseJar.isSelected())
				{
					useJar = true;
				}
				else
				{
					useJar = false;
				}
			}
		});
		chckbxmntmUseJar.setSelected(useJar);
		mnOptionen.add(chckbxmntmUseJar);

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		btnOpenDir = new JButton("");
		btnOpenDir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(false);
			}
		});
		btnOpenDir.setToolTipText("Ordner hinzuf\u00FCgen");
		btnOpenDir.setIcon(
				new ImageIcon(GUI_Swing.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		toolBar.add(btnOpenDir);

		btnOpenFile = new JButton("");
		btnOpenFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(true);
			}
		});
		btnOpenFile.setToolTipText("Datei hinzuf\u00FCgen");
		btnOpenFile.setIcon(new ImageIcon(GUI_Swing.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		toolBar.add(btnOpenFile);

		btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveOutput();
			}
		});
		btnSave.setToolTipText("Speichern");
		btnSave.setIcon(new ImageIcon(GUI_Swing.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		toolBar.add(btnSave);

		btnRunPUML = new JButton("");
		btnRunPUML.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				runPUML();
			}
		});
		btnRunPUML.setToolTipText("PUML ausf\u00FChren");
		btnRunPUML.setIcon(new ImageIcon(GUI_Swing.class.getResource("/javax/swing/plaf/basic/icons/JavaCup16.png")));
		toolBar.add(btnRunPUML);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.1);
		frame.getContentPane().add(splitPane, BorderLayout.CENTER);

		dmtnClasses = new DefaultMutableTreeNode("Klassen");
		tree = new JTree(dmtnClasses);
		splitPane.setLeftComponent(tree);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setRightComponent(tabbedPane);

		pnlClass = new JPanel();
		tabbedPane.addTab("Klassendiagramm", null, pnlClass, null);
		pnlClass.setLayout(new BorderLayout(0, 0));

		pnlClassPrev = new JPanel();
		pnlClass.add(pnlClassPrev, BorderLayout.SOUTH);

		btnClassPrev = new JButton("Vorschau");
		btnClassPrev.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
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

		pnlSeqPrev = new JPanel();
		pnlSequence.add(pnlSeqPrev, BorderLayout.SOUTH);

		btnSeqPrev = new JButton("Vorschau");
		pnlSeqPrev.add(btnSeqPrev);

		scrollPaneSequence = new JScrollPane();
		pnlSequence.add(scrollPaneSequence, BorderLayout.CENTER);

		textSequence = new JTextArea();
		scrollPaneSequence.setViewportView(textSequence);

		lblPlantumlCodeSequence = new JLabel("PlantUML Code:");
		scrollPaneSequence.setColumnHeaderView(lblPlantumlCodeSequence);
	}

	private void openFile(boolean useFiles)
	{
		File[] items =
		{};
		if (useFiles)
		{
			fDialog.setDialogTitle("Dateien auswählen");
			fDialog.setFileFilter(new FileNameExtensionFilter("Java Code (.jar, .java)", "java", "jar"));
			fDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fDialog.setMultiSelectionEnabled(true);
			fDialog.showOpenDialog(frame);
			items = fDialog.getSelectedFiles();
			for (int i = 0; i < items.length; i++)
			{
				paths.add(items[i].getPath());
			}

		}
		else
		{
			fDialog.setDialogTitle("Ordner auswählen");
			fDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fDialog.setMultiSelectionEnabled(true);
			fDialog.setAcceptAllFileFilterUsed(false);
			if (fDialog.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
				items = fDialog.getSelectedFiles();
				for (int i = 0; i < items.length; i++)
				{
					paths.add(items[i].getPath());
				}
			}
		}
		if (items.length > 0)
		{
			modified = true;
		}

	}

	private void runPUML()
	{

		try
		{
			PUMLgenerator.codeCollector.paths = paths;
			PUMLgenerator.codeCollector.setUseJarFiles(useJar);
			PUMLgenerator.codeCollector.setUseJavaFiles(useJava);
			srcCode = PUMLgenerator.codeCollector.getSourceCode();
			System.out.println(srcCode);

			PUMLgenerator.parser.parse(srcCode);
			ParsingResult tempPR = PUMLgenerator.parser.getParsingResult();
			pumlCode = PUMLgenerator.outputPUML.getPUML(tempPR);
			ArrayList<String> classes = tempPR.getClasses();
			for (int i = 0; i < classes.size(); i++)
			{
				dmtnClasses.add(new DefaultMutableTreeNode(classes.get(i)));
			}
			tree.expandRow(0);
			textClass.setText(pumlCode);

			PUMLgenerator.outputPUML.createPUMLfromString(tmpImage.getAbsolutePath(), pumlCode);
			modified = false;

		}
		catch (NullPointerException npe)
		{
			// von CodeCollector
			JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		catch (IllegalArgumentException iae)
		{
			// von CodeCollector
			JOptionPane.showMessageDialog(frame, "Kein Suchtyp ausgewählt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(frame, "Ein-/Ausgabe-Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
			// e1.printStackTrace();
		}

	}

	private void showPreview(File imageFile, String title)
	{
		if (modified)
		{
			if (paths.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Pfadliste wurde verändert.\nBitte erst PUML ausführen!", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			// Vorschau öffnen
			JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(imageFile.getAbsolutePath())), title,
					JOptionPane.PLAIN_MESSAGE);
		}
	}

	private void saveOutput()
	{

		File save;
		if (modified)
		{
			if (paths.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				JOptionPane.showMessageDialog(frame, "Pfadliste wurde verändert.\nBitte erst PUML ausführen!", "Fehler",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		else
		{
			fDialog.setFileFilter(new FileNameExtensionFilter("Image File (.png)", "png"));
			if (fDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
			{
				save = fDialog.getSelectedFile();
				try
				{
					copyFile(tmpImage, save);
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(frame, "Ein-/Ausgabe-Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void copyFile(File in, File out) throws IOException
	{
		FileChannel inChannel = null;
		FileChannel outChannel = null;
		try
		{
			inChannel = new FileInputStream(in).getChannel();
			outChannel = new FileOutputStream(out).getChannel();
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (inChannel != null)
				{
					inChannel.close();
				}
				if (outChannel != null)
				{
					outChannel.close();
				}
			}
			catch (IOException e)
			{

			}
		}
	}
}
