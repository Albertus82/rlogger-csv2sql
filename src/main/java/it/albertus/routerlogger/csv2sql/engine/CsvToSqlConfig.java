package it.albertus.routerlogger.csv2sql.engine;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.albertus.routerlogger.csv2sql.resources.Messages;
import it.albertus.util.InitializationException;
import it.albertus.util.SystemUtils;
import it.albertus.util.config.LanguageConfig;
import it.albertus.util.config.LoggingConfig;
import it.albertus.util.logging.LoggerFactory;

public class CsvToSqlConfig extends LoggingConfig implements LanguageConfig {

	private static final String DIRECTORY_NAME = "RouterLogger" + File.separator + "CSV2SQL";

	public static final String DEFAULT_LOGGING_FILES_PATH = SystemUtils.getOsSpecificLocalAppDataDir() + File.separator + DIRECTORY_NAME;

	private static final String CFG_FILE_NAME = "csv2sql.cfg";
	private static final String LOG_FILE_NAME_PATTERN = "csv2sql.%g.log";

	private static final Logger logger = LoggerFactory.getLogger(CsvToSqlConfig.class);

	private static CsvToSqlConfig instance;

	public static synchronized CsvToSqlConfig getInstance() {
		if (instance == null) {
			try {
				instance = new CsvToSqlConfig();
			}
			catch (final IOException e) {
				final String message = Messages.get("err.open.cfg", CFG_FILE_NAME);
				logger.log(Level.SEVERE, message, e);
				throw new InitializationException(message, e);
			}
		}
		return instance;
	}

	private CsvToSqlConfig() throws IOException {
		super(DIRECTORY_NAME + File.separator + CFG_FILE_NAME, true);
		init();
	}

	@Override
	protected void init() {
		super.init();
		updateLanguage();
	}

	@Override
	public void updateLanguage() {
		final String language = getString("language", Messages.DEFAULT_LANGUAGE);
		Messages.setLanguage(language);
	}

	@Override
	protected boolean isFileHandlerEnabled() {
		return getBoolean("logging.files.enabled", super.isFileHandlerEnabled());
	}

	@Override
	protected String getFileHandlerPattern() {
		return getString("logging.files.path", DEFAULT_LOGGING_FILES_PATH) + File.separator + LOG_FILE_NAME_PATTERN;
	}

	@Override
	protected int getFileHandlerLimit() {
		final Integer limit = getInt("logging.files.limit");
		if (limit != null) {
			return limit * 1024;
		}
		else {
			return super.getFileHandlerLimit();
		}
	}

	@Override
	protected int getFileHandlerCount() {
		return getInt("logging.files.count", super.getFileHandlerCount());
	}

	@Override
	protected String getLoggingLevel() {
		return getString("logging.level", super.getLoggingLevel());
	}

}
