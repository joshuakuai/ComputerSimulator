package edu.gwu.cs6461.sim.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.apache.log4j.Logger;

import edu.gwu.cs6461.sim.util.PropertiesLoader;
import edu.gwu.cs6461.sim.util.PropertiesParser;


/**
 * 
 * Class to provide text completion function to IO input text box
 * 
 * From java Tutorial 
 * Credit to Oracle
 *  
 * @author marcoyeung
 *
 */
public class TextFieldDocListener implements DocumentListener {
	/**log message to file for debugging purpose */
	private final static Logger logger = Logger.getLogger(TextFieldDocListener.class);

	/**record the editor input status*/
	private static enum Mode { INSERT, COMPLETION };

	private JTextField txtField;
	
	/**all the available commands for text completion*/
	private final List<String> commands;
	
	/**initial the mode as insert */
	private Mode mode = Mode.INSERT;
    private static final String COMMIT_ACTION = "commit";

	public TextFieldDocListener(JTextField txtField) {
		this.txtField = txtField;

        InputMap im = txtField.getInputMap();
        ActionMap am = txtField.getActionMap();
        im.put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        am.put(COMMIT_ACTION, new CommitAction());
		
        PropertiesParser prop = PropertiesLoader.getPropertyInstance();
        String [] hints = prop.getPropertyGroups("sim.gui.ioinput.textcompletion");
        commands = new ArrayList<String>(hints.length);
		for (String hint : hints) {
			commands.add(prop.getStringProperty("sim.gui.ioinput.textcompletion."+hint +".hints") );
			
		}
        Collections.sort(commands);
        logger.debug("text completion hints loaded: "+ commands.toString());
        
	}

	@Override
	public void changedUpdate(DocumentEvent ev) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**handle user key stokes and perform text completion*/
	public void insertUpdate(DocumentEvent ev) {
		
		
		if (ev.getLength() != 1) {
			return;
		}

		int pos = ev.getOffset();
		String content = null;
		try {
			content = txtField.getText(0, pos + 1);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// Find where the word starts
		int w;
		for (w = pos; w >= 0; w--) {
			if (! Character.isLetter(content.charAt(w))) {
				break;
			}
		}
		if (pos - w < 2) {
			// Too few chars
			return;
		}

		String prefix = content.substring(w + 1).toLowerCase();
		int n = Collections.binarySearch(commands, prefix);
		if (n < 0 && -n <= commands.size()) {
			String match = commands.get(-n - 1);
			if (match.startsWith(prefix)) {
				String completion = match.substring(pos - w);
				SwingUtilities.invokeLater(
						new CompletionTask(completion, pos + 1));
			}
		} else {
			// Nothing found
			mode = Mode.INSERT;
		}
	}
    
	/** Threaded task calculate the text completion  */
    private class CompletionTask implements Runnable {
        String completion;
        int position;
        
        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }
        
        public void run() {
        	StringBuilder sb = new StringBuilder(txtField.getText());
        	sb.insert(position, completion);
            txtField.setText(sb.toString());//, position);
            txtField.setCaretPosition(position + completion.length());
            txtField.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }
    	
    /** Action class to capture user tab event for text completion */
    private class CommitAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = txtField.getSelectionEnd();
                StringBuilder sb = new StringBuilder(txtField.getText());
                sb.insert(pos, " ");
                txtField.setText(sb.toString());
                txtField.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
            	txtField.replaceSelection("\t");
            }
        }
    }
}
