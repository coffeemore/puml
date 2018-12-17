package gui;

import org.eclipse.swt.widgets.Display;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
//@author Jan Sollmann
public class PathEditor 
{

	protected Shell shlPfadeBearbeiten;
	private ArrayList<String> paths;
	private Table table;
	private Button btnHizufgen;
	private Button btnLschen;
//	private TableColumn tableColumn;
	
	public PathEditor(ArrayList<String> al) {
		paths = al;
	}

	/**
	 * Open the window.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlPfadeBearbeiten.open();
		shlPfadeBearbeiten.layout();
		fillTable();
		while (!shlPfadeBearbeiten.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shlPfadeBearbeiten = new Shell();
		shlPfadeBearbeiten.setMinimumSize(new Point(450, 300));
		shlPfadeBearbeiten.setSize(450, 300);
		shlPfadeBearbeiten.setText("Pfade bearbeiten");
		shlPfadeBearbeiten.setLayout(new FillLayout(SWT.HORIZONTAL));

		table = new Table(shlPfadeBearbeiten, SWT.CHECK);
//		table.setHeaderVisible(true);
//		table.setLinesVisible(true);
//		
//		tableColumn = new TableColumn(table, SWT.NULL);
//		tableColumn.setText("Pfade");
	}

	private void fillTable() 
	{
		// table.setSize(200, 200);
		for (int i = 0; i < paths.size(); i++) 
		{
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(paths.get(i));
		}
		
		// TableItem item = new TableItem(table, SWT.NONE, 1);
		// item.setText("*** New Item " + table.indexOf(item) + " ***");
		
		Composite composite = new Composite(shlPfadeBearbeiten, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		/*btnHizufgen = new Button(composite, SWT.NONE);
		btnHizufgen.setText("Hizuf\u00FCgen");
		new Label(composite, SWT.NONE);*/
		
		btnLschen = new Button(composite, SWT.NONE);
		btnLschen.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseDown(MouseEvent e) {deleteElements();}
		});
		btnLschen.setText("L\u00F6schen");
		shlPfadeBearbeiten.pack();
	}

	//Löscht ausgewählte Zeilen
	/*protected void deleteElements() 
	{
		int a[]=table.getSelectionIndices();
		for(int i=0;i<a.length;i++) 
		{
			//System.out.println(paths.get(a[i]));
			paths.remove(a[i]);
		}
	}*/
	//Löscht  angehackte Elemente
	//Zu Implementieren wäre updaten der Tabelle um das Löschen gelöschter Elemente zu verhindern und der Übersicht halber
	protected void deleteElements() 
	{
			for(int i=0;i<=table.getItemCount()-1;i++) 
			{
				if(table.getItem(i).getChecked()) 
				{
					//System.out.println("drin");
					paths.remove(table.getItem(i).getText());
				}
			}
	}	
}
