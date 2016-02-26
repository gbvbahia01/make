package br.com.gbvbahia.maker;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.junit.Before;
import org.junit.Test;

import br.com.gbvbahia.entities.EntityNotNullTest;
import br.com.gbvbahia.maker.factories.Factory;
import br.com.gbvbahia.maker.log.LogInfo;

/**
 * @since v.1 01/05/2012
 * @author Guilherme
 */
public class MakeEntitysTest extends TestCase {

  private static Log logger = LogInfo.getLog("Test :: MakeEntitysTest");
  private Validator validator;

  @Before
  @Override
  public void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    this.validator = factory.getValidator();
  }

  public MakeEntitysTest() {
    super("Make Entitys");
  }

  @Test
  public void testEntitysError() throws Exception {
    Factory.loadSetup("make.xml");
    logger.info("Entity - EntitysError");
    boolean excecao = false;
    try {
      List<EntityNotNullTest> test = MakeEntity.makeEntities(EntityNotNullTest.class, -10);
    } catch (IllegalArgumentException e) {
      excecao = true;
      logger.debug(e.getMessage());
    }
    assertTrue("Esperado IllegalArgumentException", excecao);
    excecao = false;
    try {
      List<EntityNotNullTest> test = MakeEntity.makeEntities(EntityNotNullTest.class, -1);
    } catch (IllegalArgumentException e) {
      excecao = true;
      logger.debug(e.getMessage());
    }
    assertTrue("Esperado IllegalArgumentException", excecao);
  }

  @Test
  public void testEntitysList() throws Exception {
    Factory.loadSetup("make.xml");
    logger.info("Entity - EntitysList");
    List<EntityNotNullTest> tests = MakeEntity.makeEntities(EntityNotNullTest.class, 10);
    for (EntityNotNullTest test : tests) {
      this.validarJSR303(test);
    }
  }

  private void validarJSR303(Object test) {
    Set<ConstraintViolation<Object>> erros = this.validator.validate(test);
    for (ConstraintViolation<Object> erro : erros) {
      logger.error(erro.getMessage());
    }
    assertTrue("Erros de validação encontrados", erros.isEmpty());
  }
}