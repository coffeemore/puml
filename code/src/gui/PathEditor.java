package gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;

import org.eclipse.swt.SWT;

public class PathEditor {

	protected Shell shlPfadeBearbeiten;
	private ArrayList paths;
	
	public PathEditor (ArrayList<String> al) {
		paths = al;
	}
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlPfadeBearbeiten.open();
		shlPfadeBearbeiten.layout();
		while (!shlPfadeBearbeiten.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlPfadeBearbeiten = new Shell();
		shlPfadeBearbeiten.setSize(450, 300);
		shlPfadeBearbeiten.setText("Pfade bearbeiten");
		shlPfadeBearbeiten.setLayout(new FillLayout(SWT.VERTICAL));

	}

}
