package ru.unn.agile.matrixoperations.viewmodel;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.unn.agile.matrixoperations.model.Matrix;

import java.util.List;

import static org.junit.Assert.*;

public class ViewModelTests {
    private final float delta = 0.00001f;
    private final int leftSumMatrixRows = 2;
    private final int leftSumMatrixCols = 3;
    private final float[] dataSumLeft = {1, 2, 3,
                                         4, 5, 6};
    private Matrix leftSumMatrix;

    private final int rightSumMatrixRows = 2;
    private final int rightSumMatrixCols = 3;
    private final float[] dataSumRight = {9, 8, 7,
                                          6, 5, 4};
    private Matrix rightSumMatrix;

    private final int leftMultMatrixRows = 2;
    private final int leftMultMatrixCols = 4;
    private final float[] dataMultLeft = {1, 2, 3, 4,
                                          5, 6, 7, 8};
    private Matrix leftMultMatrix;

    private final int rightMultMatrixRows = 4;
    private final int rightMultMatrixCols = 2;
    private final float[] dataMultRight = {1, 2,
                                           3, 4,
                                           5, 6,
                                           7, 8};
    private Matrix rightMultMatrix;

    private ViewModel viewModel;

    private ILogger logger;

    public void setViewModel(final ViewModel vm) {
        viewModel = vm;
    }

    public void setLogger(final ILogger lgr) {
        logger = lgr;
    }

    @Before
    public void setUp() {
        viewModel = new ViewModel();

        leftSumMatrix = new Matrix(leftSumMatrixRows, leftSumMatrixCols, dataSumLeft);
        rightSumMatrix = new Matrix(rightSumMatrixRows, rightSumMatrixCols, dataSumRight);

        leftMultMatrix = new Matrix(leftMultMatrixRows, leftMultMatrixCols, dataMultLeft);
        rightMultMatrix = new Matrix(rightMultMatrixRows, rightMultMatrixCols, dataMultRight);
    }

    @After
    public void tearDown() {
        viewModel = null;
        leftSumMatrix = null;
        rightSumMatrix = null;
        rightMultMatrix = null;
    }

    @Test
    public void testDefaultValues() {
        assertEquals(viewModel.getDefaultRowsCount(),
                viewModel.leftMatrixRowsProperty().getValue());
        assertEquals(viewModel.getDefaultColumnsCount(),
                viewModel.leftMatrixColumnsProperty().getValue());
        assertEquals(viewModel.getDefaultRowsCount(),
                viewModel.rightMatrixRowsProperty().getValue());
        assertEquals(viewModel.getDefaultColumnsCount(),
                viewModel.rightMatrixColumnsProperty().getValue());
        assertEquals(viewModel.getDefaultRowsCount(),
                viewModel.resultMatrixRowsProperty().getValue());
        assertEquals(viewModel.getDefaultColumnsCount(),
                viewModel.resultMatrixColumnsProperty().getValue());
        assertEquals(Matrix.Operation.ADD,
                viewModel.operationProperty().getValue());
    }

    @Test
    public void testStatusForDefaultValues() {
        viewModel.calculate();
        assertEquals(ViewModel.Status.SUCCESS, viewModel.getStatus());
    }

    @Test
    public void testOperationGet() {
        doTestOperationGet(Matrix.Operation.ADD);
        doTestOperationGet(Matrix.Operation.MULTIPLY);
    }

    @Test
    public void testGetOperations() {
        for (int i = 0; i < Matrix.Operation.values().length; i++) {
            assertEquals(Matrix.Operation.values()[i], viewModel.getOperations().get(i));
        }
    }

    @Test
    public void testGetOperationsProperty() {
        for (int i = 0; i < Matrix.Operation.values().length; i++) {
            assertEquals(Matrix.Operation.values()[i], viewModel.operationsProperty().get().get(i));
        }
    }

    @Test
    public void testInvalidLeftMatrixRows() {
        viewModel.leftMatrixRowsProperty().set(-1);
        assertEquals(ViewModel.Status.INVALID_LEFT_MATRIX_ROWS, viewModel.getStatus());
        assertEquals(ViewModel.Status.INVALID_LEFT_MATRIX_ROWS, viewModel.statusProperty().get());
    }

    @Test
    public void testInvalidLeftMatrixColumns() {
        viewModel.leftMatrixColumnsProperty().set(-1);
        assertEquals(ViewModel.Status.INVALID_LEFT_MATRIX_COLS, viewModel.getStatus());
        assertEquals(ViewModel.Status.INVALID_LEFT_MATRIX_COLS, viewModel.statusProperty().get());
    }

    @Test
    public void testInvalidRightMatrixRows() {
        viewModel.rightMatrixRowsProperty().set(-1);
        assertEquals(ViewModel.Status.INVALID_RIGHT_MATRIX_ROWS, viewModel.getStatus());
        assertEquals(ViewModel.Status.INVALID_RIGHT_MATRIX_ROWS, viewModel.statusProperty().get());
    }

    @Test
    public void testInvalidRightMatrixColumns() {
        viewModel.rightMatrixColumnsProperty().set(-1);
        assertEquals(ViewModel.Status.INVALID_RIGHT_MATRIX_COLS, viewModel.getStatus());
        assertEquals(ViewModel.Status.INVALID_RIGHT_MATRIX_COLS, viewModel.statusProperty().get());
    }

    @Test
    public void testNonAgreedSumMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.ADD);
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        assertEquals(ViewModel.Status.INVALID_MATRIX_SIZE, viewModel.getStatus());
        assertEquals(ViewModel.Status.INVALID_MATRIX_SIZE, viewModel.statusProperty().get());
    }

    @Test
    public void testCalculatedDisabledForNotValidSumMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.ADD);
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        assertTrue(viewModel.getCalculationDisabled());
    }

    @Test
    public void testCalculatedDisabledPropertyForNotValidSumMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.ADD);
        doTestCalculatedDisabledPropertyForNotValidMatrix();
    }

    @Test
    public void testCalculatedEnabledForValidSumMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.ADD);
        doTestCalculatedDisabledForNotValidMatrix();
    }

    @Test
    public void testCalculationForNotValidSumMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.ADD);
        doTestCalculationForNotValidMatrix();
    }

    @Test
    public void testStatusForValidSumData() {
        setValidDataForSum();
        assertEquals(ViewModel.Status.READY, viewModel.getStatus());
    }

    @Test
    public void testNonAgreedMultMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.MULTIPLY);
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        assertEquals(ViewModel.Status.INVALID_MATRIX_SIZE, viewModel.getStatus());
    }

    @Test
    public void testStatusForValidMultData() {
        setValidDataForMult();
        assertEquals(ViewModel.Status.READY, viewModel.getStatus());
    }

    @Test
    public void testCalculatedDisabledForNotValidMultMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.MULTIPLY);
        doTestCalculatedDisabledForNotValidMatrix();
    }

    @Test
    public void testCalculatedDisabledPropertyForNotValidMultMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.MULTIPLY);
        doTestCalculatedDisabledPropertyForNotValidMatrix();
    }

    @Test
    public void testCalculatedEnabledForValidMultMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.MULTIPLY);
        updateLeftMatrix(leftMultMatrix);
        updateRightMatrix(rightMultMatrix);
        assertFalse(viewModel.getCalculationDisabled());
    }

    @Test
    public void testCalculationForNotValidMultMatrix() {
        viewModel.operationProperty().set(Matrix.Operation.MULTIPLY);
        doTestCalculationForNotValidMatrix();
    }

    @Test
    public void testRowsCountForResultSum() {
        setValidDataForSum();
        viewModel.calculate();
        assertEquals(viewModel.leftMatrix().getMatrix().getRows(),
                viewModel.resultMatrix().getMatrix().getRows());
    }

    @Test
    public void testColsCountForResultSum() {
        setValidDataForSum();
        viewModel.calculate();
        assertEquals(viewModel.leftMatrix().getMatrix().getColumns(),
                viewModel.resultMatrix().getMatrix().getColumns());
    }

    @Test
    public void testRowsCountForResultMult() {
        setValidDataForMult();
        viewModel.calculate();
        assertEquals(viewModel.leftMatrix().getMatrix().getRows(),
                viewModel.resultMatrix().getMatrix().getRows());
    }

    @Test
    public void testColumnsCountForResultMult() {
        setValidDataForMult();
        viewModel.calculate();
        assertEquals(viewModel.rightMatrix().getMatrix().getColumns(),
                viewModel.resultMatrix().getMatrix().getColumns());
    }

    @Test
    public void testReloadLeftMatrix() {
        updateLeftMatrix(leftSumMatrix);
        viewModel.reloadLeftMatrix();
        for (int i = 0; i < leftSumMatrix.getSize(); i++) {
            assertEquals(0.0f, viewModel.leftMatrix().getMatrix().getElement(i), delta);
        }
    }

    @Test
    public void testReloadRightMatrix() {
        updateRightMatrix(rightSumMatrix);
        viewModel.reloadRightMatrix();
        for (int i = 0; i < rightSumMatrix.getSize(); i++) {
            assertEquals(0.0f, viewModel.rightMatrix().getMatrix().getElement(i), delta);
        }
    }

    @Test
    public void canSetLogger() {
        viewModel.setLogger(new TestingLogger());
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantSetNullLogger() {
        viewModel.setLogger(null);
    }

    @Test
    public void canCreateViewModelWithLogger() {
        ILogger logger = new TestingLogger();
        viewModel = new ViewModel(logger);
        assertNotNull(viewModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cantCreateViewModelWithNullLogger() {
        viewModel = new ViewModel(null);
    }

    @Test
    public void canLogCalculation() {
        setUpLogger();

        viewModel.calculate();

        assertTrue(getLastLog().endsWith(LogMessages.CALCULATE));
    }

    @Test
    public void canLogOperationChanging() {
        setUpLogger();
        ObjectProperty<Matrix.Operation> operation = viewModel.operationProperty();

        operation.set(Matrix.Operation.ADD);
        operation.set(Matrix.Operation.MULTIPLY);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_OPERATION
               + " from " + Matrix.Operation.ADD.toString() + " to " + operation.get().toString()));
    }

    @Test
    public void canLogLeftMatrixColsCountChanging() {
        setUpLogger();
        IntegerProperty leftMatrixCols = viewModel.leftMatrixColumnsProperty();

        leftMatrixCols.set(5);
        leftMatrixCols.set(7);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_LEFT_MATRIX_COLS
                                            + " from " + 5 + " to " + 7));
    }

    @Test
    public void canLogLeftMatrixRowsCountChanging() {
        setUpLogger();
        IntegerProperty leftMatrixRows = viewModel.leftMatrixRowsProperty();

        leftMatrixRows.set(1);
        leftMatrixRows.set(3);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_LEFT_MATRIX_ROWS
                                         + " from " + 1 + " to " + 3));
    }

    @Test
    public void canLogRightMatrixColsCountChanging() {
        setUpLogger();
        IntegerProperty rightMatrixCols = viewModel.rightMatrixColumnsProperty();

        rightMatrixCols.set(8);
        rightMatrixCols.set(9);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_RIGHT_MATRIX_COLS
                                         + " from " + 8 + " to " + 9));
    }

    @Test
    public void canLogRightMatrixRowsCountChanging() {
        setUpLogger();
        IntegerProperty rightMatrixRows = viewModel.rightMatrixRowsProperty();

        rightMatrixRows.set(4);
        rightMatrixRows.set(6);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_RIGHT_MATRIX_ROWS
                                         + " from " + 4 + " to " + 6));
    }

    @Test
    public void canLogLeftMatrixElementChanging() {
        setUpLogger();
        FloatProperty leftMatrixEl = viewModel.leftMatrix().elementProperty(0, 1);

        leftMatrixEl.set(2.718f);
        leftMatrixEl.set(3.141f);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_LEFT_MATRIX_ELEMENT
                + " " + "[" + 0 + "][" + 1 + "]" + " from " + 2.718f + " to " + 3.141f));
    }

    @Test
    public void canLogRightMatrixElementChanging() {
        setUpLogger();
        FloatProperty rightMatrixEl = viewModel.rightMatrix().elementProperty(1, 0);

        rightMatrixEl.set(42.0f);
        rightMatrixEl.set(32.0f);

        assertTrue(getLastLog().endsWith(LogMessages.CHANGE_RIGHT_MATRIX_ELEMENT
                + " " + "[" + 1 + "][" + 0 + "]" + " from " + 42.0f + " to " + 32.0f));
    }

    @Test
    public void canGetLog() {
        ObservableList<String> log = viewModel.getLog();
        assertNotNull(log);
    }

    @Test
    public void canGetLogProperty() {
        ObjectProperty<ObservableList<String>> prop = viewModel.logProperty();
        assertNotNull(prop);
    }

    private void doTestOperationGet(final Matrix.Operation op) {
        viewModel.operationProperty().set(op);
        assertEquals(op, viewModel.getOperation());
    }

    private void doTestCalculationForNotValidMatrix() {
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        viewModel.calculate();
        assertEquals(viewModel.getDefaultRowsCount(),
                viewModel.resultMatrixRowsProperty().getValue());
        assertEquals(viewModel.getDefaultColumnsCount(),
                viewModel.resultMatrixColumnsProperty().getValue());
    }

    private void doTestCalculatedDisabledForNotValidMatrix() {
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        assertTrue(viewModel.getCalculationDisabled());
    }

    private void doTestCalculatedDisabledPropertyForNotValidMatrix() {
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightMultMatrix);
        assertTrue(viewModel.calculationDisabledProperty().get());
    }

    private void setValidDataForSum() {
        viewModel.operationProperty().setValue(Matrix.Operation.ADD);
        updateLeftMatrix(leftSumMatrix);
        updateRightMatrix(rightSumMatrix);
    }

    private void setValidDataForMult() {
        viewModel.operationProperty().setValue(Matrix.Operation.MULTIPLY);

        updateLeftMatrix(leftMultMatrix);
        updateRightMatrix(rightMultMatrix);
    }

    private void updateLeftMatrix(final Matrix m) {
        viewModel.leftMatrixRowsProperty().set(m.getRows());
        viewModel.leftMatrixColumnsProperty().set(m.getColumns());
        viewModel.leftMatrix().getMatrix().updateDimensions(m.getRows(), m.getColumns());
        for (int i = 0; i < m.getSize(); i++) {
            viewModel.leftMatrix().getMatrix().setElement(i, m.getElement(i));
        }
    }

    private void updateRightMatrix(final Matrix m) {
        viewModel.rightMatrixRowsProperty().set(m.getRows());
        viewModel.rightMatrixColumnsProperty().set(m.getColumns());
        viewModel.rightMatrix().getMatrix().updateDimensions(m.getRows(), m.getColumns());
        for (int i = 0; i < m.getSize(); i++) {
            viewModel.rightMatrix().getMatrix().setElement(i, m.getElement(i));
        }
    }

    protected void setUpLogger() {
        logger = new TestingLogger();
        viewModel.setLogger(logger);
    }

    private String getLastLog() {
        List<String> messages = logger.getLog();
        if (messages.size() > 0) {
            return messages.get(messages.size() - 1);
        } else {
            return null;
        }
    }
}
