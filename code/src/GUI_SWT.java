import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import java.util.function.Consumer;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.custom.SashForm;

public class GUI_SWT {

	protected Shell shell;
	private Label lblImage;
	private Tree tree;
	private ToolBar toolBar;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */

	public GUI_SWT() {

	}

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setMinimumSize(new Point(320, 240));
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new GridLayout(1, false));

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmDatei = new MenuItem(menu, SWT.CASCADE);
		mntmDatei.setText("Datei");

		Menu menu_1 = new Menu(mntmDatei);
		mntmDatei.setMenu(menu_1);

		MenuItem mntmPfadffnen = new MenuItem(menu_1, SWT.NONE);
		mntmPfadffnen.setText("Pfad hinzuf\u00FCgen");

		MenuItem mntmDateienHinzufgen = new MenuItem(menu_1, SWT.NONE);
		mntmDateienHinzufgen.setText("Datei hinzuf\u00FCgen");

		new MenuItem(menu_1, SWT.SEPARATOR);

		MenuItem mntmSuchlisteBearbeiten = new MenuItem(menu_1, SWT.NONE);
		mntmSuchlisteBearbeiten.setText("Suchliste bearbeiten");

		MenuItem mntmAnsicht = new MenuItem(menu, SWT.CASCADE);
		mntmAnsicht.setText("Ansicht");

		Menu menu_2 = new Menu(mntmAnsicht);
		mntmAnsicht.setMenu(menu_2);

		MenuItem mntmToolbarAnzeigen = new MenuItem(menu_2, SWT.CHECK);
		mntmToolbarAnzeigen.setText("Toolbar anzeigen");

		toolBar = new ToolBar(shell, SWT.BORDER | SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
		toolItem.setToolTipText("Pfad \u00F6ffnen");
		toolItem.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/icons8-add-list-24.png"));

		ToolItem tltmNewItem = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem.setToolTipText("Datei \u00F6ffnen");
		tltmNewItem.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/icons8-add-file-24.png"));

		ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
		toolItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblImage.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/test.png"));
			}
		});
		toolItem_1.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/icons8-synchronize-24.png"));
		toolItem_1.setToolTipText("Vorschau aktualisieren");

		ToolItem tltmNewItem_1 = new ToolItem(toolBar, SWT.NONE);
		tltmNewItem_1.setToolTipText("Test");
		tltmNewItem_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				for (int loopIndex0 = 0; loopIndex0 < 10; loopIndex0++) {
					TreeItem treeItem0 = new TreeItem(tree, 0);
					treeItem0.setText("Klasse " + loopIndex0);
					treeItem0.setChecked(true);
					for (int loopIndex1 = 0; loopIndex1 < 5; loopIndex1++) {
						TreeItem treeItem1 = new TreeItem(treeItem0, 0);
						treeItem1.setText("Methode " + loopIndex1);
						treeItem1.setChecked(true);
					}
				}

			}
		});
		tltmNewItem_1.setImage(SWTResourceManager.getImage(GUI_SWT.class, "/img/icons8-sun-24.png"));
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		SashForm sashForm = new SashForm(composite, SWT.NONE);
		sashForm.setSashWidth(5);
		
		tree = new Tree(sashForm, SWT.BORDER | SWT.CHECK);
		
		Composite composite_1 = new Composite(sashForm, SWT.BORDER);
		composite_1.setLayout(new GridLayout(1, false));
		
		Label lblVorschau = new Label(composite_1, SWT.NONE);
		lblVorschau.setText("Vorschau");
		
		lblImage = new Label(composite_1, SWT.NONE);
		lblImage.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
		sashForm.setWeights(new int[] {1, 3});

	}
}
