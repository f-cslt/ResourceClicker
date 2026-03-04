package GameProject.test.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utils.Array2D;

public class Array2DTest {
    @Test
    public void basic() {
        int rows = 160;
        int cols = 120;
        Array2D<Integer> arr = new Array2D<>(rows, cols);

        Integer cnt = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr.set(i, j, cnt++);
            }
        } 

        cnt = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                assertEquals(cnt++, arr.get(i, j));
            }
        } 
    }
}
