package fr.free.nrw.commons.modifications;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRemoveModifier extends PageModifier {

    public static final String MODIFIER_NAME = "TemplateRemoverModifier";

    public static final String PARAM_TEMPLATE_NAME = "template";

    public static final Pattern PATTERN_TEMPLATE_OPEN = Pattern.compile("\\{\\{");
    public static final Pattern PATTERN_TEMPLATE_CLOSE = Pattern.compile("\\}\\}");

    public TemplateRemoveModifier(String templateName) {
        super(MODIFIER_NAME);
        try {
            params.putOpt(PARAM_TEMPLATE_NAME, templateName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateRemoveModifier(JSONObject data) {
        super(MODIFIER_NAME);
        this.params = data;
    }

    @Override
    public String doModification(String pageName, String pageContents) {
        boolean[] coverage = new boolean[20];
        String templateRawName = params.optString(PARAM_TEMPLATE_NAME);
        // Wikitext title normalizing rules. Spaces and _ equivalent
        // They also 'condense' - any number of them reduce to just one (just like HTML)
        String templateNormalized = templateRawName.trim().replaceAll("(\\s|_)+", "(\\s|_)+");

        // Not supporting {{ inside <nowiki> and HTML comments yet
        // (Thanks to marktraceur for reminding me of the HTML comments exception)
        Pattern templateStartPattern = Pattern.compile("\\{\\{" + templateNormalized, Pattern.CASE_INSENSITIVE);
        Matcher matcher = templateStartPattern.matcher(pageContents);

        while (matcher.find()) {
            coverage[0] = true;
            int braceCount = 1;
            int startIndex = matcher.start();
            int curIndex = matcher.end();
            Matcher openMatch = PATTERN_TEMPLATE_OPEN.matcher(pageContents);
            Matcher closeMatch = PATTERN_TEMPLATE_CLOSE.matcher(pageContents);

            while (curIndex < pageContents.length()) {
                coverage[1] = true;
                boolean openFound = openMatch.find(curIndex);
                boolean closeFound = closeMatch.find(curIndex);

                if (!closeFound) {
                    coverage[2] = true;
                }

                boolean openClose = (openMatch.start() < closeMatch.start());

                if (openClose) {
                  coverage[3]
                }

                if (openFound && (!closeFound || openMatch.start() < closeMatch.start())) {
                    coverage[4] = true;
                    braceCount++;
                    curIndex = openMatch.end();
                } else if (closeFound) {
                    coverage[5] = true;
                    braceCount--;
                    curIndex = closeMatch.end();
                } else if (braceCount > 0) {
                    // The template never closes, so...remove nothing
                    coverage[6] = true;
                    curIndex = startIndex;
                    break;
                } else {
                    coverage[7] = true;
                }

                if (braceCount == 0) {
                    coverage[8] = true;
                    // The braces have all been closed!
                    break;
                } else {
                    coverage[9] = true;
                }
            }

            // Strip trailing whitespace
            while (curIndex < pageContents.length()) {
                coverage[10] = true;
                if (pageContents.charAt(curIndex) == ' ' || pageContents.charAt(curIndex) == '\n') {
                    coverage[11] = true;
                    curIndex++;
                } else {
                    coverage[12] = true;
                    break;
                }
            }

            // I am so going to hell for this, sigh
            pageContents = pageContents.substring(0, startIndex) + pageContents.substring(curIndex);
            matcher = templateStartPattern.matcher(pageContents);
        }
        for (int i = 0; i < coverage.length; i++) {
                System.out.println("branch id: " + i + " taken: " + coverage[i]);
        }
        return pageContents;
    }

    @Override
    public String getEditSummary() {
        return "Removed template " + params.optString(PARAM_TEMPLATE_NAME) + ".";
    }
}
