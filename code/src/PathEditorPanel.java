import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PathEditorPanel extends JPanel
{
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
		for (int i = 0; i < items.length; i++)
		{
			System.out.println(items[i]);
		}
		for (int i = items.length - 1; i >= 0; i--)
		{
			System.out.println("delete " + items[i]);
			model.remove(items[i]);
			paths.remove(items[i]);	
		}
	}
}
