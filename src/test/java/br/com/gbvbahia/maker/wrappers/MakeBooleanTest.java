/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gbvbahia.maker.wrappers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Guilherme
 */
public class MakeBooleanTest {

    private Log logger = LogFactory.getLog("MakeBooleanTest");

    public MakeBooleanTest() {
    }

    /**
     * Test of getBoolean method, of class MakeBoolean.
     */
    @Test
    public void testGetBoolean() {
        logger.info("Maker :: Boolean - GetBoolean");
        boolean trueCheck = false, falseCheck = false;
        for (int i = 0; i < 100; i++) {
            Boolean b = MakeBoolean.getBoolean();
            logger.info("Result Boolean: " + b);
            assertNotNull("Boolean nulo!", b);
            if (b) {
                trueCheck = true;
            }
            if (!b) {
                falseCheck = true;
            }
            if (trueCheck && falseCheck) {
                break;
            }
        }
        assertTrue("True ou False não retornado",
                trueCheck && falseCheck);
    }
}