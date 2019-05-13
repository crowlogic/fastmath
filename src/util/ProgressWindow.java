/*
 * Decompiled with CFR 0.144.
 */
package util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.accessibility.AccessibleContext;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

public class ProgressWindow extends JOptionPane {
	private boolean cancelled;
	private static Object[] cancelOption;
	private String note;
	private JProgressBar myBar;
	private JLabel noteLabel;
	private JDialog dialog;

	@Override
	public void setValue(Object newValue) {
		if (newValue instanceof Integer) {
			this.myBar.setValue((Integer) newValue);
		} else if (newValue instanceof String) {
			String cmd = (String) newValue;
			if ("Cancel".equals(cmd)) {
				this.cancelled = true;
			} else {
				System.out.println("unhandled cmd = " + cmd);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ProgressWindow pw = new ProgressWindow("message", "note", 30);
		pw.show(null);
		AtomicInteger count = new AtomicInteger();
		while (!pw.cancelled) {
			Thread.sleep(1000L);
			pw.setValue(count.incrementAndGet());
		}
		pw.close();
	}

	public void close() {
		this.dialog.setVisible(false);
		this.setVisible(false);
	}

	ProgressWindow(String message, String note, JProgressBar myBar) {
		super(new Object[] { message, note, myBar }, 1, -1, null, cancelOption, null);
		cancelOption = new Object[] { UIManager.getString("OptionPane.cancelButtonText") };
		this.cancelled = false;
		this.note = note;
		this.myBar = myBar;
		this.message = message;
	}

	public ProgressWindow(String message, String note, int max) {
		this(message, note, new JProgressBar(0, max));
	}

	public void show(Component parentComponent) {
		if (this.note != null) {
			this.noteLabel = new JLabel(this.note);
		}
		this.dialog = this.createDialog(parentComponent, UIManager.getString("ProgressMonitor.progressText"));
		this.dialog.show();
	}

	@Override
	public int getMaxCharactersPerLineCount() {
		return 60;
	}

	static Window getWindowForComponent(Component parentComponent) throws HeadlessException {
		if (parentComponent == null) {
			return ProgressWindow.getRootFrame();
		}
		if (parentComponent instanceof Frame || parentComponent instanceof Dialog) {
			return (Window) parentComponent;
		}
		return ProgressWindow.getWindowForComponent(parentComponent.getParent());
	}

	@Override
	public JDialog createDialog(Component parentComponent, String title) {
		Window window = ProgressWindow.getWindowForComponent(parentComponent);
		final JDialog dialog = window instanceof Frame ? new JDialog((Frame) window, title, false)
				: new JDialog((Dialog) window, title, false);
		Container contentPane = dialog.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add((Component) this, "Center");
		dialog.pack();
		dialog.setLocationRelativeTo(parentComponent);
		dialog.addWindowListener(new WindowAdapter() {
			boolean gotFocus = false;

			@Override
			public void windowClosing(WindowEvent we) {
				ProgressWindow.this.setValue(cancelOption[0]);
			}

			@Override
			public void windowActivated(WindowEvent we) {
				if (!this.gotFocus) {
					ProgressWindow.this.selectInitialValue();
					this.gotFocus = true;
				}
			}
		});
		this.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (dialog.isVisible() && event.getSource() == ProgressWindow.this
						&& (event.getPropertyName().equals("value") || event.getPropertyName().equals("inputValue"))) {
					dialog.setVisible(false);
					dialog.dispose();
				}
			}
		});
		return dialog;
	}

	@Override
	public AccessibleContext getAccessibleContext() {
		return this.getAccessibleContext();
	}

	private AccessibleContext getAccessibleJOptionPane() {
		return super.getAccessibleContext();
	}

}
