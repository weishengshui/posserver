/**
 * 
 */
package com.chinarewards.qqgbvpn.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;

/**
 * Common with option 'help' should only print usage info.
 * 
 * @author Cream
 * @since 2011-06-23
 */
public class SimpleParser extends GnuParser {

	private boolean shortCircuit = false;

	/**
	 * Only flatten with 'help' options.
	 */
	@Override
	protected String[] flatten(Options options, String[] arguments,
			boolean stopAtNonOption) {
		Iterator<String> iter = Arrays.asList(arguments).iterator();

		List<String> filteredArgs = new ArrayList<String>();
		while (iter.hasNext()) {
			String token = iter.next();
			if (token.startsWith("--help") || token.startsWith("-h")) {
				filteredArgs.add(token);
				shortCircuit = true;
				break;
			} else if (token.startsWith("--version")) {
				filteredArgs.add(token);
				shortCircuit = true;
				break;
			}
		}
		if (shortCircuit) {
			return super.flatten(options, filteredArgs.toArray(new String[0]),
					stopAtNonOption);
		} else {
			return super.flatten(options, arguments, stopAtNonOption);
		}
	}

	@Override
	public void checkRequiredOptions() throws MissingOptionException {
		if (!shortCircuit) {
			super.checkRequiredOptions();
		}
	}

}
