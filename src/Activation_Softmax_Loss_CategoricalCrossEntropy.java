public class Activation_Softmax_Loss_CategoricalCrossEntropy {
    //Softmax classifier - combined Softmax activation
    //and cross-entropy loss for faster backward step

    private Activation activation;
    private Loss loss;
    private Matrix output;

    public Matrix get_outputs() {
        return output;
    }

    public Matrix get_d_inputs() {
        return d_inputs;
    }

    private Matrix d_inputs;

    //Creates activation and loss function objects
    protected Activation_Softmax_Loss_CategoricalCrossEntropy() {
        this.activation = new Activation_SoftMax();
        this.loss = new Loss_CategoricalCrossEntropy();
    }

    //Forward pass
    public double forward(Matrix inputs, Matrix y_true) throws InvalidMatrixOperation, InvalidMatrixAxis, MatrixIndexesOutOfBounds, InvalidMatrixDimension {
        this.activation.forward(inputs); //Output layer's activation function
        this.output = this.activation.output(); //Set the output

        Matrix forward_matrix = this.loss.forward(this.output, y_true);
        return this.loss.calculate(forward_matrix); //Calculate and return loss value
    }

    //Backward pass
    public void backward(Matrix d_values, Matrix y_true) throws InvalidMatrixAxis, MatrixIndexesOutOfBounds, InvalidMatrixDimension {
        if (y_true.shape() == 2)
            y_true = y_true.argmax(1);

        this.d_inputs = new Matrix(d_values);

        for (int i = 0; i < d_inputs.getRows(); i++)
            d_inputs.setValue(i, (int) y_true.getValue(0, i), d_inputs.getValue(i, (int) y_true.getValue(0, i)) - 1);

        this.d_inputs.divide(d_values.getRows());
    }
}
