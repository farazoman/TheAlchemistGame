package mapMaker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class StartDialog extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6648394164566411303L;
	private JPanel jp_Frame, jp_Menu, jp_End;
	private JLabel jl_Label, jl_Comm;
	private JLabel jl_Rows, jl_Cols, jl_PixW;
	private JTextField tf_rows, tf_cols, tf_pixW;
	private JButton jb_Create;
	private int rows = 30, cols = 30, w = 5, h = 5;
	protected boolean isDone;
	
	public StartDialog(){ //this just sets up the different components for the dialog box
		isDone = false;
		
		jp_Frame = new JPanel(new BorderLayout());
		jp_Menu = new JPanel();
		jp_End = new JPanel(new BorderLayout());
		jl_Label = new JLabel("Welcome to Faraz's map maker program!");
		jl_Label.setHorizontalAlignment(SwingConstants.CENTER);
		
		jl_Comm = new JLabel("Choose the dimensions of the map below");
		
		tf_rows = new JTextField("30", 3);
		tf_cols = new JTextField("30", 3);
		tf_pixW = new JTextField("5", 3);
		
		jl_Rows = new JLabel("Rows: ");
		jl_Cols = new JLabel("Columns: ");
		jl_PixW = new JLabel("Block Size (in Pixels): ");
		
		jb_Create = new JButton("Create");
		//jb_Create.setEnabled(false);
		
		jp_Menu.add(jl_Comm);
		jp_Menu.add(jl_Rows);
		jp_Menu.add(tf_rows);
		jp_Menu.add(jl_Cols);
		jp_Menu.add(tf_cols);
		jp_Menu.add(jl_PixW);
		jp_Menu.add(tf_pixW);
		
		jp_End.add(jb_Create, BorderLayout.CENTER);

		jp_Frame.add(jl_Label,BorderLayout.NORTH);
		jp_Frame.add(jp_Menu, BorderLayout.CENTER);
		jp_Frame.add(jp_End, BorderLayout.SOUTH);
		
		setContentPane(jp_Frame);
		
		addActions();
		
		setSize(new Dimension(300,200));
		setResizable(false);

		setTitle ("Map Creator");
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo (null); 
		setVisible(true);
		
	}
	
	private void addActions(){
		
		jb_Create.addActionListener (new ActionListener ()
		{
		    public void actionPerformed (ActionEvent e)
		    {
		    	if(checkFields()){
			    	isDone = true;
			    	System.out.print("Button");
			    }
		    }
		}  );
		
		tf_rows.addFocusListener(new FocusListener ()
		{
		    public void focusLost (FocusEvent e)
		    {
			int temp = rows;
			
			try{
			    temp = (Integer.parseInt(tf_rows.getText()));//changes the text into a number
			}
			catch(Exception b){
			    temp = rows; //if error occurs then the temp value will equal the current global value number
			}

			    if(temp < 1){ //if user entered a number less than one hen the text is set to one and the temp is set to 0
				    temp = 1;
				    tf_rows.setText("1");
				}
			    if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
			    
			rows = temp; //changes global variable to the temp value		    
		     }

			@Override
			public void focusGained(FocusEvent arg0) {
				tf_rows.selectAll();
				if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
				
			}
		});
	   
		tf_cols.addFocusListener(new FocusListener ()
		{
		    public void focusLost (FocusEvent e)
		    {
			int temp = cols;
			
			try{
			    temp = (Integer.parseInt(tf_cols.getText()));//changes the text into a number
			}
			catch(Exception b){
			    temp = cols; //if error occurs then the temp value will equal the current global value number
			}

			    if(temp < 1){ //if user entered a number less than one hen the text is set to one and the temp is set to 0
				    temp = 1;
				    tf_rows.setText("1");
				}
			    if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
			    
			    
			cols = temp; //changes global variable to the temp value		    
		     }

			@Override
			public void focusGained(FocusEvent e) {
				tf_cols.selectAll();
				if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
				
			}
		});
		
		
		
		tf_pixW.addFocusListener(new FocusListener ()
		{
		    public void focusLost (FocusEvent e)
		    {
			int temp = h;
			
			try{
			    temp = (Integer.parseInt(tf_pixW.getText()));//changes the text into a number
			}
			catch(Exception b){
			    temp = h; //if error occurs then the temp value will equal the current global value number
			}

			    if(temp < 5){ //if user entered a number less than one hen the text is set to one and the temp is set to 0
				    temp = 5;
				    tf_pixW.setText("5");
				}
			    if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
			    
			    
			h = temp; //changes global variable to the temp value		
			w = temp;
		     }

			@Override
			public void focusGained(FocusEvent e) {
				tf_pixW.selectAll();
				if(checkFields()){
			    	jb_Create.setEnabled(true);
			    }
				
			}
		});
	}
	
	public boolean isVizible(){
		return this.isVisible();
	}
	
	public boolean checkFields(){
		return (rows != 0 && cols != 0 && w >= 5 && h >= 5);
	}

	public int getRow(){
		return rows;
	}
	
	public int getCol(){
		return cols;
	}
	public int getH(){
		return h;
	}
	public int getW(){
		return w;
	}
	public boolean isDone(){
		return isDone;
	}
}
	
		
	

