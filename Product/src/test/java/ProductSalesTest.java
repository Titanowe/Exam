import static org.junit.Assert.*;
import org.junit.Test;

public class ProductSalesTest {

    private ProductSales ps;
    private int[][] salesData;

    @Before
    public void setUp() {
        // Initialization of  the ProductSales instance and sales data
        ps = new ProductSales();
        salesData = new int[][] {
            {300, 150, 700},  // Year 1 sales
            {250, 200, 600}   // Year 2 sales
        };
    }

    // Test for CalculateTotalSales_ReturnsTotalSales
    @Test
    public void CalculateTotalSales_ReturnsTotalSales() {
        // Expected total sales (300 + 150 + 700 + 250 + 200 + 600 = 2200)
        int expected = 2200;
        // Call the TotalSales method and get the actual result
        int actual = ps.TotalSales(salesData);
        // Assert that the expected result matches the actual result
        assertEquals("Total sales calculation is incorrect", expected, actual);
    }

    // Test for AverageSales_ReturnsAverageProductSales
    @Test
    public void AverageSales_ReturnsAverageProductSales() {
        
        double expected = 366.67;
        // Call the AverageSales method and get the actual result
        double actual = ps.AverageSales(salesData);
        // Assert that the expected result matches the actual result with a margin of error
        assertEquals("Average sales calculation is incorrect", expected, actual, 0.01);  // 0.01 is an acceptable margin of error
    }
}
