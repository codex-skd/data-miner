package com.skd.dataminer.logs.issues;

import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IssueDetector {
    Pattern pattern();
    String type();
    JsonObject extract(String loggerName, String message, String thread);
}
