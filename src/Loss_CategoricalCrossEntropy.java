class Loss_CategoricalCrossEntropy implements Loss {
    private Matrix d_inputs;

    @Override
    //Backward pass
    public void backward(Matrix d_values, Matrix y_true) throws MatrixIndexesOutOfBounds, InvalidMatrixDimension, InvalidMatrixOperation {
        int samples = d_values.getRows(); //Number of samples

        // If labels are sparse, turn them into one-hot vector
        if (y_true.getRows() == 1)
            y_true = Matrix.oneHotVector(y_true);

        // Calculate gradient
        this.d_inputs = y_true.multiply(-1).divide(d_values);

        // Normalize gradient
        this.d_inputs = this.d_inputs.divide(samples);

    }

    @Override
    //Forward pass
    public Matrix forward(Matrix y_pred, Matrix y_true) throws IndexOutOfBoundsException, InvalidMatrixDimension, MatrixIndexesOutOfBounds, InvalidMatrixOperation {
        int samples = y_pred.getRows();

        //Clip data to prevent division by 0
        //Clip both sides to not drag mean towards any value
        Matrix y_pred_clipped = Matrix.clip(y_pred, 1e-7, 1 - 1e-7);
        Matrix correct_confidences = new Matrix(samples, 1);

        //Probabilities for target values - only if categorical labels
        if (y_true.shape() == 1) { //1D array label data
            if (y_pred.getRows() == y_true.getColumns()) { // Check if Prediction size == Labels size
                for (int i = 0; i < samples; i++)
                    correct_confidences.setValue(i, 0, y_pred_clipped.getValue(i, (int) y_true.getValue(0, i)));
            } else //2D array label data
                throw new IndexOutOfBoundsException(String.format("Prediction Matrix (%s, %s) and Labels Matrix (, %s) has different shapes", y_pred.getRows(), y_pred.getColumns(), y_true.getColumns()));

        } else {
            if (y_pred.getRows() == y_true.getRows() && y_pred.getColumns() == y_true.getColumns())
                correct_confidences = y_pred_clipped.multiply(y_true); //Losses
            else
                throw new IndexOutOfBoundsException(String.format("Prediction Matrix (%s, %s) and Labels Matrix (%s, %s) has different shapes", y_pred.getRows(), y_pred.getColumns(), y_true.getRows(), y_true.getColumns()));
        }

        return correct_confidences.log().multiply(-1);
    }

    @Override
    public double calculate(Matrix output) {

        return output.mean();
    }

    @Override
    public Matrix d_inputs() {
        return d_inputs;
    }
}



