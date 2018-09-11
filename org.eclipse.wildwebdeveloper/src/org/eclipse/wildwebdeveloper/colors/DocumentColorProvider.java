/**
 * Copyright (c) 2017 Angelo ZERR.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.wildwebdeveloper.colors;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.lsp4j.Range;

/**
 * Language server API to collect color symbols for a given text document.
 *
 */
public interface DocumentColorProvider {

	/**
	 * The request is sent from the client to the server to collect ranges which
	 * defined color symbols for the given text document.
	 * 
	 * @param uri
	 *            text document URI.
	 * @return list of ranges which defined color symbols for the given text
	 *         document.
	 */
	CompletableFuture<List<Range>> findDocumentColors(URI uri);
}