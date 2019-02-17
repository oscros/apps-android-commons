package fr.free.nrw.commons.utils;
import org.junit.Assert.assertEquals;
import org.junit.Test;
import fr.free.nrw.commons.commons.utils.ImageUtils.java;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageUtilsTest(){

  @Test
  public testCheckIfImageIsDark(){
    //Edge case which should be handled and return true
    assertTrue(checkIfImageIsDark(null));

    Bitmap bitmap = mock(Bitmap.class);
    
  }
}
