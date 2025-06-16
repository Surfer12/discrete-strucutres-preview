package com.discretelogic.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class BooleanAlgebraTest {
    @ParameterizedTest
    @DisplayName("Boolean Algebra Expression Evaluation")
    @CsvSource({
        "true & true, true",
        "true | false, true",
        "false ^ true, true"
    })
    void testBooleanAlgebraExpressions(String expression, boolean expected) {
        // Implement test logic
    }
}
