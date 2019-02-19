package fr.free.nrw.commons.modifications;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TemplateRemoveModifierTest{


  /**
   * makes sure that the method returns the input String
   * if there is no match 
   */
  @Test
  public void TemplateRemoveModifierTestNoMatch(){
    String template = "template";
    TemplateRemoveModifier trm = new TemplateRemoveModifier(template);
    trm.params = mock(JSONObject.class);
    when(trm.params.optString(anyString())).thenReturn("template");

    String result = trm.doModification("", "hej");
    assertSame(result,"hej");
  }
}
