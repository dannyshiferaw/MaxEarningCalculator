
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class test MaxEarning
 */
public class TestMaxEarning {
    @Test
    public void testMaxEarning() {
        MaxEarning maxEarning = new MaxEarning("input.txt");
        assertEquals(30, maxEarning.calcMaxEarning());
    }
}
