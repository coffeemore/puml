package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import javax.swing.event.TableColumnModelListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class PathEditor 
{

	protected Shell shlPfadeBearbeiten;
	private ArrayList<String> paths;
	Table table;
	private TableColumn tableColumn;
	//Text text = new Text(shlPfadeBearbeiten, SWT.SINGLE | SWT.BORDER);
	public PathEditor (ArrayList<String> al) 
	{
		paths = al;
		fillTable(al);
	}
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
	{
		Display display = Display.getDefault();
		createContents();
      //shlPfadeBearbeiten.pack();
		shlPfadeBearbeiten.open();
		shlPfadeBearbeiten.layout();
		while (!shlPfadeBearbeiten.isDisposed()) 
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
		shlPfadeBearbeiten = new Shell();
		shlPfadeBearbeiten.setSize(576, 601);
		shlPfadeBearbeiten.setText("Pfade bearbeiten");
		shlPfadeBearbeiten.setLayout(null);
		
		table = new Table(shlPfadeBearbeiten, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseDown(MouseEvent e) 
			{
				
			}
		});
		table.setBounds(0, 0, 554, 481);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableColumn = new TableColumn(table, SWT.NULL);
		tableColumn.setText("Pfade");
		
		Composite composite = new Composite(shlPfadeBearbeiten, SWT.NONE);
		composite.setBounds(0, 481, 554, 64);
		
		Button btnLschen = new Button(composite, SWT.NONE);
		btnLschen.setBounds(20, 10, 105, 35);
		btnLschen.setText("L\u00F6schen");
	}
	
	private void fillTable (ArrayList<String> al) 
	{
		for(int i=1;i<3;i++)
		{
			//final TableItem item = new TableItem(table, SWT.NONE);
			//item.setText("lululu");
		}
	}
}
