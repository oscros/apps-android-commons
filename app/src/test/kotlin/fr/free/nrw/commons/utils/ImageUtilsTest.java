package fr.free.nrw.commons.utils;
import android.graphics.Bitmap;

import org.junit.Test;

import static fr.free.nrw.commons.utils.ImageUtils.checkIfImageIsDark;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImageUtilsTest{


  @Test
  public void checkIfImageIsDarkTestNull(){
    //Edge case which should be handled and return true
    assertTrue(checkIfImageIsDark(null));
  }

  @Test
  public void checkIfImageIsDarkTestZeroes(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(0);
    when(bitmap.getHeight()).thenReturn(0);
    assertTrue(checkIfImageIsDark(bitmap));
  }

  @Test
  public void checkIfImageIsDarkTestWhiteImage(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(10);
    when(bitmap.getHeight()).thenReturn(10);
    when(bitmap.getPixel(anyInt(), anyInt())).thenReturn(-1);
    assertFalse(checkIfImageIsDark(bitmap));
  }

  @Test
  public void checkIfImageIsDarkTestBlackImage(){
    Bitmap bitmap = mock(Bitmap.class);
    when(bitmap.getWidth()).thenReturn(20);
    when(bitmap.getHeight()).thenReturn(20);
    when(bitmap.getPixel(anyInt(), anyInt())).thenReturn(-16777216);
    assertTrue(checkIfImageIsDark(bitmap));
  }
}
