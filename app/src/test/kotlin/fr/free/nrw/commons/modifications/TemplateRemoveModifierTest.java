package fr.free.nrw.commons.modifications;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;
import fr.free.nrw.commons.commons.modifications.TemplateRemoveModifier;


public class TemplateRemoveModifierTest{

  @Test
  public void TemplateRemoveModifierTestNoMatch{
    TemplateRemoveModifier trm = new TemplateRemoveModifier("template");
    String result = trm.doModification("", "hej");
    assertTrue(result == "hej");
  }

  @Test
  public void TemplateRemoveModifierTestWhitespaceRemoved{
    TemplateRemoveModifier trm = new TemplateRemoveModifier("template");
    String result = trm.doModification("", "template  ");
    assertNotSame(result, "template  ");
  }
}
