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
        String templateRawName = params.optString(PARAM_TEMPLATE_NAME);
        // Wikitext title normalizing rules. Spaces and _ equivalent
        // They also 'condense' - any number of them reduce to just one (just like HTML)
        String templateNormalized = templateRawName.trim().replaceAll("(\\s|_)+", "(\\s|_)+");

        // Not supporting {{ inside <nowiki> and HTML comments yet
        // (Thanks to marktraceur for reminding me of the HTML comments exception)
        Pattern templateStartPattern = Pattern.compile("\\{\\{" + templateNormalized, Pattern.CASE_INSENSITIVE);
        Matcher matcher = templateStartPattern.matcher(pageContents);

        while (matcher.find()) {
            int braceCount = 1;
            int startIndex = matcher.start();
            int curIndex = matcher.end();
            Matcher openMatch = PATTERN_TEMPLATE_OPEN.matcher(pageContents);
            Matcher closeMatch = PATTERN_TEMPLATE_CLOSE.matcher(pageContents);

            BraceAndIndex braceAndIndex = new BraceAndIndex();
            braceAndIndex = changeBraceCount(braceCount, curIndex, openMatch, closeMatch, pageContents);

            braceCount = braceAndIndex.getBraceCount();
            curIndex = braceAndIndex.getCurIndex();

            // Strip trailing whitespace
            curIndex = stripWhiteSpace(curIndex, pageContents);

            // I am so going to hell for this, sigh
            pageContents = pageContents.substring(0, startIndex) + pageContents.substring(curIndex);
            matcher = templateStartPattern.matcher(pageContents);
        }

        return pageContents;
    }

    /**
     * Changes braceCount and curIndex depending on input.
     * @param curIndex Current position in the String
     * @param braceCount number of braces
     * @param openMatch matcher at beginnig of string
     * @param closeMatch matcher at end of string
     * @param pageContents string with contents of the page
     * @return braceAndIndex with updated braceCount and curIndex
     */
    public BraceAndIndex changeBraceCount(int braceCount, int curIndex, Matcher openMatch, Matcher closeMatch, String pageContents){
      BraceAndIndex braceAndIndex = new BraceAndIndex();
      while (curIndex < pageContents.length()) {
          boolean openFound = openMatch.find(curIndex);
          boolean closeFound = closeMatch.find(curIndex);

          if (openFound && (!closeFound || openMatch.start() < closeMatch.start())) {
              braceCount++;
              curIndex = openMatch.end();
          } else if (closeFound) {
              braceCount--;
              curIndex = closeMatch.end();
          } else if (braceCount > 0) {
              // The template never closes, so...remove nothing
              curIndex = startIndex;
              break;
          }

          if (braceCount == 0) {
              // The braces have all been closed!
              break;
          }
      }
      braceAndIndex.setCurIndex(curIndex);
      braceAndIndex.setBraceCount(braceCount);
      return braceAndIndex;
    }

    /**
     * Pojo which containts both curIndex and braceCount
     * methods are setters and getters
     */
    public class BraceAndIndex{
      private int curIndex;
      private int braceCount;

      public BraceAndIndex(){
        //Takes no args
      }

      public void setCurIndex(int value){
        this.curIndex = value;
      }

      public void getCurIndex(){
        return this.curIndex;
      }

      public void setBraceCount(int value){
        this.braceCount = value;
      }

      public void getBraceCount(){
        return this.braceCount;
      }
    }


    /**
     * Strips trailing whitespace from pageContents by changing curIndex
     * @param curIndex Current position in the String
     * @param pageContents pageContents to remove whitespace from
     * @return curIndex, ie where the string should end
     */
    public int stripWhiteSpace(int curIndex, String pageContents){
      while (curIndex < pageContents.length()) {
          if (pageContents.charAt(curIndex) == ' ' || pageContents.charAt(curIndex) == '\n') {
              curIndex++;
          } else {
              break;
          }
      }
      return curIndex;
    }

    @Override
    public String getEditSummary() {
        return "Removed template " + params.optString(PARAM_TEMPLATE_NAME) + ".";
    }
}
