import java.security.InvalidParameterException;
import java.util.Random;

public class Matrix {
    private final int rows;
    private final int columns;
    private final double[][] matrix;

    /**
     * Default Matrix Constructor.
     *
     * @param n_rows    (Matrix number of rows).
     * @param n_columns (Matrix number of columns).
     */
    protected Matrix(int n_rows, int n_columns) {
        this.rows = Math.abs(n_rows);
        this.columns = Math.abs(n_columns);
        this.matrix = new double[rows][columns];

        for (int i = 0; i < this.rows; i++) //Init (n) Lists.
            for (int j = 0; j < this.columns; j++) {
                this.matrix[i][j] = 0.0;
            }
    }

    /**
     * Matrix Constructor.
     *
     * @param n_rows    (Matrix number of rows).
     * @param n_columns (Matrix number of columns).
     * @param B         (2D double array)
     */
    protected Matrix(int n_rows, int n_columns, double[][] B) throws IndexOutOfBoundsException {
        this.rows = Math.abs(n_rows);
        this.columns = Math.abs(n_columns);
        this.matrix = new double[rows][columns];

        if (B.length != n_rows || B[0].length != n_columns)
            throw new IndexOutOfBoundsException(String.format("Matrix Creation Error: given dimensions (%s, %s) are different from given Matrix (%s, %s)", n_rows, n_columns, B.length, B[0].length));
        for (int i = 0; i < this.rows; i++) //Init (n) Lists.
            System.arraycopy(B[i], 0, this.matrix[i], 0, this.columns);
    }

//    /**
//     * Vector Constructor.
//     *
//     * @param n_rows    (Matrix number of rows).
//     * @param n_columns (Matrix number of columns).
//     * @param V         (1D double array)
//     */
//    protected Matrix(int n_rows, int n_columns, double[] V) {
//        this.rows = Math.abs(n_rows);
//        this.columns = Math.abs(n_columns);
//        this.matrix = new double[rows][columns];
//
//        if (B.length != n_rows || B[0].length != n_columns)
//            throw new IndexOutOfBoundsException(String.format("Given dimensions (%s, %s) are different from given Matrix (%s, %s)", n_rows, n_columns, B.length, B[0].length));
//
//        for (int i = 0; i < this.rows; i++) { //Init (n) Lists.
//            for (int j = 0; j < this.columns; j++) {
//                if (this.rows > this.columns)
//                    this.matrix[i][j] = V[i];
//                else if (this.columns > this.rows)
//                    this.matrix[i][j] = V[j];
//                else
//                    this.matrix[i][j] = V[i];
//            }
//        }
//    }

    /**
     * Copy Constructor.
     *
     * @param B (Matrix object.)
     */
    protected Matrix(Matrix B) {
        this.rows = B.rows;
        this.columns = B.columns;
        this.matrix = new double[rows][columns];
        for (int i = 0; i < B.rows; i++) {
            for (int j = 0; j < B.columns; j++) {
                this.setValue(i, j, B.getValue(i, j));
            }
        }
    }

    /**
     * Get number of rows of Matrix.
     *
     * @return (int) number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get number of columns of Matrix.
     *
     * @return (int) number of columns.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Set value into specific cell in the Matrix.
     *
     * @param row    (Matrix row number).
     * @param column (Matrix column number).
     * @param value  (double) value to insert.
     */
    protected void setValue(int row, int column, double value) throws IndexOutOfBoundsException {
        if (!(row > this.rows) && !(column > this.columns))
            this.matrix[row][column] = value;
        else
            throw new IndexOutOfBoundsException("row/column values are out of range.");

    }

    /**
     * Retrieve value from specific cell.
     *
     * @param row    (Matrix row number).
     * @param column (Matrix column number).
     * @return (double) value of specific Matrix cell.
     */
    protected double getValue(int row, int column) throws IndexOutOfBoundsException {
        if (!(row > this.rows) && !(column > this.columns))
            return this.matrix[row][column];
        throw new IndexOutOfBoundsException("row/column values are out of range.");
    }

    /**
     * Multiply current Matrix with given Matrix (B).
     *
     * @param B (Matrix object).
     * @return new Matrix after multiplication.
     */
    protected Matrix dot(Matrix B) throws IndexOutOfBoundsException {
        if (this.columns != B.rows)
            throw new IndexOutOfBoundsException(String.format("Cannot Product (%s,%s) By (%s,%s)", this.rows, this.columns, B.rows, B.columns));

        Matrix temp = new Matrix(this.rows, B.columns);
        double cell;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < B.columns; j++) {
                cell = 0.0;
                for (int k = 0; k < this.columns; k++) {
                    cell += this.getValue(i, k) * B.getValue(k, j);
                }
                temp.setValue(i, j, cell);
            }
        }
        return temp;
    }

    /**
     * Multiply (num) value to each cell of the Matrix.
     *
     * @param num (double value)
     * @return Matrix with updated values.
     */
    protected Matrix multiply(double num) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] *= num;
        }
        return this;
    }

    /**
     * Multiply each value to each cell of the Matrices.
     *
     * @param B (Matrix object)
     * @return Matrix with updated values.
     */
    protected Matrix multiply(Matrix B) {
        if (this.rows != B.rows || this.columns != B.columns)
            throw new IndexOutOfBoundsException(String.format("Cannot Multiply (%s,%s) By (%s,%s)", this.rows, this.columns, B.rows, B.columns));
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] *= B.matrix[i][j];
        }
        return this;
    }

    /**
     * Add values from given Matrix (B) to the current Matrix.
     *
     * @param B (Matrix object).
     * @return Modified Matrix after addition.
     */
    protected Matrix add(Matrix B) throws IndexOutOfBoundsException {
        if (this.rows != B.rows || this.columns != B.columns)
            throw new IndexOutOfBoundsException(String.format("Matrices has different dimensions (%s,%s) By (%s,%s)", this.rows, this.columns, B.rows, B.columns));

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] += B.matrix[i][j];
        }
        return this;
    }

    /**
     * add (num) value to each cell of the Matrix.
     *
     * @param num (double value)
     * @return Matrix with updated values.
     */
    protected Matrix add(double num) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] += num;
        }
        return this;
    }

    /**
     * subtract (num) value to each cell of the Matrix.
     *
     * @param num (double value)
     * @return Matrix with updated values.
     */
    protected Matrix subtract(double num) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] -= num;
        }
        return this;
    }

    /**
     * Subtract values from given Matrix (B) to the current Matrix.
     *
     * @param B (Matrix object).
     * @return Modified Matrix after subtraction.
     */
    protected Matrix subtract(Matrix B) throws IndexOutOfBoundsException {
        if (B.rows == this.rows && B.columns == 1) { // if 1D array
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++)
                    this.matrix[i][j] -= B.matrix[i][0];
            }
            return this;
        }
        if (this.rows != B.rows || this.columns != B.columns)
            throw new IndexOutOfBoundsException(String.format("Matrices has different dimensions (%s,%s) By (%s,%s)", this.rows, this.columns, B.rows, B.columns));

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] -= B.matrix[i][j];
        }
        return this;
    }


    /**
     * Divide values from given Matrix (B) to the current Matrix.
     *
     * @param B (Matrix object).
     * @return Modified Matrix after Division.
     */
    protected Matrix divide(Matrix B) throws IndexOutOfBoundsException {
        if (B.rows == this.rows && B.columns == 1) { // if 1D array
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.columns; j++) {
                    if (Double.isInfinite(this.matrix[i][j] / B.matrix[i][0]))
                        this.matrix[i][j] = 1e-7;
                    else
                        this.matrix[i][j] /= B.matrix[i][0];
                }
            }
            return this;
        }
        if (this.rows != B.rows || this.columns != B.columns)
            throw new IndexOutOfBoundsException(String.format("Matrices has different dimensions (%s,%s) By (%s,%s)", this.rows, this.columns, B.rows, B.columns));

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (Double.isInfinite(this.matrix[i][j] / B.matrix[i][j]))
                    this.matrix[i][j] = 1e-7;
                else
                    this.matrix[i][j] /= B.matrix[i][j];
            }
        }
        return this;
    }

    /**
     * raise by E each cell of the Matrix.
     *
     * @return Modified Matrix after subtraction.
     */
    protected Matrix exp() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                this.matrix[i][j] = Math.exp(this.matrix[i][j]);
        }
        return this;
    }

    /**
     * Transpose matrix from nxm to mxn.
     *
     * @return transposed Matrix.
     */
    protected Matrix transpose() {
        Matrix temp = new Matrix(this.columns, this.rows);
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.columns; j++)
                temp.setValue(i, j, this.getValue(j, i));
        }
        return temp;
    }

    /**
     * Return max value in the entire matrix.
     *
     * @return (double) max value.
     */
    protected double max() {
        double max_value = 0.0;
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++)
                if (this.matrix[i][j] > max_value)
                    max_value = this.matrix[i][j];
        }
        return max_value;
    }

    /**
     * Returns the indices of the maximum values of each column/row.
     *
     * @param axis (0 or 1).
     * @return (Matrix object) vector contains indices of the maximum value of each column/row.
     */
    protected Matrix argmax(int axis) throws InvalidParameterException {
        if (this.rows == 1)
            return this;

        int max_index = 0;
        if (axis == 0) {
            Matrix temp = new Matrix(1, this.columns);

            for (int i = 0; i < this.getColumns(); i++) {
                double max_val = this.getValue(0, i);
                for (int j = 1; j < this.getRows(); j++)
                    if (this.getValue(j, i) > max_val) {
                        max_val = this.getValue(j, i);
                        max_index = j;
                    }
                temp.setValue(0, i, max_index);
            }
            return temp;
        }
        if (axis == 1) {
            Matrix temp = new Matrix(this.rows, 1);
            for (int i = 0; i < this.getRows(); i++) {
                double max_val = this.getValue(i, 0);
                for (int j = 1; j < this.getColumns(); j++)
                    if (this.getValue(i, j) > max_val) {
                        max_val = this.getValue(i, j);
                        max_index = j;
                    }
                temp.setValue(i, 0, max_index);
            }
            return temp.transpose();
        } else
            throw new InvalidParameterException("Invalid axis provided.");
    }


    /**
     * Return maximum value in each column/row of matrix.
     *
     * @param axis (0 or 1).
     * @return (Matrix object) vector with max values of each column/row.
     */
    protected Matrix max(int axis) throws InvalidParameterException {
        if (this.rows == 1)
            return new Matrix(1, 1, new double[][]{{this.max()}});

        if (axis == 0) {
            Matrix temp = new Matrix(1, this.columns);
            for (int i = 0; i < this.getColumns(); i++) {
                double max_val = this.getValue(0, i);
                for (int j = 1; j < this.getRows(); j++)
                    if (this.getValue(j, i) > max_val)
                        max_val = this.getValue(j, i);
                temp.setValue(0, i, max_val);
            }
            return temp;
        }
        if (axis == 1) {
            Matrix temp = new Matrix(this.rows, 1);
            for (int i = 0; i < this.getRows(); i++) {
                double max_val = this.getValue(i, 0);
                for (int j = 1; j < this.getColumns(); j++)
                    if (this.getValue(i, j) > max_val)
                        max_val = this.getValue(i, j);
                temp.setValue(i, 0, max_val);
            }
            return temp;

        } else
            throw new InvalidParameterException("Invalid axis provided.");
    }

    /**
     * Return summed values in each column/row of matrix.
     *
     * @param axis (0 or 1).
     * @return (Matrix object) vector with summed values of each column/row.
     */
    protected Matrix sum(int axis) throws InvalidParameterException {
        if (axis == 0) {
            Matrix temp = new Matrix(1, this.columns);
            for (int i = 0; i < this.getColumns(); i++) {
                double sum = 0.0;
                for (int j = 1; j < this.getRows(); j++)
                    sum += this.getValue(j, i);
                temp.setValue(0, i, sum);
            }
            return temp;
        }
        if (axis == 1) {
            Matrix temp = new Matrix(this.rows, 1);
            for (int i = 0; i < this.getRows(); i++) {
                double sum = 0.0;
                for (int j = 1; j < this.getColumns(); j++)
                    sum += this.getValue(i, j);
                temp.setValue(i, 0, sum);
            }
            return temp;

        } else
            throw new InvalidParameterException("Invalid axis provided.");
    }

    /**
     * Return the log (e) value of each cell of the Matrix.
     *
     * @return Matrix with updated values.
     */
    protected Matrix log() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (this.matrix[i][j] <= 0.0) // Avoid log(0).
                    this.matrix[i][j] = 1 - 1e-7;
                else
                    this.matrix[i][j] = Math.log(this.matrix[i][j]);
            }
        }
        return this;
    }

    /**
     * Return the mean value of the Matrix.
     *
     * @return (double value).
     */
    protected double mean() {
        double mean = 0.0;
        for (int i = 0; i < this.rows; i++)
            for (int j = 0; j < columns; j++)
                mean += this.matrix[i][j];
        try {
            return mean / (this.rows * this.columns);
        } catch (ArithmeticException e) {
            return 0.0;
        }
    }

    /**
     * Prints Matrix in 2D shape
     *
     * @return (Prettified Matrix String)
     */
    @Override
    public String toString() {
        StringBuilder matrix_output = new StringBuilder();
        matrix_output.append("[");
        for (double[] arr : this.matrix) {
            for (Double number : arr) {
                matrix_output.append(number).append(", ");
            }
            matrix_output.append("\n");
        }
        matrix_output.replace(matrix_output.length() - 3, matrix_output.length() - 1, "]");
        return matrix_output.toString();
    }

    /**
     * Compare values of current Matrix to the given one.
     *
     * @param obj (Matrix object)
     * @return True if equals else false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Matrix) { // Validate matrices size
            if (!(((Matrix) obj).columns == this.columns))
                return false;
            if (!(((Matrix) obj).rows == this.rows))
                return false;
        }

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                if (this.matrix[i][j] != ((Matrix) obj).matrix[i][j])
                    return false;
            }
        }
        return true;
    }

    /**
     * @param n_rows    (number of rows in matrix)
     * @param n_columns (number of columns in matrix)
     * @return (nxm Matrix filled with normal_distribution values)
     */
    protected static Matrix random(int n_rows, int n_columns) {
        Matrix temp = new Matrix(Math.abs(n_rows), Math.abs(n_columns));
        for (int i = 0; i < temp.rows; i++) {
            for (int j = 0; j < temp.columns; j++)
                temp.matrix[i][j] = new Random().nextGaussian();
        }
        return temp;
    }

    public static Matrix clip(Matrix B, double min, double max) {
        Matrix temp = new Matrix(B.rows, B.columns);
        for (int i = 0; i < B.rows; i++) {
            for (int j = 0; j < B.columns; j++) {
                if (B.matrix[i][j] < min)
                    temp.matrix[i][j] = min;
                else if (B.matrix[i][j] > max)
                    temp.matrix[i][j] = max;
                else
                    temp.matrix[i][j] = B.matrix[i][j];
            }
        }
        return temp;
    }

    public static Matrix bitwiseCompare(Matrix A, Matrix B) throws IndexOutOfBoundsException {
        if (A.rows != B.rows || A.columns != B.columns)
            throw new IndexOutOfBoundsException(String.format("Matrices has different dimensions (%s,%s) By (%s,%s)", A.rows, A.columns, B.rows, B.columns));

        Matrix temp = new Matrix(A.rows, A.columns);
        for (int i = 0; i < A.rows; i++) {
            for (int j = 0; j < A.columns; j++) {
                if (A.matrix[i][j] == B.matrix[i][j])
                    temp.matrix[i][j] = 1.0;
                else
                    temp.matrix[i][j] = 0.0;
            }
        }
        return temp;
    }

}

