package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import javax.swing.event.TableColumnModelListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Composite;

public class PathEditor 
{

	protected Shell shlPfadeBearbeiten;
	private ArrayList<String> paths;
	private Table table;
	private TableColumn tableColumn;
	
	public PathEditor (ArrayList<String> al) 
	{
		paths = al;
	}
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
	{
		Display display = Display.getDefault();
		createContents();
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
		shlPfadeBearbeiten.setSize(450, 300);
		shlPfadeBearbeiten.setText("Pfade bearbeiten");
		shlPfadeBearbeiten.setLayout(new FillLayout(SWT.VERTICAL));
		
		table = new Table(shlPfadeBearbeiten, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		tableColumn = new TableColumn(table, SWT.NULL);
		tableColumn.setText("Pfade")
	}
	
	private void fillTable (ArrayList<String> al) 
	{
		
	}
	

}
