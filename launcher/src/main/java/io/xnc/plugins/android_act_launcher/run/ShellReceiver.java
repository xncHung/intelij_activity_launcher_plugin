package io.xnc.plugins.android_act_launcher.run;

import io.xnc.plugins.android_act_launcher.util.NotificationUtil;
import org.jetbrains.android.util.AndroidOutputReceiver;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShellReceiver extends AndroidOutputReceiver {
    public static final int NO_ERROR = -2;
    public static final int WARNING_CODE = -3;
    public static final int UNTYPED_ERROR = -1;
    private static final Pattern FAILURE = Pattern.compile("Failure\\s+(.*)");
    private static final Pattern WARNING = Pattern.compile("Warning:(.*)");
    private static final Pattern TYPED_ERROR = Pattern.compile("Error\\s+[Tt]ype\\s+(\\d+).*");
    private static final String ERROR_PREFIX = "Error";
    private String failureMessage;
    private int errorType=NO_ERROR;
    private final StringBuilder output;
    private String warningMsg;

    ShellReceiver() {
        this.output = new StringBuilder();
    }

    public int getErrorType() {
        return errorType;
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
            } else if (line.startsWith(ERROR_PREFIX) && this.errorType == NO_ERROR) {
                this.errorType = UNTYPED_ERROR;
                this.failureMessage = line;
            }
            Matcher matcher = WARNING.matcher(line);
            if(matcher.matches()){
                warningMsg = matcher.group(1);
                errorType=WARNING_CODE;
            }
        }

        this.output.append(line).append('\n');
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    boolean isSuccess() {
        return errorType == NO_ERROR;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    String getErrorMsg() {
        return failureMessage;
    }
}
