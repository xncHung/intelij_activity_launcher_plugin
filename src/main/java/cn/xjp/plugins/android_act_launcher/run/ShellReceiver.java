package cn.xjp.plugins.android_act_launcher.run;

import org.jetbrains.android.util.AndroidOutputReceiver;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellReceiver extends AndroidOutputReceiver {
    public static final int NO_ERROR = -2;
    public static final int UNTYPED_ERROR = -1;
    private static final Pattern FAILURE = Pattern.compile("Failure\\s+\\[(.*)\\]");
    private static final Pattern TYPED_ERROR = Pattern.compile("Error\\s+[Tt]ype\\s+(\\d+).*");
    private static final String ERROR_PREFIX = "Error";
    private String failureMessage;
    private int errorType;
    private final StringBuilder output;

    public ShellReceiver() {
        this.output = new StringBuilder();
    }

    @Override
    protected void processNewLine(@NotNull String line) {
        if (!line.isEmpty()) {
            Matcher failureMatcher = FAILURE.matcher(line);
            if (failureMatcher.matches()) {
                this.failureMessage = failureMatcher.group(1);
            }

            Matcher errorMatcher = TYPED_ERROR.matcher(line);
            if (errorMatcher.matches()) {
                this.errorType = Integer.parseInt(errorMatcher.group(1));
                this.failureMessage = line;
            } else if (line.startsWith("Error") && this.errorType == -2) {
                this.errorType = -1;
                this.failureMessage = line;
            }
        }

        this.output.append(line).append('\n');
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    public boolean isSuccess() {
        return errorType != NO_ERROR;
    }

    public String getErrorMsg() {
        return failureMessage;
    }
}
