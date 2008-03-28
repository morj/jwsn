package ru.amse.nikitin.ui.gui.impl;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ru.amse.nikitin.ui.gui.Const;
import ru.amse.nikitin.ui.gui.ITool;

class MyCellRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 239;
	
	protected static final int dd = 0;
	
	protected final static ImageIcon[] deselectedIcons = {
    	new ImageIcon("icons\\tool_inspect.png"),
    	new ImageIcon("icons\\tool_edit.png")
    };
    
	/* protected final static ImageIcon[] selectedIcons = {
    	new ImageIcon("icons\\palm2_vs.gif"),
    	new ImageIcon("icons\\palm2_vs.gif")
    }; */
    
	protected final static String[] names = {
    	"Inspect",
    	"Edit"
    };
	
    public MyCellRenderer() {
		super();
		setBorder(BorderFactory.createEtchedBorder());
		setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getListCellRendererComponent(
      JList list,              // the list
      Object value,            // value to display
      int index,               // cell index
      boolean isSelected,      // is the cell selected
      boolean cellHasFocus)    // does the cell have focus
    {
    	// setText(names[index]);
    	setIcon(deselectedIcons[index]);
    	setToolTipText(names[index]);
        if (isSelected) {
        	// setIcon(selectedIcons[index]);
        	// setBackground(Color.BLUE);
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
        	// setIcon(deselectedIcons[index]);
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        } /**/
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}

public class ToolBox {
	protected DisplayComponent displayComponent;
	protected JList jList;
	
	protected final ITool[] tools = {
		new InspectTool(),
		new EditTool(),
	};
	
	protected final ListSelectionListener selector = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				displayComponent.setMouseTool(tools[jList.getSelectedIndex()]);
			}
		}
	};

	public ToolBox(DisplayComponent displayComponent) {
		this.displayComponent = displayComponent;
		jList = new JList(tools);
		jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jList.addListSelectionListener(selector);
		jList.setCellRenderer(new MyCellRenderer());
		jList.setFixedCellHeight(Const.TOOLBOX_ELEMENT_HEIGHT);
		jList.setFixedCellWidth(Const.TOOLBOX_ELEMENT_WIDTH);
		jList.setSelectedIndex(1);
		displayComponent.setMouseTool(tools[1]);
	}

	public JList getJList() {
		return jList;
	}
	
}
