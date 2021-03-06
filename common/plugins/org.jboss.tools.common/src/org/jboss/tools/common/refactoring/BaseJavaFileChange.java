/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.refactoring;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;
import org.jboss.tools.common.CommonPlugin;

public class BaseJavaFileChange extends CompilationUnitChange {

	public BaseJavaFileChange(String name, ICompilationUnit cunit) {
		super(name, cunit);
		setSaveMode();
	}
	
	private void setSaveMode(){
		UIJob job = new UIJob("setSaveMode"){ //$NON-NLS-1$
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				try {
					if(FileChangeFactory.isOpenInEditor(getFile())){
						setSaveMode(TextFileChange.LEAVE_DIRTY);
					}else{
						setSaveMode(TextFileChange.FORCE_SAVE);
					}
				} catch (PartInitException e) {
					CommonPlugin.getDefault().logError(e);
				}
				return Status.OK_STATUS;
			}};
		
		job.setSystem(true);
		job.schedule();
	}
}
