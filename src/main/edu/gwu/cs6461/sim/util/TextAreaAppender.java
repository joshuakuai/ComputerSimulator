package edu.gwu.cs6461.sim.util;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 
 * @author iampolo
 * 
 * Simple example of creating a Log4j appender that will
 * write to a JTextArea. 
 */
public class TextAreaAppender extends WriterAppender {
	
	static private JTextArea logtarget = null;
	
	static public void setTextArea(JTextArea jTextArea) {
		TextAreaAppender.logtarget = jTextArea;
	}
	
	@Override
	/**
	 * Format and then append the loggingEvent to the stored
	 * JTextArea.
	 */
	public void append(LoggingEvent loggingEvent) {
		final String message = this.layout.format(loggingEvent);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (logtarget!=null) {
					logtarget.append(message);
				}
			}
		});
	}
}