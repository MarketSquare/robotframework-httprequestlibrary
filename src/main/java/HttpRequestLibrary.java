import org.robotframework.javalib.library.AnnotationLibrary;

public class HttpRequestLibrary extends AnnotationLibrary {

	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	public HttpRequestLibrary() {
		super("com/github/hi_fi/httprequestlibrary/keywords/**");
		System.setProperty("org.apache.commons.logging.Log", "com.github.hi_fi.httprequestlibrary.utils.RobotLogger");
	}

	@Override
	public String getKeywordDocumentation(String keywordName) {
		if (keywordName.equals("__intro__"))
			return "``HttpRequestLibrary`` is a [http://code.google.com/p/robotframework/|Robot Framework] test library that uses the [https://hc.apache.org/index.html|Apache HTTP client].";
		return super.getKeywordDocumentation(keywordName);
	}
}