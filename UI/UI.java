package UI;

import java.awt.EventQueue;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import java.awt.GridBagConstraints;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.JScrollBar;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import search.*;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.JList;

import java.sql.*;

public class UI implements ActionListener,CaretListener
{

	private JFrame frame;
	public static String query=new String();
	public static Vector<String[]> result=new Vector<>();
	public static int mode=2;
	Connection connect;
	Statement statement;
	private ResultSet rs, resultSet;
	JLabel lblFoundNothing = new JLabel("Found Nothing!");
	JScrollPane scrollPane = null;
	JPanel panel;
	JPanel panel_1;
	//JList<String> list;
	Vector<String> matching=new Vector();
	JScrollPane sp=new JScrollPane();
	private JTextField textField;
	private final JLabel lblcount = new JLabel("Enter search terms");
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// set Nimbus look & feel
		// Reference http://stackoverflow.com/questions/10777357/jlist-filled-with-a-vector-not-showing-anything
		try {
	        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
	            if ("Nimbus".equals(info.getName())) {
	                javax.swing.UIManager.setLookAndFeel(info.getClassName());
	                break;
	            }
	        }
	    } catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
		
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panel = new JPanel();
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getExtendedKeyCode()==KeyEvent.VK_ENTER)
				{
					System.out.println("Search");
					actionPerformed(new ActionEvent(e.getSource(), e.getID(), "Search"));
				}
			}
		});
		panel.setBounds(6, 0, 800, 600);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(242, 138, 1, 2);
		panel.add(separator);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 798, 25);
		panel.add(menuBar);
		menuBar.setFont(new Font("Ubuntu", Font.BOLD, 16));
		
		JMenu mnPreferences = new JMenu("Preferences");
		mnPreferences.setFont(new Font("Ubuntu Light", Font.BOLD, 14));
		menuBar.add(mnPreferences);
		
		JMenuItem mntmManagedIndexedLocations = new JMenuItem("Managed Indexed Locations");
		mnPreferences.add(mntmManagedIndexedLocations);
		
		JMenu mnHelp = new JMenu("Help");
		mnHelp.setFont(new Font("Ubuntu Light", Font.BOLD, 14));
		menuBar.add(mnHelp);
		
		JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(this); 
		
			
		
		searchBtn.setFont(new Font("Ubuntu Medium", Font.PLAIN, 21));
		searchBtn.setBounds(639, 36, 108, 35);
		panel.add(searchBtn);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 219, 800, 8);
		panel.add(separator_1);
		
		JLabel lblSearchOnlyFor = new JLabel("Search only for these types of file:");
		lblSearchOnlyFor.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
		lblSearchOnlyFor.setBounds(228, 122, 299, 18);
		panel.add(lblSearchOnlyFor);
		
		JToggleButton tglbtnDocuments = new JToggleButton("Documents");
		tglbtnDocuments.setBounds(113, 156, 100, 27);
		panel.add(tglbtnDocuments);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("Music");
		tglbtnNewToggleButton.setBounds(225, 156, 100, 27);
		panel.add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("Video");
		tglbtnNewToggleButton_1.setBounds(337, 156, 100, 27);
		panel.add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnCompressedFiles = new JToggleButton("Compressed Files");
		tglbtnCompressedFiles.setBounds(288, 189, 144, 27);
		panel.add(tglbtnCompressedFiles);
		
		JToggleButton tglbtnPictures = new JToggleButton("Pictures");
		tglbtnPictures.setBounds(449, 156, 90, 27);
		panel.add(tglbtnPictures);
		
		JToggleButton tglbtnSoftwares = new JToggleButton("Softwares");
		tglbtnSoftwares.setBounds(551, 156, 90, 27);
		panel.add(tglbtnSoftwares);
		
		JToggleButton tglbtnCddvdImages = new JToggleButton("CD/DVD images");
		tglbtnCddvdImages.setBounds(444, 189, 163, 27);
		panel.add(tglbtnCddvdImages);
		
		lblFoundNothing.setForeground(Color.RED);
		lblFoundNothing.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
		lblFoundNothing.setBounds(313, 87, 124, 15);
		lblFoundNothing.setVisible(false);
		panel.add(lblFoundNothing);
		
		//tabbedPane.add(jlist);
		JScrollBar scrollBar = new JScrollBar();
		//jlist.add(scrollBar);
		JToggleButton tglbtnSourceCodes = new JToggleButton("Source Codes");
		tglbtnSourceCodes.setBounds(132, 189, 144, 27);
		panel.add(tglbtnSourceCodes);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getExtendedKeyCode()==KeyEvent.VK_ENTER)
				{
					System.out.println("Search u idiot!");
					actionPerformed(new ActionEvent(e.getSource(), e.getID(), "Search"));
				}
				//System.out.println("Search u idiot!");
			}
		});
		
		textField.setBounds(116, 37, 525, 35);
		panel.add(textField);
		textField.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setBounds(123, 65, 518, 130);
		panel.add(panel_1);
		lblcount.setFont(new Font("DejaVu Sans", Font.BOLD, 14));
		lblcount.setBounds(0, 552, 259, 18);
		panel.add(lblcount);
		
		
		
		//textField.addCaretListener(this);
		connect();
				
	}
	public void actionPerformed(ActionEvent arg0) 
	{
		//jlist=new JList();
		System.out.println("Source="+arg0.getSource());
		result=new Vector<>();
		if(scrollPane!=null)
		panel.remove(scrollPane);
		lblcount.setText("Press \"Search\" to start searching");
		panel.validate();
		panel.repaint();
		lblFoundNothing.setVisible(false);
		System.out.println("Button works");
		mode=2;
		query=textField.getText();
		if(query.isEmpty()==false)
		{
			result=new Vector<>();
			Search one=new Search();
			one.run();
			//one.close();
			
			/*if(result.isEmpty())
				{
					String[] temp={"Found","Nothing","!"};
					result.add(temp);
				}*/
					//scrollPane.setAlignmentX(LEFT_ALIGNMENT);
			if(!result.isEmpty())
			{
				

				Vector<String> columns=new Vector();
				String names[]={"Filename","Path","Type"};
				columns.add("Filename");
				columns.add("Path");
				columns.add("Type");
				final int n=result.size();
				String[][] t=new String[n][3];
				String[][] data=result.toArray(t);
				
				JTable tab=new JTable(data,names);
				//tab.enableInputMethods(false);
				
				tab.setBounds(0, 219, 770, 340);
				
				scrollPane=new JScrollPane(tab);
				scrollPane.setBounds(0, 219, 800, 335);
				panel.add(scrollPane);
				lblcount.setText("Found "+result.size()+" entries");
				
			}
			else
				lblFoundNothing.setVisible(true);
			result=new Vector();
			

		}
		else
			lblcount.setText("No input!");
		
		
	}
	void connect()
	{
		  try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/files?"
		              + "user=root&password=desh5489");

		      // Statements allow to issue SQL queries to the database
		      statement = connect.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      // Setup the connection with the DB
		  
	      
	}
	@Override
	public void caretUpdate(CaretEvent e) {
		// TODO Auto-generated method stub
		System.out.println(textField.getText());
		try {
			resultSet=statement.executeQuery("select * from log where term like \"%"+textField.getText()+"%\" order by count");
			matching=new Vector();
			
			if(resultSet.wasNull()==false&&resultSet.isClosed()==false)
			{
				
				while(resultSet.isClosed()==false&&resultSet.next())
				{
					matching.add(resultSet.getString("term"));
					statement.executeUpdate("update log set count="+(resultSet.getInt("count")+1)+" where term=\""+resultSet.getString("term")+"\"");
				}
				
			}
			else
				statement.executeUpdate("insert into log values (\""+textField.getText()+"\", 1)");
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JList list=new JList(matching.toArray());
		int ht=20*list.getModel().getSize();
		ht=ht<130?ht:130;
		list.setBounds(123, 65, 518, ht);
		System.out.println(list.getModel().getSize()+"\nl="+list.getSize().height);
		sp=new JScrollPane(list);
		sp.validate();
		sp.repaint();
		//panel_1.setVisible(true);
		panel_1.removeAll();
		panel_1.setLayout(null);
		sp.setBounds(0,0,518,ht);
		panel_1.add(sp);
		
		panel_1.validate();
		panel_1.repaint();
		
	}
}
