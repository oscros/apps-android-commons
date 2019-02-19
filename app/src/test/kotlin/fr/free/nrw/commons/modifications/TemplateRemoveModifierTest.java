package fr.free.nrw.commons.modifications;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TemplateRemoveModifierTest{

  @Test
  public void TemplateRemoveModifierTestNoMatch(){
    String template = "template";
    TemplateRemoveModifier trm = new TemplateRemoveModifier(template);
    trm.params = mock(JSONObject.class);
    when(trm.params.optString(anyString())).thenReturn("template");

    String result = trm.doModification("", "hej");
    assertSame(result,"hej");
  }

  @Test
  public void TemplateRemoveModifierTestWhitespaceRemoved(){
    TemplateRemoveModifier trm = new TemplateRemoveModifier("");
    trm.params = mock(JSONObject.class);

    when(trm.params.optString(anyString())).thenReturn("hej  hej");
    String result = trm.doModification("", object.toString());
    assertSame(result, "hej");
  }
}
