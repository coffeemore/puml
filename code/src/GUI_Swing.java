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
import javax.swing.ButtonGroup;
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
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JRadioButtonMenuItem;

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
	private JButton btnParse;
	private JMenuItem mntmParse;
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

		mnModus = new JMenu("Modus");
		mnOptionen.add(mnModus);

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
				System.out.println(mode);
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
				showPreview(tmpClassImage, "Klassendiagramm");
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
				// TODO
				if (epClass == "nicht gesetzt")
				{
					JOptionPane.showMessageDialog(frame, "Einstiegspunkt nicht gesetzt!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
				else
				{
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
		splitPane.setLeftComponent(treeSequence);

		// Selection Listener welches Leaf und welcher Parent ausgewählt wurden (nur
		// Leaf-Ebene)
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

		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/floppy.gif")));
		btnSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveOutput();
			}
		});
		btnSave.setToolTipText("Speichern");
		toolBar.add(btnSave);

		btnTest = new JButton("Test");
		btnTest.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					test();
				}
				catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException
						| TransformerException e1)
				{
					// TODO Auto-generated catch block
					System.err.println(e1.getCause());
					System.err.println(e1.getMessage());
				}
			}
		});
		btnTest.setBackground(new Color(150, 200, 255));
		toolBar.add(btnTest);

//		btnParse = new JButton("");
//		btnParse.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//
//				parse();
//
//			}
//		});
//		btnParse.setToolTipText("Parse");
//		btnParse.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/JavaCup16.png")));
//		toolBar.add(btnParse);
//
//		btnRunPUML = new JButton("");
//		btnRunPUML.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				runPUML();
//			}
//		});
//		btnRunPUML.setToolTipText("PUML ausf\u00FChren");
//		btnRunPUML.setIcon(new ImageIcon(GUI_Swing.class.getResource("/img/JavaCup16.png")));
//		toolBar.add(btnRunPUML);
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
			fDialog.setDialogTitle("Dateien ausw�hlen");
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
			fDialog.setDialogTitle("Ordner ausw�hlen");
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

		// wenn Dateien/Pfade ausgewählt -> verarbeiten
		if (items.length != 0)
		{
			runPUML();
		}
	}

	private void test() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException,
			TransformerException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document parsedDoc = builder.parse(new File("testfolder/xmlSpecifications/parsedData.xml"));
		createTree(parsedDoc, ptnRoot);
//		System.out.println(treeSequence.getRowCount());
		for (int i = 0; i < treeSequence.getRowCount(); i++)
		{
			treeSequence.expandRow(i);
		}

	}

	private void createTree(Document doc, DefaultMutableTreeNode root) throws XPathExpressionException,
			TransformerException, SAXException, IOException, ParserConfigurationException
	{
		root.removeAllChildren();
		NodeList nodelist = PUMLgenerator.xmlHelper.getList(doc, "//classdefinition");
//		System.out.println(nodelist.getLength() + " Klassen gefunden");
		// classdefinition loop
		for (int c = 0; c < nodelist.getLength(); c++)
		{
			String className = nodelist.item(c).getFirstChild().getNextSibling().getTextContent();
			NodeList tmpNodelist = PUMLgenerator.xmlHelper.getList(nodelist.item(c), ".//methoddefinition");
//			System.out.println(className + " mit " + tmpNodelist.getLength() + " Methoden gefunden");
			PumlTreeNode tmpClassNode = new PumlTreeNode(className, PumlTreeNode.Type.CLASS);
			root.add(tmpClassNode);
			for (int m = 0; m < tmpNodelist.getLength(); m++)
			{
				String methodName = PUMLgenerator.xmlHelper.getList(tmpNodelist.item(m), ".//name").item(0)
						.getFirstChild().getTextContent();
//				System.out.println("\t" + methodName);
				tmpClassNode.add(new PumlTreeNode(methodName, PumlTreeNode.Type.METHOD));
			}
		}
	}

	private void runPUML()
	{

		try
		{

			frame.setTitle("PUML - parsing ...");

			// Dokument aus Quelltext generieren
			PUMLgenerator.codeCollector.paths = paths;
			PUMLgenerator.codeCollector.setUseJarFiles(useJar);
			PUMLgenerator.codeCollector.setUseJavaFiles(useJava);
			srcCode = PUMLgenerator.codeCollector.getSourceCode();
			PUMLgenerator.parser.parse(srcCode);
			Document parsedDoc = PUMLgenerator.parser.getParsingResult();
			
			PUMLgenerator.xmlHelper.writeDocumentToConsole(parsedDoc);
			
			// XML output test
			// PUMLgenerator.xmlHelper.writeDocumentToConsole(seqDoc);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(parsedDoc);
			StreamResult streamResult = new StreamResult(new File("test.xml"));
			transformer.transform(domSource, streamResult);
			
			// Testdokument verwenden
//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document parsedDoc = builder.parse(new File("testfolder/xmlSpecifications/parsedData.xml"));

			createTree(parsedDoc, ptnRoot);
			for (int i = 0; i < treeSequence.getRowCount(); i++)
			{
				treeSequence.expandRow(i);
			}

			// Klassendiagramm erstellen
			Document classDoc = PUMLgenerator.classDiagramGenerator.createDiagram(parsedDoc);
			classPumlCode = PUMLgenerator.outputPUML.getPUML(classDoc);
			textClass.setText(classPumlCode);
			PUMLgenerator.outputPUML.createPUMLfromString(tmpClassImage.getAbsolutePath(), classPumlCode);

			// Sequenzdiagramm erstellen
			Document seqDoc = PUMLgenerator.seqDiagramGenerator.createDiagram(parsedDoc, "Class1", "method1");
			seqPumlCode = PUMLgenerator.outputPUML.getPUML(seqDoc);
			textSequence.setText(seqPumlCode);
			PUMLgenerator.outputPUML.createPUMLfromString(tmpSeqImage.getAbsolutePath(), seqPumlCode);

			treeSequence.expandRow(0);



//			seqPumlCode = PUMLgenerator.outputPUML.getPUML(seqDoc);
//			textSequence.setText(seqPumlCode);

			if (paths.size() == 1)
			{
				frame.setTitle("PUML - ready - " + paths.size() + " file selected");
			}
			else
			{
				frame.setTitle("PUML - ready - " + paths.size() + " files selected");
			}

		}
		catch (IllegalArgumentException iae)
		{
			// von CodeCollector
			JOptionPane.showMessageDialog(frame, "Kein Suchtyp ausgew�hlt!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		catch (XPathExpressionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch (TransformerConfigurationException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		catch (TransformerException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		catch (SAXException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (TransformerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void showPreview(File imageFile, String title)
	{
		// TODO evtl. Panel übergeben, welches Vorschau und speichern enthält
		if (paths.isEmpty())
		{
			JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			// Vorschau öffnen
			JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(imageFile.getAbsolutePath())), title,
					JOptionPane.PLAIN_MESSAGE);
		}

//		if (modified)
//		{
//			if (paths.isEmpty())
//			{
//				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
//			}
//			else
//			{
//				JOptionPane.showMessageDialog(frame, "Pfadliste wurde ver�ndert.\nBitte erst PUML ausf�hren!", "Fehler",
//						JOptionPane.ERROR_MESSAGE);
//			}
//		}
//		else
//		{
//			// Vorschau öffnen
//			JOptionPane.showMessageDialog(frame, new JLabel(new ImageIcon(imageFile.getAbsolutePath())), title,
//					JOptionPane.PLAIN_MESSAGE);
//		}
	}

	private void saveOutput()
	{
		// TODO an Unterteilung (Sequenz/Klassen) anpassen
//		File save;
//		if (modified)
//		{
//			if (paths.isEmpty())
//			{
//				JOptionPane.showMessageDialog(frame, "Pfadliste ist leer!", "Fehler", JOptionPane.ERROR_MESSAGE);
//			}
//			else
//			{
//				JOptionPane.showMessageDialog(frame, "Pfadliste wurde ver�ndert.\nBitte erst PUML ausf�hren!", "Fehler",
//						JOptionPane.ERROR_MESSAGE);
//			}
//		}
//		else
//		{
//			fDialog.setFileFilter(new FileNameExtensionFilter("Image File (.png)", "png"));
//			if (fDialog.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION)
//			{
//				save = fDialog.getSelectedFile();
//				try
//				{
//					copyFile(tmpImage, save);
//				}
//				catch (IOException e)
//				{
//					JOptionPane.showMessageDialog(frame, "Ein-/Ausgabe-Fehler", "Fehler", JOptionPane.ERROR_MESSAGE);
//				}
//			}
//		}
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
	
	private static class PumlTreeNode extends DefaultMutableTreeNode
	{
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
}
