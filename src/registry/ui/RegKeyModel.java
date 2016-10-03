/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package registry.ui;

import com.sun.jna.platform.win32.Win32Exception;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import registry.RegKey;
import registry.RegKeyRoot;

/**
 *
 * @author henry.wang.1
 */
public class RegKeyModel implements TreeModel {
	private static final Logger LOG = Logger.getLogger(RegKeyModel.class.getName());
	
	private static final RegKeyRoot[] ROOT_KEYS = RegKeyRoot.values();
	private static final Object ROOT = new Object();

	private static <R> R tryAction(Supplier<R> action) {
		return tryAction(action, null);
	}

	private static <R> R tryAction(Supplier<R> action, R defaultVal) {
		try {
			return action.get();
		} catch (Win32Exception e) {
			LOG.log(Level.SEVERE, "Error " + (e.getHR().intValue() & 0xFFFF)
				+ "(" + e.getMessage() + ")", e);
			return defaultVal;
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Unexpected error: " + e.getMessage(), e);
			System.exit(1);
			return null;
		}
	}
	
	@Override
	public Object getRoot() {
		return ROOT;
	}

	@Override
	public Object getChild(Object o, int i) {
		if (o == ROOT) {
			return ROOT_KEYS[i];
		} else {
			return tryAction(() -> ((RegKey) o).getSubKeys()[i]);
		}
	}

	@Override
	public int getChildCount(Object o) {
		if (o == ROOT) {
			return ROOT_KEYS.length;
		} else {
			return tryAction(() -> ((RegKey) o).getSubKeys().length, 0);
		}
	}

	@Override
	public boolean isLeaf(Object o) {
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath tp, Object o) {
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		if (parent == ROOT) {
			return ((RegKeyRoot) child).ordinal();
		} else {
			RegKey[] subKeys = tryAction(() -> ((RegKey) parent).getSubKeys());
			for (int i = 0; i < subKeys.length; i++) {
				if (subKeys[i] == child) {
					return i;
				}
			}
			return -1;
		}
	}

	@Override
	public void addTreeModelListener(TreeModelListener tl) {
		//Does nothing yet
	}

	@Override
	public void removeTreeModelListener(TreeModelListener tl) {
		//Does nothing yet
	}
	
}
