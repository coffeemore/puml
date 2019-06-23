import java.awt.EventQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
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
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JRadioButtonMenuItem;
import java.awt.Toolkit;

public class GUI_Swing
{

	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu mnDatei;
	private JMenuItem mntmAddFile;
	private JMenuItem mntmAddDir;
	private JSeparator separator;
	private JMenuItem mntmPathEditor;
	private JMenu mnOptions;
	private JCheckBoxMenuItem chckbxmntmUseJava;
	private JCheckBoxMenuItem chckbxmntmUseJar;
	private JToolBar toolBar;
	private JButton btnOpenDir;
	private JButton btnOpenFile;
	private JTabbedPane tabbedPane;
	private JPanel pnlClass;
	private JPanel pnlClassPrev;
	private JButton btnClassPrev;
	private JPanel pnlSequence;
	private JPanel pnlSeqPrev;
	private JButton btnSeqPrev;
	private JFileChooser fDialog;
	private PumlTreeNode ptnRoot;
	private JScrollPane scrollPaneClass;
	private JTextArea textClass;
	private JScrollPane scrollPaneSequence;
	private JTextArea textSequence;
	private JLabel lblPlantumlCodeClass;
	private JLabel lblPlantumlCodeSequence;
	private JSplitPane splitPane;
	private JPanel panel;
	private JTree treeSequence;
	private JLabel lblMethod;
	private JLabel lblClass;
	private JPanel panel_1;
	private JButton btnTest;
	private JMenu mnModus;
	private JRadioButtonMenuItem rdbtnmntmJava;
	private JRadioButtonMenuItem rdbtnmntmCpp;
	private JPanel pnlSeqPrevButton;
	private FileNameExtensionFilter filter;

	private ArrayList<String> paths;
	private int lastPathsLength;
	private ArrayList<String> srcCode;
	private String mode;
	private String classPumlCode;
	private String seqPumlCode;
	private String epClass;
	private String epMethod;
	private boolean useJava;
	private boolean useJar;
	private File tmpClassImage;
	private File tmpSeqImage;
	private Document parsedDoc;

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
		lastPathsLength = 0;
		useJava = true;
		useJar = false;
		tmpClassImage = new File(System.getProperty("user.dir"), "tmpClass.png");
		tmpSeqImage = new File(System.getProperty("user.dir"), "tmpSeq.png");
		epClass = "nicht gesetzt";
		epMethod = "nicht gesetzt";

		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(GUI_Swing.class.getResource("/img/PUML_Logo_32.png")));
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("PUML - no files selected");
		frame.setMinimumSize(new Dimension(640, 480));

		fDialog = new JFileChooser(new File("."));
		filter = new FileNameExtensionFilter("Java Code (.jar, .java)", "java", "jar");
		mode = "java";

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
				if (lastPathsLength != paths.size())
				{
					runPUML();
					lastPathsLength = paths.size();
					if (lastPathsLength == 0)
					{
						frame.setTitle("PUML - no files selected");
					}
				}
			}
		});
		mnDatei.add(mntmPathEditor);

		mnOptions = new JMenu("Optionen");
		menuBar.add(mnOptions);

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
		mnOptions.add(chckbxmntmUseJava);

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
		mnOptions.add(chckbxmntmUseJar);

		mnModus = new JMenu("Modus");
		mnOptions.add(mnModus);

		rdbtnmntmJava = new JRadioButtonMenuItem("Java");
		rdbtnmntmJava.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnmntmJava.isSelected() && mode != "java")
				{
					changeMode("java");
				}
			}
		});
		rdbtnmntmJava.setSelected(true);
		mnModus.add(rdbtnmntmJava);

		rdbtnmntmCpp = new JRadioButtonMenuItem("C++");
		rdbtnmntmCpp.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (rdbtnmntmCpp.isSelected() && mode != "cpp")
				{
					changeMode("cpp");
				}
			}
		});
		mnModus.add(rdbtnmntmCpp);

		ButtonGroup bgMode = new ButtonGroup();
		bgMode.add(rdbtnmntmJava);
		bgMode.add(rdbtnmntmCpp);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

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
				if (paths.isEmpty())
				{
					JOptionPane.showMessageDialog(frame, "Pfadliste ist leer.\nBitte erst Dateien laden!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{

					showPreview(tmpClassImage, "Klassendiagramm");
				}
			}
		});
		btnClassPrev.setHorizontalAlignment(SwingConstants.LEFT);
		pnlClassPrev.add(btnClassPrev);

		scrollPaneClass = new JScrollPane();
		pnlClass.add(scrollPaneClass, BorderLayout.CENTER);

		textClass = new JTextArea();
		textClass.setEditable(false);
		scrollPaneClass.setViewportView(textClass);

		lblPlantumlCodeClass = new JLabel("PlantUML Code:");
		scrollPaneClass.setColumnHeaderView(lblPlantumlCodeClass);

		pnlSequence = new JPanel();
		tabbedPane.addTab("Sequenzdiagramm", null, pnlSequence, null);
		pnlSequence.setLayout(new BorderLayout(0, 0));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		pnlSequence.add(splitPane, BorderLayout.CENTER);

		panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		scrollPaneSequence = new JScrollPane();
		panel.add(scrollPaneSequence);

		textSequence = new JTextArea();
		textSequence.setEditable(false);
		scrollPaneSequence.setViewportView(textSequence);

		lblPlantumlCodeSequence = new JLabel("PlantUML Code:");
		scrollPaneSequence.setColumnHeaderView(lblPlantumlCodeSequence);

		pnlSeqPrev = new JPanel();
		panel.add(pnlSeqPrev, BorderLayout.SOUTH);
		pnlSeqPrev.setLayout(new BorderLayout(0, 0));

		btnSeqPrev = new JButton("Vorschau");
		btnSeqPrev.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (paths.isEmpty())
				{
					JOptionPane.showMessageDialog(frame, "Pfadliste ist leer.\nBitte erst Dateien laden!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				else if (epClass == "nicht gesetzt")
				{
					JOptionPane.showMessageDialog(frame, "Einstiegspunkt nicht gesetzt!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					createSequenceDiagramm(parsedDoc);
					System.out.println(epClass + " " + epMethod);
					showPreview(tmpSeqImage, "Sequenzdiagramm");
				}

			}
		});
		pnlSeqPrevButton = new JPanel();
		pnlSeqPrevButton.add(btnSeqPrev);
		pnlSeqPrev.add(pnlSeqPrevButton, BorderLayout.SOUTH);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder("Entry-Point:"));
		pnlSeqPrev.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));

		lblMethod = new JLabel("Methode: " + epMethod);
		panel_1.add(lblMethod);

		lblClass = new JLabel("Klasse: " + epClass);
		panel_1.add(lblClass, BorderLayout.NORTH);

		ptnRoot = new PumlTreeNode("Klassen", PumlTreeNode.Type.ROOT);
		treeSequence = new JTree(ptnRoot);
		treeSequence.setToolTipText("Methode anklicken um Einstiegspunkt zu setzen");
		splitPane.setLeftComponent(new JScrollPane(treeSequence));

		treeSequence.addTreeSelectionListener(new TreeSelectionListener()
		{
			public void valueChanged(TreeSelectionEvent e)
			{
				PumlTreeNode node = (PumlTreeNode) treeSequence.getLastSelectedPathComponent();
				if (node == null)
				{
					return;
				}

				if (node.getType() == PumlTreeNode.Type.METHOD)
				{
					epClass = node.getParent().toString();
					epMethod = node.toString();
					lblClass.setText("Klasse: " + epClass);
					lblMethod.setText("Methode: " + epMethod);
				}
			}
		});

		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		btnOpenDir = new JButton("");
		btnOpenDir.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/directory.gif")));
		btnOpenDir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(false);
			}
		});
		btnOpenDir.setToolTipText("Ordner hinzuf\u00FCgen");
		toolBar.add(btnOpenDir);

		btnOpenFile = new JButton("");
		btnOpenFile.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/file.gif")));
		btnOpenFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				openFile(true);
			}
		});
		btnOpenFile.setToolTipText("Datei hinzuf\u00FCgen");
		toolBar.add(btnOpenFile);

		btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					test();
				}
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnTest.setBackground(new Color(150, 200, 255));
		//toolBar.add(btnTest);
	}

	private void resetElements()
	{
		ptnRoot.removeAllChildren();
		textClass.setText("");
		textSequence.setText("");
		paths = new ArrayList<>();
		lastPathsLength = 0;
		srcCode = new ArrayList<>();
		classPumlCode = "";
		seqPumlCode = "";
		epClass = "nicht gesetzt";
		epMethod = "nicht gesetzt";

		frame.setTitle("PUML - no files selected");
		frame.revalidate();

	}

	private void changeMode(String tmpMode)
	{
		switch (tmpMode)
		{
		case "java":
		{
			filter = new FileNameExtensionFilter("Java Code (.jar, .java)", "java", "jar");
			mode = "java";
//			System.out.println("mode = java");
			chckbxmntmUseJava.setEnabled(true);
			chckbxmntmUseJar.setEnabled(true);
			resetElements();
			break;
		}
		case "cpp":
		{
			filter = new FileNameExtensionFilter("C++ Code (.cpp, .hpp)", "cpp", "hpp");
			mode = "cpp";
//			System.out.println("mode = c++");
			chckbxmntmUseJava.setEnabled(false);
			chckbxmntmUseJar.setEnabled(false);
			resetElements();
			break;
		}
		default:
		{
			System.err.println("kein gültiger Modus");
		}
		}
	}

	private void openFile(boolean useFiles)
	{
		File[] items =
		{};
		if (useFiles)
		{
			fDialog.setDialogTitle("Dateien ausw\u00e4hlen");
			fDialog.setFileFilter(filter);
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
			fDialog.setDialogTitle("Ordner ausw\u00e4hlen");
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
		lastPathsLength = paths.size();

		// wenn Dateien/Pfade ausgewählt -> verarbeiten
		if (items.length != 0)
		{
			System.out.println("PUML ausführen");
			runPUML();
			System.out.println("PUML ausgeführt");
		}
	}

	private void test() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,
			TransformerException
	{
		
		PUMLgenerator.xmlHelper.getDocumentFrom("testfolder/xmlSpecifications/parsedData.xml");
//		Document parsedDoc = builder.parse(new File("testfolder/xmlSpecifications/parsedData.xml"));
		
		createTree(parsedDoc, ptnRoot);
//		System.out.println(treeSequence.getRowCount());
		for (int i = 0; i < treeSequence.getRowCount(); i++)
		{
			treeSequence.expandRow(i);
		}
	}

	private void createTree(Document doc, DefaultMutableTreeNode root)
	{

		try
		{
			root.removeAllChildren();
			NodeList nodelist = PUMLgenerator.xmlHelper.getList(doc, "//classdefinition");
//			System.out.println(nodelist.getLength() + " Klassen gefunden");
			// classdefinition loop
			for (int c = 0; c < nodelist.getLength(); c++)
			{
//				String className = nodelist.item(c).getFirstChild().getNextd().getTextContent();
				String className = PUMLgenerator.xmlHelper.getList(nodelist.item(c), "./name[1]").item(0).getTextContent();
				NodeList tmpNodelist = PUMLgenerator.xmlHelper.getList(nodelist.item(c), ".//methoddefinition");
//				System.out.println(className + " mit " + tmpNodelist.getLength() + " Methoden gefunden");
				PumlTreeNode tmpClassNode = new PumlTreeNode(className, PumlTreeNode.Type.CLASS);
				root.add(tmpClassNode);
				for (int m = 0; m < tmpNodelist.getLength(); m++)
				{
					String methodName = PUMLgenerator.xmlHelper.getList(tmpNodelist.item(m), ".//name").item(0)
							.getFirstChild().getTextContent();
//					System.out.println("\t" + methodName);
					tmpClassNode.add(new PumlTreeNode(methodName, PumlTreeNode.Type.METHOD));
				}
			}
		}
		catch (XPathExpressionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void runPUML()
	{

		try
		{
			if (paths.isEmpty())
			{
				resetElements();
			}
			else
			{
				frame.setTitle("PUML - parsing ...");

				// Dokument aus Quelltext generieren
				PUMLgenerator.codeCollector.paths = paths;
				PUMLgenerator.codeCollector.setUseJarFiles(useJar);
				PUMLgenerator.codeCollector.setUseJavaFiles(useJava);
				srcCode = PUMLgenerator.codeCollector.getSourceCode();
				PUMLgenerator.parser.parse(srcCode);
				parsedDoc = PUMLgenerator.parser.getParsingResult();

//				PUMLgenerator.xmlHelper.writeDocumentToConsole(parsedDoc);
				saveDoc(parsedDoc, "testParsed.xml");

				// Testdokument verwenden
//				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder builder = factory.newDocumentBuilder();
//				Document parsedDoc = builder.parse(new File("testfolder/xmlSpecifications/parsedData.xml"));

				
				createTree(parsedDoc, ptnRoot);
				DefaultTreeModel model = (DefaultTreeModel) treeSequence.getModel();
				model.reload();
				for (int i = 0; i < treeSequence.getRowCount(); i++)
				{
					treeSequence.expandRow(i);
				}

				createClassDiagramm(parsedDoc);
				treeSequence.expandRow(0);

				if (paths.size() == 1)
				{
					frame.setTitle("PUML - ready - " + paths.size() + " file selected");
				}
				else
				{
					frame.setTitle("PUML - ready - " + paths.size() + " files selected");
				}
			}

		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createClassDiagramm(Document parsedDoc)
	{
		// Klassendiagramm erstellen
		try
		{
			Document classDoc = PUMLgenerator.classDiagramGenerator.createDiagram(parsedDoc);
			saveDoc(classDoc, "testClass.xml");
			classPumlCode = PUMLgenerator.outputPUML.getPUML(classDoc);
			textClass.setText(classPumlCode);
			PUMLgenerator.outputPUML.createPUMLfromString(tmpClassImage.getAbsolutePath(), classPumlCode);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void createSequenceDiagramm(Document parsedDoc)
	{
		// Sequenzdiagramm erstellen
		try
		{
			Document seqDoc = PUMLgenerator.seqDiagramGenerator.createDiagram(parsedDoc, epClass, epMethod);
			saveDoc(seqDoc, "testSequence.xml");
			seqPumlCode = PUMLgenerator.outputPUML.getPUML(seqDoc);
			textSequence.setText(seqPumlCode);
			PUMLgenerator.outputPUML.createPUMLfromString(tmpSeqImage.getAbsolutePath(), seqPumlCode);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void showPreview(File imageFile, String title)
	{

		// Vorschau öffnen
		JOptionPane.showMessageDialog(frame, new PreviewPanel(imageFile), title, JOptionPane.PLAIN_MESSAGE);
	}

	private void saveOutput(File tmpImage)
	{
		// TODO an Unterteilung (Sequenz/Klassen) anpassen
		File save;
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

	@SuppressWarnings("resource")
	public void copyFile(File in, File out) throws IOException
	{
		FileChannel inChannel = null;
		FileChannel outChannel = null;

		inChannel = new FileInputStream(in).getChannel();
		outChannel = new FileOutputStream(out).getChannel();
		inChannel.transferTo(0, inChannel.size(), outChannel);
	}

	private void saveDoc(Document doc, String filePath) throws TransformerException
	{
		// XML output test
		// PUMLgenerator.xmlHelper.writeDocumentToConsole(seqDoc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(doc);
		StreamResult streamResult = new StreamResult(new File(filePath));
		transformer.transform(domSource, streamResult);
	}

	private static class PumlTreeNode extends DefaultMutableTreeNode
	{
		private static final long serialVersionUID = 1L;

		public enum Type
		{
			ROOT, CLASS, METHOD
		}

		private Type type;

		public PumlTreeNode(Object userObject, Type type)
		{
			super.setUserObject(userObject);
			this.type = type;
		}

		public Type getType()
		{
			return type;
		}
	}

	private class PathEditorPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;
		private JPanel pnlDelete;
		private JButton btnDelete;
		private DefaultListModel<String> model;
		private JList<String> list;
		private ArrayList<String> paths;

		/**
		 * Create the panel.
		 */
		public PathEditorPanel(ArrayList<String> paths)
		{
			this.paths = paths;
			setLayout(new BorderLayout(0, 0));

			pnlDelete = new JPanel();
			add(pnlDelete, BorderLayout.SOUTH);

			btnDelete = new JButton("L\u00F6schen");
			btnDelete.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					deleteItem();
				}
			});
			pnlDelete.add(btnDelete);

			model = new DefaultListModel<>();
			list = new JList<>(model);
			add(new JScrollPane(list), BorderLayout.CENTER);
			for (int i = 0; i < paths.size(); i++)
			{
				model.addElement(paths.get(i));
			}
		}

		private void deleteItem()
		{
			int[] items = list.getSelectedIndices();
			for (int i = items.length - 1; i >= 0; i--)
			{
				model.remove(items[i]);
				paths.remove(items[i]);
			}
		}
	}

	private class PreviewPanel extends JPanel
	{
		private static final long serialVersionUID = 1L;

		public PreviewPanel(File imageFile)
		{
			this.setLayout(new BorderLayout(0, 0));
			this.add(new JLabel(new ImageIcon(imageFile.getAbsolutePath())), BorderLayout.CENTER);
			JButton btnSave = new JButton(new ImageIcon(GUI_Swing.class.getResource("/img/floppy.gif")));
			btnSave.setToolTipText("Diagramm Speichern");
			btnSave.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					saveOutput(imageFile);
				}
			});
			JPanel tmpPanel = new JPanel();
			tmpPanel.add(btnSave);
			this.add(tmpPanel, BorderLayout.SOUTH);
		}
	}
}
