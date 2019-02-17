package fr.free.nrw.commons.utils;
import org.junit.Assert.assertTrue;
import org.junit.Assert.assertFalse;
import org.junit.Test;
import android.graphics.Bitmap;
import android.graphics.Color;
import fr.free.nrw.commons.commons.utils.ImageUtils.java;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatcher.any;

public class ImageUtilsTest(){


  @Test
  public checkIfImageIsDarkTestNull(){
    //Edge case which should be handled and return true
    assertTrue(checkIfImageIsDark(null));
  }

  @Test
  public checkIfImageIsDarkTestZeroes(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(0);
    when(bitmap.getHeight()).thenReturn(0);
    assertTrue(checkIfImageIsDark(bitmap));
  }

  @Test
  public checkIfImageIsDarkTestWhiteImage(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(10);
    when(bitmap.getHeight()).thenReturn(10);
    when(bitmap.getPixel(any(), any())).thenReturn(16777215);
    assertFalse(checkIfImageIsDark(bitmap));
  }

  @Test
  public checkIfImageIsDarkTestBlackImage(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(20);
    when(bitmap.getHeight()).thenReturn(20);
    when(bitmap.getPixel(any(), any())).thenReturn(0);
    assertTrue(checkIfImageIsDark(bitmap));
  }
}
