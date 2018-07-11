package com.github.hi_fi.httprequestlibrary.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.python.util.PythonInterpreter;

public class RobotLogger implements Log {

    public enum Level {
        ALL(0), TRACE(1), DEBUG(2), INFO(3), WARN(4), ERROR(5), FATAL(6), NONE(7);
        private int order;

        Level(int order) {
            this.order = order;
        }

        public int getLevel() {
            return this.order;
        }

    }

    /** The name of this simple log instance */
    protected volatile String logName = null;
    /** The current log level */
    protected static volatile Level currentLogLevel;

    protected static volatile Boolean debugOverride;

    static protected final String systemPrefix = "org.apache.commons.logging.robotlogger.";

    public RobotLogger(String name) {
        logName = name;

        // Set initial log level
        // Used to be: set default log level to ERROR
        // IMHO it should be lower, but at least info ( costin ).
        setLevel(Level.INFO);

        // Set log level from properties
        String lvl = getStringProperty(systemPrefix + "log." + logName);
        int i = String.valueOf(name).lastIndexOf(".");
        while (null == lvl && i > -1) {
            name = name.substring(0, i);
            lvl = getStringProperty(systemPrefix + "log." + name);
            i = String.valueOf(name).lastIndexOf(".");
        }

        try {
            lvl = Robot.getRobotVariable("LOG LEVEL", "INFO");
        } catch (Exception e) {
            // Not doing anything. Meaning logging is done with default level set from
            // startup parameter or from tests before class initialization
        }
        if (null == lvl) {
            lvl = getStringProperty(systemPrefix + "defaultlog");
            lvl = lvl == null ? "INFO" : lvl;
        }
        setLevel(Level.valueOf(lvl.toUpperCase()));
        this.debug("Enabled logger for: " + logName + " with level: " + lvl);
    }

    public static void logHTML(Object log) {
        pythonInterpreter.get().eval("logger.info(repr('" + convertStringToLogger(log) + "'), html=true)");
    }

    public void debug(Object log) {
        if (this.isDebugEnabled()) {
            try {
                pythonInterpreter.get().eval("logger.debug('" + convertStringToLogger(log) + "')");
            } catch (Exception e) {
                //Python logger fails with e.g. Chinese characters, so this done as fallback
                System.out.println("*DEBUG* "+log);
            }
        }
    }

    public void debug(Object message, Throwable t) {
        this.debug(message);
        this.debug(ExceptionUtils.getStackTrace(t));
    }

    public void error(Object log) {
        pythonInterpreter.get().eval("logger.error(repr('" + convertStringToLogger(log) + "')");
    }

    public void error(Object message, Throwable t) {
        this.error(message);
        this.error(ExceptionUtils.getStackTrace(t));
    }

    public void fatal(Object message) {
        this.error(message);
    }

    public void fatal(Object message, Throwable t) {
        this.error(message, t);
    }

    public void info(Object log) {
        pythonInterpreter.get().eval("logger.info(repr('" + convertStringToLogger(log) + "')");
    }

    public void info(Object message, Throwable t) {
        this.info(message);
        this.info(ExceptionUtils.getStackTrace(t));
    }

    public void trace(Object log) {
        pythonInterpreter.get().eval("logger.trace(repr('" + convertStringToLogger(log) + "')");
    }

    public void trace(Object message, Throwable t) {
        this.trace(message);
        this.trace(ExceptionUtils.getStackTrace(t));
    }

    public void warn(Object log) {
        pythonInterpreter.get().eval("logger.warn(repr('" + convertStringToLogger(log) + "')");
    }

    public void warn(Object message, Throwable t) {
        this.warn(message);
        this.warn(ExceptionUtils.getStackTrace(t));

    }

    private static String getStringProperty(String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        } catch (SecurityException e) {
            // Ignore
        }
        return prop;
    }

    public void setLevel(Level currentLogLevel) {
        RobotLogger.currentLogLevel = currentLogLevel;
    }

    protected static ThreadLocal<PythonInterpreter> pythonInterpreter = new ThreadLocal<PythonInterpreter>() {

        @Override
        protected PythonInterpreter initialValue() {
            PythonInterpreter pythonInterpreter = new PythonInterpreter();
            pythonInterpreter.exec("from robot.api import logger");
            return pythonInterpreter;
        }
    };

    public static void setDebugToAll(Boolean debug) {
        RobotLogger.debugOverride = debug;
    }

    public boolean isDebugEnabled() {
        try {
            return RobotLogger.debugOverride || currentLogLevel.getLevel() <= Level.DEBUG.getLevel();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorEnabled() {
        return currentLogLevel.getLevel() <= Level.ERROR.getLevel();
    }

    public boolean isFatalEnabled() {
        return currentLogLevel.getLevel() <= Level.FATAL.getLevel();
    }

    public boolean isInfoEnabled() {
        return currentLogLevel.getLevel() <= Level.INFO.getLevel();
    }

    public boolean isTraceEnabled() {
        return currentLogLevel.getLevel() <= Level.TRACE.getLevel();
    }

    public boolean isWarnEnabled() {
        return currentLogLevel.getLevel() <= Level.WARN.getLevel();
    }
    
    private static String convertStringToLogger(Object log) {
        return log.toString().replace("'", "\\\'").replace("\\", "\\\\");
    }
    

}
