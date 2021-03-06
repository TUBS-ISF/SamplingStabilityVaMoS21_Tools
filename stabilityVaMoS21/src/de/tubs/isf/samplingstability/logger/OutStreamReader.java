package de.tubs.isf.samplingstability.logger;

/**
 * Stream that logs every read line with the {@link Logger} as an info.
 * 
 * @author Joshua Sprey
 * @author Sebastian Krieter
 */
public class OutStreamReader implements IOutputReader {

	@Override
	public void readOutput(String line) throws Exception {
		Logger.getInstance().logInfo(line, 1);

	}

}
