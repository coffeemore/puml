import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Text;

/**
 * Die Klasse benötigt im Build Path den Verweis zur jeweiligen SWT-Bibliothek
 * zum jeweiligen OS. Bitte aber nur ein Paket verlinken.
 * 
 * @author Julian Uebe
 *
 */
public class GUI_SWT
{

	protected Shell shell;
	private Label lblImage;
	private Tree tree;
	private ToolBar toolBar;
	private PathEditor pathEditor;
	private FileDialog fDialog;
	private DirectoryDialog dDialog;
	private Menu menu;
	private MenuItem mntmDatei;
	private Menu menu_1;
	private MenuItem mntmPfadffnen;
	private MenuItem mntmDateienHinzufgen;
	private MenuItem mntmSuchlisteBearbeiten;
	private MenuItem mntmOptionen;
	private Menu menu_2;
	private MenuItem mntmToolbarAnzeigen;
	private MenuItem mntmJavadateienVerwenden;
	private MenuItem mntmJardateienVerwenden;
	private ToolItem tltmP;
	private ToolItem tltmNewItem;
	private ToolItem toolItem_1;
	private ToolItem tltmNewItem_1;
	private Composite composite;
	private SashForm sashForm;
	private Label lblVorschau;
	private Composite composite_2;
	private Composite composite_3;
	private Text text;
	private MessageBox messageBox;

	private ArrayList<String> paths;
	private String srcCode;
	private String pumlCode;
	private boolean useJava;
	private boolean useJar;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public GUI_SWT()
	{
		paths = new ArrayList<String>();
		useJava = true;
		useJar = true;
	}

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents()
	{
		shell = new Shell();
		shell.setMinimumSize(new Point(320, 240));
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));

		pathEditor = new PathEditor(paths);

		fDialog = new FileDialog(shell, SWT.MULTI);
		fDialog.setText("Dateien auswählen");
		fDialog.setFilterPath(System.getProperty("user.dir"));

		dDialog = new DirectoryDialog(shell);
		dDialog.setText("Ordner auswählen");
		dDialog.setFilterPath(System.getProperty("user.dir"));

		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		mntmDatei = new MenuItem(menu, SWT.CASCADE);
		mntmDatei.setText("Datei");

		menu_1 = new Menu(mntmDatei);
		mntmDatei.setMenu(menu_1);

		mntmPfadffnen = new MenuItem(menu_1, SWT.NONE);
		mntmPfadffnen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				openFile(false);
			}
		});
		mntmPfadffnen.setText("Pfad hinzuf\u00FCgen");

		mntmDateienHinzufgen = new MenuItem(menu_1, SWT.NONE);
		mntmDateienHinzufgen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				openFile(true);
			}
		});
		mntmDateienHinzufgen.setText("Datei hinzuf\u00FCgen");

		new MenuItem(menu_1, SWT.SEPARATOR);

		mntmSuchlisteBearbeiten = new MenuItem(menu_1, SWT.NONE);
		mntmSuchlisteBearbeiten.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				pathEditor.open();
			}
		});
		mntmSuchlisteBearbeiten.setText("Suchliste bearbeiten");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmPumlAusfhren = new MenuItem(menu_1, SWT.NONE);
		mntmPumlAusfhren.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				runPUML();
			}
		});
		mntmPumlAusfhren.setText("PUML ausf\u00FChren");

		mntmOptionen = new MenuItem(menu, SWT.CASCADE);
		mntmOptionen.setText("Optionen");

		menu_2 = new Menu(mntmOptionen);
		mntmOptionen.setMenu(menu_2);

		mntmToolbarAnzeigen = new MenuItem(menu_2, SWT.CHECK);
		mntmToolbarAnzeigen.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				toolBar.setVisible(mntmToolbarAnzeigen.getSelection());
				// toolBar.setEnabled(mntmToolbarAnzeigen.getSelection());
			}
		});
		mntmToolbarAnzeigen.setText("Toolbar anzeigen");
		mntmToolbarAnzeigen.setSelection(true);

		mntmJavadateienVerwenden = new MenuItem(menu_2, SWT.CHECK);
		mntmJavadateienVerwenden.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if (mntmJavadateienVerwenden.getSelection())
				{
					useJava = true;
				}
				else
				{
					useJava = false;
				}
			}
		});
		mntmJavadateienVerwenden.setText("Java-Dateien verwenden");
		mntmJavadateienVerwenden.setSelection(useJava);

		mntmJardateienVerwenden = new MenuItem(menu_2, SWT.CHECK);
		mntmJardateienVerwenden.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				if (mntmJardateienVerwenden.getSelection())
				{
					useJar = true;
				}
				else
				{
					useJar = false;
				}
			}
		});
		mntmJardateienVerwenden.setText("Jar-Dateien verwenden");
		mntmJardateienVerwenden.setSelection(useJar);

		toolBar = new ToolBar(shell, SWT.BORDER | SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		tltmP = new ToolItem(toolBar, SWT.NONE);
		tltmP.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				openFile(false);
			}
		});
		tltmP.setToolTipText("Pfad hinzuf\u00FCgen");
		tltmP.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/javax/swing/plaf/metal/icons/ocean/directory.gif"));

		tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem
				.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/javax/swing/plaf/metal/icons/ocean/file.gif"));
		tltmNewItem.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				openFile(true);
			}
		});
		tltmNewItem.setToolTipText("Datei hinzuf\u00FCgen");

		toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				lblImage.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/test.png"));
				lblImage.pack();

			}
		});
		toolItem_1.setImage(
				SWTResourceManager.getImage(GUI_SWT.class, "/com/sun/java/swing/plaf/windows/icons/HardDrive.gif"));
		toolItem_1.setToolTipText("Test Vorschau");

		tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_1.setToolTipText("PUML ausf\u00FChren");
		tltmNewItem_1.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

//		for (int loopIndex0 = 0; loopIndex0 < 10; loopIndex0++)
//		{
//		    TreeItem treeItem0 = new TreeItem(tree, 0);
//		    treeItem0.setText("Klasse " + loopIndex0);
//		    treeItem0.setChecked(true);
//		    for (int loopIndex1 = 0; loopIndex1 < 5; loopIndex1++)
//		    {
//			TreeItem treeItem1 = new TreeItem(treeItem0, 0);
//			treeItem1.setText("Methode " + loopIndex1);
//			treeItem1.setChecked(true);
//		    }
//		}
				runPUML();
			}
		});
		tltmNewItem_1
				.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/javax/swing/plaf/basic/icons/JavaCup16.png"));

		composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		sashForm = new SashForm(composite, SWT.NONE);
		sashForm.setSashWidth(5);

		tree = new Tree(sashForm, SWT.BORDER | SWT.CHECK);

		composite_3 = new Composite(sashForm, SWT.NONE);
		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));

		text = new Text(composite_3, SWT.BORDER);
		text.setEditable(false);
		text.setToolTipText("PlantUML Code");

		composite_2 = new Composite(sashForm, SWT.BORDER);
		composite_2.setToolTipText("Vorschau");

		lblVorschau = new Label(composite_2, SWT.NONE);
		lblVorschau.setLocation(10, 10);
		lblVorschau.setSize(48, 15);
		lblVorschau.setText("Vorschau");
		lblVorschau.setToolTipText("Vorschau");

		lblImage = new Label(composite_2, SWT.NONE);
		lblImage.setLocation(10, 31);
		lblImage.setSize(192, 15);
		lblImage.setToolTipText("Vorschau");

		sashForm.setWeights(new int[]
		{ 1, 1, 2 });

	}

	private void openFile(boolean useFiles)
	{
		String[] items;
		fDialog.setFilterExtensions(new String[]
		{ "*.java", "*.jar" });
		if (useFiles)
		{
			fDialog.open();
			items = fDialog.getFileNames();
			String path = fDialog.getFilterPath();
			for (int i = 0; i < items.length; i++)
			{
				paths.add(path + "/" + items[i]);
			}
		}
		else
		{
			String dir = dDialog.open();
			paths.add(dir);
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
				// TODO Klassennamen in den Tree aufnehmen
				// System.out.println(classes.get(i));
			}
			// System.out.println(pumlCode);
			PUMLgenerator.outputPUML.savePUMLtoFile(PUMLgenerator.outputPUML.getPUML(tempPR), System.getProperty("user.dir") + "output.txt");

			text.setText(pumlCode);

			// TODO Image durch erzeugtes Image ersetzen
			lblImage.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/test.png"));
			lblImage.pack();

		}
		catch (NullPointerException npe)
		{
			// von CodeCollector
			// System.err.println("Pfadliste ist leer");
			messageBox = new MessageBox(shell, SWT.ICON_ERROR);
			messageBox.setMessage("Pfadliste leer");
			messageBox.setText("Fehler");
			messageBox.open();
		}
		catch (IllegalArgumentException iae)
		{
			// von CodeCollector
			// System.err.println(".jar und .java wird ignoriert");
			messageBox = new MessageBox(shell, SWT.ICON_ERROR);
			messageBox.setMessage("Bitte mindestens einen Suchtyp auswählen (jar/java)");
			messageBox.setText("Fehler");
			messageBox.open();
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	}
}
