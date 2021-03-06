/*******************************************************************************
 * Copyright (c) 2019 Red Hat Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Mickael Istria (Red Hat Inc.) - initial implementation
 *******************************************************************************/
package org.eclipse.wildwebdeveloper.debug;

import java.io.IOException;
import java.util.Collections;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.lsp4e.debug.DSPPlugin;
import org.eclipse.lsp4e.debug.launcher.DSPLaunchDelegate;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wildwebdeveloper.Activator;
import org.eclipse.wildwebdeveloper.InitializeLaunchConfigurations;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NodeAttachDebugDelegate extends DSPLaunchDelegate {

	static final String ID = "org.eclipse.wildwebdeveloper.launchConfiguration.nodeAttach"; //$NON-NLS-1$

	// see https://github.com/Microsoft/vscode-node-debug/blob/master/src/node/nodeDebug.ts LaunchRequestArguments
	static final String ADDRESS = "address"; //$NON-NLS-1$
	static final String PORT = "port"; //$NON-NLS-1$

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		ILaunchConfigurationWorkingCopy wc = configuration.getWorkingCopy();
		// user settings
		JsonObject param = new JsonObject();
		param.addProperty(ADDRESS, wc.getAttribute(ADDRESS, "no address defined")); //$NON-NLS-1$
		param.addProperty(PORT, wc.getAttribute(PORT, -1));
		wc.setAttribute(DSPPlugin.ATTR_DSP_PARAM, new Gson().toJson(param));
		wc.setAttribute(DSPPlugin.ATTR_DSP_MODE, DSPPlugin.DSP_MODE_LAUNCH); // we LAUNCH the adapter then CONNECTS to node
		wc.setAttribute(DSPPlugin.ATTR_DSP_MONITOR_DEBUG_ADAPTER, true);
		wc.setAttribute(DSPPlugin.ATTR_DSP_CMD, InitializeLaunchConfigurations.getNodeJsLocation());
		try {
			wc.setAttribute(DSPPlugin.ATTR_DSP_ARGS, Collections.singletonList(FileLocator.toFileURL(getClass().getResource("/language-servers/node_modules/node-debug2/out/src/nodeDebug.js")).getPath())); //$NON-NLS-1$
			configuration = wc.doSave();
			super.launch(configuration, mode, launch, monitor);
		} catch (IOException e) {
			IStatus errorStatus = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			Activator.getDefault().getLog().log(errorStatus);
			ErrorDialog.openError(Display.getDefault().getActiveShell(), "Debug error", e.getMessage(), errorStatus); //$NON-NLS-1$
		}

	}

}
