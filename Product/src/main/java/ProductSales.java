public class ProductSales {

    public static void main(String[] args) {
        // Example usage
        int[][] productSales = {{100, 200, 150}, {300, 250, 400}, {500, 600, 700}};
        ProductSales sales = new ProductSales();
        
        System.out.println("Total Sales: " + sales.TotalSales(productSales));
        System.out.println("Average Sales: " + sales.AverageSales(productSales));
        System.out.println("Max Sale: " + sales.MaxSale(productSales));
        System.out.println("Min Sale: " + sales.MinSale(productSales));
    }

    public int TotalSales(int[][] productSales) {
        int total = 0;

        // Iterate through the product sales array and sum all sales
        for (int[] year : productSales) {
            for (int sale : year) {
                total += sale;
            }
        }

        return total;
    }

    public double AverageSales(int[][] productSales) {
        int total = TotalSales(productSales);
        int count = 0;

        // Calculate total count of all sales
        for (int[] year : productSales) {
            count += year.length;
        }

        return (double) total / count;
    }

    public int MaxSale(int[][] productSales) {
        int max = productSales[0][0];

        // Iterate through sales and find the maximum sale
        for (int[] year : productSales) {
            for (int sale : year) {
                if (sale > max) {
                    max = sale;
                }
            }
        }

        return max;
    }

    public int MinSale(int[][] productSales) {
        int min = productSales[0][0];

        // Iterate through sales and find the minimum sale
        for (int[] year : productSales) {
            for (int sale : year) {
                if (sale < min) {
                    min = sale;
                }
            }
        }

        return min;
    }
}
